package kr.skyware.commons.http.method;

import java.io.IOException;

import kr.skyware.commons.http.client.impl.client.HttpClient;
import kr.skyware.commons.http.connect.ClientConnectionRequest;
import kr.skyware.commons.http.header.ConnectionReleaseTrigger;

public interface AbortableHttpRequest {

    /**
     * Sets the {@link ClientConnectionRequest}
     * callback that can be used to abort a long-lived request for a connection.
     * If the request is already aborted, throws an {@link IOException}.
     *
     * @see kr.skyware.commons.http.header.ClientConnectionManager
     */
    void setConnectionRequest(ClientConnectionRequest connRequest) throws IOException;

    /**
     * Sets the {@link ConnectionReleaseTrigger} callback that can
     * be used to abort an active connection.
     * Typically, this will be the
     *   {@link kr.skyware.commons.http.header.ManagedClientConnection} itself.
     * If the request is already aborted, throws an {@link IOException}.
     */
    void setReleaseTrigger(ConnectionReleaseTrigger releaseTrigger) throws IOException;

    /**
     * Aborts this http request. Any active execution of this method should
     * return immediately. If the request has not started, it will abort after
     * the next execution. Aborting this request will cause all subsequent
     * executions with this request to fail.
     *
     * @see HttpClient#execute(kr.skyware.commons.http.header.HttpUriRequest)
     * @see HttpClient#execute(kr.skyware.commons.http.header.HttpHost,
     *      kr.skyware.commons.http.header.HttpRequest)
     * @see HttpClient#execute(kr.skyware.commons.http.header.HttpUriRequest,
     *      kr.skyware.commons.http.header.HttpContext)
     * @see kr.skyware.commons.http.header.HttpHost,
     *      HttpContext)
     */
    void abort();

}

