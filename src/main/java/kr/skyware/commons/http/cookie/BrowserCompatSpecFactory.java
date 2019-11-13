package kr.skyware.commons.http.cookie;

import java.util.Collection;

import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.cookie.param.CookieSpecPNames;
import kr.skyware.commons.http.header.HttpContext;
import kr.skyware.commons.http.header.HttpParams;

@Immutable
public class BrowserCompatSpecFactory implements CookieSpecFactory, CookieSpecProvider {

    public enum SecurityLevel {
        SECURITYLEVEL_DEFAULT,
        SECURITYLEVEL_IE_MEDIUM
    }

    private final String[] datepatterns;
    private final SecurityLevel securityLevel;

    public BrowserCompatSpecFactory(final String[] datepatterns, final SecurityLevel securityLevel) {
        super();
        this.datepatterns = datepatterns;
        this.securityLevel = securityLevel;
    }

    public BrowserCompatSpecFactory(final String[] datepatterns) {
        this(null, SecurityLevel.SECURITYLEVEL_DEFAULT);
    }

    public BrowserCompatSpecFactory() {
        this(null, SecurityLevel.SECURITYLEVEL_DEFAULT);
    }

    public CookieSpec newInstance(final HttpParams params) {
        if (params != null) {

            String[] patterns = null;
            final Collection<?> param = (Collection<?>) params.getParameter(
                    CookieSpecPNames.DATE_PATTERNS);
            if (param != null) {
                patterns = new String[param.size()];
                patterns = param.toArray(patterns);
            }
            return new BrowserCompatSpec(patterns, securityLevel);
        } else {
            return new BrowserCompatSpec(null, securityLevel);
        }
    }

    public CookieSpec create(final HttpContext context) {
        return new BrowserCompatSpec(this.datepatterns);
    }

}
