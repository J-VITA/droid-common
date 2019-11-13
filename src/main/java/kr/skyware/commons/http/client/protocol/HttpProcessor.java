package kr.skyware.commons.http.client.protocol;

import kr.skyware.commons.http.interceptor.HttpRequestInterceptor;
import kr.skyware.commons.http.interceptor.HttpResponseInterceptor;

public interface HttpProcessor
        extends HttpRequestInterceptor, HttpResponseInterceptor {

    // no additional methods
}
