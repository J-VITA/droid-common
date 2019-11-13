package kr.skyware.commons.http.client.auth;

import kr.skyware.commons.http.header.HttpParams;

public interface AuthSchemeFactory {

    /**
     * Creates an instance of {@link AuthScheme} using given HTTP parameters.
     *
     * @param params HTTP parameters.
     *
     * @return auth scheme.
     */
    AuthScheme newInstance(HttpParams params);

}
