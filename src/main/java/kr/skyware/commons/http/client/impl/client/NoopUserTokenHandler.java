package kr.skyware.commons.http.client.impl.client;

import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.client.UserTokenHandler;
import kr.skyware.commons.http.header.HttpContext;

@Immutable
public class NoopUserTokenHandler implements UserTokenHandler {

    public static final NoopUserTokenHandler INSTANCE = new NoopUserTokenHandler();

    public Object getUserToken(final HttpContext context) {
        return null;
    }

}
