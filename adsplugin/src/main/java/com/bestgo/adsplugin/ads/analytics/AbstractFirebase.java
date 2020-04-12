package com.bestgo.adsplugin.ads.analytics;

import android.os.Bundle;
import android.util.Log;

public abstract class AbstractFirebase {
    public void logEvent(String category, String action) {
    }

    public void logEvent(String category, String action, String label) {
    }

    public void logEvent(String category, String action, String label, long value) {
    }

    public void logEvent(String category, String action, String label, double value) {
    }

    public void logEvent(String category, String action, long value) {
    }

    public void logEvent(String category, String action, double value) {
    }

    public void logEvent(String category, long value) {
    }

    public void logEvent(String category, double value) {
    }

    public void logEvent(String category, Bundle values) {
    }
}