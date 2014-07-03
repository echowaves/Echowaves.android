package com.echowaves.android;

import android.app.Fragment;

/**
 * Created by dmitry on 7/2/14.
 *
 */
abstract public class EWTabFragment extends Fragment {
    @SuppressWarnings("unused")
    protected abstract void updateWave(String waveName);
}
