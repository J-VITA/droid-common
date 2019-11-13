package kr.skyware.commons.http.client.auth;

import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.header.HttpContext;
import kr.skyware.commons.http.header.HttpParams;

@Immutable
public class NTLMSchemeFactory implements AuthSchemeFactory, AuthSchemeProvider {

    public AuthScheme newInstance(final HttpParams params) {
        return new NTLMScheme();
    }

    public AuthScheme create(final HttpContext context) {
        return new NTLMScheme();
    }

}
