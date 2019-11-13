package kr.skyware.commons.http.execute;

import java.io.Closeable;

import kr.skyware.commons.http.HttpResponse;

public interface CloseableHttpResponse extends HttpResponse, Closeable {
}

