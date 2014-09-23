package com.echowaves.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.echowaves.android.model.EWImage;
import com.echowaves.android.util.EWJsonHttpResponseHandler;
import com.loopj.android.image.SmartImageView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

public class EchoWaveTabFragment extends EWTabFragment {

    private View view;
    private TextView emptyWaveText;


    @Override
    public void onStart() {
        super.onStart();
        Log.d("!!!!!!!!!!!!!", "EchoWaveTabFragment onStart()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_echo_wave, container, false);
        this.emptyWaveText = (TextView) view.findViewById(R.id.echowave_emptyWaveText);

        Log.d("EchoWaveTabFragment", view.toString());

        return view;
    }

    @Override
    public void updateWave(String waveName) {
        super.updateWave(waveName);
        Log.d("EchoWaveTabFragment updateWave", waveName);
        updateView();
    }

    private void updateView() {

//        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                Toast.makeText(HelloGridView.this, "" + position, Toast.LENGTH_SHORT).show();
//            }
//        });

        EWImage.getAllImagesForWave(WavePickerFragment.getCurrentWaveName(), new EWJsonHttpResponseHandler(view.getContext()) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonResponseArray) {
                Log.d(">>>>>>>>>>>>>>>>>>>> EchoWaveTabFragment finished Loading imageNames", Integer.toString(jsonResponseArray.length()));

                String[] imageNames = new String[jsonResponseArray.length()];
                String[] waveNames = new String[jsonResponseArray.length()];

                if (jsonResponseArray.length() == 0) {
                    emptyWaveText.setVisibility(View.VISIBLE);
                }
                for (int i = 0; i < jsonResponseArray.length(); i++) {
                    try {
//                        String thumb = EWConstants.EWAWSBucket + "/img/" + jsonResponseArray.getJSONObject(i).getString("name_2") + "/thumb_" + jsonResponseArray.getJSONObject(i).getString("name");
//                        String fullUrl = EWConstants.EWAWSBucket + "/img/" + jsonResponseArray.getJSONObject(i).getString("name_2") + "/" + jsonResponseArray.getJSONObject(i).getString("name");
//                        String waveName = jsonResponseArray.getJSONObject(i).getString("name_2");

                        imageNames[i] = jsonResponseArray.getJSONObject(i).getString("name");
                        waveNames[i] = jsonResponseArray.getJSONObject(i).getString("name_2");

                    } catch (JSONException e) {
                        Log.d("JSONException", e.toString(), e);
                    }
                }

                GridView gridview = (GridView) view.findViewById(R.id.echowave_gridView);
                gridview.setAdapter(new ImageAdapter(view.getContext(), imageNames, waveNames));

            }

        });

    }


    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        // references to our imageNames
        private String[] imageNames;
        private String[] waveNames;

        public ImageAdapter(Context c, String[] thumbURLs, String[] waves) {
            mContext = c;
            this.imageNames = thumbURLs;
            this.waveNames = waves;
        }

        public int getCount() {
            return imageNames.length;
        }

        public Object getItem(int position) {
//            return mThumbURLs[position];
            return null;
        }

        public long getItemId(int position) {
//            return position;
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.d("@@@@@@@@@@@@@@@ImageViewAdapter ", "position:" + position + " url:" + imageNames[position]);

            SmartImageView imageView;

//            if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new SmartImageView(mContext);

            imageView.setLayoutParams(new GridView.LayoutParams(parent.getWidth() / 3 - 5, parent.getWidth() / 3 - 5));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(2, 2, 2, 2);
//            } else {
//                imageView = (SmartImageView) convertView;
//            }

            String thumb = EWConstants.EWAWSBucket + "/img/" + waveNames[position] + "/thumb_" + imageNames[position];

            imageView.setImageUrl(thumb);

            // image view click listener
            imageView.setOnClickListener(new OnImageClickListener(position));

            return imageView;
        }

        class OnImageClickListener implements View.OnClickListener {

            int postion;

            // constructor
            public OnImageClickListener(int position) {
                this.postion = position;
            }

            @Override
            public void onClick(View v) {
                // on selecting grid view image
                // launch full screen activity
                Intent deatailedImageIntent = new Intent(getActivity(), DetailedImagePagerActivity.class);
                deatailedImageIntent.putExtra("imageNames", imageNames);
                deatailedImageIntent.putExtra("waveNames", waveNames);
                deatailedImageIntent.putExtra("position", postion);
                startActivity(deatailedImageIntent);
            }

        }
    }

}
