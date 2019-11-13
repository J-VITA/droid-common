package kr.skyware.commons.http.factory;

import kr.skyware.commons.http.config.ConnectionConfig;
import kr.skyware.commons.http.connect.HttpConnection;

public interface HttpConnectionFactory<T, C extends HttpConnection> {

    C create(T route, ConnectionConfig config);

}
