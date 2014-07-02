package com.echowaves.android;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;

import com.echowaves.android.model.EWImage;
import com.echowaves.android.model.EWWave;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.image.SmartImageView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

public class EchoWaveFragment extends Fragment {
    @Override
    public void onStart() {
        super.onStart();
        Log.d("!!!!!!!!!!!!!", "EchoWaveFragment onStart()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_echo_wave, container, false);

        Log.d("EchoWaveFragment", view.toString());

//        GridView gridview = (GridView) view.findViewById(R.id.gridView);
//        gridview.setAdapter(new ImageAdapter(view.getContext()));





//        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                Toast.makeText(HelloGridView.this, "" + position, Toast.LENGTH_SHORT).show();
//            }
//        });



//        EWImage.getAllImagesForWave(WavePickerFragment.getCurrentWaveName(), new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(JSONArray jsonResponseArray) {
//                Log.d(">>>>>>>>>>>>>>>>>>>> EchoWaveFragment finished Loading images", jsonResponseArray.toString());
//
////                waves = new String[jsonResponseArray.length()];
////                for (int i = 0; i < jsonResponseArray.length(); i++) {
////                    try {
////                        Log.d("jsonObject", jsonResponseArray.getJSONObject(i).toString());
////                        Log.d("wave", jsonResponseArray.getJSONObject(i).getString("name"));
////                        waves[i] = jsonResponseArray.getJSONObject(i).getString("name");
////                    } catch (JSONException e) {
////                        Log.d("JSONException", e.toString(), e);
////                    }
////                }
////
////                spinnerWaves = (Spinner) view.findViewById(R.id.wavePicker);
////
////                ArrayAdapter<String> waves_adapter =
////                        new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, waves);
////
////                waves_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////
////                spinnerWaves.setAdapter(waves_adapter);
////                spinnerWaves.setSelection(getCurrentWaveIndex());
////
////                spinnerWaves.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
////                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////                        spinnerWaves.setSelection(position);
////                        currentWaveIndex = position;
////
//////                String selState = (String) spinnerWaves.getSelectedItem();
//////                selectedWave.setText("Selected Android OS:" + selState);
////                    }
////
////                    @Override
////                    public void onNothingSelected(AdapterView<?> arg0) {
////
////                    }
////                });
//
//
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                if (headers != null) {
//                    for (Header h : headers) {
//                        Log.d("................ failed   key: ", h.getName());
//                        Log.d("................ failed value: ", h.getValue());
//                    }
//                }
//                if (responseBody != null) {
//                    Log.d("................ failed : ", new String(responseBody));
//                }
//                if (error != null) {
//                    Log.d("................ failed error: ", error.toString());
//
//                }
//            }
//        });





        return view;
    }

//    public class ImageAdapter extends BaseAdapter {
//        private Context mContext;
//
//        public ImageAdapter(Context c) {
//            mContext = c;
//        }
//
//        public int getCount() {
//            return mThumbURLs.length;
//        }
//
//        public Object getItem(int position) {
//            return null;
//        }
//
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        // create a new ImageView for each item referenced by the Adapter
//        public View getView(int position, View convertView, ViewGroup parent) {
//
//            Log.d("@@@@@@@@@@@@@@@ImageViewAdapter", "position:" + position + " url:" + mThumbURLs[position]);
//
//            SmartImageView imageView;
//            if (convertView == null) {  // if it's not recycled, initialize some attributes
//                imageView = new SmartImageView(mContext);
//                imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
//                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//                imageView.setPadding(5, 5, 5, 5);
//            } else {
//                imageView = (SmartImageView) convertView;
//            }
//
//            imageView.setImageUrl(mThumbURLs[position]);
//            return imageView;
//        }
//
//        // references to our images
//        private String[] mThumbURLs = {};
//    }


}
