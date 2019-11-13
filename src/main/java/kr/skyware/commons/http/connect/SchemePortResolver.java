package kr.skyware.commons.http.connect;


import kr.skyware.commons.http.exception.UnsupportedSchemeException;
import kr.skyware.commons.http.header.HttpHost;

/**
 * Strategy for default port resolution for protocol schemes.
 *
 * @since 4.3
 */
public interface SchemePortResolver {

    /**
     * Returns the actual port for the host based on the protocol scheme.
     */
    int resolve(HttpHost host) throws UnsupportedSchemeException;

}