package kr.skyware.commons.http.client.impl.client;

import kr.skyware.commons.http.annotation.ThreadSafe;
import kr.skyware.commons.http.client.AbstractHttpClient;
import kr.skyware.commons.http.connect.HttpConnectionParams;
import kr.skyware.commons.http.header.ClientConnectionManager;
import kr.skyware.commons.http.header.HttpParams;
import kr.skyware.commons.http.params.HttpProtocolParams;
import kr.skyware.commons.http.params.SyncBasicHttpParams;
import kr.skyware.commons.http.client.protocol.BasicHttpProcessor;
import kr.skyware.commons.http.client.protocol.RequestAddCookies;
import kr.skyware.commons.http.client.protocol.RequestAuthCache;
import kr.skyware.commons.http.client.protocol.RequestClientConnControl;
import kr.skyware.commons.http.client.protocol.RequestContent;
import kr.skyware.commons.http.client.protocol.RequestDefaultHeaders;
import kr.skyware.commons.http.client.protocol.RequestExpectContinue;
import kr.skyware.commons.http.client.protocol.RequestProxyAuthentication;
import kr.skyware.commons.http.client.protocol.RequestTargetAuthentication;
import kr.skyware.commons.http.client.protocol.RequestTargetHost;
import kr.skyware.commons.http.client.protocol.RequestUserAgent;
import kr.skyware.commons.http.client.protocol.ResponseProcessCookies;
import kr.skyware.commons.http.util.HTTP;
import kr.skyware.commons.http.util.HttpVersion;

@ThreadSafe
@Deprecated
public class DefaultHttpClient extends AbstractHttpClient {

    /**
     * Creates a new HTTP client from parameters and a connection manager.
     *
     * @param params    the parameters
     * @param conman    the connection manager
     */
    public DefaultHttpClient(
            final ClientConnectionManager conman,
            final HttpParams params) {
        super(conman, params);
    }


    /**
     * @since 4.1
     */
    public DefaultHttpClient(
            final ClientConnectionManager conman) {
        super(conman, null);
    }


    public DefaultHttpClient(final HttpParams params) {
        super(null, params);
    }


    public DefaultHttpClient() {
        super(null, null);
    }


    /**
     * Creates the default set of HttpParams by invoking {@link DefaultHttpClient#setDefaultHttpParams(HttpParams)}
     *
     * @return a new instance of {@link SyncBasicHttpParams} with the defaults applied to it.
     */
    @Override
    protected HttpParams createHttpParams() {
        final HttpParams params = new SyncBasicHttpParams();
        setDefaultHttpParams(params);
        return params;
    }

    /**
     * Saves the default set of HttpParams in the provided parameter.
     * These are:
     * <ul>
     * <li>{@link kr.skyware.commons.http.params.CoreProtocolPNames#PROTOCOL_VERSION}:
     *   1.1</li>
     * <li>{@link kr.skyware.commons.http.params.CoreProtocolPNames#HTTP_CONTENT_CHARSET}:
     *   ISO-8859-1</li>
     * <li>{@link kr.skyware.commons.http.params.CoreConnectionPNames#TCP_NODELAY}:
     *   true</li>
     * <li>{@link kr.skyware.commons.http.params.CoreConnectionPNames#SOCKET_BUFFER_SIZE}:
     *   8192</li>
     * <li>{@link kr.skyware.commons.http.params.CoreProtocolPNames#USER_AGENT}:
     *   Apache-HttpClient/<release> (java 1.5)</li>
     * </ul>
     */
    public static void setDefaultHttpParams(final HttpParams params) {
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.DEF_CONTENT_CHARSET.name());
        HttpConnectionParams.setTcpNoDelay(params, true);
        HttpConnectionParams.setSocketBufferSize(params, 8192);
        HttpProtocolParams.setUserAgent(params, HttpClientBuilder.DEFAULT_USER_AGENT);
    }

    /**
     * Create the processor with the following interceptors:
     * <ul>
     * <li>{@link RequestDefaultHeaders}</li>
     * <li>{@link RequestContent}</li>
     * <li>{@link RequestTargetHost}</li>
     * <li>{@link RequestClientConnControl}</li>
     * <li>{@link RequestUserAgent}</li>
     * <li>{@link RequestExpectContinue}</li>
     * <li>{@link RequestAddCookies}</li>
     * <li>{@link ResponseProcessCookies}</li>
     * <li>{@link RequestAuthCache}</li>
     * <li>{@link RequestTargetAuthentication}</li>
     * <li>{@link RequestProxyAuthentication}</li>
     * </ul>
     * <p>
     * @return the processor with the added interceptors.
     */
    @Override
    protected BasicHttpProcessor createHttpProcessor() {
        final BasicHttpProcessor httpproc = new BasicHttpProcessor();
        httpproc.addInterceptor(new RequestDefaultHeaders());
        // Required protocol interceptors
        httpproc.addInterceptor(new RequestContent());
        httpproc.addInterceptor(new RequestTargetHost());
        // Recommended protocol interceptors
        httpproc.addInterceptor(new RequestClientConnControl());
        httpproc.addInterceptor(new RequestUserAgent());
        httpproc.addInterceptor(new RequestExpectContinue());
        // HTTP state management interceptors
        httpproc.addInterceptor(new RequestAddCookies());
        httpproc.addInterceptor(new ResponseProcessCookies());
        // HTTP authentication interceptors
        httpproc.addInterceptor(new RequestAuthCache());
        httpproc.addInterceptor(new RequestTargetAuthentication());
        httpproc.addInterceptor(new RequestProxyAuthentication());
        return httpproc;
    }

}
