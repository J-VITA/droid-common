package kr.skyware.commons.http.client.protocol;

import java.io.IOException;
import java.net.InetAddress;

import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.connect.HttpConnection;
import kr.skyware.commons.http.exception.HttpException;
import kr.skyware.commons.http.exception.ProtocolException;
import kr.skyware.commons.http.execute.HttpCoreContext;
import kr.skyware.commons.http.header.HttpContext;
import kr.skyware.commons.http.header.HttpHost;
import kr.skyware.commons.http.header.HttpInetConnection;
import kr.skyware.commons.http.header.HttpRequest;
import kr.skyware.commons.http.interceptor.HttpRequestInterceptor;
import kr.skyware.commons.http.util.Args;
import kr.skyware.commons.http.util.HTTP;
import kr.skyware.commons.http.util.HttpVersion;
import kr.skyware.commons.http.util.ProtocolVersion;

@Immutable
public class RequestTargetHost implements HttpRequestInterceptor {

    public RequestTargetHost() {
        super();
    }

    public void process(final HttpRequest request, final HttpContext context)
            throws HttpException, IOException {
        Args.notNull(request, "HTTP request");

        final HttpCoreContext corecontext = HttpCoreContext.adapt(context);

        final ProtocolVersion ver = request.getRequestLine().getProtocolVersion();
        final String method = request.getRequestLine().getMethod();
        if (method.equalsIgnoreCase("CONNECT") && ver.lessEquals(HttpVersion.HTTP_1_0)) {
            return;
        }

        if (!request.containsHeader(HTTP.TARGET_HOST)) {
            HttpHost targethost = corecontext.getTargetHost();
            if (targethost == null) {
                final HttpConnection conn = corecontext.getConnection();
                if (conn instanceof HttpInetConnection) {
                    // Populate the context with a default HTTP host based on the
                    // inet address of the target host
                    final InetAddress address = ((HttpInetConnection) conn).getRemoteAddress();
                    final int port = ((HttpInetConnection) conn).getRemotePort();
                    if (address != null) {
                        targethost = new HttpHost(address.getHostName(), port);
                    }
                }
                if (targethost == null) {
                    if (ver.lessEquals(HttpVersion.HTTP_1_0)) {
                        return;
                    } else {
                        throw new ProtocolException("Target host missing");
                    }
                }
            }
            request.addHeader(HTTP.TARGET_HOST, targethost.toHostString());
        }
    }

}
