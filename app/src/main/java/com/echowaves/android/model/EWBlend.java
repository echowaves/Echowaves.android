package com.echowaves.android.model;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * copyright echowaves
 * Created by dmitry
 * on 6/18/14.
 */

public class EWBlend extends EWDataModel {
    public static void autoCompleteFor(String waveName, AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("term", waveName);
        HTTP_CLIENT.get(getAbsoluteUrl("/autocomplete-wave-name.json"), params, responseHandler);
    }
}
