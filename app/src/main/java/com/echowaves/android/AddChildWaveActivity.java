package com.echowaves.android;

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

public class AddChildWaveActivity extends EWActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child_wave);

        ImageView backButton = (ImageView) findViewById(R.id.addwave_imageViewBack);
        //Listening to button event
        backButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                finish();
            }
        });

        // show soft keyboard automagically
        final EditText waveName = (EditText) findViewById(R.id.addwave_childWaveName);
        waveName.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

//add a . to the end of the new wave name
        waveName.setText(WavePickerFragment.getCurrentWaveName() + ".");
//        set cursor to the end
        waveName.setSelection(waveName.getText().length());

        Button createChildWaveButton = (Button) findViewById(R.id.addwave_createChildWaveButton);
        //Listening to button event
        createChildWaveButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(final View v) {
                EWWave.createChildWaveWithName(waveName.getText().toString(),
                        new EWJsonHttpResponseHandler(v.getContext()) {

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                                WavePickerFragment.resetCurrentWaveIndex();
                                Log.d(">>>>>>>>>>>>>>>>>>>> ", jsonResponse.toString());
                                finish();
                            }

                        }
                );

            }
        });


    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.add_child_wave, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
