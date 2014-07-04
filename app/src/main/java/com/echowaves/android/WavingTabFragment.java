package com.echowaves.android;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WavingTabFragment extends EWTabFragment {


    @Override
    public void onStart() {
        super.onStart();
        Log.d("!!!!!!!!!!!!!", "WavingTabFragment onStart()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_waving, container, false);
        Log.d("WavingTabFragment", view.toString());

        return view;
    }

    @Override
    public void updateWave(String waveName) {
        super.updateWave(waveName);
        Log.d("WavingTabFragment updateWave", waveName);
    }
}
