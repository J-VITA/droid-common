package kr.skyware.commons.http.client.impl.client;

import kr.skyware.commons.http.annotation.NotThreadSafe;
import kr.skyware.commons.http.header.HttpRoute;

@NotThreadSafe // RequestWrapper is @NotThreadSafe
public class RoutedRequest {

    protected final RequestWrapper request; // @NotThreadSafe
    protected final HttpRoute route; // @Immutable

    /**
     * Creates a new routed request.
     *
     * @param req   the request
     * @param route   the route
     */
    public RoutedRequest(final RequestWrapper req, final HttpRoute route) {
        super();
        this.request = req;
        this.route   = route;
    }

    public final RequestWrapper getRequest() {
        return request;
    }

    public final HttpRoute getRoute() {
        return route;
    }

}
