package kr.skyware.commons.http.io;

import java.io.IOException;

import kr.skyware.commons.http.HttpResponse;
import kr.skyware.commons.http.annotation.NotThreadSafe;
import kr.skyware.commons.http.client.DefaultHttpResponseFactory;
import kr.skyware.commons.http.exception.HttpException;
import kr.skyware.commons.http.exception.NoHttpResponseException;
import kr.skyware.commons.http.exception.ParseException;
import kr.skyware.commons.http.factory.HttpResponseFactory;
import kr.skyware.commons.http.header.HttpParams;
import kr.skyware.commons.http.header.ParserCursor;
import kr.skyware.commons.http.message.LineParser;
import kr.skyware.commons.http.util.Args;
import kr.skyware.commons.http.util.CharArrayBuffer;
import kr.skyware.commons.http.util.StatusLine;
import kr.skyware.commons.http.util.args.MessageConstraints;

@NotThreadSafe
public class DefaultHttpResponseParser extends AbstractMessageParser<HttpResponse> {

    private final HttpResponseFactory responseFactory;
    private final CharArrayBuffer lineBuf;

    /**
     * Creates an instance of this class.
     *
     * @param buffer the session input buffer.
     * @param lineParser the line parser.
     * @param responseFactory the factory to use to create
     *    {@link HttpResponse}s.
     * @param params HTTP parameters.
     *
     * @deprecated (4.3) use
     *   {@link DefaultHttpResponseParser#DefaultHttpResponseParser(SessionInputBuffer, LineParser,
     *     HttpResponseFactory, MessageConstraints)}
     */
    @Deprecated
    public DefaultHttpResponseParser(
            final SessionInputBuffer buffer,
            final LineParser lineParser,
            final HttpResponseFactory responseFactory,
            final HttpParams params) {
        super(buffer, lineParser, params);
        this.responseFactory = Args.notNull(responseFactory, "Response factory");
        this.lineBuf = new CharArrayBuffer(128);
    }

    /**
     * Creates new instance of DefaultHttpResponseParser.
     *
     * @param buffer the session input buffer.
     * @param lineParser the line parser. If <code>null</code>
     *   {@link kr.skyware.commons.http.message.BasicLineParser#INSTANCE} will be used
     * @param responseFactory the response factory. If <code>null</code>
     *   {@link DefaultHttpResponseFactory#INSTANCE} will be used.
     * @param constraints the message constraints. If <code>null</code>
     *   {@link MessageConstraints#DEFAULT} will be used.
     *
     * @since 4.3
     */
    public DefaultHttpResponseParser(
            final SessionInputBuffer buffer,
            final LineParser lineParser,
            final HttpResponseFactory responseFactory,
            final MessageConstraints constraints) {
        super(buffer, lineParser, constraints);
        this.responseFactory = responseFactory != null ? responseFactory :
                DefaultHttpResponseFactory.INSTANCE;
        this.lineBuf = new CharArrayBuffer(128);
    }

    /**
     * @since 4.3
     */
    public DefaultHttpResponseParser(
            final SessionInputBuffer buffer,
            final MessageConstraints constraints) {
        this(buffer, null, null, constraints);
    }

    /**
     * @since 4.3
     */
    public DefaultHttpResponseParser(final SessionInputBuffer buffer) {
        this(buffer, null, null, MessageConstraints.DEFAULT);
    }

    @Override
    protected HttpResponse parseHead(
            final SessionInputBuffer sessionBuffer)
            throws IOException, HttpException, ParseException {

        this.lineBuf.clear();
        final int i = sessionBuffer.readLine(this.lineBuf);
        if (i == -1) {
            throw new NoHttpResponseException("The target server failed to respond");
        }
        //create the status line from the status string
        final ParserCursor cursor = new ParserCursor(0, this.lineBuf.length());
        final StatusLine statusline = lineParser.parseStatusLine(this.lineBuf, cursor);
        return this.responseFactory.newHttpResponse(statusline, null);
    }

}
