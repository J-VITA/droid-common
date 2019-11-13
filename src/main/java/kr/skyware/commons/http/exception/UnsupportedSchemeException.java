package kr.skyware.commons.http.exception;

import java.io.IOException;

import kr.skyware.commons.http.annotation.Immutable;

@Immutable
public class UnsupportedSchemeException extends IOException {

    private static final long serialVersionUID = 3597127619218687636L;

    /**
     * Creates a UnsupportedSchemeException with the specified detail message.
     */
    public UnsupportedSchemeException(final String message) {
        super(message);
    }

}