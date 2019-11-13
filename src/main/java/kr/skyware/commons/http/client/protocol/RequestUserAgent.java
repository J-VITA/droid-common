package kr.skyware.commons.http.client.protocol;

import java.io.IOException;

import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.exception.HttpException;
import kr.skyware.commons.http.header.HttpContext;
import kr.skyware.commons.http.header.HttpParams;
import kr.skyware.commons.http.header.HttpRequest;
import kr.skyware.commons.http.interceptor.HttpRequestInterceptor;
import kr.skyware.commons.http.params.CoreProtocolPNames;
import kr.skyware.commons.http.util.Args;
import kr.skyware.commons.http.util.HTTP;

@Immutable
public class RequestUserAgent implements HttpRequestInterceptor {

    private final String userAgent;

    public RequestUserAgent(final String userAgent) {
        super();
        this.userAgent = userAgent;
    }

    public RequestUserAgent() {
        this(null);
    }

    public void process(final HttpRequest request, final HttpContext context)
            throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        if (!request.containsHeader(HTTP.USER_AGENT)) {
            String s = null;
            final HttpParams params = request.getParams();
            if (params != null) {
                s = (String) params.getParameter(CoreProtocolPNames.USER_AGENT);
            }
            if (s == null) {
                s = this.userAgent;
            }
            if (s != null) {
                request.addHeader(HTTP.USER_AGENT, s);
            }
        }
    }

}
