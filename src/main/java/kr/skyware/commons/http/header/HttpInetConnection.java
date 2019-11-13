package kr.skyware.commons.http.header;

import java.net.InetAddress;

import kr.skyware.commons.http.connect.HttpConnection;

public interface HttpInetConnection extends HttpConnection {

    InetAddress getLocalAddress();

    int getLocalPort();

    InetAddress getRemoteAddress();

    int getRemotePort();

}
