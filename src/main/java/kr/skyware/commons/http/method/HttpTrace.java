package kr.skyware.commons.http.method;

import java.net.URI;

import kr.skyware.commons.http.annotation.NotThreadSafe;

@NotThreadSafe
public class HttpTrace extends HttpRequestBase {

    public final static String METHOD_NAME = "TRACE";

    public HttpTrace() {
        super();
    }

    public HttpTrace(final URI uri) {
        super();
        setURI(uri);
    }

    /**
     * @throws IllegalArgumentException if the uri is invalid.
     */
    public HttpTrace(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    @Override
    public String getMethod() {
        return METHOD_NAME;
    }

}
