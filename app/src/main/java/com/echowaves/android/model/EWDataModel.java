package com.echowaves.android.model;

import android.app.ProgressDialog;
import android.content.Context;

import com.echowaves.android.R;
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

    private static ProgressDialog progressDialog = null;

    static {
        PersistentCookieStore cookieStore = new PersistentCookieStore( ApplicationContextProvider.getContext());
        client.setCookieStore(cookieStore);
    }

    public static void showLoadingIndicator(Context context) {
//        if(progressDialog == null) {
            progressDialog=new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Initializing Please Wait");
            progressDialog.setTitle("Loading");
            progressDialog.setIcon(R.drawable.ic_launcher);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.show();
//        }
    }

    public static void hideLoadingIndicator() {
        if(progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public static boolean isLoadingIndicatorShowing() {
        if(progressDialog != null)
            return true;
        return false;
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
