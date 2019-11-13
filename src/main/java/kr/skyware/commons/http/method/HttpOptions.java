package kr.skyware.commons.http.method;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import kr.skyware.commons.http.HttpResponse;
import kr.skyware.commons.http.annotation.NotThreadSafe;
import kr.skyware.commons.http.header.Header;
import kr.skyware.commons.http.header.HeaderElement;
import kr.skyware.commons.http.header.HeaderIterator;
import kr.skyware.commons.http.util.Args;

@NotThreadSafe
public class HttpOptions extends HttpRequestBase {

    public final static String METHOD_NAME = "OPTIONS";

    public HttpOptions() {
        super();
    }

    public HttpOptions(final URI uri) {
        super();
        setURI(uri);
    }

    /**
     * @throws IllegalArgumentException if the uri is invalid.
     */
    public HttpOptions(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    @Override
    public String getMethod() {
        return METHOD_NAME;
    }

    public Set<String> getAllowedMethods(final HttpResponse response) {
        Args.notNull(response, "HTTP response");

        final HeaderIterator it = response.headerIterator("Allow");
        final Set<String> methods = new HashSet<String>();
        while (it.hasNext()) {
            final Header header = it.nextHeader();
            final HeaderElement[] elements = header.getElements();
            for (final HeaderElement element : elements) {
                methods.add(element.getName());
            }
        }
        return methods;
    }

}
