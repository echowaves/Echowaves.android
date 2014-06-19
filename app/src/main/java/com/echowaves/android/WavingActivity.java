package com.echowaves.android;

import android.os.Bundle;

import com.echowaves.android.model.EWWave;

public class WavingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waving);

        EWWave.debug();

    }


}
