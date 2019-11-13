package kr.skyware.commons.http.cookie;

import java.util.Collection;

import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.cookie.param.CookieSpecPNames;
import kr.skyware.commons.http.header.HttpContext;
import kr.skyware.commons.http.header.HttpParams;

@Immutable
public class RFC2109SpecFactory implements CookieSpecFactory, CookieSpecProvider {

    private final String[] datepatterns;
    private final boolean oneHeader;

    public RFC2109SpecFactory(final String[] datepatterns, final boolean oneHeader) {
        super();
        this.datepatterns = datepatterns;
        this.oneHeader = oneHeader;
    }

    public RFC2109SpecFactory() {
        this(null, false);
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
            final boolean singleHeader = params.getBooleanParameter(
                    CookieSpecPNames.SINGLE_COOKIE_HEADER, false);

            return new RFC2109Spec(patterns, singleHeader);
        } else {
            return new RFC2109Spec();
        }
    }

    public CookieSpec create(final HttpContext context) {
        return new RFC2109Spec(this.datepatterns, this.oneHeader);
    }

}
