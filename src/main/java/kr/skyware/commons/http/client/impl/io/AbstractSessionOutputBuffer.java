package kr.skyware.commons.http.client.impl.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

import kr.skyware.commons.http.annotation.NotThreadSafe;
import kr.skyware.commons.http.header.HttpParams;
import kr.skyware.commons.http.io.BufferInfo;
import kr.skyware.commons.http.io.HttpTransportMetrics;
import kr.skyware.commons.http.io.HttpTransportMetricsImpl;
import kr.skyware.commons.http.io.SessionOutputBuffer;
import kr.skyware.commons.http.params.CoreConnectionPNames;
import kr.skyware.commons.http.params.CoreProtocolPNames;
import kr.skyware.commons.http.util.Args;
import kr.skyware.commons.http.util.ByteArrayBuffer;
import kr.skyware.commons.http.util.CharArrayBuffer;
import kr.skyware.commons.http.util.Consts;
import kr.skyware.commons.http.util.HTTP;

@NotThreadSafe
public abstract class AbstractSessionOutputBuffer implements SessionOutputBuffer, BufferInfo {

    private static final byte[] CRLF = new byte[] {HTTP.CR, HTTP.LF};

    private OutputStream outstream;
    private ByteArrayBuffer buffer;
    private Charset charset;
    private boolean ascii;
    private int minChunkLimit;
    private HttpTransportMetricsImpl metrics;
    private CodingErrorAction onMalformedCharAction;
    private CodingErrorAction onUnmappableCharAction;

    private CharsetEncoder encoder;
    private ByteBuffer bbuf;

    protected AbstractSessionOutputBuffer(
            final OutputStream outstream,
            final int buffersize,
            final Charset charset,
            final int minChunkLimit,
            final CodingErrorAction malformedCharAction,
            final CodingErrorAction unmappableCharAction) {
        super();
        Args.notNull(outstream, "Input stream");
        Args.notNegative(buffersize, "Buffer size");
        this.outstream = outstream;
        this.buffer = new ByteArrayBuffer(buffersize);
        this.charset = charset != null ? charset : Consts.ASCII;
        this.ascii = this.charset.equals(Consts.ASCII);
        this.encoder = null;
        this.minChunkLimit = minChunkLimit >= 0 ? minChunkLimit : 512;
        this.metrics = createTransportMetrics();
        this.onMalformedCharAction = malformedCharAction != null ? malformedCharAction :
                CodingErrorAction.REPORT;
        this.onUnmappableCharAction = unmappableCharAction != null? unmappableCharAction :
                CodingErrorAction.REPORT;
    }

    public AbstractSessionOutputBuffer() {
    }

    protected void init(final OutputStream outstream, final int buffersize, final HttpParams params) {
        Args.notNull(outstream, "Input stream");
        Args.notNegative(buffersize, "Buffer size");
        Args.notNull(params, "HTTP parameters");
        this.outstream = outstream;
        this.buffer = new ByteArrayBuffer(buffersize);
        final String charset = (String) params.getParameter(CoreProtocolPNames.HTTP_ELEMENT_CHARSET);
        this.charset = charset != null ? Charset.forName(charset) : Consts.ASCII;
        this.ascii = this.charset.equals(Consts.ASCII);
        this.encoder = null;
        this.minChunkLimit = params.getIntParameter(CoreConnectionPNames.MIN_CHUNK_LIMIT, 512);
        this.metrics = createTransportMetrics();
        final CodingErrorAction a1 = (CodingErrorAction) params.getParameter(
                CoreProtocolPNames.HTTP_MALFORMED_INPUT_ACTION);
        this.onMalformedCharAction = a1 != null ? a1 : CodingErrorAction.REPORT;
        final CodingErrorAction a2 = (CodingErrorAction) params.getParameter(
                CoreProtocolPNames.HTTP_UNMAPPABLE_INPUT_ACTION);
        this.onUnmappableCharAction = a2 != null? a2 : CodingErrorAction.REPORT;
    }

    /**
     * @since 4.1
     */
    protected HttpTransportMetricsImpl createTransportMetrics() {
        return new HttpTransportMetricsImpl();
    }

    /**
     * @since 4.1
     */
    public int capacity() {
        return this.buffer.capacity();
    }

    /**
     * @since 4.1
     */
    public int length() {
        return this.buffer.length();
    }

