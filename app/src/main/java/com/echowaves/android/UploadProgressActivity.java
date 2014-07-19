package com.echowaves.android;

import android.content.Context;
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

import java.io.File;
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
                Log.d("Asset location = ", imageLocation);

                File imageFile = new File(imageLocation);
                if (imageFile.exists()) {   // is there a better way to do this?
                    Bitmap bm = BitmapFactory.decodeFile(imageLocation);
                    int orientation = cursor.getInt(5);

                    Log.d("###################### orientation: ", String.valueOf(orientation));
                    Matrix matrix = new Matrix();
                    matrix.postRotate(orientation);

                    bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true); // rotating bitmap

                    imageView.setImageBitmap(bm);
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
