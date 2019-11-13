package kr.skyware.commons.http.cookie;

import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.exception.MalformedCookieException;

@Immutable
public class RFC2965DiscardAttributeHandler implements CookieAttributeHandler {

    public RFC2965DiscardAttributeHandler() {
        super();
    }

    public void parse(final SetCookie cookie, final String commenturl)
            throws MalformedCookieException {
        if (cookie instanceof SetCookie2) {
            final SetCookie2 cookie2 = (SetCookie2) cookie;
            cookie2.setDiscard(true);
        }
    }

    public void validate(final Cookie cookie, final CookieOrigin origin)
            throws MalformedCookieException {
    }

    public boolean match(final Cookie cookie, final CookieOrigin origin) {
        return true;
    }

}