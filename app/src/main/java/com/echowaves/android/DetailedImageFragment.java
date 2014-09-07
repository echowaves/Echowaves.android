package com.echowaves.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.echowaves.android.model.EWImage;
import com.echowaves.android.util.EWJsonHttpResponseHandler;
import com.echowaves.android.util.TouchImageView;
import com.loopj.android.image.SmartImageView;

import org.apache.http.Header;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;


public class DetailedImageFragment extends Fragment implements EWConstants {

    String imageName;
    String waveName;

    ImageView backButton;

    ImageButton saveButton;
    ImageButton shareButton;
    ImageButton deleteButton;

    TextView dateTimeTextView;
    TextView waveNameTextView;
    SmartImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_detailed_image, container, false);

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
                getActivity().finish();
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
//            shareButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
        } else {
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
        imageView.setImageUrl(EWConstants.EWAWSBucket + "/img/" + waveName + "/thumb_" + imageName);

        return rootView;
    }

}
