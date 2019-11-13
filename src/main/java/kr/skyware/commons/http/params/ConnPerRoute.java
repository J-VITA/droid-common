package kr.skyware.commons.http.params;

import kr.skyware.commons.http.header.HttpRoute;

public interface ConnPerRoute {

    int getMaxForRoute(HttpRoute route);

}
