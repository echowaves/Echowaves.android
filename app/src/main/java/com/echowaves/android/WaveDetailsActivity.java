package com.echowaves.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.echowaves.android.model.EWWave;
import com.echowaves.android.util.EWJsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * © Echowaves
 * Created by dmitry on 7/4/14.
 */
public class WaveDetailsActivity extends EWActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave_details);

        final TextView waveNameLabel = (TextView) findViewById(R.id.wave_details_waveName);
        waveNameLabel.setText(WavePickerFragment.getCurrentWaveName());

        ImageView backButton = (ImageView) findViewById(R.id.wave_details_imageViewBack);
        //Listening to button event
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent home = new Intent(getApplicationContext(), NavigationTabBarActivity.class);
                startActivity(home);
            }
        });

        final Button deleteWaveButton = (Button) findViewById(R.id.wave_details_deleteWaveButton);


        EWWave.getWaveDetails(WavePickerFragment.getCurrentWaveName(),

                new EWJsonHttpResponseHandler(this) {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                        Log.d(">>>>>>>>>>>>>>>>>>>> WaveDetailsJson object:", jsonResponse.toString());

                        try {
                            Log.d(">>>>>>>>>>>>>>>>>>>> WaveDetailsJson parent_wave_id:", jsonResponse.get("parent_wave_id").toString());

                            if (jsonResponse.isNull("parent_wave_id")) {
                                Log.d(">>>>>>>>>>>>>>>>> parent_waveId == null", "");
                                deleteWaveButton.setEnabled(false);
                                deleteWaveButton.setVisibility(View.GONE);
                            } else {
                                Log.d(">>>>>>>>>>>>>>>>> parent_waveId != null", "");
                                deleteWaveButton.setEnabled(true);
                                deleteWaveButton.setVisibility(View.VISIBLE);

                                //        Listening to button event
                                deleteWaveButton.setOnClickListener(new View.OnClickListener() {

                                    public void onClick(final View v) {
                                        AlertDialog.Builder alertDialogConfirmWaveDeletion = new AlertDialog.Builder(
                                                v.getContext());
                                        alertDialogConfirmWaveDeletion.setTitle("Delete Wave?");

                                        // set dialog message
                                        alertDialogConfirmWaveDeletion
                                                .setMessage("Are you really sure you want to delete " +
                                                        WavePickerFragment.getCurrentWaveName() +
                                                        " wave? All wave's photos will be gone!")
                                                .setCancelable(false)
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        // if this button is clicked, close
                                                        // current activity
                                                        EWWave.deleteChildWave(WavePickerFragment.getCurrentWaveName(), new EWJsonHttpResponseHandler(v.getContext()) {

                                                            @Override
                                                            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                                                                Log.d(">>>>>>>>>>>>>>>>>>>> ", jsonResponse.toString());
                                                                WavePickerFragment.reloadWaves();
                                                                finish();
                                                            }

                                                        });

                                                    }
                                                })
                                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        // if this button is clicked, just close
                                                        // the dialog box and do nothing
                                                        dialog.cancel();
                                                    }
                                                });

                                        // create alert dialog
                                        AlertDialog alertDialog = alertDialogConfirmWaveDeletion.create();

                                        // show it
                                        alertDialog.show();

                                    }
                                });


                                //                        Intent tuneIn = new Intent(getApplicationContext(), NavigationTabBarActivity.class);
                                //                        startActivity(tuneIn);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
        );


    }
}

