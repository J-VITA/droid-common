package kr.skyware.commons.http.client.impl.io;

import java.io.IOException;
import java.net.Socket;

import kr.skyware.commons.http.annotation.NotThreadSafe;
import kr.skyware.commons.http.header.HttpParams;
import kr.skyware.commons.http.util.Args;

@NotThreadSafe
public class SocketOutputBuffer extends AbstractSessionOutputBuffer {

    /**
     * Creates an instance of this class.
     *
     * @param socket the socket to write data to.
     * @param buffersize the size of the internal buffer. If this number is less
     *   than <code>0</code> it is set to the value of
     *   {@link Socket#getSendBufferSize()}. If resultant number is less
     *   than <code>1024</code> it is set to <code>1024</code>.
     * @param params HTTP parameters.
     */
    public SocketOutputBuffer(
            final Socket socket,
            final int buffersize,
            final HttpParams params) throws IOException {
        super();
        Args.notNull(socket, "Socket");
        int n = buffersize;
        if (n < 0) {
            n = socket.getSendBufferSize();
        }
        if (n < 1024) {
            n = 1024;
        }
        init(socket.getOutputStream(), n, params);
    }

}
