package com.echowaves.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class BlendsTabFragment extends EWTabFragment {
    @Override
    public void onStart() {
        super.onStart();
        Log.d("!!!!!!!!!!!!!", "BlendsTabFragment onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("!!!!!!!!!!!!!", "BlendsTabFragment onResume()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blends, container, false);

        Log.d("BlendsTabFragment", view.toString());

        Button addBlendingButton = (Button) view.findViewById(R.id.blends_addBlending);
        //Listening to button event
        addBlendingButton.
                setOnClickListener(
                        new View.OnClickListener() {
                            public void onClick(final View v) {
                                Intent addChildWaveIntent = new Intent(v.getContext(), BlendwithActivity.class);
                                startActivity(addChildWaveIntent);
                            }
                        }
                );

        return view;
    }

    @Override
    public void updateWave(String waveName) {
        super.updateWave(waveName);
        Log.d("BlendsTabFragment updateWave", waveName);
    }
}
