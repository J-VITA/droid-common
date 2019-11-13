package kr.skyware.commons.http.client.impl.entity;

import java.io.IOException;
import java.io.OutputStream;

import kr.skyware.commons.http.HttpEntity;
import kr.skyware.commons.http.HttpMessage;
import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.entity.ContentLengthStrategy;
import kr.skyware.commons.http.exception.HttpException;
import kr.skyware.commons.http.io.ChunkedOutputStream;
import kr.skyware.commons.http.io.ContentLengthOutputStream;
import kr.skyware.commons.http.io.IdentityOutputStream;
import kr.skyware.commons.http.io.SessionOutputBuffer;
import kr.skyware.commons.http.util.Args;

@Immutable // assuming injected dependencies are immutable
public class EntitySerializer {

    private final ContentLengthStrategy lenStrategy;

    public EntitySerializer(final ContentLengthStrategy lenStrategy) {
        super();
        this.lenStrategy = Args.notNull(lenStrategy, "Content length strategy");
    }

    /**
     * Creates a transfer codec based on properties of the given HTTP message
     * and returns {@link OutputStream} instance that transparently encodes
     * output data as it is being written out to the output stream.
     * <p>
     * This method is called by the public
     * {@link #serialize(SessionOutputBuffer, HttpMessage, HttpEntity)}.
     *
     * @param outbuffer the session output buffer.
     * @param message the HTTP message.
     * @return output stream.
     * @throws HttpException in case of HTTP protocol violation.
     * @throws IOException in case of an I/O error.
     */
    protected OutputStream doSerialize(
            final SessionOutputBuffer outbuffer,
            final HttpMessage message) throws HttpException, IOException {
        final long len = this.lenStrategy.determineLength(message);
        if (len == ContentLengthStrategy.CHUNKED) {
            return new ChunkedOutputStream(outbuffer);
        } else if (len == ContentLengthStrategy.IDENTITY) {
            return new IdentityOutputStream(outbuffer);
        } else {
            return new ContentLengthOutputStream(outbuffer, len);
        }
    }

    /**
     * Writes out the content of the given HTTP entity to the session output
     * buffer based on properties of the given HTTP message.
     *
     * @param outbuffer the output session buffer.
     * @param message the HTTP message.
     * @param entity the HTTP entity to be written out.
     * @throws HttpException in case of HTTP protocol violation.
     * @throws IOException in case of an I/O error.
     */
    public void serialize(
            final SessionOutputBuffer outbuffer,
            final HttpMessage message,
            final HttpEntity entity) throws HttpException, IOException {
        Args.notNull(outbuffer, "Session output buffer");
        Args.notNull(message, "HTTP message");
        Args.notNull(entity, "HTTP entity");
        final OutputStream outstream = doSerialize(outbuffer, message);
        entity.writeTo(outstream);
        outstream.close();
    }

}
