package com.echowaves.android;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.echowaves.android.model.EWImage;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.image.SmartImageView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

public class EchoWaveTabFragment extends EWTabFragment {

    private View view;

    @Override
    public void onStart() {
        super.onStart();
        Log.d("!!!!!!!!!!!!!", "EchoWaveTabFragment onStart()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_echo_wave, container, false);

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

        EWImage.getAllImagesForWave(WavePickerFragment.getCurrentWaveName(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonResponseArray) {
                Log.d(">>>>>>>>>>>>>>>>>>>> EchoWaveTabFragment finished Loading images", Integer.toString(jsonResponseArray.length()));

                String[] thumbUrls = new String[jsonResponseArray.length()];

                for (int i = 0; i < jsonResponseArray.length(); i++) {
                    try {
                        String thumb = EWConstants.EWAWSBucket + "/img/" + jsonResponseArray.getJSONObject(i).getString("name_2") + "/thumb_" + jsonResponseArray.getJSONObject(i).getString("name");
//                        Log.d("thumbUrl:", thumb);
                        thumbUrls[i] = thumb;
                    } catch (JSONException e) {
                        Log.d("JSONException", e.toString(), e);
                    }
                }

                GridView gridview = (GridView) view.findViewById(R.id.echowave_gridView);
                gridview.setAdapter(new ImageAdapter(view.getContext(), thumbUrls));

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

    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        // references to our images
        private String[] mThumbURLs = {};

        public ImageAdapter(Context c, String[] thumbURLs) {
            mContext = c;
            mThumbURLs = thumbURLs;
        }

        public int getCount() {
            return mThumbURLs.length;
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
            Log.d("@@@@@@@@@@@@@@@ImageViewAdapter ", "position:" + position + " url:" + mThumbURLs[position]);

            SmartImageView imageView;

//            if (convertView == null) {  // if it's not recycled, initialize some attributes
                imageView = new SmartImageView(mContext);

                imageView.setLayoutParams(new GridView.LayoutParams(parent.getWidth() / 3 - 5, parent.getWidth() / 3 - 5));
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setPadding(2, 2, 2, 2);
//            } else {
//                imageView = (SmartImageView) convertView;
//            }
            imageView.setImageUrl(mThumbURLs[position]);

            return imageView;
        }
    }
}
