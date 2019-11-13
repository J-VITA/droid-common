package kr.skyware.commons.http.connect;

import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.exception.HttpException;
import kr.skyware.commons.http.header.HttpContext;
import kr.skyware.commons.http.header.HttpHost;
import kr.skyware.commons.http.header.HttpRequest;
import kr.skyware.commons.http.util.Args;

@Immutable
public class DefaultProxyRoutePlanner extends DefaultRoutePlanner {

    private final HttpHost proxy;

    public DefaultProxyRoutePlanner(final HttpHost proxy, final SchemePortResolver schemePortResolver) {
        super(schemePortResolver);
        this.proxy = Args.notNull(proxy, "Proxy host");
    }

    public DefaultProxyRoutePlanner(final HttpHost proxy) {
        this(proxy, null);
    }

    @Override
    protected HttpHost determineProxy(
            final HttpHost target,
            final HttpRequest request,
            final HttpContext context) throws HttpException {
        return proxy;
    }

}
