package com.bestgo.adsplugin.ads.analytics;

public class DebugFacebook extends AbstractFacebook {
    @Override
    public void logEvent(String name, double value) {
        System.out.println("DebugFacebook, name=" + name);
    }
}
