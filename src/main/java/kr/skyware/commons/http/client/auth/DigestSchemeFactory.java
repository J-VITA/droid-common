package kr.skyware.commons.http.client.auth;

import java.nio.charset.Charset;

import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.header.HttpContext;
import kr.skyware.commons.http.header.HttpParams;

@Immutable
public class DigestSchemeFactory implements AuthSchemeFactory, AuthSchemeProvider {

    private final Charset charset;

    /**
     * @since 4.3
     */
    public DigestSchemeFactory(final Charset charset) {
        super();
        this.charset = charset;
    }

    public DigestSchemeFactory() {
        this(null);
    }

    public AuthScheme newInstance(final HttpParams params) {
        return new DigestScheme();
    }

    public AuthScheme create(final HttpContext context) {
        return new DigestScheme(this.charset);
    }

}
