package kr.skyware.commons.http.client.protocol;

public interface ClientContext {

    /**
     * Attribute name of a {@link kr.skyware.commons.http.header.RouteInfo}
     * object that represents the actual connection route.
     *
     * @since 4.3
     */
    public static final String ROUTE   = "http.route";

    /**
     * Attribute name of a {@link kr.skyware.commons.http.client.Scheme}
     * object that represents the actual protocol scheme registry.
     */
    @Deprecated
    public static final String SCHEME_REGISTRY   = "http.scheme-registry";

    /**
     * Attribute name of a {@link kr.skyware.commons.http.config.Lookup} object that represents
     * the actual {@link kr.skyware.commons.http.cookie.CookieSpecRegistry} registry.
     */
    public static final String COOKIESPEC_REGISTRY   = "http.cookiespec-registry";

    /**
     * Attribute name of a {@link kr.skyware.commons.http.cookie.CookieSpec}
     * object that represents the actual cookie specification.
     */
    public static final String COOKIE_SPEC           = "http.cookie-spec";

    /**
     * Attribute name of a {@link kr.skyware.commons.http.cookie.CookieOrigin}
     * object that represents the actual details of the origin server.
     */
    public static final String COOKIE_ORIGIN         = "http.cookie-origin";

    /**
     * Attribute name of a {@link kr.skyware.commons.http.cookie.CookieStore}
     * object that represents the actual cookie store.
     */
    public static final String COOKIE_STORE          = "http.cookie-store";

    /**
     * Attribute name of a {@link kr.skyware.commons.http.client.CredentialsProvider}
     * object that represents the actual credentials provider.
     */
    public static final String CREDS_PROVIDER        = "http.auth.credentials-provider";

    /**
     * Attribute name of a {@link kr.skyware.commons.http.client.auth.AuthCache} object
     * that represents the auth scheme cache.
     */
    public static final String AUTH_CACHE            = "http.auth.auth-cache";

    /**
     * Attribute name of a {@link kr.skyware.commons.http.client.auth.AuthState}
     * object that represents the actual target authentication state.
     */
    public static final String TARGET_AUTH_STATE     = "http.auth.target-scope";

    /**
     * Attribute name of a {@link kr.skyware.commons.http.client.auth.AuthState}
     * object that represents the actual proxy authentication state.
     */
    public static final String PROXY_AUTH_STATE      = "http.auth.proxy-scope";

    /**
     * @deprecated (4.1)  do not use
     */
    @Deprecated
    public static final String AUTH_SCHEME_PREF      = "http.auth.scheme-pref";

    /**
     * Attribute name of a {@link java.lang.Object} object that represents
     * the actual user identity such as user {@link java.security.Principal}.
     */
    public static final String USER_TOKEN            = "http.user-token";

    /**
     * Attribute name of a {@link kr.skyware.commons.http.config.Lookup} object that represents
     * the actual {@link kr.skyware.commons.http.client.auth.AuthSchemeRegistry} registry.
     */
    public static final String AUTHSCHEME_REGISTRY   = "http.authscheme-registry";

    public static final String SOCKET_FACTORY_REGISTRY = "http.socket-factory-registry";

    /**
     * Attribute name of a {@link kr.skyware.commons.http.config.RequestConfig} object that
     * represents the actual request configuration.
     *
     * @since 4.3
     */
    public static final String REQUEST_CONFIG = "http.request-config";

}
