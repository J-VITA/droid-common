package kr.skyware.commons.http.client;

import java.util.Locale;

import kr.skyware.commons.http.HttpResponse;
import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.factory.HttpResponseFactory;
import kr.skyware.commons.http.header.HttpContext;
import kr.skyware.commons.http.message.BasicHttpResponse;
import kr.skyware.commons.http.message.BasicStatusLine;
import kr.skyware.commons.http.util.Args;
import kr.skyware.commons.http.util.ProtocolVersion;
import kr.skyware.commons.http.util.StatusLine;

@Immutable
public class DefaultHttpResponseFactory implements HttpResponseFactory {

    public static final DefaultHttpResponseFactory INSTANCE = new DefaultHttpResponseFactory();

    /** The catalog for looking up reason phrases. */
    protected final ReasonPhraseCatalog reasonCatalog;


    /**
     * Creates a new response factory with the given catalog.
     *
     * @param catalog   the catalog of reason phrases
     */
    public DefaultHttpResponseFactory(final ReasonPhraseCatalog catalog) {
        this.reasonCatalog = Args.notNull(catalog, "Reason phrase catalog");
    }

    /**
     * Creates a new response factory with the default catalog.
     * The default catalog is {@link EnglishReasonPhraseCatalog}.
     */
    public DefaultHttpResponseFactory() {
        this(EnglishReasonPhraseCatalog.INSTANCE);
    }


    // non-javadoc, see interface HttpResponseFactory
    public HttpResponse newHttpResponse(
            final ProtocolVersion ver,
            final int status,
            final HttpContext context) {
        Args.notNull(ver, "HTTP version");
        final Locale loc = determineLocale(context);
        final String reason   = this.reasonCatalog.getReason(status, loc);
        final StatusLine statusline = new BasicStatusLine(ver, status, reason);
        return new BasicHttpResponse(statusline, this.reasonCatalog, loc);
    }


    // non-javadoc, see interface HttpResponseFactory
    public HttpResponse newHttpResponse(
            final StatusLine statusline,
            final HttpContext context) {
        Args.notNull(statusline, "Status line");
        return new BasicHttpResponse(statusline, this.reasonCatalog, determineLocale(context));
    }

    /**
     * Determines the locale of the response.
     * The implementation in this class always returns the default locale.
     *
     * @param context   the context from which to determine the locale, or
     *                  <code>null</code> to use the default locale
     *
     * @return  the locale for the response, never <code>null</code>
     */
    protected Locale determineLocale(final HttpContext context) {
        return Locale.getDefault();
    }

}
