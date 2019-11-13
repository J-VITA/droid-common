package kr.skyware.commons.http.exception;

import kr.skyware.commons.http.annotation.Immutable;

@Immutable
public class CircularRedirectException extends RedirectException {

    /**
     * Creates a new CircularRedirectException with a <tt>null</tt> detail message.
     */
    public CircularRedirectException() {
        super();
    }

    /**
     * Creates a new CircularRedirectException with the specified detail message.
     *
     * @param message The exception detail message
     */
    public CircularRedirectException(final String message) {
        super(message);
    }

    /**
     * Creates a new CircularRedirectException with the specified detail message and cause.
     *
     * @param message the exception detail message
     * @param cause the <tt>Throwable</tt> that caused this exception, or <tt>null</tt>
     * if the cause is unavailable, unknown, or not a <tt>Throwable</tt>
     */
    public CircularRedirectException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
