package com.facebook.ads.internal.adapters;

import com.facebook.ads.AdError;

public interface InterstitialAdapterListener
{
    void onInterstitialAdLoaded(final InterstitialAdapter p0);
    
    void onInterstitialError(final InterstitialAdapter p0, final AdError p1);
    
    void onInterstitialLoggingImpression(final InterstitialAdapter p0);
    
    void onInterstitialAdClicked(final InterstitialAdapter p0, final String p1, final boolean p2);
    
    void onInterstitialAdDisplayed(final InterstitialAdapter p0);
    
    void onInterstitialAdDismissed(final InterstitialAdapter p0);
}
