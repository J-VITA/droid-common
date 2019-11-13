package kr.skyware.commons.http.io;

import java.io.IOException;

import kr.skyware.commons.http.annotation.NotThreadSafe;
import kr.skyware.commons.http.header.HttpRequest;
import kr.skyware.commons.http.message.LineFormatter;

@NotThreadSafe
public class DefaultHttpRequestWriter extends AbstractMessageWriter<HttpRequest> {

    /**
     * Creates an instance of DefaultHttpRequestWriter.
     *
     * @param buffer the session output buffer.
     * @param formatter the line formatter If <code>null</code>
     *   {@link kr.skyware.commons.http.message.BasicLineFormatter#INSTANCE}
     *   will be used.
     */
    public DefaultHttpRequestWriter(
            final SessionOutputBuffer buffer,
            final LineFormatter formatter) {
        super(buffer, formatter);
    }

    public DefaultHttpRequestWriter(final SessionOutputBuffer buffer) {
        this(buffer, null);
    }

    @Override
    protected void writeHeadLine(final HttpRequest message) throws IOException {
        lineFormatter.formatRequestLine(this.lineBuf, message.getRequestLine());
        this.sessionBuffer.writeLine(this.lineBuf);
    }

}