package kr.skyware.commons.http.client.auth;

import kr.skyware.commons.http.header.HttpContext;

/**
 * Factory for {@link AuthScheme} implementations.
 *
 * @since 4.3
 */
public interface AuthSchemeProvider {

    /**
     * Creates an instance of {@link AuthScheme}.
     *
     * @return auth scheme.
     */
    AuthScheme create(HttpContext context);

}
