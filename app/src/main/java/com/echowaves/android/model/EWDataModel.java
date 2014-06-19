package com.echowaves.android.model;

import com.echowaves.android.BaseActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;

/**
 * copyright echowaves
 * Created by dmitry
 * on 6/18/14.
 */

public class EWDataModel {
//    private static final String BASE_URL = "http://echowaves.com/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    static {
        PersistentCookieStore cookieStore = new PersistentCookieStore(new BaseActivity().getApplicationContext());
        client.setCookieStore(cookieStore);
    }

//    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
//        client.get(getAbsoluteUrl(url), params, responseHandler);
//    }
//
//    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
//        client.post(getAbsoluteUrl(url), params, responseHandler);
//    }
//
//    private static String getAbsoluteUrl(String relativeUrl) {
//        return BASE_URL + relativeUrl;
//    }

    public static void debug() {
        System.out.println("debug method called");
    }
}
