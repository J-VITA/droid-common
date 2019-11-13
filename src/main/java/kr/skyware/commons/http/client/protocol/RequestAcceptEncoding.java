package kr.skyware.commons.http.client.protocol;

import java.io.IOException;

import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.exception.HttpException;
import kr.skyware.commons.http.header.HttpContext;
import kr.skyware.commons.http.header.HttpRequest;
import kr.skyware.commons.http.interceptor.HttpRequestInterceptor;

@Immutable
public class RequestAcceptEncoding implements HttpRequestInterceptor {

    /**
     * Adds the header {@code "Accept-Encoding: gzip,deflate"} to the request.
     */
    public void process(
            final HttpRequest request,
            final HttpContext context) throws HttpException, IOException {

        /* Signal support for Accept-Encoding transfer encodings. */
        if (!request.containsHeader("Accept-Encoding")) {
            request.addHeader("Accept-Encoding", "gzip,deflate");
        }
    }

}
