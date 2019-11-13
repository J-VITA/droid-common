package kr.skyware.commons.http.client;

import java.io.IOException;

import kr.skyware.commons.http.HttpResponse;
import kr.skyware.commons.http.exception.HttpException;
import kr.skyware.commons.http.header.HttpContext;
import kr.skyware.commons.http.header.HttpHost;
import kr.skyware.commons.http.header.HttpRequest;

public interface RequestDirector {


    /**
     * Executes a request.
     * <br/><b>Note:</b>
     * For the time being, a new director is instantiated for each request.
     * This is the same behavior as for <code>HttpMethodDirector</code>
     * in HttpClient 3.
     *
     * @param target    the target host for the request.
     *                  Implementations may accept <code>null</code>
     *                  if they can still determine a route, for example
     *                  to a default target or by inspecting the request.
     * @param request   the request to execute
     * @param context   the context for executing the request
     *
     * @return  the final response to the request.
     *          This is never an intermediate response with status code 1xx.
     *
     * @throws HttpException            in case of a problem
     * @throws IOException              in case of an IO problem
     *                                     or if the connection was aborted
     */
    HttpResponse execute(HttpHost target, HttpRequest request, HttpContext context)
            throws HttpException, IOException;

}
