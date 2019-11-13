package kr.skyware.commons.http.client.impl.client;

import java.util.Collection;

import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.client.auth.AUTH;
import kr.skyware.commons.http.config.RequestConfig;
import kr.skyware.commons.http.util.HttpStatus;

@Immutable
public class TargetAuthenticationStrategy extends AuthenticationStrategyImpl {

    public static final TargetAuthenticationStrategy INSTANCE = new TargetAuthenticationStrategy();

    public TargetAuthenticationStrategy() {
        super(HttpStatus.SC_UNAUTHORIZED, AUTH.WWW_AUTH);
    }

    @Override
    Collection<String> getPreferredAuthSchemes(final RequestConfig config) {
        return config.getTargetPreferredAuthSchemes();
    }

}
