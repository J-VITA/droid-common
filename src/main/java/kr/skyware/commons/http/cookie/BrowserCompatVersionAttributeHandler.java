package kr.skyware.commons.http.cookie;

import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.exception.MalformedCookieException;
import kr.skyware.commons.http.util.Args;

@Immutable
public class BrowserCompatVersionAttributeHandler extends
        AbstractCookieAttributeHandler {

    public BrowserCompatVersionAttributeHandler() {
        super();
    }

    /**
     * Parse cookie version attribute.
     */
    public void parse(final SetCookie cookie, final String value)
            throws MalformedCookieException {
        Args.notNull(cookie, "Cookie");
        if (value == null) {
            throw new MalformedCookieException("Missing value for version attribute");
        }
        int version = 0;
        try {
            version = Integer.parseInt(value);
        } catch (final NumberFormatException e) {
            // Just ignore invalid versions
        }
        cookie.setVersion(version);
    }

}
