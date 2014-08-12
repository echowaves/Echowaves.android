package com.echowaves.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.echowaves.android.model.EWWave;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


public class NavigationTabBarActivity extends EWFragmentActivity implements TabHost.OnTabChangeListener, WavePickerFragment.OnWaveSelectedListener {

    private WavingTabFragment wavingFragment;
    private EchoWaveTabFragment echoWaveFragment;
    private BlendsTabFragment blendsTabFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation_tab_bar);

        TabHost tabHost = (TabHost) findViewById(R.id.nav_tabhost);
        tabHost.setup();

        final TabWidget tabWidget = tabHost.getTabWidget();
        final FrameLayout tabContent = tabHost.getTabContentView();

        // Get the original tab textviews and remove them from the viewgroup.
        TextView[] originalTextViews = new TextView[tabWidget.getTabCount()];
        for (int index = 0; index < tabWidget.getTabCount(); index++) {
            originalTextViews[index] = (TextView) tabWidget.getChildTabViewAt(index);
        }
        tabWidget.removeAllViews();

        // Ensure that all tab content childs are not visible at startup.
        for (int index = 0; index < tabContent.getChildCount(); index++) {
            tabContent.getChildAt(index).setVisibility(View.GONE);
        }

        // Create the tabspec based on the textview childs in the xml file.
        // Or create simple tabspec instances in any other way...
        for (int index = 0; index < originalTextViews.length; index++) {
            final TextView tabWidgetTextView = originalTextViews[index];
            final View tabContentView = tabContent.getChildAt(index);
            TabHost.TabSpec tabSpec = tabHost.newTabSpec((String) tabWidgetTextView.getTag());
            tabSpec.setContent(new TabHost.TabContentFactory() {
                @Override
                public View createTabContent(String tag) {
                    return tabContentView;
                }
            });
            if (tabWidgetTextView.getBackground() == null) {
                tabSpec.setIndicator(tabWidgetTextView.getText());
            } else {
                tabSpec.setIndicator(tabWidgetTextView.getText(), tabWidgetTextView.getBackground());
            }
            tabHost.addTab(tabSpec);
        }

        // tune out button
        TextView tuneOutButton = (TextView) findViewById(R.id.nav_tuneOut);
        //Listening to button event
        tuneOutButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(final View v) {

                EWWave.tuneOut(new JsonHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        EWWave.showLoadingIndicator(v.getContext());
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                        Log.d(">>>>>>>>>>>>>>>>>>>> ", jsonResponse.toString());

                        Intent home = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(home);
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

                            String msg = "";
                            if (null != responseBody) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(responseBody);
                                    msg = jsonResponse.getString("error");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                msg = error.getMessage();
                            }


                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder.setTitle("Error")
                                    .setMessage(msg)
                                    .setCancelable(false)
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }

                    @Override
                    public void onFinish() {
                        EWWave.hideLoadingIndicator();
                    }
                });

            }
        });

        wavingFragment = (WavingTabFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_waving);
        echoWaveFragment = (EchoWaveTabFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_echoWave);
        blendsTabFragment = (BlendsTabFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_blends);


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("==========================================: ", "onResume called");
        onAWaveSelected(WavePickerFragment.getCurrentWaveName());
    }

    @Override
    public void onTabChanged(String tabId) {
        Log.d("$$$$$$$$$$$$$$$$$ NavigationTabBarActivity", "onTabChanged(): tabId=" + tabId);

    }

    @Override
    public void onAWaveSelected(String waveName) {
        Log.d("%%%%%%%%%%%%%%%%% NavigationTabBarActivity", "waveSelected:" + waveName);

        if (WavePickerFragment.getCurrentWaveName() != null) {
            wavingFragment.updateWave(WavePickerFragment.getCurrentWaveName());
            echoWaveFragment.updateWave(WavePickerFragment.getCurrentWaveName());
            blendsTabFragment.updateWave(WavePickerFragment.getCurrentWaveName());
        }
    }


}
