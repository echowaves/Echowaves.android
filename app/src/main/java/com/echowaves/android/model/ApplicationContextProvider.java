package com.echowaves.android.model;

import android.app.Application;
import android.content.Context;

/**
 * copyright echowaves
 * Created by dmitry
 * on 6/19/14.
 */

public class ApplicationContextProvider extends Application {

    public final static String APP_DOMAIN = "com.echowaves";
    public final static String LOGIN_KEY = APP_DOMAIN + ".login";
    public final static String PASS_KEY = APP_DOMAIN + ".password";

    /**
     * Keeps a reference of the application context
     */
    private static Context context;

    public ApplicationContextProvider() {
        context = this;
    }

    /**
     * Returns the application context
     *
     * @return application context
     */
    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
