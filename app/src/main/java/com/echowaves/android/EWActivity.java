package com.echowaves.android;

import android.app.Activity;

import com.flurry.android.FlurryAgent;

/**
 * copyright echowaves
 * Created by dmitry
 * on 6/14/14.
 */

abstract public class EWActivity extends Activity implements EWConstants {

    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this, FlurryKey);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }

}
