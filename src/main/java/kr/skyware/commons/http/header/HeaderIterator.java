package kr.skyware.commons.http.header;

import java.util.Iterator;

import kr.skyware.commons.http.header.Header;

public interface HeaderIterator extends Iterator<Object> {
    /**
     * Indicates whether there is another header in this iteration.
     *
     * @return  <code>true</code> if there is another header,
     *          <code>false</code> otherwise
     */
    boolean hasNext();

    /**
     * Obtains the next header from this iteration.
     * This method should only be called while {@link #hasNext hasNext}
     * is true.
     *
     * @return  the next header in this iteration
     */
    Header nextHeader();

}
