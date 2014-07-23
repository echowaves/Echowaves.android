package com.echowaves.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.echowaves.android.model.ApplicationContextProvider;
import com.echowaves.android.model.EWImage;
import com.echowaves.android.model.EWWave;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;

public class UploadProgressActivity extends EWActivity {
    private TextView photosCount;

    @Override
    public void onStart() {
        super.onStart();
        Log.d("!!!!!!!!!!!!!", "WavingTabFragment onStart()");
        photosCount.setText(Long.toString(ApplicationContextProvider.getPhotosCountSinceLast()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_progress);

        Button pauseAllButton = (Button) findViewById(R.id.upload_pauseAllButton);
        //Listening to button event
        pauseAllButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent navBarIntent = new Intent(getApplicationContext(), NavigationTabBarActivity.class);
                startActivity(navBarIntent);
            }
        });


        waveAll(ApplicationContextProvider.getContext());

    }

    private void waveAll(Context context) {
        String[] projection = new String[]{
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.MIME_TYPE,
                MediaStore.Images.ImageColumns.ORIENTATION
        };

        Cursor cursor = null;
        do {
            photosCount = (TextView) findViewById(R.id.upload_count);

            if (cursor != null) {
                cursor.close();
            }
            String selection = MediaStore.Images.Media.DATE_TAKEN + " > ?";
            String[] selectionArgs = {String.valueOf(ApplicationContextProvider.getCurrentAssetDateTime().getTime())};
            cursor = context.getContentResolver()
                    .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection,
                            selectionArgs, MediaStore.Images.ImageColumns.DATE_TAKEN + " ASC");

            // Put it in the image view
            if (cursor.moveToFirst()) {
                final ImageView imageView = (ImageView) findViewById(R.id.upload_imageView);
                String imageLocation = cursor.getString(1);
                Log.d("###################### Asset location = ", imageLocation);

                File imageFile = new File(imageLocation);
                if (imageFile.exists()) {   // is there a better way to do this?
                    Bitmap bm = BitmapFactory.decodeFile(imageLocation);
                    int orientation = cursor.getInt(5);

                    Log.d("###################### orientation: ", String.valueOf(orientation));
                    Matrix matrix = new Matrix();
                    matrix.postRotate(orientation);

                    bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true); // rotating bitmap

                    imageView.setImageBitmap(bm);

                    String mimeType = cursor.getString(4);

                    try {
                        EWImage.uploadPhoto(imageFile, mimeType, new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onStart() {
                                        EWWave.showLoadingIndicator(ApplicationContextProvider.getContext());
                                    }

                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                        Log.d(">>>>>>>>>>>>>>>>>>>> statusCode:" + statusCode, responseBody.toString());
    //                                    Intent createWave = new Intent(getApplicationContext(), NavigationTabBarActivity.class);
    //                                    startActivity(createWave);
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
                                    public void onFinish() {
                                        EWWave.hideLoadingIndicator();
                                    }
                                }
                        );
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }


                }

//                try {
//                    Log.d("~~~~~~~~~~~~ sleeping", "");
//                    Thread.sleep(500);
//                } catch(InterruptedException ex) {
//                    Thread.currentThread().interrupt();
//                }

                Log.d("Asset time = ", cursor.getString(3));
                ApplicationContextProvider.setCurrentAssetDateTime(new Date(Long.valueOf(cursor.getString(3))));

            }


        } while (cursor.getCount() > 0 && !cursor.isLast());

//        ApplicationContextProvider.setCurrentAssetDateTime(new Date());
//        Intent navBarIntent = new Intent(getApplicationContext(), NavigationTabBarActivity.class);
//        startActivity(navBarIntent);

    }

}
