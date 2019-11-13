package kr.skyware.commons.http.interceptor;

import java.io.IOException;

import kr.skyware.commons.http.exception.HttpException;
import kr.skyware.commons.http.header.HttpContext;
import kr.skyware.commons.http.header.HttpRequest;

public interface HttpRequestInterceptor {
    void process(HttpRequest request, HttpContext context)
            throws HttpException, IOException;
}
