package com.echowaves.android.model;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

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

//    +(void) saveImageToAssetLibrary:(UIImage*) image


}
