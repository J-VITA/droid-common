package kr.skyware.commons.http.client;

import kr.skyware.commons.http.HttpResponse;
import kr.skyware.commons.http.client.auth.AuthState;
import kr.skyware.commons.http.connect.AuthenticationStrategy;
import kr.skyware.commons.http.header.HttpContext;
import kr.skyware.commons.http.header.HttpHost;
import kr.skyware.commons.http.util.HttpClientAndroidLog;

public class HttpAuthenticator extends kr.skyware.commons.http.client.auth.HttpAuthenticator {

    public HttpAuthenticator(final HttpClientAndroidLog log) {
        super(log);
    }

    public HttpAuthenticator() {
        super();
    }

    public boolean authenticate (
            final HttpHost host,
            final HttpResponse response,
            final AuthenticationStrategy authStrategy,
            final AuthState authState,
            final HttpContext context) {
        return handleAuthChallenge(host, response, authStrategy, authState, context);
    }

}