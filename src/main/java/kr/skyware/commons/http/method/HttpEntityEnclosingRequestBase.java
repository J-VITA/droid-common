package kr.skyware.commons.http.method;

import kr.skyware.commons.http.HttpEntity;
import kr.skyware.commons.http.annotation.NotThreadSafe;
import kr.skyware.commons.http.header.Header;
import kr.skyware.commons.http.header.HttpEntityEnclosingRequest;
import kr.skyware.commons.http.util.CloneUtils;
import kr.skyware.commons.http.util.HTTP;

@NotThreadSafe // HttpRequestBase is @NotThreadSafe
public abstract class HttpEntityEnclosingRequestBase
        extends HttpRequestBase implements HttpEntityEnclosingRequest {

    private HttpEntity entity;

    public HttpEntityEnclosingRequestBase() {
        super();
    }

    public HttpEntity getEntity() {
        return this.entity;
    }

    public void setEntity(final HttpEntity entity) {
        this.entity = entity;
    }

    public boolean expectContinue() {
        final Header expect = getFirstHeader(HTTP.EXPECT_DIRECTIVE);
        return expect != null && HTTP.EXPECT_CONTINUE.equalsIgnoreCase(expect.getValue());
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        final HttpEntityEnclosingRequestBase clone =
                (HttpEntityEnclosingRequestBase) super.clone();
        if (this.entity != null) {
            clone.entity = CloneUtils.cloneObject(this.entity);
        }
        return clone;
    }

}
