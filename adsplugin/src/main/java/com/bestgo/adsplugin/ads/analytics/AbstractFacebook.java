package com.bestgo.adsplugin.ads.analytics;

import android.os.Bundle;

public abstract class AbstractFacebook {
    public void logEvent(String name, double value) {
    }

    public void logEvent(String name, String action) {
    }

    public void logEvent(String name, Bundle bundle) {
    }

    public void logEvent(String name, String action, String label) {
    }
}