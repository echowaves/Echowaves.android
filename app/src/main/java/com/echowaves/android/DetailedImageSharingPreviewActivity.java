package com.echowaves.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.echowaves.android.model.EWImage;
import com.echowaves.android.util.TouchImageView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


public class DetailedImageSharingPreviewActivity extends Activity {

//    private RelativeLayout navBar;


    private String imageName;
    private String waveName;

    private TextView backButton;
    private TextView waveNameTextView;
    private TouchImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_image_sharing_preview);


//        navBar = (RelativeLayout) findViewById(R.id.detailedimagePreview_navBar);


        imageName = (String) getIntent().getSerializableExtra("SHARED_IMAGE");
        waveName = (String) getIntent().getSerializableExtra("FROM_WAVE");

        backButton = (TextView) findViewById(R.id.detailedimagePreview_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                finish();
            }
        });


        waveNameTextView = (TextView) findViewById(R.id.detailedimagePreview_waveName);
        waveNameTextView.setText(waveName);

        imageView = (TouchImageView) findViewById(R.id.detailedimagePreview_image);

        EWImage.loadThumbImage(imageName, waveName, new AsyncHttpResponseHandler() {


                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        Bitmap bmp = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
                        imageView = (TouchImageView) findViewById(R.id.detailedimagePreview_image);
                        imageView.setImageBitmap(bmp);

                        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if (headers != null) {
                            for (Header h : headers) {
                                Log.d("................ failed   key: ", h.getName());
                                Log.d("................ failed value: ", h.getValue());
                            }
                        }
                        if (responseBody != null) {
                            Log.d("................ failed : ", new String(responseBody));
                        }
                        if (error != null) {
                            Log.d("................ failed error: ", error.toString());

                            String msg = "";
                            if (null != responseBody) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(new String(responseBody));
                                    msg = jsonResponse.getString("error");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                msg = error.getMessage();
                            }


                            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                            builder.setTitle("Error")
                                    .setMessage(msg)
                                    .setCancelable(false)
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();

                        }
                    }


                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                }
        );


    }

}
