package com.echowaves.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.echowaves.android.model.ApplicationContextProvider;
import com.echowaves.android.model.EWWave;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

public class HomeActivity extends BaseActivity {

    private static int tuneInCount = 0;

    @Override
    protected void onResume() {
        super.onResume();
        // The activity has become visible (it is now "resumed").

        final String storedWaveName = EWWave.getStoredWaveName();
        final String storedWavePassword = EWWave.getStoredWavePassword();
        // auto tunein
        if (!"".equals(storedWaveName) && tuneInCount == 0) {
            EWWave.tuneInWithNameAndPassword(storedWaveName, storedWavePassword, new JsonHttpResponseHandler() {
//                @Override
//                public void onStart() {
//                    EWWave.showLoadingIndicator(ApplicationContextProvider.getContext());
//                }

                @Override
                public void onSuccess(JSONObject jsonResponse) {
                    tuneInCount++;
                    Log.d(">>>>>>>>>>>>>>>>>>>> ", jsonResponse.toString());

                    EWWave.storeCredentialForWave(storedWaveName, storedWavePassword);

                    Intent tuneIn = new Intent(ApplicationContextProvider.getContext(), NavigationTabBarActivity.class);
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

//                        String msg = "";
//                        if (null != responseBody) {
//                            try {
//                                JSONObject jsonResponse = new JSONObject(new String(responseBody));
//                                msg = jsonResponse.getString("error");
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        } else {
//                            msg = error.getMessage();
//                        }


//                        AlertDialog.Builder builder = new AlertDialog.Builder(ApplicationContextProvider.getContext());
//                        builder.setTitle("Error")
//                                .setMessage(msg)
//                                .setCancelable(false)
//                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                    }
//                                });
//                        AlertDialog alert = builder.create();
//                        alert.show();
                    }
                }


//                @Override
//                public void onFinish() {
//                    EWWave.hideLoadingIndicator();
//                }
            });

        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        Button tuneInButton = (Button) findViewById(R.id.home_tuneIn);
        //Listening to button event
        tuneInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent tuneIn = new Intent(ApplicationContextProvider.getContext(), SignInActivity.class);
                startActivity(tuneIn);
            }
        });

        Button createWaveButton = (Button) findViewById(R.id.home_createWave);
        //Listening to button event
        createWaveButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent createWave = new Intent(ApplicationContextProvider.getContext(), SignUpActivity.class);
                startActivity(createWave);
            }
        });


    }

}
