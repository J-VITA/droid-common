package kr.skyware.commons.http.client.protocol;

import java.io.IOException;
import java.util.Collection;

import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.exception.HttpException;
import kr.skyware.commons.http.header.Header;
import kr.skyware.commons.http.header.HttpContext;
import kr.skyware.commons.http.header.HttpRequest;
import kr.skyware.commons.http.interceptor.HttpRequestInterceptor;
import kr.skyware.commons.http.params.ClientPNames;
import kr.skyware.commons.http.util.Args;

@Immutable
public class RequestDefaultHeaders implements HttpRequestInterceptor {

    private final Collection<? extends Header> defaultHeaders;

    /**
     * @since 4.3
     */
    public RequestDefaultHeaders(final Collection<? extends Header> defaultHeaders) {
        super();
        this.defaultHeaders = defaultHeaders;
    }

    public RequestDefaultHeaders() {
        this(null);
    }

    public void process(final HttpRequest request, final HttpContext context)
            throws HttpException, IOException {
        Args.notNull(request, "HTTP request");

        final String method = request.getRequestLine().getMethod();
        if (method.equalsIgnoreCase("CONNECT")) {
            return;
        }

        // Add default headers
        @SuppressWarnings("unchecked")
        Collection<? extends Header> defHeaders = (Collection<? extends Header>)
                request.getParams().getParameter(ClientPNames.DEFAULT_HEADERS);
        if (defHeaders == null) {
            defHeaders = this.defaultHeaders;
        }

        if (defHeaders != null) {
            for (final Header defHeader : defHeaders) {
                request.addHeader(defHeader);
            }
        }
    }

}
