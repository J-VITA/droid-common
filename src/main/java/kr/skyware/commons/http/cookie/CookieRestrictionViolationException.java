package kr.skyware.commons.http.cookie;

import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.exception.MalformedCookieException;

@Immutable
public class CookieRestrictionViolationException extends MalformedCookieException {

    /**
     * Creates a new CookeFormatViolationException with a <tt>null</tt> detail
     * message.
     */
    public CookieRestrictionViolationException() {
        super();
    }

    /**
     * Creates a new CookeRestrictionViolationException with a specified
     * message string.
     *
     * @param message The exception detail message
     */
    public CookieRestrictionViolationException(final String message) {
        super(message);
    }

}
