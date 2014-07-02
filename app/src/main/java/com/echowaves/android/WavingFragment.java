package com.echowaves.android;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WavingFragment extends Fragment {


    @Override
    public void onStart() {
        super.onStart();
        Log.d("!!!!!!!!!!!!!", "WavingFragment onStart()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_waving, container, false);
        Log.d("WavingFragment", view.toString());

        return view;
    }
}
