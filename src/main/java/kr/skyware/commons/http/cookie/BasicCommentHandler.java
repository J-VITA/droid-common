package kr.skyware.commons.http.cookie;

import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.exception.MalformedCookieException;
import kr.skyware.commons.http.util.Args;

@Immutable
public class BasicCommentHandler extends AbstractCookieAttributeHandler {

    public BasicCommentHandler() {
        super();
    }

    public void parse(final SetCookie cookie, final String value)
            throws MalformedCookieException {
        Args.notNull(cookie, "Cookie");
        cookie.setComment(value);
    }

}
