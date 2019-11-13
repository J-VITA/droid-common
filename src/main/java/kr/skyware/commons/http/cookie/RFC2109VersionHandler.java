package kr.skyware.commons.http.cookie;

import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.exception.MalformedCookieException;
import kr.skyware.commons.http.util.Args;

@Immutable
public class RFC2109VersionHandler extends AbstractCookieAttributeHandler {

    public RFC2109VersionHandler() {
        super();
    }

    public void parse(final SetCookie cookie, final String value)
            throws MalformedCookieException {
        Args.notNull(cookie, "Cookie");
        if (value == null) {
            throw new MalformedCookieException("Missing value for version attribute");
        }
        if (value.trim().length() == 0) {
            throw new MalformedCookieException("Blank value for version attribute");
        }
        try {
            cookie.setVersion(Integer.parseInt(value));
        } catch (final NumberFormatException e) {
            throw new MalformedCookieException("Invalid version: "
                    + e.getMessage());
        }
    }

    @Override
    public void validate(final Cookie cookie, final CookieOrigin origin)
            throws MalformedCookieException {
        Args.notNull(cookie, "Cookie");
        if (cookie.getVersion() < 0) {
            throw new CookieRestrictionViolationException("Cookie version may not be negative");
        }
    }

}
