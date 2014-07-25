package com.echowaves.android;

import android.app.Activity;
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

    private static String[] waves;
    private static int currentWaveIndex;
    private OnWaveSelectedListener mOnWaveSelectedListener;
    private Spinner spinnerWaves;

    public static String getCurrentWaveName() {
        return waves[currentWaveIndex];
    }
    public static void resetCurrentWaveIndex() {
        currentWaveIndex = 0;
    }

    public static int getCurrentWaveIndex() {
        return currentWaveIndex;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mOnWaveSelectedListener = (OnWaveSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnWaveSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnWaveSelectedListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_wave_picker, container, false);
        Log.i("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Waving fragment ", "onCreateView()");

        EWWave.getAllMyWaves(new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                Log.d(">>>>>>>>>>>>>>>>>>>> WavePickerFragment starting Loading", "");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonResponseArray) {
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
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        // your code here
                        Log.d("^^^^^^^^^^^^^^^^^^^^^^^^^^^", "spinner on item selected:" + waves[position]);
                        spinnerWaves.setSelection(position);
                        currentWaveIndex = position;

                        mOnWaveSelectedListener.onAWaveSelected(waves[position]);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                        Log.d("^^^^^^^^^^^^^^^^^^^^^^^^^^^", "spinner nothing selected");
                    }

                });


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


        return view;
    }

    // NavigationTabBarActivity must implement this interface
    public interface OnWaveSelectedListener {
        public void onAWaveSelected(String waveName);
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.i("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Waving fragment ", "onResume()");
//
//    }


}
