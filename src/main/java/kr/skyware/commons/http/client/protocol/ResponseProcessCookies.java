package kr.skyware.commons.http.client.protocol;

import java.io.IOException;
import java.util.List;

import kr.skyware.commons.http.HttpResponse;
import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.cookie.Cookie;
import kr.skyware.commons.http.cookie.CookieOrigin;
import kr.skyware.commons.http.cookie.CookieSpec;
import kr.skyware.commons.http.cookie.CookieStore;
import kr.skyware.commons.http.cookie.StateManagement;
import kr.skyware.commons.http.exception.HttpException;
import kr.skyware.commons.http.exception.MalformedCookieException;
import kr.skyware.commons.http.header.Header;
import kr.skyware.commons.http.header.HeaderIterator;
import kr.skyware.commons.http.header.HttpContext;
import kr.skyware.commons.http.interceptor.HttpResponseInterceptor;
import kr.skyware.commons.http.util.Args;
import kr.skyware.commons.http.util.HttpClientAndroidLog;

@Immutable
public class ResponseProcessCookies implements HttpResponseInterceptor {

    public HttpClientAndroidLog log = new HttpClientAndroidLog(getClass());

    public ResponseProcessCookies() {
        super();
    }

    public void process(final HttpResponse response, final HttpContext context)
            throws HttpException, IOException {
        Args.notNull(response, "HTTP request");
        Args.notNull(context, "HTTP context");

        final HttpClientContext clientContext = HttpClientContext.adapt(context);

        // Obtain actual CookieSpec instance
        final CookieSpec cookieSpec = clientContext.getCookieSpec();
        if (cookieSpec == null) {
            this.log.debug("Cookie spec not specified in HTTP context");
            return;
        }
        // Obtain cookie store
        final CookieStore cookieStore = clientContext.getCookieStore();
        if (cookieStore == null) {
            this.log.debug("Cookie store not specified in HTTP context");
            return;
        }
        // Obtain actual CookieOrigin instance
        final CookieOrigin cookieOrigin = clientContext.getCookieOrigin();
        if (cookieOrigin == null) {
            this.log.debug("Cookie origin not specified in HTTP context");
            return;
        }
        HeaderIterator it = response.headerIterator(StateManagement.SET_COOKIE);
        processCookies(it, cookieSpec, cookieOrigin, cookieStore);

        // see if the cookie spec supports cookie versioning.
        if (cookieSpec.getVersion() > 0) {
            // process set-cookie2 headers.
            // Cookie2 will replace equivalent Cookie instances
            it = response.headerIterator(StateManagement.SET_COOKIE2);
            processCookies(it, cookieSpec, cookieOrigin, cookieStore);
        }
    }

    private void processCookies(
            final HeaderIterator iterator,
            final CookieSpec cookieSpec,
            final CookieOrigin cookieOrigin,
            final CookieStore cookieStore) {
        while (iterator.hasNext()) {
            final Header header = iterator.nextHeader();
            try {
                final List<Cookie> cookies = cookieSpec.parse(header, cookieOrigin);
                for (final Cookie cookie : cookies) {
                    try {
                        cookieSpec.validate(cookie, cookieOrigin);
                        cookieStore.addCookie(cookie);

                        if (this.log.isDebugEnabled()) {
                            this.log.debug("Cookie accepted [" + formatCooke(cookie) + "]");
                        }
                    } catch (final MalformedCookieException ex) {
                        if (this.log.isWarnEnabled()) {
                            this.log.warn("Cookie rejected [" + formatCooke(cookie) + "] "
                                    + ex.getMessage());
                        }
                    }
                }
            } catch (final MalformedCookieException ex) {
                if (this.log.isWarnEnabled()) {
                    this.log.warn("Invalid cookie header: \""
                            + header + "\". " + ex.getMessage());
                }
            }
        }
    }

    private static String formatCooke(final Cookie cookie) {
        final StringBuilder buf = new StringBuilder();
        buf.append(cookie.getName());
        buf.append("=\"");
        String v = cookie.getValue();
        if (v != null) {
            if (v.length() > 100) {
                v = v.substring(0, 100) + "...";
            }
            buf.append(v);
        }
        buf.append("\"");
        buf.append(", version:");
        buf.append(Integer.toString(cookie.getVersion()));
        buf.append(", domain:");
        buf.append(cookie.getDomain());
        buf.append(", path:");
        buf.append(cookie.getPath());
        buf.append(", expiry:");
        buf.append(cookie.getExpiryDate());
        return buf.toString();
    }

}
