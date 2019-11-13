package kr.skyware.commons.http.connect;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

import kr.skyware.commons.http.HttpEntity;
import kr.skyware.commons.http.HttpResponse;
import kr.skyware.commons.http.annotation.NotThreadSafe;
import kr.skyware.commons.http.client.BHttpConnectionBase;
import kr.skyware.commons.http.entity.ContentLengthStrategy;
import kr.skyware.commons.http.exception.HttpException;
import kr.skyware.commons.http.factory.HttpMessageWriterFactory;
import kr.skyware.commons.http.header.HttpClientConnection;
import kr.skyware.commons.http.header.HttpEntityEnclosingRequest;
import kr.skyware.commons.http.header.HttpRequest;
import kr.skyware.commons.http.io.DefaultHttpRequestWriterFactory;
import kr.skyware.commons.http.io.DefaultHttpResponseParserFactory;
import kr.skyware.commons.http.io.HttpMessageParser;
import kr.skyware.commons.http.io.HttpMessageParserFactory;
import kr.skyware.commons.http.io.HttpMessageWriter;
import kr.skyware.commons.http.util.Args;
import kr.skyware.commons.http.util.HttpStatus;
import kr.skyware.commons.http.util.args.MessageConstraints;

@NotThreadSafe
public class DefaultBHttpClientConnection extends BHttpConnectionBase
        implements HttpClientConnection {

    private final HttpMessageParser<HttpResponse> responseParser;
    private final HttpMessageWriter<HttpRequest> requestWriter;

    /**
     * Creates new instance of DefaultBHttpClientConnection.
     *
     * @param buffersize buffer size. Must be a positive number.
     * @param fragmentSizeHint fragment size hint.
     * @param chardecoder decoder to be used for decoding HTTP protocol elements.
     *   If <code>null</code> simple type cast will be used for byte to char conversion.
     * @param charencoder encoder to be used for encoding HTTP protocol elements.
     *   If <code>null</code> simple type cast will be used for char to byte conversion.
     * @param constraints Message constraints. If <code>null</code>
     *   {@link MessageConstraints#DEFAULT} will be used.
     * @param incomingContentStrategy incoming content length strategy. If <code>null</code>
     *   {@link kr.skyware.commons.http.entity.LaxContentLengthStrategy#INSTANCE} will be used.
     * @param outgoingContentStrategy outgoing content length strategy. If <code>null</code>
     *   {@link kr.skyware.commons.http.entity.StrictContentLengthStrategy#INSTANCE} will be used.
     * @param requestWriterFactory request writer factory. If <code>null</code>
     *   {@link DefaultHttpRequestWriterFactory#INSTANCE} will be used.
     * @param responseParserFactory response parser factory. If <code>null</code>
     *   {@link DefaultHttpResponseParserFactory#INSTANCE} will be used.
     */
    public DefaultBHttpClientConnection(
            final int buffersize,
            final int fragmentSizeHint,
            final CharsetDecoder chardecoder,
            final CharsetEncoder charencoder,
            final MessageConstraints constraints,
            final ContentLengthStrategy incomingContentStrategy,
            final ContentLengthStrategy outgoingContentStrategy,
            final HttpMessageWriterFactory<HttpRequest> requestWriterFactory,
            final HttpMessageParserFactory<HttpResponse> responseParserFactory) {
        super(buffersize, fragmentSizeHint, chardecoder, charencoder,
                constraints, incomingContentStrategy, outgoingContentStrategy);
        this.requestWriter = (requestWriterFactory != null ? requestWriterFactory :
                DefaultHttpRequestWriterFactory.INSTANCE).create(getSessionOutputBuffer());
        this.responseParser = (responseParserFactory != null ? responseParserFactory :
                DefaultHttpResponseParserFactory.INSTANCE).create(getSessionInputBuffer(), constraints);
    }

    public DefaultBHttpClientConnection(
            final int buffersize,
            final CharsetDecoder chardecoder,
            final CharsetEncoder charencoder,
            final MessageConstraints constraints) {
        this(buffersize, buffersize, chardecoder, charencoder, constraints, null, null, null, null);
    }

    public DefaultBHttpClientConnection(final int buffersize) {
        this(buffersize, buffersize, null, null, null, null, null, null, null);
    }

    protected void onResponseReceived(final HttpResponse response) {
    }

    protected void onRequestSubmitted(final HttpRequest request) {
    }

    @Override
    public void bind(final Socket socket) throws IOException {
        super.bind(socket);
    }

    public boolean isResponseAvailable(final int timeout) throws IOException {
        ensureOpen();
        try {
            return awaitInput(timeout);
        } catch (final SocketTimeoutException ex) {
            return false;
        }
    }

    public void sendRequestHeader(final HttpRequest request)
            throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        ensureOpen();
        this.requestWriter.write(request);
        onRequestSubmitted(request);
        incrementRequestCount();
    }

    public void sendRequestEntity(final HttpEntityEnclosingRequest request)
            throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        ensureOpen();
        final HttpEntity entity = request.getEntity();
        if (entity == null) {
            return;
        }
        final OutputStream outstream = prepareOutput(request);
        entity.writeTo(outstream);
        outstream.close();
    }

    public HttpResponse receiveResponseHeader() throws HttpException, IOException {
        ensureOpen();
        final HttpResponse response = this.responseParser.parse();
        onResponseReceived(response);
        if (response.getStatusLine().getStatusCode() >= HttpStatus.SC_OK) {
            incrementResponseCount();
        }
        return response;
    }

    public void receiveResponseEntity(
            final HttpResponse response) throws HttpException, IOException {
        Args.notNull(response, "HTTP response");
        ensureOpen();
        final HttpEntity entity = prepareInput(response);
        response.setEntity(entity);
    }

    public void flush() throws IOException {
        ensureOpen();
        doFlush();
    }

}
