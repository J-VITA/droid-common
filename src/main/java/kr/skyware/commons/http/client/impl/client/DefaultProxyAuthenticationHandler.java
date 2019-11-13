package kr.skyware.commons.http.client.impl.client;

import java.util.List;
import java.util.Map;

import kr.skyware.commons.http.HttpResponse;
import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.client.auth.AUTH;
import kr.skyware.commons.http.client.auth.params.AuthPNames;
import kr.skyware.commons.http.exception.MalformedChallengeException;
import kr.skyware.commons.http.header.Header;
import kr.skyware.commons.http.header.HttpContext;
import kr.skyware.commons.http.util.Args;
import kr.skyware.commons.http.util.HttpStatus;

@Immutable
public class DefaultProxyAuthenticationHandler extends AbstractAuthenticationHandler {

    public DefaultProxyAuthenticationHandler() {
        super();
    }

    public boolean isAuthenticationRequested(
            final HttpResponse response,
            final HttpContext context) {
        Args.notNull(response, "HTTP response");
        final int status = response.getStatusLine().getStatusCode();
        return status == HttpStatus.SC_PROXY_AUTHENTICATION_REQUIRED;
    }

    public Map<String, Header> getChallenges(
            final HttpResponse response,
            final HttpContext context) throws MalformedChallengeException {
        Args.notNull(response, "HTTP response");
        final Header[] headers = response.getHeaders(AUTH.PROXY_AUTH);
        return parseChallenges(headers);
    }

    @Override
    protected List<String> getAuthPreferences(
            final HttpResponse response,
            final HttpContext context) {
        @SuppressWarnings("unchecked")
        final
        List<String> authpref = (List<String>) response.getParams().getParameter(
                AuthPNames.PROXY_AUTH_PREF);
        if (authpref != null) {
            return authpref;
        } else {
            return super.getAuthPreferences(response, context);
        }
    }

}