    /**
     * @since 4.1
     */
    public int available() {
        return capacity() - length();
    }

    protected void flushBuffer() throws IOException {
        final int len = this.buffer.length();
        if (len > 0) {
            this.outstream.write(this.buffer.buffer(), 0, len);
            this.buffer.clear();
            this.metrics.incrementBytesTransferred(len);
        }
    }

    public void flush() throws IOException {
        flushBuffer();
        this.outstream.flush();
    }

    public void write(final byte[] b, final int off, final int len) throws IOException {
        if (b == null) {
            return;
        }
        // Do not want to buffer large-ish chunks
        // if the byte array is larger then MIN_CHUNK_LIMIT
        // write it directly to the output stream
        if (len > this.minChunkLimit || len > this.buffer.capacity()) {
            // flush the buffer
            flushBuffer();
            // write directly to the out stream
            this.outstream.write(b, off, len);
            this.metrics.incrementBytesTransferred(len);
        } else {
            // Do not let the buffer grow unnecessarily
            final int freecapacity = this.buffer.capacity() - this.buffer.length();
            if (len > freecapacity) {
                // flush the buffer
                flushBuffer();
            }
            // buffer
            this.buffer.append(b, off, len);
        }
    }

    public void write(final byte[] b) throws IOException {
        if (b == null) {
            return;
        }
        write(b, 0, b.length);
    }

    public void write(final int b) throws IOException {
        if (this.buffer.isFull()) {
            flushBuffer();
        }
        this.buffer.append(b);
    }

    /**
     * Writes characters from the specified string followed by a line delimiter
     * to this session buffer.
     * <p>
     * This method uses CR-LF as a line delimiter.
     *
     * @param      s   the line.
     * @exception  IOException  if an I/O error occurs.
     */
    public void writeLine(final String s) throws IOException {
        if (s == null) {
            return;
        }
        if (s.length() > 0) {
            if (this.ascii) {
                for (int i = 0; i < s.length(); i++) {
                    write(s.charAt(i));
                }
            } else {
                final CharBuffer cbuf = CharBuffer.wrap(s);
                writeEncoded(cbuf);
            }
        }
        write(CRLF);
    }

    /**
     * Writes characters from the specified char array followed by a line
     * delimiter to this session buffer.
     * <p>
     * This method uses CR-LF as a line delimiter.
     *
     * @param      charbuffer the buffer containing chars of the line.
     * @exception  IOException  if an I/O error occurs.
     */
    public void writeLine(final CharArrayBuffer charbuffer) throws IOException {
        if (charbuffer == null) {
            return;
        }
        if (this.ascii) {
            int off = 0;
            int remaining = charbuffer.length();
            while (remaining > 0) {
                int chunk = this.buffer.capacity() - this.buffer.length();
                chunk = Math.min(chunk, remaining);
                if (chunk > 0) {
                    this.buffer.append(charbuffer, off, chunk);
                }
                if (this.buffer.isFull()) {
                    flushBuffer();
                }
                off += chunk;
                remaining -= chunk;
            }
        } else {
            final CharBuffer cbuf = CharBuffer.wrap(charbuffer.buffer(), 0, charbuffer.length());
            writeEncoded(cbuf);
        }
        write(CRLF);
    }

    private void writeEncoded(final CharBuffer cbuf) throws IOException {
        if (!cbuf.hasRemaining()) {
            return;
        }
        if (this.encoder == null) {
            this.encoder = this.charset.newEncoder();
            this.encoder.onMalformedInput(this.onMalformedCharAction);
            this.encoder.onUnmappableCharacter(this.onUnmappableCharAction);
        }
        if (this.bbuf == null) {
            this.bbuf = ByteBuffer.allocate(1024);
        }
        this.encoder.reset();
        while (cbuf.hasRemaining()) {
            final CoderResult result = this.encoder.encode(cbuf, this.bbuf, true);
            handleEncodingResult(result);
        }
        final CoderResult result = this.encoder.flush(this.bbuf);
        handleEncodingResult(result);
        this.bbuf.clear();
    }

    private void handleEncodingResult(final CoderResult result) throws IOException {
        if (result.isError()) {
            result.throwException();
        }
        this.bbuf.flip();
        while (this.bbuf.hasRemaining()) {
            write(this.bbuf.get());
        }
        this.bbuf.compact();
    }

    public HttpTransportMetrics getMetrics() {
        return this.metrics;
    }

}
