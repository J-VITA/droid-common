package kr.skyware.commons.http.connect;

import java.net.InetAddress;

import kr.skyware.commons.http.annotation.ThreadSafe;
import kr.skyware.commons.http.client.Scheme;
import kr.skyware.commons.http.connect.route.HttpRoutePlanner;
import kr.skyware.commons.http.exception.HttpException;
import kr.skyware.commons.http.factory.SchemeRegistry;
import kr.skyware.commons.http.header.HttpContext;
import kr.skyware.commons.http.header.HttpHost;
import kr.skyware.commons.http.header.HttpRequest;
import kr.skyware.commons.http.header.HttpRoute;
import kr.skyware.commons.http.params.ConnRouteParams;
import kr.skyware.commons.http.util.Args;
import kr.skyware.commons.http.util.Asserts;

@ThreadSafe
public class DefaultHttpRoutePlanner implements HttpRoutePlanner {

    /** The scheme registry. */
    protected final SchemeRegistry schemeRegistry; // class is @ThreadSafe

    /**
     * Creates a new default route planner.
     *
     * @param schreg    the scheme registry
     */
    public DefaultHttpRoutePlanner(final SchemeRegistry schreg) {
        Args.notNull(schreg, "Scheme registry");
        schemeRegistry = schreg;
    }

    public HttpRoute determineRoute(final HttpHost target,
                                    final HttpRequest request,
                                    final HttpContext context)
            throws HttpException {

        Args.notNull(request, "HTTP request");

        // If we have a forced route, we can do without a target.
        HttpRoute route =
                ConnRouteParams.getForcedRoute(request.getParams());
        if (route != null) {
            return route;
        }

        // If we get here, there is no forced route.
        // So we need a target to compute a route.

        Asserts.notNull(target, "Target host");

        final InetAddress local =
                ConnRouteParams.getLocalAddress(request.getParams());
        final HttpHost proxy =
                ConnRouteParams.getDefaultProxy(request.getParams());

        final Scheme schm;
        try {
            schm = this.schemeRegistry.getScheme(target.getSchemeName());
        } catch (final IllegalStateException ex) {
            throw new HttpException(ex.getMessage());
        }
        // as it is typically used for TLS/SSL, we assume that
        // a layered scheme implies a secure connection
        final boolean secure = schm.isLayered();

        if (proxy == null) {
            route = new HttpRoute(target, local, secure);
        } else {
            route = new HttpRoute(target, local, proxy, secure);
        }
        return route;
    }

}
