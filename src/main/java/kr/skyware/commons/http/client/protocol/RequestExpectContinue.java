package kr.skyware.commons.http.client.protocol;

import java.io.IOException;

import kr.skyware.commons.http.HttpEntity;
import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.config.RequestConfig;
import kr.skyware.commons.http.exception.HttpException;
import kr.skyware.commons.http.header.HttpContext;
import kr.skyware.commons.http.header.HttpEntityEnclosingRequest;
import kr.skyware.commons.http.header.HttpRequest;
import kr.skyware.commons.http.interceptor.HttpRequestInterceptor;
import kr.skyware.commons.http.util.Args;
import kr.skyware.commons.http.util.HTTP;
import kr.skyware.commons.http.util.HttpVersion;
import kr.skyware.commons.http.util.ProtocolVersion;

@Immutable
public class RequestExpectContinue implements HttpRequestInterceptor {

    public RequestExpectContinue() {
        super();
    }

    public void process(final HttpRequest request, final HttpContext context)
            throws HttpException, IOException {
        Args.notNull(request, "HTTP request");

        if (!request.containsHeader(HTTP.EXPECT_DIRECTIVE)) {
            if (request instanceof HttpEntityEnclosingRequest) {
                final ProtocolVersion ver = request.getRequestLine().getProtocolVersion();
                final HttpEntity entity = ((HttpEntityEnclosingRequest)request).getEntity();
                // Do not send the expect header if request body is known to be empty
                if (entity != null
                        && entity.getContentLength() != 0 && !ver.lessEquals(HttpVersion.HTTP_1_0)) {
                    final HttpClientContext clientContext = HttpClientContext.adapt(context);
                    final RequestConfig config = clientContext.getRequestConfig();
                    if (config.isExpectContinueEnabled()) {
                        request.addHeader(HTTP.EXPECT_DIRECTIVE, HTTP.EXPECT_CONTINUE);
                    }
                }
            }
        }
    }

}
