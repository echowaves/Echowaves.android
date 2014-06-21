package com.echowaves.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class SignUpActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ImageView backButton = (ImageView) findViewById(R.id.signup_imageViewBack);        //Listening to button event
        backButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent home = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(home);
            }
        });


        Button createWaveButton = (Button) findViewById(R.id.create_wave_button);
        //Listening to button event
        createWaveButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent createWave = new Intent(getApplicationContext(), NavigationTabBarActivity.class);
                startActivity(createWave);
            }
        });

    }



}
