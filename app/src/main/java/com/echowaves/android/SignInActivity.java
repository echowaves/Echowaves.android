package com.echowaves.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


/**
 * A login screen that offers login via wave_name/password.

 */
public class SignInActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        ImageView backButton = (ImageView) findViewById(R.id.imageViewBack);
        //Listening to button event
        backButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent home = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(home);
            }
        });

        Button tuneInButton = (Button) findViewById(R.id.sign_in_button);
        //Listening to button event
        tuneInButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

//                AsyncHttpClient client = new AsyncHttpClient();

                Intent tuneIn = new Intent(getApplicationContext(), NavigationTabBarActivity.class);
                startActivity(tuneIn);
            }
        });


    }



}



