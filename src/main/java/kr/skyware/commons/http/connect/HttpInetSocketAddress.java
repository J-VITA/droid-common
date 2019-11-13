package kr.skyware.commons.http.connect;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import kr.skyware.commons.http.header.HttpHost;
import kr.skyware.commons.http.util.Args;

public class HttpInetSocketAddress extends InetSocketAddress {

    private static final long serialVersionUID = -6650701828361907957L;

    private final HttpHost httphost;

    public HttpInetSocketAddress(final HttpHost httphost, final InetAddress addr, final int port) {
        super(addr, port);
        Args.notNull(httphost, "HTTP host");
        this.httphost = httphost;
    }

    public HttpHost getHttpHost() {
        return this.httphost;
    }

    @Override
    public String toString() {
        return this.httphost.getHostName() + ":" + getPort();
    }

}
