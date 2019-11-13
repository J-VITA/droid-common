package kr.skyware.commons.http.connect;

import java.net.InetAddress;

import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.config.RequestConfig;
import kr.skyware.commons.http.connect.route.HttpRoutePlanner;
import kr.skyware.commons.http.exception.HttpException;
import kr.skyware.commons.http.exception.ProtocolException;
import kr.skyware.commons.http.exception.UnsupportedSchemeException;
import kr.skyware.commons.http.header.HttpContext;
import kr.skyware.commons.http.header.HttpHost;
import kr.skyware.commons.http.header.HttpRequest;
import kr.skyware.commons.http.header.HttpRoute;
import kr.skyware.commons.http.client.protocol.HttpClientContext;
import kr.skyware.commons.http.util.Args;

@Immutable
public class DefaultRoutePlanner implements HttpRoutePlanner {

    private final SchemePortResolver schemePortResolver;

    public DefaultRoutePlanner(final SchemePortResolver schemePortResolver) {
        super();
        this.schemePortResolver = schemePortResolver != null ? schemePortResolver :
                DefaultSchemePortResolver.INSTANCE;
    }

    public HttpRoute determineRoute(
            final HttpHost host,
            final HttpRequest request,
            final HttpContext context) throws HttpException {
        Args.notNull(request, "Request");
        if (host == null) {
            throw new ProtocolException("Target host is not specified");
        }
        final HttpClientContext clientContext = HttpClientContext.adapt(context);
        final RequestConfig config = clientContext.getRequestConfig();
        final InetAddress local = config.getLocalAddress();
        HttpHost proxy = config.getProxy();
        if (proxy == null) {
            proxy = determineProxy(host, request, context);
        }

        final HttpHost target;
        if (host.getPort() <= 0) {
            try {
                target = new HttpHost(
                        host.getHostName(),
                        this.schemePortResolver.resolve(host),
                        host.getSchemeName());
            } catch (final UnsupportedSchemeException ex) {
                throw new HttpException(ex.getMessage());
            }
        } else {
            target = host;
        }
        final boolean secure = target.getSchemeName().equalsIgnoreCase("https");
        if (proxy == null) {
            return new HttpRoute(target, local, secure);
        } else {
            return new HttpRoute(target, local, proxy, secure);
        }
    }

    protected HttpHost determineProxy(
            final HttpHost target,
            final HttpRequest request,
            final HttpContext context) throws HttpException {
        return null;
    }

}
