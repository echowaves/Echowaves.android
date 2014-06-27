package com.echowaves.android.model;

import android.content.SharedPreferences;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * copyright echowaves
 * Created by dmitry
 * on 6/18/14.
 */

public class EWWave extends EWDataModel {
    static final private SharedPreferences prefs = new SecurePreferences(ApplicationContextProvider.getContext());

    public static void storeCredentialForWave(String waveName, String wavePassword) {
        prefs.edit().putString(ApplicationContextProvider.LOGIN_KEY, waveName).commit();
        prefs.edit().putString(ApplicationContextProvider.PASS_KEY, wavePassword).commit();
    }

    public static String getStoredWaveName() {
        return prefs.getString(ApplicationContextProvider.LOGIN_KEY, "");
    }

    public static String getStoredWavePassword() {
        return prefs.getString(ApplicationContextProvider.PASS_KEY, "");
    }

    public static void createWaveWithName(String waveName,
                                          String wavePassword,
                                          String confirmPassword,
                                          AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("name", waveName);
        params.put("pass", wavePassword);
        params.put("pass1", confirmPassword);
        HTTP_CLIENT.post(getAbsoluteUrl("/register.json"), params, responseHandler);
    }

    synchronized public static void getAllMyWaves(AsyncHttpResponseHandler responseHandler) {
        HTTP_CLIENT.get(getAbsoluteUrl("/all-my-waves.json"), new RequestParams(), responseHandler);
    }

    public static void tuneInWithNameAndPassword(String waveName,
                                                 String wavePassword,
                                                 AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("name", waveName);
        params.put("pass", wavePassword);
        HTTP_CLIENT.post(getAbsoluteUrl("/login.json"), params, responseHandler);
    }

    public static void tuneOut(AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        HTTP_CLIENT.post(getAbsoluteUrl("/logout.json"), params, responseHandler);
    }
}
