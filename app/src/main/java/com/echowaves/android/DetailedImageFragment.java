package com.echowaves.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;


public class DetailedImageFragment extends Fragment {

    String imageName;
    String waveName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_detailed_image, container, false);

        imageName = getArguments().getString("imageName");
        waveName = getArguments().getString("waveName");

//        Log.d(imageName)

//        TextView textView = (TextView) rootView.findViewById(R.id.detailedimage_text);
//
//        textView.setText(imageName);


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
                getActivity().finish();
            }
        });

        TextView dateTime = (TextView) rootView.findViewById(R.id.detailedimage_dateTime);
        dateTime.setText(imageName);

        TextView waveNameTextView = (TextView) rootView.findViewById(R.id.detailedimage_waveName);
        waveNameTextView.setText(waveName);

        SmartImageView myImage = (SmartImageView) rootView.findViewById(R.id.detailedimage_image);
        String thumb = EWConstants.EWAWSBucket + "/img/" + waveName + "/thumb_" + imageName;
        myImage.setImageUrl(thumb);

        return rootView;
    }

}
