package kr.skyware.commons.application;

import android.content.Context;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


import kr.skyware.commons.http.AsyncHttpClient;
import kr.skyware.commons.http.RequestParams;
import kr.skyware.commons.http.entity.StringEntity;
import kr.skyware.commons.http.handler.AsyncHttpResponseHandler;
import kr.skyware.commons.http.header.Header;
import kr.skyware.commons.util.Logging;

/**
 * 테스트 목적으로 작성되어짐.
 * 사용시 커스텀해서 사용 권장.
* */
public class SkywareRequest{

    public static String BASE_URL;

    private static AsyncHttpClient client = new AsyncHttpClient();

    private static SkywareRequest singleton;

    private static final Object mutex = new Object();
    public static SkywareRequest getInstance(Context context, String base_url) {
        SkywareRequest r = singleton;
        if (r == null) {
            synchronized (mutex) {
                r = singleton;
                if (r == null) {
                    r = new SkywareRequest(context, base_url);
                    singleton = r;
                }
            }
        }
        return r;
    }

    public SkywareRequest(Context context){
    }
    public SkywareRequest(Context context, String base_url){
        BASE_URL = base_url;
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) throws UnsupportedEncodingException {
        Logging.d("request params\n [\n " + params.toString() + "\n]");
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }
    public static void get(Context context, String url, Header[] headers, RequestParams params, AsyncHttpResponseHandler responseHandler) throws UnsupportedEncodingException {
        Logging.d("request params\n [\n " + params.toString() + "\n]");
        client.get(context, getAbsoluteUrl(url), headers, params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) throws UnsupportedEncodingException{
        Logging.d("request params\n [\n " + params.toString() + "\n]");
        client.post(url, params, responseHandler);
    }
    public static void post(Context context, String url, JSONObject jsonParams, AsyncHttpResponseHandler responseHandler) throws UnsupportedEncodingException {
        Logging.d("request params\n [\n " + jsonParams.toString() + "\n]");
        StringEntity entity = new StringEntity(URLEncoder.encode(jsonParams.toString(), "UTF-8"));
        client.post(context, getAbsoluteUrl(url), entity, "application/json;charset=UTF-8", responseHandler);
    }

    public static void put(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) throws UnsupportedEncodingException{
        Logging.d("request params\n [\n " + params.toString() + "\n]");
        client.put(context, getAbsoluteUrl(url), params, responseHandler);
    }

    public static void delete(Context context, String url, Header[] headers, RequestParams params, AsyncHttpResponseHandler responseHandler) throws  UnsupportedEncodingException{
        Logging.d("request params\n [\n " + params.toString() + "\n]");
        client.delete(context, getAbsoluteUrl(url), headers, params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        Logging.d("request url :::" + BASE_URL + relativeUrl + ":::");
        return BASE_URL + relativeUrl;
    }
}
