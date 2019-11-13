package kr.skyware.commons.http.cookie;

import java.util.Collection;

import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.cookie.param.CookieSpecPNames;
import kr.skyware.commons.http.header.HttpContext;
import kr.skyware.commons.http.header.HttpParams;

@Immutable
public class NetscapeDraftSpecFactory implements CookieSpecFactory, CookieSpecProvider {

    private final String[] datepatterns;

    public NetscapeDraftSpecFactory(final String[] datepatterns) {
        super();
        this.datepatterns = datepatterns;
    }

    public NetscapeDraftSpecFactory() {
        this(null);
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
            return new NetscapeDraftSpec(patterns);
        } else {
            return new NetscapeDraftSpec();
        }
    }

    public CookieSpec create(final HttpContext context) {
        return new NetscapeDraftSpec(this.datepatterns);
    }

}
