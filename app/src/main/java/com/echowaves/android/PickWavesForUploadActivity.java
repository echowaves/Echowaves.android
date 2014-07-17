package com.echowaves.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class PickWavesForUploadActivity extends EWActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_waves_for_upload);

        ImageView backButton = (ImageView) findViewById(R.id.pickwaves_imageViewBack);
        //Listening to button event
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent home = new Intent(getApplicationContext(), NavigationTabBarActivity.class);
                startActivity(home);
            }
        });

    }

}
