package kr.skyware.commons.http.client.impl.client;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import kr.skyware.commons.http.annotation.ThreadSafe;
import kr.skyware.commons.http.client.CredentialsProvider;
import kr.skyware.commons.http.client.auth.AuthSchemeProvider;
import kr.skyware.commons.http.client.auth.AuthState;
import kr.skyware.commons.http.client.execchain.ClientExecChain;
import kr.skyware.commons.http.concurrent.cancellable.HttpExecutionAware;
import kr.skyware.commons.http.config.Lookup;
import kr.skyware.commons.http.config.RequestConfig;
import kr.skyware.commons.http.connect.ClientConnectionRequest;
import kr.skyware.commons.http.connect.HttpClientConnectionManager;
import kr.skyware.commons.http.connect.route.HttpRoutePlanner;
import kr.skyware.commons.http.cookie.CookieSpecProvider;
import kr.skyware.commons.http.cookie.CookieStore;
import kr.skyware.commons.http.exception.ClientProtocolException;
import kr.skyware.commons.http.exception.HttpException;
import kr.skyware.commons.http.execute.BasicHttpContext;
import kr.skyware.commons.http.execute.CloseableHttpResponse;
import kr.skyware.commons.http.factory.SchemeRegistry;
import kr.skyware.commons.http.header.ClientConnectionManager;
import kr.skyware.commons.http.header.HttpContext;
import kr.skyware.commons.http.header.HttpHost;
import kr.skyware.commons.http.header.HttpParams;
import kr.skyware.commons.http.header.HttpParamsNames;
import kr.skyware.commons.http.header.HttpRequest;
import kr.skyware.commons.http.header.HttpRoute;
import kr.skyware.commons.http.header.ManagedClientConnection;
import kr.skyware.commons.http.method.Configurable;
import kr.skyware.commons.http.method.HttpRequestWrapper;
import kr.skyware.commons.http.params.ClientPNames;
import kr.skyware.commons.http.params.HttpClientParamConfig;
import kr.skyware.commons.http.client.protocol.HttpClientContext;
import kr.skyware.commons.http.util.Args;
import kr.skyware.commons.http.util.HttpClientAndroidLog;

@ThreadSafe
class InternalHttpClient extends CloseableHttpClient {

    public HttpClientAndroidLog log = new HttpClientAndroidLog(getClass());

    private final ClientExecChain execChain;
    private final HttpClientConnectionManager connManager;
    private final HttpRoutePlanner routePlanner;
    private final Lookup<CookieSpecProvider> cookieSpecRegistry;
    private final Lookup<AuthSchemeProvider> authSchemeRegistry;
    private final CookieStore cookieStore;
    private final CredentialsProvider credentialsProvider;
    private final RequestConfig defaultConfig;
    private final List<Closeable> closeables;

    public InternalHttpClient(
            final ClientExecChain execChain,
            final HttpClientConnectionManager connManager,
            final HttpRoutePlanner routePlanner,
            final Lookup<CookieSpecProvider> cookieSpecRegistry,
            final Lookup<AuthSchemeProvider> authSchemeRegistry,
            final CookieStore cookieStore,
            final CredentialsProvider credentialsProvider,
            final RequestConfig defaultConfig,
            final List<Closeable> closeables) {
        super();
        Args.notNull(execChain, "HTTP client exec chain");
        Args.notNull(connManager, "HTTP connection manager");
        Args.notNull(routePlanner, "HTTP route planner");
        this.execChain = execChain;
        this.connManager = connManager;
        this.routePlanner = routePlanner;
        this.cookieSpecRegistry = cookieSpecRegistry;
        this.authSchemeRegistry = authSchemeRegistry;
        this.cookieStore = cookieStore;
        this.credentialsProvider = credentialsProvider;
        this.defaultConfig = defaultConfig;
        this.closeables = closeables;
    }

    private HttpRoute determineRoute(
            final HttpHost target,
            final HttpRequest request,
            final HttpContext context) throws HttpException {
        HttpHost host = target;
        if (host == null) {
            host = (HttpHost) request.getParams().getParameter(ClientPNames.DEFAULT_HOST);
        }
        return this.routePlanner.determineRoute(host, request, context);
    }

