package com.echowaves.android;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.echowaves.android.model.ApplicationContextProvider;
import com.echowaves.android.model.EWWave;
import com.echowaves.android.util.EWJsonHttpResponseHandler;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.IOException;

public class HomeActivity extends EWActivity {

    //    public static final String PROPERTY_REG_ID = "registration_id";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static String shareToken = null; // we will access this property in deep actions to determine if need to open nested actions
    private static int tuneInCount = 0;
    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    String SENDER_ID = "1003354539827";
    GoogleCloudMessaging gcm;
    String regid;

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

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

        // Check device for Play Services APK.
        if (checkPlayServices()) {

            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(getApplicationContext());

            if (regid.isEmpty()) {
                new RegisterInBackground().execute();
            }

            Log.d("^^^^^^^^^^^^^^^^^^^^^^^^", "regId: " + regid);

        }

    }

    /**
     * Gets the current registration ID for application on GCM service.
     * <p/>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     * registration ID.
     */
    private String getRegistrationId(Context context) {
        String registrationId = EWWave.getStoredDeviceToken();
        if (registrationId.isEmpty()) {
            Log.i("home", "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = EWWave.getStoredAppVersion();
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i("home", "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
//    private SharedPreferences getGCMPreferences() {
//        // This sample app persists the registration ID in shared preferences, but
//        // how you store the regID in your app is up to you.
//        return getSharedPreferences(HomeActivity.class.getSimpleName(),
//                Context.MODE_PRIVATE);
//    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("home", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
     * or CCS to send messages to your app. Not needed for this demo since the
     * device sends upstream messages to a server that echoes back the message
     * using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend() {
        // Your implementation here.
        Log.d("^^^^^^^^^^^^^^^^^^", "background1 regid: " + regid);
    }

    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId   registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        int appVersion = getAppVersion(context);
        Log.i("home", "Saving regId on app version " + appVersion);

        EWWave.storeDeviceToken(regId);
        EWWave.storeAppVersion(appVersion);
        Log.d("^^^^^^^^^^^^^^^^^^", "background2 regid: " + regid);
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p/>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private class RegisterInBackground extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String msg;
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                }
                regid = gcm.register(SENDER_ID);
                msg = "Device registered, registration ID=" + regid;

                // You should send the registration ID to your server over HTTP,
                // so it can use GCM/HTTP or CCS to send messages to your app.
                // The request to your server should be authenticated if your app
                // is using accounts.
                sendRegistrationIdToBackend();

                // For this demo: we don't need to send it because the device
                // will send upstream messages to a server that echo back the
                // message using the 'from' address in the message.

                // Persist the regID - no need to register again.
                storeRegistrationId(getApplicationContext(), regid);
            } catch (IOException ex) {
                msg = "Error :" + ex.getMessage();
                // If there is an error, don't just keep trying to register.
                // Require the user to click a button again, or perform
                // exponential back-off.
            }
            return msg;
        }


        @Override
        protected void onPostExecute(String msg) {
        }
    }
}
