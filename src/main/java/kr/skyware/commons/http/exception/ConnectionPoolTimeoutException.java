package kr.skyware.commons.http.exception;

import kr.skyware.commons.http.annotation.Immutable;

@Immutable
public class ConnectionPoolTimeoutException extends ConnectTimeoutException {

    private static final long serialVersionUID = -7898874842020245128L;

    /**
     * Creates a ConnectTimeoutException with a <tt>null</tt> detail message.
     */
    public ConnectionPoolTimeoutException() {
        super();
    }

    /**
     * Creates a ConnectTimeoutException with the specified detail message.
     *
     * @param message The exception detail message
     */
    public ConnectionPoolTimeoutException(final String message) {
        super(message);
    }

}