    private void setupContext(final HttpClientContext context) {
        if (context.getAttribute(HttpClientContext.TARGET_AUTH_STATE) == null) {
            context.setAttribute(HttpClientContext.TARGET_AUTH_STATE, new AuthState());
        }
        if (context.getAttribute(HttpClientContext.PROXY_AUTH_STATE) == null) {
            context.setAttribute(HttpClientContext.PROXY_AUTH_STATE, new AuthState());
        }
        if (context.getAttribute(HttpClientContext.AUTHSCHEME_REGISTRY) == null) {
            context.setAttribute(HttpClientContext.AUTHSCHEME_REGISTRY, this.authSchemeRegistry);
        }
        if (context.getAttribute(HttpClientContext.COOKIESPEC_REGISTRY) == null) {
            context.setAttribute(HttpClientContext.COOKIESPEC_REGISTRY, this.cookieSpecRegistry);
        }
        if (context.getAttribute(HttpClientContext.COOKIE_STORE) == null) {
            context.setAttribute(HttpClientContext.COOKIE_STORE, this.cookieStore);
        }
        if (context.getAttribute(HttpClientContext.CREDS_PROVIDER) == null) {
            context.setAttribute(HttpClientContext.CREDS_PROVIDER, this.credentialsProvider);
        }
        if (context.getAttribute(HttpClientContext.REQUEST_CONFIG) == null) {
            context.setAttribute(HttpClientContext.REQUEST_CONFIG, this.defaultConfig);
        }
    }

    @Override
    protected CloseableHttpResponse doExecute(
            final HttpHost target,
            final HttpRequest request,
            final HttpContext context) throws IOException, ClientProtocolException {
        Args.notNull(request, "HTTP request");
        HttpExecutionAware execAware = null;
        if (request instanceof HttpExecutionAware) {
            execAware = (HttpExecutionAware) request;
        }
        try {
            final HttpRequestWrapper wrapper = HttpRequestWrapper.wrap(request);
            final HttpClientContext localcontext = HttpClientContext.adapt(
                    context != null ? context : new BasicHttpContext());
            RequestConfig config = null;
            if (request instanceof Configurable) {
                config = ((Configurable) request).getConfig();
            }
            if (config == null) {
                final HttpParams params = request.getParams();
                if (params instanceof HttpParamsNames) {
                    if (!((HttpParamsNames) params).getNames().isEmpty()) {
                        config = HttpClientParamConfig.getRequestConfig(params);
                    }
                } else {
                    config = HttpClientParamConfig.getRequestConfig(params);
                }
            }
            if (config != null) {
                localcontext.setRequestConfig(config);
            }
            setupContext(localcontext);
            final HttpRoute route = determineRoute(target, wrapper, localcontext);
            return this.execChain.execute(route, wrapper, localcontext, execAware);
        } catch (final HttpException httpException) {
            throw new ClientProtocolException(httpException);
        }
    }

    public void close() {
        this.connManager.shutdown();
        if (this.closeables != null) {
            for (final Closeable closeable: this.closeables) {
                try {
                    closeable.close();
                } catch (final IOException ex) {
                    this.log.error(ex.getMessage(), ex);
                }
            }
        }
    }

    public HttpParams getParams() {
        throw new UnsupportedOperationException();
    }

    public ClientConnectionManager getConnectionManager() {

        return new ClientConnectionManager() {

            public void shutdown() {
                connManager.shutdown();
            }

            public ClientConnectionRequest requestConnection(
                    final HttpRoute route, final Object state) {
                throw new UnsupportedOperationException();
            }

            public void releaseConnection(
                    final ManagedClientConnection conn,
                    final long validDuration, final TimeUnit timeUnit) {
                throw new UnsupportedOperationException();
            }

            public SchemeRegistry getSchemeRegistry() {
                throw new UnsupportedOperationException();
            }

            public void closeIdleConnections(final long idletime, final TimeUnit tunit) {
                connManager.closeIdleConnections(idletime, tunit);
            }

            public void closeExpiredConnections() {
                connManager.closeExpiredConnections();
            }

        };

    }

}
