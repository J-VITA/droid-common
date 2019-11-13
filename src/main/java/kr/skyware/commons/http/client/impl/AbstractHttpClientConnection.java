package kr.skyware.commons.http.client.impl;

import java.io.IOException;
import java.net.SocketTimeoutException;

import kr.skyware.commons.http.HttpConnectionMetricsImpl;
import kr.skyware.commons.http.HttpEntity;
import kr.skyware.commons.http.HttpResponse;
import kr.skyware.commons.http.annotation.NotThreadSafe;
import kr.skyware.commons.http.client.DefaultHttpResponseFactory;
import kr.skyware.commons.http.client.impl.entity.EntityDeserializer;
import kr.skyware.commons.http.client.impl.entity.EntitySerializer;
import kr.skyware.commons.http.connect.HttpConnectionMetrics;
import kr.skyware.commons.http.entity.LaxContentLengthStrategy;
import kr.skyware.commons.http.entity.StrictContentLengthStrategy;
import kr.skyware.commons.http.exception.HttpException;
import kr.skyware.commons.http.factory.HttpResponseFactory;
import kr.skyware.commons.http.header.HttpClientConnection;
import kr.skyware.commons.http.header.HttpEntityEnclosingRequest;
import kr.skyware.commons.http.header.HttpParams;
import kr.skyware.commons.http.header.HttpRequest;
import kr.skyware.commons.http.io.DefaultHttpResponseParser;
import kr.skyware.commons.http.io.EofSensor;
import kr.skyware.commons.http.io.HttpMessageParser;
import kr.skyware.commons.http.io.HttpMessageWriter;
import kr.skyware.commons.http.io.HttpRequestWriter;
import kr.skyware.commons.http.io.HttpTransportMetrics;
import kr.skyware.commons.http.io.SessionInputBuffer;
import kr.skyware.commons.http.io.SessionOutputBuffer;
import kr.skyware.commons.http.util.Args;
import kr.skyware.commons.http.util.HttpStatus;

@NotThreadSafe
public abstract class AbstractHttpClientConnection implements HttpClientConnection {

    private final EntitySerializer entityserializer;
    private final EntityDeserializer entitydeserializer;

    private SessionInputBuffer inbuffer = null;
    private SessionOutputBuffer outbuffer = null;
    private EofSensor eofSensor = null;
    private HttpMessageParser<HttpResponse> responseParser = null;
    private HttpMessageWriter<HttpRequest> requestWriter = null;
    private HttpConnectionMetricsImpl metrics = null;

    /**
     * Creates an instance of this class.
     * <p>
     * This constructor will invoke {@link #createEntityDeserializer()}
     * and {@link #createEntitySerializer()} methods in order to initialize
     * HTTP entity serializer and deserializer implementations for this
     * connection.
     */
    public AbstractHttpClientConnection() {
        super();
        this.entityserializer = createEntitySerializer();
        this.entitydeserializer = createEntityDeserializer();
    }

    /**
     * Asserts if the connection is open.
     *
     * @throws IllegalStateException if the connection is not open.
     */
    protected abstract void assertOpen() throws IllegalStateException;

    /**
     * Creates an instance of {@link EntityDeserializer} with the
     * {@link LaxContentLengthStrategy} implementation to be used for
     * de-serializing entities received over this connection.
     * <p>
     * This method can be overridden in a super class in order to create
     * instances of {@link EntityDeserializer} using a custom
     * {@link kr.skyware.commons.http.entity.ContentLengthStrategy}.
     *
     * @return HTTP entity deserializer
     */
    protected EntityDeserializer createEntityDeserializer() {
        return new EntityDeserializer(new LaxContentLengthStrategy());
    }

    /**
     * Creates an instance of {@link EntitySerializer} with the
     * {@link StrictContentLengthStrategy} implementation to be used for
     * serializing HTTP entities sent over this connection.
     * <p>
     * This method can be overridden in a super class in order to create
     * instances of {@link EntitySerializer} using a custom
     * {@link kr.skyware.commons.http.entity.ContentLengthStrategy}.
     *
     * @return HTTP entity serialzier.
     */
    protected EntitySerializer createEntitySerializer() {
        return new EntitySerializer(new StrictContentLengthStrategy());
    }

    /**
     * Creates an instance of {@link DefaultHttpResponseFactory} to be used
     * for creating {@link HttpResponse} objects received by over this
     * connection.
     * <p>
     * This method can be overridden in a super class in order to provide
     * a different implementation of the {@link HttpResponseFactory} interface.
     *
     * @return HTTP response factory.
     */
    protected HttpResponseFactory createHttpResponseFactory() {
        return DefaultHttpResponseFactory.INSTANCE;
    }

