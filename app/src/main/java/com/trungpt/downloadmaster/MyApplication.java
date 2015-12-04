package com.trungpt.downloadmaster;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

/**
 * Created by Trung on 11/16/2015.
 */
public class MyApplication extends MultiDexApplication
{
//    private Tracker mTracker;
//
//    synchronized public Tracker getDefaultTracker()
//    {
//        if (mTracker == null)
//        {
//            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
//            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
//            mTracker = analytics.newTracker(R.xml.global_tracker);
//        }
//        return mTracker;
//    }
//
    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);

        MultiDex.install(this);
    }

}
