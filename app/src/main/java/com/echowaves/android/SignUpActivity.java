package com.echowaves.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.echowaves.android.model.EWWave;
import com.echowaves.android.util.EWJsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;


public class SignUpActivity extends EWActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // show soft keyboard automagically
        EditText waveName = (EditText) findViewById(R.id.signup_wave_name);
        waveName.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        ImageView backButton = (ImageView) findViewById(R.id.signup_imageViewBack);        //Listening to button event
        backButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(final View v) {
                Intent home = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(home);
            }
        });


        Button createWaveButton = (Button) findViewById(R.id.signup_create_wave_button);
        //Listening to button event
        createWaveButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(final View v) {

                final String waveName = ((EditText) findViewById(R.id.signup_wave_name)).getText().toString();
                final String wavePassword = ((EditText) findViewById(R.id.signup_password)).getText().toString();
                final String confirmPassword = ((EditText) findViewById(R.id.signup_password_confirm)).getText().toString();

                EWWave.createWaveWithName(
                        waveName,
                        wavePassword,
                        confirmPassword,
                        new EWJsonHttpResponseHandler(v.getContext()) {
                            @Override
                            public void onStart() {
                                EWWave.showLoadingIndicator(v.getContext());
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                                Log.d(">>>>>>>>>>>>>>>>>>>> ", jsonResponse.toString());

                                EWWave.storeCredentialForWave(waveName, wavePassword);

                                Intent createWave = new Intent(getApplicationContext(), NavigationTabBarActivity.class);
                                startActivity(createWave);
                            }


                            @Override
                            public void onFinish() {
                                EWWave.hideLoadingIndicator();
                            }
                        }
                );
            }

        });

    }


}