    /**
     * Creates an instance of {@link HttpMessageParser} to be used for parsing
     * HTTP responses received over this connection.
     * <p>
     * This method can be overridden in a super class in order to provide
     * a different implementation of the {@link HttpMessageParser} interface or
     * to pass a different implementation of the
     * {@link kr.skyware.commons.http.message.LineParser} to the the
     * {@link DefaultHttpResponseParser} constructor.
     *
     * @param buffer the session input buffer.
     * @param responseFactory the HTTP response factory.
     * @param params HTTP parameters.
     * @return HTTP message parser.
     */
    protected HttpMessageParser<HttpResponse> createResponseParser(
            final SessionInputBuffer buffer,
            final HttpResponseFactory responseFactory,
            final HttpParams params) {
        return new DefaultHttpResponseParser(buffer, null, responseFactory, params);
    }

    /**
     * Creates an instance of {@link HttpMessageWriter} to be used for
     * writing out HTTP requests sent over this connection.
     * <p>
     * This method can be overridden in a super class in order to provide
     * a different implementation of the {@link HttpMessageWriter} interface or
     * to pass a different implementation of
     * {@link kr.skyware.commons.http.message.LineFormatter} to the the default implementation
     * {@link HttpRequestWriter}.
     *
     * @param buffer the session output buffer
     * @param params HTTP parameters
     * @return HTTP message writer
     */
    protected HttpMessageWriter<HttpRequest> createRequestWriter(
            final SessionOutputBuffer buffer,
            final HttpParams params) {
        return new HttpRequestWriter(buffer, null, params);
    }

    /**
     * @since 4.1
     */
    protected HttpConnectionMetricsImpl createConnectionMetrics(
            final HttpTransportMetrics inTransportMetric,
            final HttpTransportMetrics outTransportMetric) {
        return new HttpConnectionMetricsImpl(inTransportMetric, outTransportMetric);
    }

    /**
     * Initializes this connection object with {@link SessionInputBuffer} and
     * {@link SessionOutputBuffer} instances to be used for sending and
     * receiving data. These session buffers can be bound to any arbitrary
     * physical output medium.
     * <p>
     * This method will invoke {@link #createHttpResponseFactory()},
     * {@link #createRequestWriter(SessionOutputBuffer, HttpParams)}
     * and {@link #createResponseParser(SessionInputBuffer, HttpResponseFactory, HttpParams)}
     * methods to initialize HTTP request writer and response parser for this
     * connection.
     *
     * @param inbuffer the session input buffer.
     * @param outbuffer the session output buffer.
     * @param params HTTP parameters.
     */
    protected void init(
            final SessionInputBuffer inbuffer,
            final SessionOutputBuffer outbuffer,
            final HttpParams params) {
        this.inbuffer = Args.notNull(inbuffer, "Input session buffer");
        this.outbuffer = Args.notNull(outbuffer, "Output session buffer");
        if (inbuffer instanceof EofSensor) {
            this.eofSensor = (EofSensor) inbuffer;
        }
        this.responseParser = createResponseParser(
                inbuffer,
                createHttpResponseFactory(),
                params);
        this.requestWriter = createRequestWriter(
                outbuffer, params);
        this.metrics = createConnectionMetrics(
                inbuffer.getMetrics(),
                outbuffer.getMetrics());
    }

    public boolean isResponseAvailable(final int timeout) throws IOException {
        assertOpen();
        try {
            return this.inbuffer.isDataAvailable(timeout);
        } catch (final SocketTimeoutException ex) {
            return false;
        }
    }

    public void sendRequestHeader(final HttpRequest request)
            throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        assertOpen();
        this.requestWriter.write(request);
        this.metrics.incrementRequestCount();
    }

    public void sendRequestEntity(final HttpEntityEnclosingRequest request)
            throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        assertOpen();
        if (request.getEntity() == null) {
            return;
        }
        this.entityserializer.serialize(
                this.outbuffer,
                request,
                request.getEntity());
    }

    protected void doFlush() throws IOException {
        this.outbuffer.flush();
    }

    public void flush() throws IOException {
        assertOpen();
        doFlush();
    }

    public HttpResponse receiveResponseHeader()
            throws HttpException, IOException {
        assertOpen();
        final HttpResponse response = this.responseParser.parse();
        if (response.getStatusLine().getStatusCode() >= HttpStatus.SC_OK) {
            this.metrics.incrementResponseCount();
        }
        return response;
    }

    public void receiveResponseEntity(final HttpResponse response)
            throws HttpException, IOException {
        Args.notNull(response, "HTTP response");
        assertOpen();
        final HttpEntity entity = this.entitydeserializer.deserialize(this.inbuffer, response);
        response.setEntity(entity);
    }

    protected boolean isEof() {
        return this.eofSensor != null && this.eofSensor.isEof();
    }

    public boolean isStale() {
        if (!isOpen()) {
            return true;
        }
        if (isEof()) {
            return true;
        }
        try {
            this.inbuffer.isDataAvailable(1);
            return isEof();
        } catch (final SocketTimeoutException ex) {
            return false;
        } catch (final IOException ex) {
            return true;
        }
    }

    public HttpConnectionMetrics getMetrics() {
        return this.metrics;
    }

}
