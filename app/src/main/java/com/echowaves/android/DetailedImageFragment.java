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
import com.loopj.android.image.SmartImageView;

import org.apache.http.Header;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;


public class DetailedImageFragment extends Fragment implements EWConstants {

    String imageName;
    String waveName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_detailed_image, container, false);

        imageName = getArguments().getString("imageName");
        waveName = getArguments().getString("waveName");

        ImageView backButton = (ImageView) rootView.findViewById(R.id.detailedimage_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                getActivity().finish();
            }
        });

        ImageButton saveButton = (ImageButton) rootView.findViewById(R.id.detailedimage_saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                getActivity().finish();
            }
        });

        ImageButton shareButton = (ImageButton) rootView.findViewById(R.id.detailedimage_shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                getActivity().finish();
            }
        });

        ImageButton deleteButton = (ImageButton) rootView.findViewById(R.id.detailedimage_deleteButton);
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

            TextView dateTimeTextView = (TextView) rootView.findViewById(R.id.detailedimage_dateTime);
            dateTimeTextView.setText(naturalDateFormat.format(dateTime));
        } catch (ParseException ew) {
            Log.e("parsing exception", ew.toString(), ew);
        }


        TextView waveNameTextView = (TextView) rootView.findViewById(R.id.detailedimage_waveName);
        waveNameTextView.setText(waveName);

        SmartImageView myImage = (SmartImageView) rootView.findViewById(R.id.detailedimage_image);
        String thumb = EWConstants.EWAWSBucket + "/img/" + waveName + "/thumb_" + imageName;
        myImage.setImageUrl(thumb);

        return rootView;
    }

}
