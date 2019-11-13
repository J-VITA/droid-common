package kr.skyware.commons.http.cookie;

import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.exception.MalformedCookieException;
import kr.skyware.commons.http.util.Args;

@Immutable
public class BasicSecureHandler extends AbstractCookieAttributeHandler {

    public BasicSecureHandler() {
        super();
    }

    public void parse(final SetCookie cookie, final String value)
            throws MalformedCookieException {
        Args.notNull(cookie, "Cookie");
        cookie.setSecure(true);
    }

    @Override
    public boolean match(final Cookie cookie, final CookieOrigin origin) {
        Args.notNull(cookie, "Cookie");
        Args.notNull(origin, "Cookie origin");
        return !cookie.isSecure() || origin.isSecure();
    }

}
