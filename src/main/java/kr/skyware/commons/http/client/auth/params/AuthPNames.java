package kr.skyware.commons.http.client.auth.params;

public interface AuthPNames {

    /**
     * Defines the charset to be used when encoding
     * {@link kr.skyware.commons.http.client.auth.Credentials}.
     * <p>
     * This parameter expects a value of type {@link String}.
     */
    public static final String CREDENTIAL_CHARSET = "http.auth.credential-charset";

    /**
     * Defines the order of preference for supported
     *  {@link kr.skyware.commons.http.client.auth.AuthScheme}s when authenticating with
     *  the target host.
     * <p>
     * This parameter expects a value of type {@link java.util.Collection}. The
     * collection is expected to contain {@link String} instances representing
     * a name of an authentication scheme as returned by
     * {@link kr.skyware.commons.http.client.auth.AuthScheme#getSchemeName()}.
     */
    public static final String TARGET_AUTH_PREF = "http.auth.target-scheme-pref";

    /**
     * Defines the order of preference for supported
     *  {@link kr.skyware.commons.http.client.auth.AuthScheme}s when authenticating with the
     *  proxy host.
     * <p>
     * This parameter expects a value of type {@link java.util.Collection}. The
     * collection is expected to contain {@link String} instances representing
     * a name of an authentication scheme as returned by
     * {@link kr.skyware.commons.http.client.auth.AuthScheme#getSchemeName()}.
     */
    public static final String PROXY_AUTH_PREF = "http.auth.proxy-scheme-pref";

}
