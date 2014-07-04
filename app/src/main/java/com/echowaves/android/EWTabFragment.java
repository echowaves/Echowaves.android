package com.echowaves.android;

import android.app.Fragment;

/**
 * Created by dmitry on 7/2/14.
 *
 */
abstract public class EWTabFragment extends Fragment {
    private String waveName;

    @SuppressWarnings("unused")
    public String getWaveName() {
        return waveName;
    }

    public void updateWave(String waveName) {
        this.waveName = waveName;
    }
}
