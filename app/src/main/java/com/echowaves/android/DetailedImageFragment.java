package com.echowaves.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.opengl.GLES10;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.echowaves.android.model.ApplicationContextProvider;
import com.echowaves.android.model.EWImage;
import com.echowaves.android.util.EWJsonHttpResponseHandler;
import com.echowaves.android.util.TouchImageView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.util.Date;

import javax.microedition.khronos.opengles.GL10;


public class DetailedImageFragment extends Fragment implements EWConstants {
    static final int REQUEST_SELECT_CONTACT = 1;

    private static boolean navVisible = true;
    //    private boolean fullRes = false;
    private RelativeLayout navBar;

    private Button fullResButton;
    private ProgressBar progressBar;


    private String imageName;
    private String waveName;
    private ImageView backButton;
    private ImageButton saveButton;
    private ImageButton shareButton;
    private ImageButton deleteButton;
    private TextView dateTimeTextView;
    private TextView waveNameTextView;
    private TouchImageView imageView;

    @Override
    public View getView() {
        if (navVisible) {
            navBar.setVisibility(View.VISIBLE);
            waveNameTextView.setVisibility(View.VISIBLE);
        } else {
            navBar.setVisibility(View.GONE);
            waveNameTextView.setVisibility(View.INVISIBLE);
        }
        return super.getView();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_detailed_image, container, false);

        navBar = (RelativeLayout) rootView.findViewById(R.id.detailedimage_navBar);

        fullResButton = (Button) rootView.findViewById(R.id.detailedimage_fullResButton);
        fullResButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
//                fullRes = true;
                fullResButton.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

//                imageView.setImageUrl(EWConstants.EWAWSBucket + "/img/" + waveName + "/" + imageName);
//                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                imageView.setMaxZoom(10f);
                EWImage.loadFullImage(imageName, waveName, new AsyncHttpResponseHandler() {

                            @Override
                            public void onProgress(int bytesWritten, int totalSize) {
                                progressBar.setProgress(100 * bytesWritten / totalSize);
                            }


                            @Override
                            public void onStart() {
                                super.onStart();
                                progressBar.setProgress(0);
                                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                imageView.setMaxZoom(1f);
                                imageView.sharedConstructing(rootView.getContext());
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                int[] maxSize = new int[1];
                                GLES10.glGetIntegerv(GL10.GL_MAX_TEXTURE_SIZE, maxSize, 0);
                                int maxDim = maxSize[0];

                                Bitmap bmp = BitmapFactory.decodeStream(new ByteArrayInputStream(responseBody));

                                final int height = bmp.getHeight();
                                final int width = bmp.getWidth();
                                int scale = 1;


                                if (height > maxDim || width > maxDim) {
                                    // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                                    // height and width larger than the requested height and width.
                                    while ((height / scale) > maxDim
                                            || (width / scale) > maxDim) {
                                        scale *= 2;
                                    }

                                    Bitmap scaled = Bitmap.createScaledBitmap(bmp, bmp.getWidth() / scale, bmp.getHeight() / scale, true);

                                    imageView.setImageBitmap(scaled);

                                } else {
                                    imageView.setImageBitmap(bmp);
                                }

                                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                imageView.setMaxZoom(10f);
                                imageView.sharedConstructing(rootView.getContext());
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

//                            @Override
//                            public void onCancel() {
//                                Log.d("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@", "cancelled request");
//                                boolean deleteSuccessfull = tmpFile.delete();
//                                Log.d("2. delete file success ", String.valueOf(deleteSuccessfull));
//                                uploadProgressDetailsActivityIsActive = false;
//                                cancelButton.setEnabled(false);
//
//                            }


                            @Override
                            public void onFinish() {
                                super.onFinish();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                );
            }
        });


        progressBar = (ProgressBar) rootView.findViewById(R.id.detailedimage_progressBar);

        imageName = getArguments().getString("imageName");
        waveName = getArguments().getString("waveName");

