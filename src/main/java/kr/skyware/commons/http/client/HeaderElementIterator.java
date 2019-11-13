package kr.skyware.commons.http.client;

import java.util.Iterator;

import kr.skyware.commons.http.header.HeaderElement;

public interface HeaderElementIterator extends Iterator<Object> {

    /**
     * Indicates whether there is another header element in this
     * iteration.
     *
     * @return  <code>true</code> if there is another header element,
     *          <code>false</code> otherwise
     */
    boolean hasNext();

    /**
     * Obtains the next header element from this iteration.
     * This method should only be called while {@link #hasNext hasNext}
     * is true.
     *
     * @return  the next header element in this iteration
     */
    HeaderElement nextElement();

}
