package kr.skyware.commons.http.connect;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import kr.skyware.commons.http.annotation.ThreadSafe;
import kr.skyware.commons.http.header.HttpRoute;
import kr.skyware.commons.http.header.ManagedHttpClientConnection;
import kr.skyware.commons.http.pool.AbstractConnPool;
import kr.skyware.commons.http.pool.ConnFactory;
import kr.skyware.commons.http.util.HttpClientAndroidLog;

@ThreadSafe
class CPool extends AbstractConnPool<HttpRoute, ManagedHttpClientConnection, CPoolEntry> {

    private static final AtomicLong COUNTER = new AtomicLong();

    public HttpClientAndroidLog log = new HttpClientAndroidLog(CPool.class);
    private final long timeToLive;
    private final TimeUnit tunit;

    public CPool(
            final ConnFactory<HttpRoute, ManagedHttpClientConnection> connFactory,
            final int defaultMaxPerRoute, final int maxTotal,
            final long timeToLive, final TimeUnit tunit) {
        super(connFactory, defaultMaxPerRoute, maxTotal);
        this.timeToLive = timeToLive;
        this.tunit = tunit;
    }

    @Override
    protected CPoolEntry createEntry(final HttpRoute route, final ManagedHttpClientConnection conn) {
        final String id = Long.toString(COUNTER.getAndIncrement());
        return new CPoolEntry(this.log, id, route, conn, this.timeToLive, this.tunit);
    }

}
