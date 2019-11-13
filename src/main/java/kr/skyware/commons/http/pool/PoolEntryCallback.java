package kr.skyware.commons.http.pool;

import kr.skyware.commons.http.connect.PoolEntry;

public interface PoolEntryCallback<T, C> {

    void process(PoolEntry<T, C> entry);

}
