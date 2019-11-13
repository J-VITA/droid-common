package kr.skyware.commons.http.method;

import kr.skyware.commons.http.config.RequestConfig;

public interface Configurable {

    /**
     * Returns actual request configuration.
     */
    RequestConfig getConfig();

}
