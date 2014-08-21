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


/**
 * A login screen that offers login via wave_name/password.
 */
public class SignInActivity extends EWActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // show soft keyboard automagically
        EditText waveName = (EditText) findViewById(R.id.tunein_wave_name);
        waveName.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        String storedWaveName = EWWave.getStoredWaveName();
        String storedWavePassword = EWWave.getStoredWavePassword();
        if (!"".equals(storedWaveName)) {
            waveName.setText(storedWaveName);
            EditText wavePassword = (EditText) findViewById(R.id.tunein_wave_password);
            wavePassword.setText(storedWavePassword);
        }


        ImageView backButton = (ImageView) findViewById(R.id.tunein_imageViewBack);
        //Listening to button event
        backButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent home = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(home);
            }
        });

        Button tuneInButton = (Button) findViewById(R.id.tunein_sign_in_button);
        //Listening to button event
        tuneInButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(final View v) {

                final String waveName = ((EditText) findViewById(R.id.tunein_wave_name)).getText().toString();
                final String wavePassword = ((EditText) findViewById(R.id.tunein_wave_password)).getText().toString();

                EWWave.tuneInWithNameAndPassword(waveName, wavePassword, new EWJsonHttpResponseHandler(v.getContext()) {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                        Log.d(">>>>>>>>>>>>>>>>>>>> ", jsonResponse.toString());

                        EWWave.storeCredentialForWave(waveName, wavePassword);

//                        Intent tuneIn = new Intent(getApplicationContext(), NavigationTabBarActivity.class);
                        Intent tuneIn = new Intent(getApplicationContext(), NavigationTabBarActivity.class);
                        startActivity(tuneIn);
                    }

                });
            }
        });

    }

}
