package com.echowaves.android.model;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.Date;

/**
 * copyright echowaves
 * Created by dmitry
 * on 6/19/14.
 */

public class ApplicationContextProvider extends Application {

    public final static String APP_DOMAIN = "com.echowaves";
    public final static String LOGIN_KEY = APP_DOMAIN + ".login";
    public final static String PASS_KEY = APP_DOMAIN + ".password";
    public final static String CURRENT_ASSET_DATE_TIME_KEY = "currentAssetDateTime";
    /**
     * Keeps a reference of the application context
     */
    private static ApplicationContextProvider context;
    private static Date currentAssetDateTime;


    public ApplicationContextProvider() {
        context = this;
    }

    /**
     * Returns the application context
     *
     * @return application context
     */
    public static Context getContext() {
        if (context == null) {
            context = new ApplicationContextProvider();
        }
        return context;
    }

    public static int getPhotosCountSinceLast() {
        return getPhotosCountSince(getCurrentAssetDateTime());
    }

    public static int getPhotosCountSince(Date date) {
//  http://stackoverflow.com/questions/22302357/delete-image-from-android-gallery-captured-after-particular-date-time
        String[] projection = new String[]{
//                MediaStore.Images.ImageColumns._ID,
//                MediaStore.Images.ImageColumns.DATA,
//                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.MIME_TYPE
        };
        String selection = MediaStore.Images.Media.DATE_TAKEN + " > ?";
        String[] selectionArgs = {String.valueOf(date.getTime())};
        final Cursor cursor = getContext().getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection,
                        selectionArgs, MediaStore.Images.ImageColumns.DATE_TAKEN + " ASC");

        return cursor.getCount();
    }

    public static Date getCurrentAssetDateTime() {
        SharedPreferences prefs = new SecurePreferences(getContext());

        if (currentAssetDateTime == null) {
            long storedTime = prefs.getLong(CURRENT_ASSET_DATE_TIME_KEY, 0);
            if (storedTime == 0) {
                storedTime = (new Date()).getTime();
                setCurrentAssetDateTime(new Date(storedTime));
            }
            currentAssetDateTime = new Date(storedTime);
        }
        return currentAssetDateTime;
    }

    public static void setCurrentAssetDateTime(Date currentAssetDateTime) {
        SharedPreferences prefs = new SecurePreferences(getContext());

        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(CURRENT_ASSET_DATE_TIME_KEY, currentAssetDateTime.getTime());
        editor.commit();
        ApplicationContextProvider.currentAssetDateTime = currentAssetDateTime;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        context = getApplicationContext();
    }

}
