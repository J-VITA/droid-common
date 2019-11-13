package kr.skyware.commons.http.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import kr.skyware.commons.http.factory.LayeredSocketFactory;
import kr.skyware.commons.http.factory.SchemeLayeredSocketFactory;
import kr.skyware.commons.http.header.HttpParams;

@Deprecated
class SchemeLayeredSocketFactoryAdaptor extends SchemeSocketFactoryAdaptor
        implements SchemeLayeredSocketFactory {

    private final LayeredSocketFactory factory;

    SchemeLayeredSocketFactoryAdaptor(final LayeredSocketFactory factory) {
        super(factory);
        this.factory = factory;
    }

    public Socket createLayeredSocket(
            final Socket socket,
            final String target, final int port,
            final HttpParams params) throws IOException, UnknownHostException {
        return this.factory.createSocket(socket, target, port, true);
    }

}
