package kr.skyware.commons.http.client.impl.client;

import kr.skyware.commons.http.HttpResponse;
import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.client.HeaderElementIterator;
import kr.skyware.commons.http.connect.ConnectionKeepAliveStrategy;
import kr.skyware.commons.http.header.HeaderElement;
import kr.skyware.commons.http.header.HttpContext;
import kr.skyware.commons.http.message.BasicHeaderElementIterator;
import kr.skyware.commons.http.util.Args;
import kr.skyware.commons.http.util.HTTP;

@Immutable
public class DefaultConnectionKeepAliveStrategy implements ConnectionKeepAliveStrategy {

    public static final DefaultConnectionKeepAliveStrategy INSTANCE = new DefaultConnectionKeepAliveStrategy();

    public long getKeepAliveDuration(final HttpResponse response, final HttpContext context) {
        Args.notNull(response, "HTTP response");
        final HeaderElementIterator it = new BasicHeaderElementIterator(
                response.headerIterator(HTTP.CONN_KEEP_ALIVE));
        while (it.hasNext()) {
            final HeaderElement he = it.nextElement();
            final String param = he.getName();
            final String value = he.getValue();
            if (value != null && param.equalsIgnoreCase("timeout")) {
                try {
                    return Long.parseLong(value) * 1000;
                } catch(final NumberFormatException ignore) {
                }
            }
        }
        return -1;
    }

}
