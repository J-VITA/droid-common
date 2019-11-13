package kr.skyware.commons.http.message;

import kr.skyware.commons.http.annotation.NotThreadSafe;
import kr.skyware.commons.http.header.HttpRequest;
import kr.skyware.commons.http.header.RequestLine;
import kr.skyware.commons.http.util.Args;
import kr.skyware.commons.http.util.HttpVersion;
import kr.skyware.commons.http.util.ProtocolVersion;

@NotThreadSafe
public class BasicHttpRequest extends AbstractHttpMessage implements HttpRequest {

    private final String method;
    private final String uri;

    private RequestLine requestline;

    /**
     * Creates an instance of this class using the given request method
     * and URI.
     *
     * @param method request method.
     * @param uri request URI.
     */
    public BasicHttpRequest(final String method, final String uri) {
        super();
        this.method = Args.notNull(method, "Method name");
        this.uri = Args.notNull(uri, "Request URI");
        this.requestline = null;
    }

    /**
     * Creates an instance of this class using the given request method, URI
     * and the HTTP protocol version.
     *
     * @param method request method.
     * @param uri request URI.
     * @param ver HTTP protocol version.
     */
    public BasicHttpRequest(final String method, final String uri, final ProtocolVersion ver) {
        this(new BasicRequestLine(method, uri, ver));
    }

    /**
     * Creates an instance of this class using the given request line.
     *
     * @param requestline request line.
     */
    public BasicHttpRequest(final RequestLine requestline) {
        super();
        this.requestline = Args.notNull(requestline, "Request line");
        this.method = requestline.getMethod();
        this.uri = requestline.getUri();
    }

    /**
     * Returns the HTTP protocol version to be used for this request.
     *
     * @see #BasicHttpRequest(String, String)
     */
    public ProtocolVersion getProtocolVersion() {
        return getRequestLine().getProtocolVersion();
    }

    /**
     * Returns the request line of this request.
     *
     * @see #BasicHttpRequest(String, String)
     */
    public RequestLine getRequestLine() {
        if (this.requestline == null) {
            this.requestline = new BasicRequestLine(this.method, this.uri, HttpVersion.HTTP_1_1);
        }
        return this.requestline;
    }

    @Override
    public String toString() {
        return this.method + ' ' + this.uri + ' ' + this.headergroup;
    }

}
