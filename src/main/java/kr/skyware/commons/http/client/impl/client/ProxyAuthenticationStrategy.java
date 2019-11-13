package kr.skyware.commons.http.client.impl.client;

import java.util.Collection;

import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.client.auth.AUTH;
import kr.skyware.commons.http.config.RequestConfig;
import kr.skyware.commons.http.util.HttpStatus;

@Immutable
public class ProxyAuthenticationStrategy extends AuthenticationStrategyImpl {

    public static final ProxyAuthenticationStrategy INSTANCE = new ProxyAuthenticationStrategy();

    public ProxyAuthenticationStrategy() {
        super(HttpStatus.SC_PROXY_AUTHENTICATION_REQUIRED, AUTH.PROXY_AUTH);
    }

    @Override
    Collection<String> getPreferredAuthSchemes(final RequestConfig config) {
        return config.getProxyPreferredAuthSchemes();
    }

}
