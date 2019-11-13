package kr.skyware.commons.http.cookie;

import kr.skyware.commons.http.header.HttpContext;

/**
 * Factory for {@link CookieSpec} implementations.
 *
 * @since 4.3
 */
public interface CookieSpecProvider {

    /**
     * Creates an instance of {@link CookieSpec}.
     *
     * @return auth scheme.
     */
    CookieSpec create(HttpContext context);

}
