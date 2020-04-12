package com.bestgo.adsplugin.ads.analytics;


import android.os.Bundle;
import android.util.Log;

public class DebugFireBase extends AbstractFirebase {
    public static final String TAG = "DebugFireBase";
    public void logEvent(String key, String value) {
        Log.d(TAG, key + "=" + value);
    }

    public void logEvent(String key, String name, String value) {
        Log.d(TAG, key + "=" + name + "," + value);
    }

    public void logEvent(String key, String name, long value) {
        Log.d(TAG, key + "=" + name + "," + value);
    }

    public void logEvent(String key, long value) {
        Log.d(TAG, key + "=" + value);
    }

    public void logEvent(String key, Bundle values) {
        Log.d(TAG, key + "=" + values);
    }
}