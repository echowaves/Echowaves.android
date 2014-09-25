package com.echowaves.android.model;

import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Log;

import com.echowaves.android.EWConstants;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.image.WebImage;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * copyright echowaves
 * Created by dmitry
 * on 6/18/14.
 */

public class EWImage extends EWDataModel {

    public static void getAllImagesForWave(String waveName, AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("wave_name", waveName);
        HTTP_CLIENT.get(getAbsoluteUrl("/wave.json"), params, responseHandler);
    }

    public static RequestHandle uploadPhoto(File imageFile, AsyncHttpResponseHandler responseHandler) throws FileNotFoundException {

        RequestParams params = new RequestParams();
        params.put("file", imageFile);

        params.setAutoCloseInputStreams(true);

        return HTTP_CLIENT.post(getAbsoluteUrl("/upload"), params, responseHandler);
    }

    public static void deleteImage(String imageName, String waveName, AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("image_name", imageName);
        params.put("wave_name", waveName);
        HTTP_CLIENT.post(getAbsoluteUrl("/delete-image.json"), params, responseHandler);
    }

    public static boolean saveImageToAssetLibrary(String imageName, String waveName, boolean fullImage) {
        String urlString;
        if (fullImage) {
            urlString = EWConstants.EWAWSBucket + "/img/" + waveName + "/" + imageName;
        } else {
            urlString = EWConstants.EWAWSBucket + "/img/" + waveName + "/thumb_" + imageName;
        }

        WebImage webImage = new WebImage(urlString);

        Bitmap bitmap = webImage.getBitmap(ApplicationContextProvider.getContext());

        Log.d("***************************** bitMap: ", bitmap.toString());

        MediaStore.Images.Media.insertImage(ApplicationContextProvider.getContext().getContentResolver(), bitmap, waveName + "_" + imageName, waveName + "_" + imageName);
        return true;
    }

    public static void loadFullImage(String imageName, String waveName, AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        HTTP_CLIENT.get(EWAWSBucket + "/img/" + waveName + "/" + imageName, params, responseHandler);
    }

    public static void loadThumbImage(String imageName, String waveName, AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        HTTP_CLIENT.get(EWAWSBucket + "/img/" + waveName + "/thumb_" + imageName, params, responseHandler);
    }


    public static void shareImage(String imageName, String waveName, AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("image_name", imageName);
        params.put("wave_name", waveName);
        HTTP_CLIENT.post(getAbsoluteUrl("/share-image.json"), params, responseHandler);
    }
}