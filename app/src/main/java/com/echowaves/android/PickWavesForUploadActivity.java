package com.echowaves.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.echowaves.android.model.EWWave;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

// http://www.vogella.com/tutorials/AndroidListView/article.html

public class PickWavesForUploadActivity extends EWActivity {

    private static List<Model> waves;

    private static ListView wavesListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_waves_for_upload);

        ImageView backButton = (ImageView) findViewById(R.id.pickwaves_imageViewBack);
        //Listening to button event
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent home = new Intent(getApplicationContext(), NavigationTabBarActivity.class);
                startActivity(home);
            }
        });


        EWWave.getAllMyWaves(new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                Log.d(">>>>>>>>>>>>>>>>>>>> PickWavesForUploadActivity starting Loading", "");
            }

            @Override
            public void onSuccess(JSONArray jsonResponseArray) {
                Log.d(">>>>>>>>>>>>>>>>>>>> PickWavesForUploadActivity finished Loading", jsonResponseArray.toString());

                waves = new ArrayList<Model>(jsonResponseArray.length());
                for (int i = 0; i < jsonResponseArray.length(); i++) {
                    try {
                        Log.d("jsonObject", jsonResponseArray.getJSONObject(i).toString());
                        String waveName = jsonResponseArray.getJSONObject(i).getString("name");
                        boolean waveActive = jsonResponseArray.getJSONObject(i).getInt("active") == 1;
                        Log.d("wave", waveName);
                        Log.d("waveStatus", String.valueOf(waveActive));
                        waves.add(new Model(waveName, waveActive));
                    } catch (JSONException e) {
                        Log.d("JSONException", e.toString(), e);
                    }
                }

                wavesListView = (ListView) findViewById(R.id.pickwaves_listview);

                ArrayAdapter<Model> waves_adapter =
                        new ArrayAdapter<Model>(wavesListView.getContext(), R.layout.pick_wave_for_upload_row, waves) {
                            @Override
                            public View getView(final int position, View convertView, ViewGroup parent) {
                                View rowView = convertView;
                                // reuse views
                                if (rowView == null) {
                                    LayoutInflater inflater = getLayoutInflater();
                                    rowView = inflater.inflate(R.layout.pick_wave_for_upload_row, null);
                                    // configure view holder
                                    final ViewHolder viewHolder = new ViewHolder();
                                    viewHolder.waveName = (TextView) rowView.findViewById(R.id.waverow_name);
                                    viewHolder.waveActive = (CheckBox) rowView
                                            .findViewById(R.id.waverow_active);

                                    viewHolder.waveActive
                                            .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                @Override
                                                public void onCheckedChanged(CompoundButton buttonView,
                                                                             boolean isChecked) {
//                                                    Model element = (Model) viewHolder.waveActive
//                                                            .getTag();
//                                                    element.setActive(buttonView.isChecked());

                                                    Log.d("making a wave active: " + position + ":", String.valueOf(isChecked));
                                                }
                                            });


                                    rowView.setTag(viewHolder);
                                }

                                // fill data
                                ViewHolder holder = (ViewHolder) rowView.getTag();
                                String waveName = waves.get(position).getName();
                                holder.waveName.setText(waveName);
                                boolean waveActive = waves.get(position).isActive();
                                holder.waveActive.setChecked(waveActive);

                                return rowView;
                            }
                        };

//                waves_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                wavesListView.setAdapter(waves_adapter);

//                wavesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                        // your code here
//                        Log.d("^^^^^^^^^^^^^^^^^^^^^^^^^^^", "PickWavesForUploadActivity on item selected:" + position);
////                        spinnerWaves.setSelection(position);
//                    }
//
//                });


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


    }


    static class ViewHolder {
        public TextView waveName;
        public CheckBox waveActive;
    }

    public class Model {

        private String name;
        private boolean active;

        public Model(String name, boolean active) {
            this.name = name;
            this.active = active;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

    }

}
