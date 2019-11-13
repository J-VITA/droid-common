package kr.skyware.commons.http.client;

import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;

import kr.skyware.commons.http.HttpResponse;
import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.client.auth.AuthCache;
import kr.skyware.commons.http.client.auth.AuthOption;
import kr.skyware.commons.http.client.auth.AuthScheme;
import kr.skyware.commons.http.client.auth.AuthScope;
import kr.skyware.commons.http.client.auth.Credentials;
import kr.skyware.commons.http.client.impl.client.BasicAuthCache;
import kr.skyware.commons.http.connect.AuthenticationStrategy;
import kr.skyware.commons.http.exception.AuthenticationException;
import kr.skyware.commons.http.exception.MalformedChallengeException;
import kr.skyware.commons.http.header.Header;
import kr.skyware.commons.http.header.HttpContext;
import kr.skyware.commons.http.header.HttpHost;
import kr.skyware.commons.http.params.AuthPolicy;
import kr.skyware.commons.http.client.protocol.ClientContext;
import kr.skyware.commons.http.util.Args;
import kr.skyware.commons.http.util.HttpClientAndroidLog;

@Immutable
class AuthenticationStrategyAdaptor implements AuthenticationStrategy {

    public HttpClientAndroidLog log = new HttpClientAndroidLog(getClass());

    private final AuthenticationHandler handler;

    public AuthenticationStrategyAdaptor(final AuthenticationHandler handler) {
        super();
        this.handler = handler;
    }

    public boolean isAuthenticationRequested(
            final HttpHost authhost,
            final HttpResponse response,
            final HttpContext context) {
        return this.handler.isAuthenticationRequested(response, context);
    }

    public Map<String, Header> getChallenges(
            final HttpHost authhost,
            final HttpResponse response,
            final HttpContext context) throws MalformedChallengeException {
        return this.handler.getChallenges(response, context);
    }

    public Queue<AuthOption> select(
            final Map<String, Header> challenges,
            final HttpHost authhost,
            final HttpResponse response,
            final HttpContext context) throws MalformedChallengeException {
        Args.notNull(challenges, "Map of auth challenges");
        Args.notNull(authhost, "Host");
        Args.notNull(response, "HTTP response");
        Args.notNull(context, "HTTP context");

        final Queue<AuthOption> options = new LinkedList<AuthOption>();
        final CredentialsProvider credsProvider = (CredentialsProvider) context.getAttribute(
                ClientContext.CREDS_PROVIDER);
        if (credsProvider == null) {
            this.log.debug("Credentials provider not set in the context");
            return options;
        }

        final AuthScheme authScheme;
        try {
            authScheme = this.handler.selectScheme(challenges, response, context);
        } catch (final AuthenticationException ex) {
            if (this.log.isWarnEnabled()) {
                this.log.warn(ex.getMessage(), ex);
            }
            return options;
        }
        final String id = authScheme.getSchemeName();
        final Header challenge = challenges.get(id.toLowerCase(Locale.ENGLISH));
        authScheme.processChallenge(challenge);

        final AuthScope authScope = new AuthScope(
                authhost.getHostName(),
                authhost.getPort(),
                authScheme.getRealm(),
                authScheme.getSchemeName());

        final Credentials credentials = credsProvider.getCredentials(authScope);
        if (credentials != null) {
            options.add(new AuthOption(authScheme, credentials));
        }
        return options;
    }

    public void authSucceeded(
            final HttpHost authhost, final AuthScheme authScheme, final HttpContext context) {
        AuthCache authCache = (AuthCache) context.getAttribute(ClientContext.AUTH_CACHE);
        if (isCachable(authScheme)) {
            if (authCache == null) {
                authCache = new BasicAuthCache();
                context.setAttribute(ClientContext.AUTH_CACHE, authCache);
            }
            if (this.log.isDebugEnabled()) {
                this.log.debug("Caching '" + authScheme.getSchemeName() +
                        "' auth scheme for " + authhost);
            }
            authCache.put(authhost, authScheme);
        }
    }

    public void authFailed(
            final HttpHost authhost, final AuthScheme authScheme, final HttpContext context) {
        final AuthCache authCache = (AuthCache) context.getAttribute(ClientContext.AUTH_CACHE);
        if (authCache == null) {
            return;
        }
        if (this.log.isDebugEnabled()) {
            this.log.debug("Removing from cache '" + authScheme.getSchemeName() +
                    "' auth scheme for " + authhost);
        }
        authCache.remove(authhost);
    }

    private boolean isCachable(final AuthScheme authScheme) {
        if (authScheme == null || !authScheme.isComplete()) {
            return false;
        }
        final String schemeName = authScheme.getSchemeName();
        return schemeName.equalsIgnoreCase(AuthPolicy.BASIC) ||
                schemeName.equalsIgnoreCase(AuthPolicy.DIGEST);
    }

    public AuthenticationHandler getHandler() {
        return this.handler;
    }

}
