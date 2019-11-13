package kr.skyware.commons.http.cookie;

import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.exception.MalformedCookieException;

@Immutable
public abstract class AbstractCookieAttributeHandler implements CookieAttributeHandler {

    public void validate(final Cookie cookie, final CookieOrigin origin)
            throws MalformedCookieException {
        // Do nothing
    }

    public boolean match(final Cookie cookie, final CookieOrigin origin) {
        // Always match
        return true;
    }

}
