package com.echowaves.android;

import android.app.Activity;

import com.flurry.android.FlurryAgent;

/**
 * copyright echowaves
 * Created by dmitry
 * on 6/14/14.
 */

public class BaseActivity extends Activity {
    public static String FlurryKey = "V4V86GZNZGPXMHR9FM6Q";

    @Override
    protected void onStart()
    {
        super.onStart();
        FlurryAgent.onStartSession(this, BaseActivity.FlurryKey);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }

}
