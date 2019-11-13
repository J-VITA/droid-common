package kr.skyware.commons.http.client;

import kr.skyware.commons.http.header.HttpRoute;

public interface BackoffManager {

    /**
     * Called when we have decided that the result of
     * using a connection should be interpreted as a
     * backoff signal.
     */
    public void backOff(HttpRoute route);

    /**
     * Called when we have determined that the result of
     * using a connection has succeeded and that we may
     * probe for more connections.
     */
    public void probe(HttpRoute route);
}
