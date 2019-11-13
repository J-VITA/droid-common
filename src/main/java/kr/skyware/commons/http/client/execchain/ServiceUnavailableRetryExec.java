package kr.skyware.commons.http.client.execchain;

import java.io.IOException;
import java.io.InterruptedIOException;

import kr.skyware.commons.http.annotation.Immutable;
import kr.skyware.commons.http.client.ServiceUnavailableRetryStrategy;
import kr.skyware.commons.http.concurrent.cancellable.HttpExecutionAware;
import kr.skyware.commons.http.exception.HttpException;
import kr.skyware.commons.http.execute.CloseableHttpResponse;
import kr.skyware.commons.http.header.Header;
import kr.skyware.commons.http.header.HttpRoute;
import kr.skyware.commons.http.method.HttpRequestWrapper;
import kr.skyware.commons.http.client.protocol.HttpClientContext;
import kr.skyware.commons.http.util.Args;
import kr.skyware.commons.http.util.HttpClientAndroidLog;

@Immutable
public class ServiceUnavailableRetryExec implements ClientExecChain {

    public HttpClientAndroidLog log = new HttpClientAndroidLog(getClass());

    private final ClientExecChain requestExecutor;
    private final ServiceUnavailableRetryStrategy retryStrategy;

    public ServiceUnavailableRetryExec(
            final ClientExecChain requestExecutor,
            final ServiceUnavailableRetryStrategy retryStrategy) {
        super();
        Args.notNull(requestExecutor, "HTTP request executor");
        Args.notNull(retryStrategy, "Retry strategy");
        this.requestExecutor = requestExecutor;
        this.retryStrategy = retryStrategy;
    }

    public CloseableHttpResponse execute(
            final HttpRoute route,
            final HttpRequestWrapper request,
            final HttpClientContext context,
            final HttpExecutionAware execAware) throws IOException, HttpException {
        final Header[] origheaders = request.getAllHeaders();
        for (int c = 1;; c++) {
            final CloseableHttpResponse response = this.requestExecutor.execute(
                    route, request, context, execAware);
            try {
                if (this.retryStrategy.retryRequest(response, c, context)) {
                    response.close();
                    final long nextInterval = this.retryStrategy.getRetryInterval();
                    if (nextInterval > 0) {
                        try {
                            this.log.trace("Wait for " + nextInterval);
                            Thread.sleep(nextInterval);
                        } catch (final InterruptedException e) {
                            Thread.currentThread().interrupt();
                            throw new InterruptedIOException();
                        }
                    }
                    request.setHeaders(origheaders);
                } else {
                    return response;
                }
            } catch (final RuntimeException ex) {
                response.close();
                throw ex;
            }
        }
    }

}
