package com.echowaves.android;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;


public class NavigationTabBarActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation_tab_bar);

        TabHost tabs=(TabHost)findViewById(R.id.tabhost);
        tabs.setup();
        TabHost.TabSpec spec=tabs.newTabSpec("tag1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Clock");
        tabs.addTab(spec);
        spec=tabs.newTabSpec("tag2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Button");
        tabs.addTab(spec);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.navigation_tab_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    protected void onStart()
//    {
//        super.onStart();
//        FlurryAgent.onStartSession(this, BaseActivity.FluryKey);
//    }
//
//    @Override
//    protected void onStop()
//    {
//        super.onStop();
//        FlurryAgent.onEndSession(this);
//    }

}
