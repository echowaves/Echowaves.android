package com.echowaves.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.echowaves.android.model.ApplicationContextProvider;
import com.echowaves.android.model.EWImage;
import com.echowaves.android.model.EWWave;
import com.echowaves.android.util.EWJsonHttpResponseHandler;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;

public class UploadProgressActivity extends EWActivity {
    private TextView photosCount;
    private ProgressBar progressBar;
    private ImageView imageView;
    private Button cancelButton;
    private Button pauseAllButton;

    private Date currentAssetDate;

    private boolean uploadProgressDetailsActivityIsActive;

    private WaveOperation waveOperation;
    private RequestHandle currentUploadHandle;

    private int photosToUpload = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_progress);

        waveOperation = new WaveOperation(this);

        photosCount = (TextView) findViewById(R.id.upload_count);
        photosToUpload = ApplicationContextProvider.getPhotosCountSinceLast();
        photosCount.setText(String.valueOf(photosToUpload));

        imageView = (ImageView) findViewById(R.id.upload_imageView);
        progressBar = (ProgressBar) findViewById(R.id.upload_progressBar);

        uploadProgressDetailsActivityIsActive = false;

        pauseAllButton = (Button) findViewById(R.id.upload_pauseAllButton);
        //Listening to button event
        pauseAllButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (currentUploadHandle != null) {
                    currentUploadHandle.cancel(true);
                    currentUploadHandle = null;
                }

                if (waveOperation != null) {
                    waveOperation.cancel(true);
                    waveOperation = null;
                }
                finish();
            }
        });

        cancelButton = (Button) findViewById(R.id.upload_cancelButton);
        cancelButton.setEnabled(false);


        waveOperation.execute();
    }


    private class WaveOperation extends AsyncTask<Void, Object, String> {
        private Context context;

        public WaveOperation(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... arg0) {
            waveAll();
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
//            Intent navBarIntent = new Intent(context, NavigationTabBarActivity.class);
//            startActivity(navBarIntent);
            setResult(2);

            if(photosToUpload > 0) {
                EWWave.sendPushNotifyBadge(photosToUpload, new EWJsonHttpResponseHandler(this.context));
                Log.d("******************************************************", "sending push notification that " + photosToUpload + " photos uploaded");
            }
            finish();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onCancelled() {
        }

        //reduce image size otherwise the image does not render, eghh
        private Bitmap loadImage(String imgPath) {
            BitmapFactory.Options options;
            try {
                options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                return BitmapFactory.decodeFile(imgPath, options);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Object... params) {
            cancelButton.setEnabled(false);

            Integer totalCount = (Integer) params[0];
            photosCount.setText(totalCount.toString());

            final File tmpFile = (File) params[1];
            final Date assetDate = (Date) params[2];

//            Bitmap bitMap = BitmapFactory.decodeFile(tmpFile.getAbsolutePath());
            imageView.setImageBitmap(loadImage(tmpFile.getAbsolutePath()));

            cancelButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    Log.d("###################################", "canceling ");
                    if (currentUploadHandle != null) {
                        currentUploadHandle.cancel(true);
                    }

                    ApplicationContextProvider.setCurrentAssetDateTime(currentAssetDate);
                    uploadProgressDetailsActivityIsActive = false;
                    cancelButton.setEnabled(false);
                    photosToUpload--;
                }
            });


//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            try {
                currentUploadHandle = EWImage.uploadPhoto(tmpFile,
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
                                    boolean deleteSuccessfull = tmpFile.delete();
                                    Log.d("1. delete file success ", String.valueOf(deleteSuccessfull));
                                    uploadProgressDetailsActivityIsActive = false;
                                    cancelButton.setEnabled(false);

                                }
                            }

                            @Override
                            public void onCancel() {
                                Log.d("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@", "cancelled request");
                                boolean deleteSuccessfull = tmpFile.delete();
                                Log.d("2. delete file success ", String.valueOf(deleteSuccessfull));
                                uploadProgressDetailsActivityIsActive = false;
                                cancelButton.setEnabled(false);

                            }


                            @Override
                            public void onFinish() {
                                super.onFinish();
//                                        EWWave.hideLoadingIndicator();
                                boolean deleteSuccessfull = tmpFile.delete();
                                Log.d("3. delete file success ", String.valueOf(deleteSuccessfull));
                                uploadProgressDetailsActivityIsActive = false;
                                cancelButton.setEnabled(false);

                            }
                        }
                );

                cancelButton.setEnabled(true);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.d("!!!!!!!!!!!!!!!!!!!!", "fileNotFound exception" + e.toString());
            }


        }


        private void waveAll() {
            String[] projection = new String[]{
                    MediaStore.Images.ImageColumns._ID,
                    MediaStore.Images.ImageColumns.DATA,
                    MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.ImageColumns.DATE_TAKEN,
                    MediaStore.Images.ImageColumns.MIME_TYPE,
                    MediaStore.Images.ImageColumns.ORIENTATION
            };

            String selection = MediaStore.Images.Media.DATE_TAKEN + " > ?";
            String[] selectionArgs = {String.valueOf(ApplicationContextProvider.getCurrentAssetDateTime().getTime())};
            final Cursor cursor = context.getContentResolver()
                    .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection,
                            selectionArgs, MediaStore.Images.ImageColumns.DATE_TAKEN + " ASC");

            // Put it in the image view
            while (cursor.moveToNext() && !isCancelled()) {
//                Log.d("&&&&&&&&&&&&&&&&&&&&&& totalCount:", String.valueOf(totalCount));

                final String imageLocation = cursor.getString(1);
                Log.d("###################### Asset location = ", imageLocation);

                File imageFile = new File(imageLocation);
                if (imageFile.exists()) {   // is there a better way to do this?

                    Bitmap bm = BitmapFactory.decodeFile(imageLocation);
                    int orientation = cursor.getInt(5);

//                    Log.d("###################### orientation: ", String.valueOf(orientation));
                    long timeTaken = cursor.getLong(3);

                    final String dateTaken = simpleDateFormat.format(timeTaken);

                    Matrix matrix = new Matrix();
                    matrix.postRotate(orientation);

                    bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true); // rotating bitmap


                    // write temp file
                    String tmpPath = Environment.getExternalStorageDirectory().toString();

                    final File tmpFile = new File(tmpPath, dateTaken + ".jpg");
                    OutputStream fOut = null;
                    try {
                        fOut = new FileOutputStream(tmpFile);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
//                    tmpFile.flush();
//                    tmpFile.close();

                    uploadProgressDetailsActivityIsActive = true;
                    currentAssetDate = new Date(Long.valueOf(cursor.getString(3)));

                    //publish progress
                    publishProgress(ApplicationContextProvider.getPhotosCountSinceLast(), tmpFile, new Date(timeTaken));


                    while (uploadProgressDetailsActivityIsActive) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                currentUploadHandle = null;
            }
//            ApplicationContextProvider.setCurrentAssetDateTime(new Date());
        }
    }
}
