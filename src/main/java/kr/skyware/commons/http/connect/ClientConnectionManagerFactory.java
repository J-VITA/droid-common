package kr.skyware.commons.http.connect;

import kr.skyware.commons.http.factory.SchemeRegistry;
import kr.skyware.commons.http.header.ClientConnectionManager;
import kr.skyware.commons.http.header.HttpParams;

public interface ClientConnectionManagerFactory {

    ClientConnectionManager newInstance(
            HttpParams params,
            SchemeRegistry schemeRegistry);

}
