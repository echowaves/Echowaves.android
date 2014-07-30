package com.echowaves.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class UploadProgressActivity extends EWActivity {
    public static RequestHandle currentRequestHandle;
    private TextView photosCount;
    private ProgressBar progressBar;
    private ImageView imageView;
    private Button cancelButton;
    private Button pauseAllButton;


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

        pauseAllButton = (Button) findViewById(R.id.upload_pauseAllButton);
        //Listening to button event
        pauseAllButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                currentRequestHandle.cancel(true);
                EWWave.cancelAllSynchRequests(true);
                Intent navBarIntent = new Intent(getApplicationContext(), NavigationTabBarActivity.class);
                startActivity(navBarIntent);
            }
        });

        imageView = (ImageView) findViewById(R.id.upload_imageView);
        progressBar = (ProgressBar) findViewById(R.id.upload_progressBar);
        photosCount = (TextView) findViewById(R.id.upload_count);

        cancelButton = (Button) findViewById(R.id.upload_cancelButton);
//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View arg0) {
//                if (currentRequestHandle != null)
//                    currentRequestHandle.cancel(true);
//            }
//        });


        pauseAllButton = (Button) findViewById(R.id.upload_pauseAllButton);

        new WaveOperation().execute("");

    }


    private class WaveOperation extends AsyncTask<String, Object, String> {

        @Override
        protected String doInBackground(String... params) {
            waveAll(ApplicationContextProvider.getContext());
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Object... params) {
            Bitmap bitmap = (Bitmap) params[0];
            Integer totalCount = (Integer) params[1];
            imageView.setImageBitmap(bitmap);
            photosCount.setText(totalCount.toString());
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

            String selection = MediaStore.Images.Media.DATE_TAKEN + " > ?";
            String[] selectionArgs = {String.valueOf(ApplicationContextProvider.getCurrentAssetDateTime().getTime())};
            Cursor cursor = context.getContentResolver()
                    .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection,
                            selectionArgs, MediaStore.Images.ImageColumns.DATE_TAKEN + " ASC");

            int totalCount = cursor.getCount();
            // Put it in the image view
            while (cursor.moveToNext()) {
//                Log.d("&&&&&&&&&&&&&&&&&&&&&& totalCount:", String.valueOf(totalCount));

                final String imageLocation = cursor.getString(1);
                Log.d("###################### Asset location = ", imageLocation);

                File imageFile = new File(imageLocation);
                if (imageFile.exists()) {   // is there a better way to do this?

                    Bitmap bm = BitmapFactory.decodeFile(imageLocation);
                    int orientation = cursor.getInt(5);

//                    Log.d("###################### orientation: ", String.valueOf(orientation));
                    long timeTaken = cursor.getLong(3);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmssSSSS");

                    final String dateTaken = simpleDateFormat.format(timeTaken);

                    Matrix matrix = new Matrix();
                    matrix.postRotate(orientation);

                    bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true); // rotating bitmap

                    publishProgress(bm, ApplicationContextProvider.getPhotosCountSinceLast());


                    final ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                    try {
                        EWImage.uploadPhoto(stream.toByteArray(), dateTaken + ".jpg", new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onProgress(int bytesWritten, int totalSize) {
                                        super.onProgress(bytesWritten, totalSize);
                                        Log.d("--------------progress: ", String.valueOf(bytesWritten) + " of " + String.valueOf(totalSize));
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
                                        progressBar.setProgress(100);
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
                                        super.onFinish();
//                                        EWWave.hideLoadingIndicator();
                                    }
                                }
                        );
                    } catch (FileNotFoundException e) {
                        Log.e("FileNotFound", e.toString());
                        e.printStackTrace();
                    }


//                    try {
                    Log.d("Asset time = ", cursor.getString(3));
                    ApplicationContextProvider.setCurrentAssetDateTime(new Date(Long.valueOf(cursor.getString(3))));
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }

                }
            }


            ApplicationContextProvider.setCurrentAssetDateTime(new Date());
            Intent navBarIntent = new Intent(getApplicationContext(), NavigationTabBarActivity.class);
            startActivity(navBarIntent);

        }

    }

}
