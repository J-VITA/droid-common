package kr.skyware.commons.http.client.impl;

import kr.skyware.commons.http.HttpResponse;
import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.connect.ConnectionReuseStrategy;
import kr.skyware.commons.http.header.HttpContext;

@Immutable
public class NoConnectionReuseStrategy implements ConnectionReuseStrategy {

    public static final NoConnectionReuseStrategy INSTANCE = new NoConnectionReuseStrategy();

    public NoConnectionReuseStrategy() {
        super();
    }

    public boolean keepAlive(final HttpResponse response, final HttpContext context) {
        return false;
    }

}
