package com.echowaves.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.TextView;

import com.echowaves.android.model.ApplicationContextProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UploadProgressActivity extends EWActivity {
    public final static int OPERATION_CANCEL = 100;
    public final static int OPERATION_PAUSE_ALL = 101;
    public final static int OPERATION_CONTINUE = 102;


    private boolean uploadProgressDetailsActivityIsActive = false;
    private TextView photosCount;
    private WaveOperation waveOperation;
    private Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_progress);
        activity = this;
        photosCount = (TextView) findViewById(R.id.uploadprogress_count);

        waveOperation = new WaveOperation(this);
        waveOperation.execute();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intetData) {
        Log.d("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^", "onActivityResult");
        switch (resultCode) {
            case OPERATION_CANCEL:
                break;
            case OPERATION_PAUSE_ALL:
                break;
            case OPERATION_CONTINUE:
                break;
        }
        uploadProgressDetailsActivityIsActive = false;
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
            Intent navBarIntent = new Intent(context, NavigationTabBarActivity.class);
            startActivity(navBarIntent);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onCancelled() {
        }

        @Override
        protected void onProgressUpdate(Object... params) {
            Integer totalCount = (Integer) params[0];
            photosCount.setText(totalCount.toString());
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
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmssSSSS");

                    final String dateTaken = simpleDateFormat.format(timeTaken);

                    Matrix matrix = new Matrix();
                    matrix.postRotate(orientation);

                    bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true); // rotating bitmap

                    //publish progress
                    publishProgress(ApplicationContextProvider.getPhotosCountSinceLast());

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

                    Intent uploadDetailsIntent = new Intent(activity, UploadProgressDetailsActivity.class);
                    uploadDetailsIntent.putExtra("tmpFile", tmpFile);
                    uploadDetailsIntent.putExtra("assetDate", new Date(timeTaken));
                    uploadProgressDetailsActivityIsActive = true;
                    activity.startActivityForResult(uploadDetailsIntent, 0);

                    while (uploadProgressDetailsActivityIsActive == true) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }


//            ApplicationContextProvider.setCurrentAssetDateTime(new Date());
        }


    }

}
