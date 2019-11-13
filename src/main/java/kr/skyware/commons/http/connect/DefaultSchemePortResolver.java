package kr.skyware.commons.http.connect;

import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.exception.UnsupportedSchemeException;
import kr.skyware.commons.http.header.HttpHost;
import kr.skyware.commons.http.util.Args;

@Immutable
public class DefaultSchemePortResolver implements SchemePortResolver {

    public static final DefaultSchemePortResolver INSTANCE = new DefaultSchemePortResolver();

    public int resolve(final HttpHost host) throws UnsupportedSchemeException {
        Args.notNull(host, "HTTP host");
        final int port = host.getPort();
        if (port > 0) {
            return port;
        }
        final String name = host.getSchemeName();
        if (name.equalsIgnoreCase("http")) {
            return 80;
        } else if (name.equalsIgnoreCase("https")) {
            return 443;
        } else {
            throw new UnsupportedSchemeException(name + " protocol is not supported");
        }
    }

}
