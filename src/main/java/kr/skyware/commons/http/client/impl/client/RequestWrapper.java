package kr.skyware.commons.http.client.impl.client;

import java.net.URI;
import java.net.URISyntaxException;

import kr.skyware.commons.http.annotation.NotThreadSafe;
import kr.skyware.commons.http.exception.ProtocolException;
import kr.skyware.commons.http.header.HttpRequest;
import kr.skyware.commons.http.header.HttpUriRequest;
import kr.skyware.commons.http.header.RequestLine;
import kr.skyware.commons.http.message.AbstractHttpMessage;
import kr.skyware.commons.http.message.BasicRequestLine;
import kr.skyware.commons.http.params.HttpProtocolParams;
import kr.skyware.commons.http.util.Args;
import kr.skyware.commons.http.util.ProtocolVersion;

@NotThreadSafe
public class RequestWrapper extends AbstractHttpMessage implements HttpUriRequest {

    private final HttpRequest original;

    private URI uri;
    private String method;
    private ProtocolVersion version;
    private int execCount;

    public RequestWrapper(final HttpRequest request) throws ProtocolException {
        super();
        Args.notNull(request, "HTTP request");
        this.original = request;
        setParams(request.getParams());
        setHeaders(request.getAllHeaders());
        // Make a copy of the original URI
        if (request instanceof HttpUriRequest) {
            this.uri = ((HttpUriRequest) request).getURI();
            this.method = ((HttpUriRequest) request).getMethod();
            this.version = null;
        } else {
            final RequestLine requestLine = request.getRequestLine();
            try {
                this.uri = new URI(requestLine.getUri());
            } catch (final URISyntaxException ex) {
                throw new ProtocolException("Invalid request URI: "
                        + requestLine.getUri(), ex);
            }
            this.method = requestLine.getMethod();
            this.version = request.getProtocolVersion();
        }
        this.execCount = 0;
    }

    public void resetHeaders() {
        // Make a copy of original headers
        this.headergroup.clear();
        setHeaders(this.original.getAllHeaders());
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(final String method) {
        Args.notNull(method, "Method name");
        this.method = method;
    }

    public ProtocolVersion getProtocolVersion() {
        if (this.version == null) {
            this.version = HttpProtocolParams.getVersion(getParams());
        }
        return this.version;
    }

    public void setProtocolVersion(final ProtocolVersion version) {
        this.version = version;
    }


    public URI getURI() {
        return this.uri;
    }

    public void setURI(final URI uri) {
        this.uri = uri;
    }

    public RequestLine getRequestLine() {
        final String method = getMethod();
        final ProtocolVersion ver = getProtocolVersion();
        String uritext = null;
        if (uri != null) {
            uritext = uri.toASCIIString();
        }
        if (uritext == null || uritext.length() == 0) {
            uritext = "/";
        }
        return new BasicRequestLine(method, uritext, ver);
    }

    public void abort() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public boolean isAborted() {
        return false;
    }

    public HttpRequest getOriginal() {
        return this.original;
    }

    public boolean isRepeatable() {
        return true;
    }

    public int getExecCount() {
        return this.execCount;
    }

    public void incrementExecCount() {
        this.execCount++;
    }

}
