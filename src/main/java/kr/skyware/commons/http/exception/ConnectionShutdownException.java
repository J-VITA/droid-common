package kr.skyware.commons.http.exception;

import kr.skyware.commons.http.annotation.Immutable;

@Immutable
public class ConnectionShutdownException extends IllegalStateException {

    private static final long serialVersionUID = 5868657401162844497L;

    /**
     * Creates a new ConnectionShutdownException with a <tt>null</tt> detail message.
     */
    public ConnectionShutdownException() {
        super();
    }

}