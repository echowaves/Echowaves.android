package com.echowaves.android.model;

import android.app.Application;
import android.content.Context;

/**
 * copyright echowaves
 * Created by dmitry
 * on 6/19/14.
 */

public class ApplicationContextProvider extends Application {
    /**
     * Keeps a reference of the application context
     */
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

    }

    /**
     * Returns the application context
     *
     * @return application context
     */
    public static Context getContext() {
        return context;
    }

}
