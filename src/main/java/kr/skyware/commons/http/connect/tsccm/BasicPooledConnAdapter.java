package kr.skyware.commons.http.connect.tsccm;

import kr.skyware.commons.http.connect.AbstractPoolEntry;
import kr.skyware.commons.http.connect.AbstractPooledConnAdapter;
import kr.skyware.commons.http.header.ClientConnectionManager;

public class BasicPooledConnAdapter extends AbstractPooledConnAdapter {

    /**
     * Creates a new adapter.
     *
     * @param tsccm   the connection manager
     * @param entry   the pool entry for the connection being wrapped
     */
    protected BasicPooledConnAdapter(final ThreadSafeClientConnManager tsccm,
                                     final AbstractPoolEntry entry) {
        super(tsccm, entry);
        markReusable();
    }

    @Override
    protected ClientConnectionManager getManager() {
        // override needed only to make method visible in this package
        return super.getManager();
    }

    @Override
    protected AbstractPoolEntry getPoolEntry() {
        // override needed only to make method visible in this package
        return super.getPoolEntry();
    }

    @Override
    protected void detach() {
        // override needed only to make method visible in this package
        super.detach();
    }

}
