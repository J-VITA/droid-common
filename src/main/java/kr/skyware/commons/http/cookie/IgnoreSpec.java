package kr.skyware.commons.http.cookie;

import java.util.Collections;
import java.util.List;

import kr.skyware.commons.http.annotation.NotThreadSafe;
import kr.skyware.commons.http.exception.MalformedCookieException;
import kr.skyware.commons.http.header.Header;

@NotThreadSafe // superclass is @NotThreadSafe
public class IgnoreSpec extends CookieSpecBase {

    public int getVersion() {
        return 0;
    }

    public List<Cookie> parse(final Header header, final CookieOrigin origin)
            throws MalformedCookieException {
        return Collections.emptyList();
    }

    public List<Header> formatCookies(final List<Cookie> cookies) {
        return Collections.emptyList();
    }

    public Header getVersionHeader() {
        return null;
    }
}
