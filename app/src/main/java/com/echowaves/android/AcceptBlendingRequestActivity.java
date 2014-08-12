package com.echowaves.android;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.echowaves.android.model.EWWave;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class AcceptBlendingRequestActivity extends EWActivity {

    String fromWave;

    String currentlySelectedWaveName;


    ArrayList<String> allMyWaves;

    ListView allMyWavesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_blending_request);

        ImageView backButton = (ImageView) findViewById(R.id.acceptBlending_imageViewBack);
        //Listening to button event
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                finish();
            }
        });


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fromWave = extras.getString("FROM_WAVE");
            TextView fromWaveView = (TextView) findViewById(R.id.acceptBlending_fromWaveName);
            fromWaveView.setText(fromWave);
        }

        currentlySelectedWaveName = WavePickerFragment.getCurrentWaveName();

        EWWave.getAllMyWaves(new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                EWWave.showLoadingIndicator(getLayoutInflater().getContext());
                Log.d(">>>>>>>>>>>>>>>>>>>> PickWavesForUploadActivity starting Loading", "");
            }

            @Override
            public void onFinish() {
                EWWave.hideLoadingIndicator();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonResponseArray) {
                Log.d(">>>>>>>>>>>>>>>>>>>> PickWavesForUploadActivity finished Loading", jsonResponseArray.toString());

                allMyWaves = new ArrayList<String>(jsonResponseArray.length());
                for (int i = 0; i < jsonResponseArray.length(); i++) {
                    try {
                        Log.d("jsonObject", jsonResponseArray.getJSONObject(i).toString());
                        String waveName = jsonResponseArray.getJSONObject(i).getString("name");
                        Log.d("wave", waveName);
                        allMyWaves.add(waveName);
                    } catch (JSONException e) {
                        Log.d("JSONException", e.toString(), e);
                    }
                }

                allMyWavesListView = (ListView) findViewById(R.id.acceptBlending_allMyWaves);
                allMyWavesListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

                ArrayAdapter<String> waves_adapter =
                        new ArrayAdapter<String>(allMyWavesListView.getContext(), android.R.layout.simple_list_item_single_choice, allMyWaves) {
                            @Override
                            public View getView(final int position, View convertView, ViewGroup parent) {
                                final CheckedTextView view = (CheckedTextView) super.getView(position, convertView, parent);
                                view.setTextColor(Color.BLACK);
                                if (allMyWaves.get(position).equals(currentlySelectedWaveName)) {
                                    allMyWavesListView.setItemChecked(position, true);
                                }
                                return view;
                            }
                        };

                allMyWavesListView.setAdapter(waves_adapter);

                // Defining the item click listener for listView
                AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                        currentlySelectedWaveName = allMyWaves.get(position);
                        Log.d("************************************* selected wave: ", currentlySelectedWaveName);
                    }
                };

                // Setting the item click listener for listView
                allMyWavesListView.setOnItemClickListener(itemClickListener);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {
                if (headers != null) {
                    for (Header h : headers) {
                        Log.d("................ failed   key: ", h.getName());
                        Log.d("................ failed value: ", h.getValue());
                    }
                }
                if (responseBody != null) {
                    Log.d("................ failed : ", responseBody);
                }
                if (error != null) {
                    Log.d("................ failed error: ", error.toString());

                }
            }
        });


        Button acceptButton = (Button) findViewById(R.id.acceptBlending_acceptButton);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if(currentlySelectedWaveName.equals(WavePickerFragment.getCurrentWaveName()) ||
                   currentlySelectedWaveName.equals(fromWave)) {
                    finish();
                }


            }
        });

    }


}
