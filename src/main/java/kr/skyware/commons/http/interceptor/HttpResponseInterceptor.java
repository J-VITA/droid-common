package kr.skyware.commons.http.interceptor;

import java.io.IOException;

import kr.skyware.commons.http.HttpResponse;
import kr.skyware.commons.http.exception.HttpException;
import kr.skyware.commons.http.header.HttpContext;

public interface HttpResponseInterceptor {
    void process(HttpResponse response, HttpContext context)
            throws HttpException, IOException;
}
