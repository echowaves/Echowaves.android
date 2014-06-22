package com.echowaves.android.model;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * copyright echowaves
 * Created by dmitry
 * on 6/18/14.
 */

public class EWWave extends EWDataModel {

    public static void createWaveWithName(String waveName,
                                          String wavePassword,
                                          String confirmPassword,
                                          AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("name", waveName);
        params.put("pass", wavePassword);
        params.put("pass1", confirmPassword);
        client.post(getAbsoluteUrl("/register.json"), params, responseHandler);
    }

    public static void tuneInWithNameAndPassword(String waveName,
                                                 String wavePassword,
                                                 AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("name", waveName);
        params.put("pass", wavePassword);
        client.post(getAbsoluteUrl("/login.json"), params, responseHandler);
    }

    public static void tuneOut(AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        client.post(getAbsoluteUrl("/logout.json"), params, responseHandler);
    }
}
