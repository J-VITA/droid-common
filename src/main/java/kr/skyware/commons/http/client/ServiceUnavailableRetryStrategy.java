package kr.skyware.commons.http.client;

import kr.skyware.commons.http.HttpResponse;
import kr.skyware.commons.http.header.HttpContext;

public interface ServiceUnavailableRetryStrategy {

    /**
     * Determines if a method should be retried given the response from the target server.
     *
     * @param response the response from the target server
     * @param executionCount the number of times this method has been
     * unsuccessfully executed
     * @param context the context for the request execution

     * @return <code>true</code> if the method should be retried, <code>false</code>
     * otherwise
     */
    boolean retryRequest(HttpResponse response, int executionCount, HttpContext context);

    /**
     * @return The interval between the subsequent auto-retries.
     */
    long getRetryInterval();

}
