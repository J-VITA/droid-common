package kr.skyware.commons.http.client.protocol;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.config.CookieSpecs;
import kr.skyware.commons.http.config.Lookup;
import kr.skyware.commons.http.config.RequestConfig;
import kr.skyware.commons.http.cookie.Cookie;
import kr.skyware.commons.http.cookie.CookieOrigin;
import kr.skyware.commons.http.cookie.CookieSpec;
import kr.skyware.commons.http.cookie.CookieSpecProvider;
import kr.skyware.commons.http.cookie.CookieStore;
import kr.skyware.commons.http.cookie.SetCookie2;
import kr.skyware.commons.http.exception.HttpException;
import kr.skyware.commons.http.header.Header;
import kr.skyware.commons.http.header.HttpContext;
import kr.skyware.commons.http.header.HttpHost;
import kr.skyware.commons.http.header.HttpRequest;
import kr.skyware.commons.http.header.HttpUriRequest;
import kr.skyware.commons.http.header.RouteInfo;
import kr.skyware.commons.http.interceptor.HttpRequestInterceptor;
import kr.skyware.commons.http.util.Args;
import kr.skyware.commons.http.util.HttpClientAndroidLog;
import kr.skyware.commons.util.TextUtils;

@Immutable
public class RequestAddCookies implements HttpRequestInterceptor {

    public HttpClientAndroidLog log = new HttpClientAndroidLog(getClass());

    public RequestAddCookies() {
        super();
    }

    public void process(final HttpRequest request, final HttpContext context)
            throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        Args.notNull(context, "HTTP context");

        final String method = request.getRequestLine().getMethod();
        if (method.equalsIgnoreCase("CONNECT")) {
            return;
        }

        final HttpClientContext clientContext = HttpClientContext.adapt(context);

        // Obtain cookie store
        final CookieStore cookieStore = clientContext.getCookieStore();
        if (cookieStore == null) {
            this.log.debug("Cookie store not specified in HTTP context");
            return;
        }

        // Obtain the registry of cookie specs
        final Lookup<CookieSpecProvider> registry = clientContext.getCookieSpecRegistry();
        if (registry == null) {
            this.log.debug("CookieSpec registry not specified in HTTP context");
            return;
        }

        // Obtain the target host, possibly virtual (required)
        final HttpHost targetHost = clientContext.getTargetHost();
        if (targetHost == null) {
            this.log.debug("Target host not set in the context");
            return;
        }

        // Obtain the route (required)
        final RouteInfo route = clientContext.getHttpRoute();
        if (route == null) {
            this.log.debug("Connection route not set in the context");
            return;
        }

        final RequestConfig config = clientContext.getRequestConfig();
        String policy = config.getCookieSpec();
        if (policy == null) {
            policy = CookieSpecs.BEST_MATCH;
        }
        if (this.log.isDebugEnabled()) {
            this.log.debug("CookieSpec selected: " + policy);
        }

        URI requestURI = null;
        if (request instanceof HttpUriRequest) {
            requestURI = ((HttpUriRequest) request).getURI();
        } else {
            try {
                requestURI = new URI(request.getRequestLine().getUri());
            } catch (final URISyntaxException ignore) {
            }
        }
        final String path = requestURI != null ? requestURI.getPath() : null;
        final String hostName = targetHost.getHostName();
        int port = targetHost.getPort();
        if (port < 0) {
            port = route.getTargetHost().getPort();
        }

        final CookieOrigin cookieOrigin = new CookieOrigin(
                hostName,
                port >= 0 ? port : 0,
                !TextUtils.isEmpty(path) ? path : "/",
                route.isSecure());

        // Get an instance of the selected cookie policy
        final CookieSpecProvider provider = registry.lookup(policy);
        if (provider == null) {
            throw new HttpException("Unsupported cookie policy: " + policy);
        }
        final CookieSpec cookieSpec = provider.create(clientContext);
        // Get all cookies available in the HTTP state
        final List<Cookie> cookies = new ArrayList<Cookie>(cookieStore.getCookies());
        // Find cookies matching the given origin
        final List<Cookie> matchedCookies = new ArrayList<Cookie>();
        final Date now = new Date();
        for (final Cookie cookie : cookies) {
            if (!cookie.isExpired(now)) {
                if (cookieSpec.match(cookie, cookieOrigin)) {
                    if (this.log.isDebugEnabled()) {
                        this.log.debug("Cookie " + cookie + " match " + cookieOrigin);
                    }
                    matchedCookies.add(cookie);
                }
            } else {
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Cookie " + cookie + " expired");
                }
            }
        }
        // Generate Cookie request headers
        if (!matchedCookies.isEmpty()) {
            final List<Header> headers = cookieSpec.formatCookies(matchedCookies);
            for (final Header header : headers) {
                request.addHeader(header);
            }
        }

        final int ver = cookieSpec.getVersion();
        if (ver > 0) {
            boolean needVersionHeader = false;
            for (final Cookie cookie : matchedCookies) {
                if (ver != cookie.getVersion() || !(cookie instanceof SetCookie2)) {
                    needVersionHeader = true;
                }
            }

            if (needVersionHeader) {
                final Header header = cookieSpec.getVersionHeader();
                if (header != null) {
                    // Advertise cookie version support
                    request.addHeader(header);
                }
            }
        }

        // Stick the CookieSpec and CookieOrigin instances to the HTTP context
        // so they could be obtained by the response interceptor
        context.setAttribute(HttpClientContext.COOKIE_SPEC, cookieSpec);
        context.setAttribute(HttpClientContext.COOKIE_ORIGIN, cookieOrigin);
    }

}
