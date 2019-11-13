package kr.skyware.commons.http.cookie;

import kr.skyware.commons.http.header.HttpParams;

public interface CookieSpecFactory {

    /**
     * Creates an instance of {@link CookieSpec} using given HTTP parameters.
     *
     * @param params HTTP parameters.
     *
     * @return cookie spec.
     */
    CookieSpec newInstance(HttpParams params);

}
