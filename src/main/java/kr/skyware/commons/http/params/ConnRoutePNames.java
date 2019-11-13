package kr.skyware.commons.http.params;


public interface ConnRoutePNames {

    /**
     * Parameter for the default proxy.
     * The default value will be used by some
     * {@link kr.skyware.commons.http.connect.route.HttpRoutePlanner HttpRoutePlanner}
     * implementations, in particular the default implementation.
     * <p>
     * This parameter expects a value of type {@link kr.skyware.commons.http.header.HttpHost}.
     * </p>
     */
    public static final String DEFAULT_PROXY = "http.route.default-proxy";

    /**
     * Parameter for the local address.
     * On machines with multiple network interfaces, this parameter
     * can be used to select the network interface from which the
     * connection originates.
     * It will be interpreted by the standard
     * {@link kr.skyware.commons.http.connect.route.HttpRoutePlanner HttpRoutePlanner}
     * implementations, in particular the default implementation.
     * <p>
     * This parameter expects a value of type {@link java.net.InetAddress}.
     * </p>
     */
    public static final String LOCAL_ADDRESS = "http.route.local-address";

    /**
     * Parameter for an forced route.
     * The forced route will be interpreted by the standard
     * {@link kr.skyware.commons.http.connect.route.HttpRoutePlanner HttpRoutePlanner}
     * implementations.
     * Instead of computing a route, the given forced route will be
     * returned, even if it points to the wrong target host.
     * <p>
     * This parameter expects a value of type
     * {@link kr.skyware.commons.http.header.HttpRoute HttpRoute}.
     * </p>
     */
    public static final String FORCED_ROUTE = "http.route.forced-route";

}

