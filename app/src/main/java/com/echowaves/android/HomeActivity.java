package com.echowaves.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.echowaves.android.model.ApplicationContextProvider;
import com.echowaves.android.model.EWWave;
import com.echowaves.android.util.EWJsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

public class HomeActivity extends EWActivity {

    private static int tuneInCount = 0;

    public static String shareToken = null; // we will access this property in deep actions to determine if need to open nested actions

    @Override
    protected void onResume() {
        super.onResume();
        // The activity has become visible (it is now "resumed").

        Intent intent = getIntent();
// check if this intent is started via custom scheme link
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            // may be some test here with your custom uri
            shareToken = uri.getQueryParameter("token"); // "token" is set
            Log.d("%%%%%%%%%%%%%%%%%%%%%%%%%% token", shareToken);
        }


        final String storedWaveName = EWWave.getStoredWaveName();
        final String storedWavePassword = EWWave.getStoredWavePassword();
        // auto tunein
        if (!"".equals(storedWaveName) && tuneInCount == 0) {
            EWWave.tuneInWithNameAndPassword(storedWaveName, storedWavePassword, new EWJsonHttpResponseHandler(this) {
//                @Override
//                public void onStart() {
//                    EWWave.showLoadingIndicator(ApplicationContextProvider.getContext());
//                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                    tuneInCount++;
                    Log.d(">>>>>>>>>>>>>>>>>>>> ", jsonResponse.toString());

                    EWWave.storeCredentialForWave(storedWaveName, storedWavePassword);

                    Intent tuneIn = new Intent(ApplicationContextProvider.getContext(), NavigationTabBarActivity.class);
                    startActivity(tuneIn);
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
