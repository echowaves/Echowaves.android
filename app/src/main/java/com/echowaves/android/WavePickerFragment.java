package com.echowaves.android;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.echowaves.android.model.EWWave;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

public class WavePickerFragment extends Fragment {

    private Spinner spinnerWaves;

    private static String[] waves;
    private static int currentWaveIndex;

    public static String getCurrentWaveName() {
        return waves[currentWaveIndex];
    }

    public static int getCurrentWaveIndex() {
        return currentWaveIndex;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_wave_picker, container, false);
        Log.i("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Waving fragment ", "onCreateView()");


        EWWave.getAllMyWaves(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONArray jsonResponseArray) {
                Log.d(">>>>>>>>>>>>>>>>>>>> WavePickerFragment finished Loading", jsonResponseArray.toString());

                waves = new String[jsonResponseArray.length()];
                for (int i = 0; i < jsonResponseArray.length(); i++) {
                    try {
                        Log.d("jsonObject", jsonResponseArray.getJSONObject(i).toString());
                        Log.d("wave", jsonResponseArray.getJSONObject(i).getString("name"));
                        waves[i] = jsonResponseArray.getJSONObject(i).getString("name");
                    } catch (JSONException e) {
                        Log.d("JSONException", e.toString(), e);
                    }
                }

                spinnerWaves = (Spinner) view.findViewById(R.id.wavePicker);

                ArrayAdapter<String> waves_adapter =
                        new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, waves);

                waves_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinnerWaves.setAdapter(waves_adapter);
                spinnerWaves.setSelection(getCurrentWaveIndex());

                spinnerWaves.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        spinnerWaves.setSelection(position);
                        currentWaveIndex = position;

//                String selState = (String) spinnerWaves.getSelectedItem();
//                selectedWave.setText("Selected Android OS:" + selState);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }
                });


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

                }
            }
        });


        return view;
    }
}
