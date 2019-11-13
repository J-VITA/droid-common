package kr.skyware.commons.http.pool;

import java.io.IOException;

public interface ConnFactory<T, C> {

    C create(T route) throws IOException;

}
