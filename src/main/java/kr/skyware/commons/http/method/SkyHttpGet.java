package kr.skyware.commons.http.method;

import java.net.URI;

public final class SkyHttpGet extends HttpEntityEnclosingRequestBase {

    public final static String METHOD_NAME = "GET";

    public SkyHttpGet() {
        super();
    }

    /**
     * @param uri target url as URI
     */
    public SkyHttpGet(final URI uri) {
        super();
        setURI(uri);
    }

    /**
     * @param uri target url as String
     * @throws IllegalArgumentException if the uri is invalid.
     */
    public SkyHttpGet(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    @Override
    public String getMethod() {
        return METHOD_NAME;
    }
}
