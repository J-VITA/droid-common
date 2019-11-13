package kr.skyware.commons.http.client.impl.client;

import java.util.HashMap;

import kr.skyware.commons.http.annotation.NotThreadSafe;
import kr.skyware.commons.http.client.auth.AuthCache;
import kr.skyware.commons.http.client.auth.AuthScheme;
import kr.skyware.commons.http.connect.DefaultSchemePortResolver;
import kr.skyware.commons.http.connect.SchemePortResolver;
import kr.skyware.commons.http.exception.UnsupportedSchemeException;
import kr.skyware.commons.http.header.HttpHost;
import kr.skyware.commons.http.util.Args;

@NotThreadSafe
public class BasicAuthCache implements AuthCache {

    private final HashMap<HttpHost, AuthScheme> map;
    private final SchemePortResolver schemePortResolver;

    /**
     * Default constructor.
     *
     * @since 4.3
     */
    public BasicAuthCache(final SchemePortResolver schemePortResolver) {
        super();
        this.map = new HashMap<HttpHost, AuthScheme>();
        this.schemePortResolver = schemePortResolver != null ? schemePortResolver :
                DefaultSchemePortResolver.INSTANCE;
    }

    public BasicAuthCache() {
        this(null);
    }

    protected HttpHost getKey(final HttpHost host) {
        if (host.getPort() <= 0) {
            final int port;
            try {
                port = schemePortResolver.resolve(host);
            } catch (final UnsupportedSchemeException ignore) {
                return host;
            }
            return new HttpHost(host.getHostName(), port, host.getSchemeName());
        } else {
            return host;
        }
    }

    public void put(final HttpHost host, final AuthScheme authScheme) {
        Args.notNull(host, "HTTP host");
        this.map.put(getKey(host), authScheme);
    }

    public AuthScheme get(final HttpHost host) {
        Args.notNull(host, "HTTP host");
        return this.map.get(getKey(host));
    }

    public void remove(final HttpHost host) {
        Args.notNull(host, "HTTP host");
        this.map.remove(getKey(host));
    }

    public void clear() {
        this.map.clear();
    }

    @Override
    public String toString() {
        return this.map.toString();
    }

}
