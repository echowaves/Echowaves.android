package com.echowaves.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.image.SmartImageView;


public class DetailedImageFragment extends Fragment {

    String imageUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_detailed_image, container, false);

        imageUrl = getArguments().getString("imageUrl");

//        TextView textView = (TextView) rootView.findViewById(R.id.detailedimage_text);
//
//        textView.setText(imageUrl);


        SmartImageView myImage = (SmartImageView) rootView.findViewById(R.id.detailedimage_image);

        myImage.setImageUrl(imageUrl);

        return rootView;
    }

}
