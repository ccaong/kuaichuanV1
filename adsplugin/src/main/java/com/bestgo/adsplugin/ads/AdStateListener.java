package com.bestgo.adsplugin.ads;

public abstract class AdStateListener {
    public void onAdLoaded(AdType adType, int index) {
    }
    public void onAdLoadFailed(AdType adType, int index, String reason) {
    }
    public void onAdOpen(AdType adType, int index) {
    }
    public void onAdClosed(AdType adType, int index) {
    }
    public void onAdClick(AdType adType, int index) {
    }
}
