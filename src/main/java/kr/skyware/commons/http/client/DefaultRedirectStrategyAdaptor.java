package kr.skyware.commons.http.client;

import java.net.URI;

import kr.skyware.commons.http.HttpResponse;
import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.client.RedirectHandler;
import kr.skyware.commons.http.client.RedirectStrategy;
import kr.skyware.commons.http.exception.ProtocolException;
import kr.skyware.commons.http.handler.SkyRedirectHandler;
import kr.skyware.commons.http.header.HttpContext;
import kr.skyware.commons.http.header.HttpRequest;
import kr.skyware.commons.http.header.HttpUriRequest;
import kr.skyware.commons.http.method.HttpGet;
import kr.skyware.commons.http.method.HttpHead;

@Immutable
class DefaultRedirectStrategyAdaptor implements RedirectStrategy {

    private final SkyRedirectHandler handler;

    public DefaultRedirectStrategyAdaptor(final SkyRedirectHandler handler) {
        super();
        this.handler = handler;
    }

    public boolean isRedirected(
            final HttpRequest request,
            final HttpResponse response,
            final HttpContext context) throws ProtocolException {
        return this.handler.isRedirectRequested(response, context);
    }

    public HttpUriRequest getRedirect(
            final HttpRequest request,
            final HttpResponse response,
            final HttpContext context) throws ProtocolException {
        final URI uri = this.handler.getLocationURI(response, context);
        final String method = request.getRequestLine().getMethod();
        if (method.equalsIgnoreCase(HttpHead.METHOD_NAME)) {
            return new HttpHead(uri);
        } else {
            return new HttpGet(uri);
        }
    }

    public RedirectHandler getHandler() {
        return this.handler;
    }

}
