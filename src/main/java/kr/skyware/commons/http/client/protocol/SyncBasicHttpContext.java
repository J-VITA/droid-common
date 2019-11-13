package kr.skyware.commons.http.client.protocol;

import kr.skyware.commons.http.execute.BasicHttpContext;
import kr.skyware.commons.http.header.HttpContext;

public class SyncBasicHttpContext extends BasicHttpContext {

    public SyncBasicHttpContext(final HttpContext parentContext) {
        super(parentContext);
    }

    /**
     * @since 4.2
     */
    public SyncBasicHttpContext() {
        super();
    }

    @Override
    public synchronized Object getAttribute(final String id) {
        return super.getAttribute(id);
    }

    @Override
    public synchronized void setAttribute(final String id, final Object obj) {
        super.setAttribute(id, obj);
    }

    @Override
    public synchronized Object removeAttribute(final String id) {
        return super.removeAttribute(id);
    }

    /**
     * @since 4.2
     */
    @Override
    public synchronized void clear() {
        super.clear();
    }

}
