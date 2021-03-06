package com.facebook.ads;

public abstract class AbstractAdListener implements AdListener, InterstitialAdListener
{
    @Override
    public void onError(final Ad ad, final AdError adError) {
    }
    
    @Override
    public void onAdLoaded(final Ad ad) {
    }
    
    @Override
    public void onAdClicked(final Ad ad) {
    }
    
    @Override
    public void onInterstitialDisplayed(final Ad ad) {
    }
    
    @Override
    public void onInterstitialDismissed(final Ad ad) {
    }
    
    @Override
    public void onLoggingImpression(final Ad ad) {
    }
}
