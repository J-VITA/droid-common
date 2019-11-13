package kr.skyware.commons.http.exception;

import kr.skyware.commons.http.annotation.Immutable;

@Immutable
public class NonRepeatableRequestException extends ProtocolException {

    private static final long serialVersionUID = 82685265288806048L;

    /**
     * Creates a new NonRepeatableEntityException with a <tt>null</tt> detail message.
     */
    public NonRepeatableRequestException() {
        super();
    }

    /**
     * Creates a new NonRepeatableEntityException with the specified detail message.
     *
     * @param message The exception detail message
     */
    public NonRepeatableRequestException(final String message) {
        super(message);
    }

    /**
     * Creates a new NonRepeatableEntityException with the specified detail message.
     *
     * @param message The exception detail message
     * @param cause the cause
     */
    public NonRepeatableRequestException(final String message, final Throwable cause) {
        super(message, cause);
    }



}
