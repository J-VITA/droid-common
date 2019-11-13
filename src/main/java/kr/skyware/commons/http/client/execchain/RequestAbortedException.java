package kr.skyware.commons.http.client.execchain;

import java.io.InterruptedIOException;

import kr.skyware.commons.http.annotation.Immutable;

@Immutable
public class RequestAbortedException extends InterruptedIOException {

    public RequestAbortedException(final String message) {
        super(message);
    }

    public RequestAbortedException(final String message, final Throwable cause) {
        super(message);
        if (cause != null) {
            initCause(cause);
        }
    }

}
