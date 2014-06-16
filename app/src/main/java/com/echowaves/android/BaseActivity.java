package com.echowaves.android;

import android.app.Activity;

import com.flurry.android.FlurryAgent;

/**
 * Created by dmitry on 6/14/14.
 */
public class BaseActivity extends Activity {
    public static String FluryKey = "V4V86GZNZGPXMHR9FM6Q";

    @Override
    protected void onStart()
    {
        super.onStart();
        FlurryAgent.onStartSession(this, BaseActivity.FluryKey);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }

}
