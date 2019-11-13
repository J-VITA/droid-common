package kr.skyware.commons.http.cookie;

import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.header.HttpContext;
import kr.skyware.commons.http.header.HttpParams;

@Immutable
public class IgnoreSpecFactory implements CookieSpecFactory, CookieSpecProvider {

    public IgnoreSpecFactory() {
        super();
    }

    public CookieSpec newInstance(final HttpParams params) {
        return new IgnoreSpec();
    }

    public CookieSpec create(final HttpContext context) {
        return new IgnoreSpec();
    }

}
