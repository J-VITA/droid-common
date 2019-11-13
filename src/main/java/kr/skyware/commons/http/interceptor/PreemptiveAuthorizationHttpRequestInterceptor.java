package kr.skyware.commons.http.interceptor;

import java.io.IOException;

import kr.skyware.commons.http.client.CredentialsProvider;
import kr.skyware.commons.http.client.auth.AuthScope;
import kr.skyware.commons.http.client.auth.AuthState;
import kr.skyware.commons.http.client.auth.BasicScheme;
import kr.skyware.commons.http.client.auth.Credentials;
import kr.skyware.commons.http.client.protocol.ClientContext;
import kr.skyware.commons.http.client.protocol.ExecutionContext;
import kr.skyware.commons.http.exception.HttpException;
import kr.skyware.commons.http.header.HttpContext;
import kr.skyware.commons.http.header.HttpHost;
import kr.skyware.commons.http.header.HttpRequest;


public class PreemptiveAuthorizationHttpRequestInterceptor implements HttpRequestInterceptor {
    public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
        AuthState authState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);
        CredentialsProvider credsProvider = (CredentialsProvider) context.getAttribute(
                ClientContext.CREDS_PROVIDER);
        HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);

        if (authState.getAuthScheme() == null) {
            AuthScope authScope = new AuthScope(targetHost.getHostName(), targetHost.getPort());
            Credentials creds = credsProvider.getCredentials(authScope);
            if (creds != null) {
                authState.setAuthScheme(new BasicScheme());
                authState.setCredentials(creds);
            }
        }
    }
}
