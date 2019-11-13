package kr.skyware.commons.http.client.auth;

import kr.skyware.commons.http.header.HttpHost;

public interface AuthCache {

    void put(HttpHost host, AuthScheme authScheme);

    AuthScheme get(HttpHost host);

    void remove(HttpHost host);

    void clear();

}