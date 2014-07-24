package com.echowaves.android.model;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

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

    public static void uploadPhoto(byte[] photoByteArray, String photoName, AsyncHttpResponseHandler responseHandler) throws FileNotFoundException {
        InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(photoByteArray));
        RequestParams params = new RequestParams();

        params.put("file", inputStream, photoName);

        HTTP_CLIENT.post(getAbsoluteUrl("/upload"), params, responseHandler);
    }

}
