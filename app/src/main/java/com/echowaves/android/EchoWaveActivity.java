package com.echowaves.android;

import android.os.Bundle;

import com.echowaves.android.model.EWImage;

public class EchoWaveActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_echo_wave);


        EWImage.debug();
    }

}
