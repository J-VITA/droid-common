package kr.skyware.commons.http.io;

import java.io.IOException;

import kr.skyware.commons.http.HttpMessage;
import kr.skyware.commons.http.annotation.NotThreadSafe;
import kr.skyware.commons.http.exception.HttpException;
import kr.skyware.commons.http.header.Header;
import kr.skyware.commons.http.header.HeaderIterator;
import kr.skyware.commons.http.header.HttpParams;
import kr.skyware.commons.http.message.BasicLineFormatter;
import kr.skyware.commons.http.message.LineFormatter;
import kr.skyware.commons.http.util.Args;
import kr.skyware.commons.http.util.CharArrayBuffer;

@NotThreadSafe
public abstract class AbstractMessageWriter<T extends HttpMessage> implements HttpMessageWriter<T> {

    protected final SessionOutputBuffer sessionBuffer;
    protected final CharArrayBuffer lineBuf;
    protected final LineFormatter lineFormatter;

    /**
     * Creates an instance of AbstractMessageWriter.
     *
     * @param buffer the session output buffer.
     * @param formatter the line formatter.
     * @param params HTTP parameters.
     *
     * @deprecated (4.3) use
     *   {@link AbstractMessageWriter#AbstractMessageWriter(SessionOutputBuffer, LineFormatter)}
     */
    @Deprecated
    public AbstractMessageWriter(final SessionOutputBuffer buffer,
                                 final LineFormatter formatter,
                                 final HttpParams params) {
        super();
        Args.notNull(buffer, "Session input buffer");
        this.sessionBuffer = buffer;
        this.lineBuf = new CharArrayBuffer(128);
        this.lineFormatter = (formatter != null) ? formatter : BasicLineFormatter.INSTANCE;
    }

    /**
     * Creates an instance of AbstractMessageWriter.
     *
     * @param buffer the session output buffer.
     * @param formatter the line formatter If <code>null</code> {@link BasicLineFormatter#INSTANCE}
     *   will be used.
     *
     * @since 4.3
     */
    public AbstractMessageWriter(
            final SessionOutputBuffer buffer,
            final LineFormatter formatter) {
        super();
        this.sessionBuffer = Args.notNull(buffer, "Session input buffer");
        this.lineFormatter = (formatter != null) ? formatter : BasicLineFormatter.INSTANCE;
        this.lineBuf = new CharArrayBuffer(128);
    }

    /**
     * Subclasses must override this method to write out the first header line
     * based on the {@link HttpMessage} passed as a parameter.
     *
     * @param message the message whose first line is to be written out.
     * @throws IOException in case of an I/O error.
     */
    protected abstract void writeHeadLine(T message) throws IOException;

    public void write(final T message) throws IOException, HttpException {
        Args.notNull(message, "HTTP message");
        writeHeadLine(message);
        for (final HeaderIterator it = message.headerIterator(); it.hasNext(); ) {
            final Header header = it.nextHeader();
            this.sessionBuffer.writeLine
                    (lineFormatter.formatHeader(this.lineBuf, header));
        }
        this.lineBuf.clear();
        this.sessionBuffer.writeLine(this.lineBuf);
    }

}
