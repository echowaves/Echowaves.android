package com.echowaves.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.echowaves.android.model.ApplicationContextProvider;
import com.echowaves.android.model.EWImage;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Date;

public class UploadProgressDetailsActivity extends EWActivity {
    private TextView photosCount;
    private ProgressBar progressBar;
    private ImageView imageView;
    private Button cancelButton;
    private Button pauseAllButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_progress_details);

        imageView = (ImageView) findViewById(R.id.upload_imageView);
        progressBar = (ProgressBar) findViewById(R.id.upload_progressBar);
        photosCount = (TextView) findViewById(R.id.upload_count);

        pauseAllButton = (Button) findViewById(R.id.upload_pauseAllButton);

        photosCount.setText(String.valueOf(ApplicationContextProvider.getPhotosCountSinceLast()));

        //Listening to button event
//        pauseAllButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View arg0) {
//                if (waveOperation != null) {
//                    waveOperation.cancel(true);
//                }
//                backToWaving();
//            }
//        });


        cancelButton = (Button) findViewById(R.id.upload_cancelButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Log.d("###################################", "canceling ");

                imageView.invalidate();
                progressBar.invalidate();
                cancelButton.invalidate();
                pauseAllButton.invalidate();

                finish();

            }
        });


        pauseAllButton = (Button) findViewById(R.id.upload_pauseAllButton);


        Intent thisIntent = getIntent();
        final File tmpFile = (File) thisIntent.getSerializableExtra("tmpFile");
        final Date assetDate = (Date) thisIntent.getSerializableExtra("assetDate");

        Bitmap bitMap = BitmapFactory.decodeFile(tmpFile.getAbsolutePath());
        imageView.setImageBitmap(bitMap);

        try {
            EWImage.uploadPhoto(tmpFile,
                    new AsyncHttpResponseHandler() {
                        @Override
                        public void onProgress(int bytesWritten, int totalSize) {
//                                        super.onProgress(bytesWritten, totalSize);
//                                        Log.d("--------------progress: ", String.valueOf(bytesWritten) + " of " + String.valueOf(totalSize));
                            progressBar.setProgress(100 * bytesWritten / totalSize);
                        }


                        @Override
                        public void onStart() {
                            super.onStart();
//                                        currentAssetDate = new Date(Long.valueOf(cursor.getString(3)));
//                                        EWWave.showLoadingIndicator(ApplicationContextProvider.getContext());
                            progressBar.setProgress(0);

                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            Log.d(">>>>>>>>>>>>>>>>>>>> statusCode:" + statusCode, Arrays.toString(responseBody));
                            //                                    Intent createWave = new Intent(getApplicationContext(), NavigationTabBarActivity.class);
                            //                                    startActivity(createWave);
//                                        progressBar.setProgress(100);
                            ApplicationContextProvider.setCurrentAssetDateTime(assetDate);
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


                                AlertDialog.Builder builder = new AlertDialog.Builder(ApplicationContextProvider.getContext());
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
                        public void onCancel() {
                            Log.d("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@", "cancelled request");
                            tmpFile.delete();
                        }


                        @Override
                        public void onFinish() {
                            super.onFinish();
//                                        EWWave.hideLoadingIndicator();
                            tmpFile.delete();
                            finish();
                        }
                    }
            );


        } catch (FileNotFoundException e) {
            Log.e("FileNotFound", e.toString());
            e.printStackTrace();
        }
    }

}
