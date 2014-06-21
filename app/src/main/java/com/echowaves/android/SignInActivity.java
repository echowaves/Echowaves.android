package com.echowaves.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.echowaves.android.model.EWWave;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A login screen that offers login via wave_name/password.
 */
public class SignInActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // show soft keyboard automagically
        EditText waveName = (EditText) findViewById(R.id.tunein_wave_name);
        waveName.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

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

                EWWave.tuneInWithNameAndPassword(waveName, wavePassword, new JsonHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        EWWave.showLoadingIndicator(v.getContext());
                    }

                    @Override
                    public void onSuccess(JSONObject jsonResponse) {
                        Log.d(">>>>>>>>>>>>>>>>>>>> ", jsonResponse.toString());
                        Intent tuneIn = new Intent(getApplicationContext(), NavigationTabBarActivity.class);
                        startActivity(tuneIn);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if (headers != null) {
                            for (Header h : headers) {
                                Log.d("................ failed   key: ", h.getName());
                                Log.d("................ failed value: ", h.getValue());
                            }
                        }
                        if (responseBody != null) {
                            Log.d("................ failed : ", new String(responseBody));
                        }
                        if (error != null) {
                            Log.d("................ failed error: ", error.toString());

                            String msg = "";
                            if (null != responseBody) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(new String(responseBody));
                                    msg = jsonResponse.getString("error");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                msg = error.getMessage();
                            }


                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder.setTitle("Error")
                                    .setMessage(msg)
                                    .setCancelable(false)
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }


                    @Override
                    public void onFinish() {
                        EWWave.hideLoadingIndicator();
                    }
                });
            }
        });

    }

}
