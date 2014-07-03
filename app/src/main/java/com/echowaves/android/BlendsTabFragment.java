package com.echowaves.android;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        return view;
    }

    @Override
    public void updateWave(String waveName) {
        Log.d("BlendsTabFragment updateWave", waveName);
    }
}