        backButton = (ImageView) rootView.findViewById(R.id.detailedimage_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                getActivity().finish();
            }
        });

        saveButton = (ImageButton) rootView.findViewById(R.id.detailedimage_saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (EWImage.saveImageToAssetLibrary(imageName, waveName, false)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                    builder.setTitle("Saved")
                            .setMessage("Photo Saved Locally")
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                }

            }
        });


        shareButton = (ImageButton) rootView.findViewById(R.id.detailedimage_shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_SELECT_CONTACT);
                }
            }
        });


        deleteButton = (ImageButton) rootView.findViewById(R.id.detailedimage_deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {


                AlertDialog.Builder alertDialogConfirmImageDeletion = new AlertDialog.Builder(
                        rootView.getContext());
                alertDialogConfirmImageDeletion.setTitle("Delete Photo?");

                // set dialog message
                alertDialogConfirmImageDeletion
                        .setMessage("Delete?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, close
                                // current activity
                                EWImage.deleteImage(imageName, waveName, new EWJsonHttpResponseHandler(rootView.getContext()) {
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                                                Log.d(">>>>>>>>>>>>>>>>>>>> ", jsonResponse.toString());
                                                getActivity().finish();
                                            }
                                        }
                                );
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogConfirmImageDeletion.create();

                // show it
                alertDialog.show();
            }
        });

        if (waveName.equals(WavePickerFragment.getCurrentWaveName())) {
            shareButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
            saveButton.setVisibility(View.GONE);
        } else {
            shareButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
            saveButton.setVisibility(View.VISIBLE);
        }

        try {
            String dateTimeString = imageName.split("\\.")[0];
            Date dateTime = simpleDateFormat.parse(dateTimeString);

            dateTimeTextView = (TextView) rootView.findViewById(R.id.detailedimage_dateTime);
            dateTimeTextView.setText(naturalDateFormat.format(dateTime));
        } catch (ParseException ew) {
            Log.e("parsing exception", ew.toString(), ew);
        }


        waveNameTextView = (TextView) rootView.findViewById(R.id.detailedimage_waveName);
        waveNameTextView.setText(waveName);

        imageView = (TouchImageView) rootView.findViewById(R.id.detailedimage_image);

        EWImage.loadThumbImage(imageName, waveName, new AsyncHttpResponseHandler() {


                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        Bitmap bmp = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
                        imageView = (TouchImageView) rootView.findViewById(R.id.detailedimage_image);
                        imageView.setImageBitmap(bmp);

                        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                                imageView.setMaxZoom(10f);
                        imageView.sharedConstructing(rootView.getContext());

                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d("$$$$$$$$$$$$$$$$$$$$$$$$$$$$", "cicked");
                                if (navVisible) {
                                    navBar.setVisibility(View.GONE);
                                    navVisible = false;
                                } else {
                                    navBar.setVisibility(View.VISIBLE);
                                    navVisible = true;
                                }
                            }
                        });

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


                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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


        return rootView;
    }

    /**
     * the following method is used to send sms based on the contact information returned from the address book
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_SELECT_CONTACT && resultCode == Activity.RESULT_OK) {
            Uri contactUri = data.getData();
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.Contacts.DISPLAY_NAME,};
            final Cursor cursor = getActivity().getContentResolver().query(contactUri, projection,
                    null, null, null);
            // If the cursor returned is valid, get the phone number
            if (cursor != null && cursor.moveToFirst()) {
                // Do something with the phone number

                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                final String number = cursor.getString(numberIndex);
                final String name = cursor.getString(nameIndex);
                Log.d("!!!!!!!!!!!!!!!!!! result number", number);
                Log.d("!!!!!!!!!!!!!!!!!! result name  ", name);


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirm SMS Sending")
                        .setMessage("Do you want to send SMS invite to " + name + " " + number + "?")
                        .setCancelable(true)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                EWImage.shareImage(imageName, waveName, new EWJsonHttpResponseHandler(getActivity()) {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                                        Log.d(">>>>>>>>>>>>>>>>>>>> ", jsonResponse.toString());

                                        try {
                                            final String token = jsonResponse.getString("token");

                                            final String msg = "Look at my photo and blend with my wave http://echowaves.com/mobile?token=" + token;
                                            SmsManager sm = SmsManager.getDefault();
                                            sm.sendTextMessage(number, null, msg, null, null);

                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                                            builder.setTitle("Success!")
                                                    .setMessage("SMS invite successfully sent to " + name)
                                                    .setCancelable(false)
                                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                        }
                                                    });
                                            AlertDialog alert = builder.create();
                                            alert.show();
                                        } catch (JSONException jsonException) {
                                            Log.e("json exception", jsonException.toString());
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                                            builder.setTitle("Error!")
                                                    .setMessage("Error.")
                                                    .setCancelable(false)
                                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                        }
                                                    });
                                            AlertDialog alert = builder.create();
                                            alert.show();

                                        }


                                    }
                                });


                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();


            }
        }

    }
}
