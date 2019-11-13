package kr.skyware.commons.http.client.protocol;

import java.io.IOException;

import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.exception.HttpException;
import kr.skyware.commons.http.header.HttpContext;
import kr.skyware.commons.http.header.HttpRequest;
import kr.skyware.commons.http.header.RouteInfo;
import kr.skyware.commons.http.interceptor.HttpRequestInterceptor;
import kr.skyware.commons.http.util.Args;
import kr.skyware.commons.http.util.HTTP;
import kr.skyware.commons.http.util.HttpClientAndroidLog;

@Immutable
public class RequestClientConnControl implements HttpRequestInterceptor {

    public HttpClientAndroidLog log = new HttpClientAndroidLog(getClass());

    private static final String PROXY_CONN_DIRECTIVE = "Proxy-Connection";

    public RequestClientConnControl() {
        super();
    }

    public void process(final HttpRequest request, final HttpContext context)
            throws HttpException, IOException {
        Args.notNull(request, "HTTP request");

        final String method = request.getRequestLine().getMethod();
        if (method.equalsIgnoreCase("CONNECT")) {
            request.setHeader(PROXY_CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
            return;
        }

        final HttpClientContext clientContext = HttpClientContext.adapt(context);

        // Obtain the client connection (required)
        final RouteInfo route = clientContext.getHttpRoute();
        if (route == null) {
            this.log.debug("Connection route not set in the context");
            return;
        }

        if (route.getHopCount() == 1 || route.isTunnelled()) {
            if (!request.containsHeader(HTTP.CONN_DIRECTIVE)) {
                request.addHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
            }
        }
        if (route.getHopCount() == 2 && !route.isTunnelled()) {
            if (!request.containsHeader(PROXY_CONN_DIRECTIVE)) {
                request.addHeader(PROXY_CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
            }
        }
    }

}
