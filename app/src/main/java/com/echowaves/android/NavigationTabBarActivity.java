package com.echowaves.android;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.echowaves.android.model.ApplicationContextProvider;
import com.echowaves.android.model.EWImage;
import com.echowaves.android.model.EWWave;
import com.echowaves.android.util.EWJsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


public class NavigationTabBarActivity extends EWFragmentActivity implements TabHost.OnTabChangeListener, WavePickerFragment.OnWaveSelectedListener {

    private WavingTabFragment wavingFragment;
    private EchoWaveTabFragment echoWaveFragment;
    private BlendsTabFragment blendsTabFragment;
    private TabHost tabHost;

    private Button waveAllNowButton;

    static int currentTab;
//    static String currentWave;

    private static int TAKE_PHOTO_CODE = 3321;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation_tab_bar);

        tabHost = (TabHost) findViewById(R.id.nav_tabhost);
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
            Log.d("~~~~~~~~~~~~~~~~~~~~~ Loading tab", String.valueOf(index));
            final TextView tabWidgetTextView = originalTextViews[index];
            final View tabContentView = tabContent.getChildAt(index);
            TabHost.TabSpec tabSpec = tabHost.newTabSpec((String) tabWidgetTextView.getTag());
            tabSpec.setContent(new TabHost.TabContentFactory() {
                @Override
                public View createTabContent(String tag) {
                    return tabContentView;
                }
            });
//            if (tabWidgetTextView.getBackground() == null) {
//                tabSpec.setIndicator(tabWidgetTextView.getText());
//            } else {
            tabSpec.setIndicator(tabWidgetTextView.getText(), tabWidgetTextView.getBackground());
//            }
            tabHost.addTab(tabSpec);
        }

        tabHost.setOnTabChangedListener(this);

        // tune out button
        TextView tuneOutButton = (TextView) findViewById(R.id.nav_tuneOut);
        //Listening to button event
        tuneOutButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(final View v) {

                EWWave.tuneOut(new EWJsonHttpResponseHandler(v.getContext()) {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                        Log.d(">>>>>>>>>>>>>>>>>>>> ", jsonResponse.toString());

                        Intent home = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(home);
                    }

                });

            }
        });


        // photo cam button
        ImageButton takePictureButton = (ImageButton) findViewById(R.id.nav_takePictureButton);
        //Listening to button event
        takePictureButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(final View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    currentTab = 1;
                    tabHost.setCurrentTab(currentTab);
                    startActivityForResult(takePictureIntent, TAKE_PHOTO_CODE);
                }
            }
        });


        wavingFragment = (WavingTabFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_waving);
        echoWaveFragment = (EchoWaveTabFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_echoWave);
        blendsTabFragment = (BlendsTabFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_blends);


        //check if the entry into the application was through the share token
        if (HomeActivity.shareToken != null) {
            final Intent acceptBlendingIntent = new Intent(this, AcceptBlendingRequestActivity.class);
            //  here we will will have to figure out the waveName from the token and pass it along to the next activity
            EWImage.retreiveImageByToken(HomeActivity.shareToken, new EWJsonHttpResponseHandler(this) {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                    try {
                        String imageName = jsonResponse.getString("name");
                        String waveName = jsonResponse.getString("name_2");

                        HomeActivity.shareToken = null;

                        acceptBlendingIntent.putExtra("FROM_WAVE", waveName);
                        acceptBlendingIntent.putExtra("SHARED_IMAGE", imageName);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("==========================================: ", "onActivityResult");
        if (resultCode == RESULT_OK ) {
            Log.d("==========================================: ", "onActivityResult:RESULT_OK");
            if (requestCode == TAKE_PHOTO_CODE) {
                Log.d("==========================================: ", "onActivityResult:TAKE_PHOTO_CODE");
                Intent pickWavesIntent = new Intent(this, PickWavesForUploadActivity.class);
                startActivity(pickWavesIntent);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("NavigationTabBarActivity ==========================================: ", "onResume called");
        Log.d("NavigationTabBarActivity ==========================================: ", "setting currentWave to " + WavePickerFragment.getCurrentWaveName());
        onAWaveSelected(WavePickerFragment.getCurrentWaveName());
        Log.d("NavigationTabBarActivity ==========================================: ", "setting tab to " + String.valueOf(currentTab));
        tabHost.setCurrentTab(currentTab);

        waveAllNowButton = (Button) findViewById(R.id.nav_waveAllNowButton);
        int photosCount = ApplicationContextProvider.getPhotosCountSinceLast();
        if(photosCount > 0) {
            waveAllNowButton.setVisibility(View.VISIBLE);
            waveAllNowButton.setText("Wave: " + photosCount);
            waveAllNowButton.
                    setOnClickListener(
                            new View.OnClickListener() {
                                public void onClick(final View v) {
                                    Intent pickWavesIntent = new Intent(v.getContext(), PickWavesForUploadActivity.class);
                                    startActivity(pickWavesIntent);
                                }
                            }
                    );
        } else {
            waveAllNowButton.setVisibility(View.GONE);
        }


    }

    @Override
    public void onTabChanged(String tabId) {
        Log.d("NavigationTabBarActivity ==========================================: ", "onTabChanged(): tabId=" + tabId.substring(3));
        currentTab = Integer.parseInt(tabId.substring(3));
    }

    @Override
    public void onAWaveSelected(String waveName) {
        Log.d("NavigationTabBarActivity ==========================================: ", "waveSelected:" + waveName);
        WavePickerFragment.setCurrentWaveName(waveName);
        if (WavePickerFragment.getCurrentWaveName() != null) {
            wavingFragment.updateWave(WavePickerFragment.getCurrentWaveName());
            echoWaveFragment.updateWave(WavePickerFragment.getCurrentWaveName());
            blendsTabFragment.updateWave(WavePickerFragment.getCurrentWaveName());
        }
    }


}
