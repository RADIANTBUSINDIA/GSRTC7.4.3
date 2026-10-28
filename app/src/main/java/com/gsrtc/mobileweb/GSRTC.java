package com.gsrtc.mobileweb;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import com.google.firebase.crashlytics.FirebaseCrashlytics;


public class GSRTC extends Application {

    private static Context appContext;

    public static Context getAppContext() {
        return appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
        com.gsrtc.mobileweb.utils.divyang.DivyangAccessibilityManager.init(this);
        com.gsrtc.mobileweb.utils.divyang.DivyangSpeaker.init(this);
    }

//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(getBaseContext());
//    }

    public static SharedPreferences getDefaultSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(getAppContext());
    }
}
