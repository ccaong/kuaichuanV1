package com.facebook.ads.internal.adapters;

import com.facebook.ads.AdError;
import android.view.View;

public interface BannerAdapterListener
{
    void onBannerAdLoaded(final BannerAdapter p0, final View p1);
    
    void onBannerError(final BannerAdapter p0, final AdError p1);
    
    void onBannerLoggingImpression(final BannerAdapter p0);
    
    void onBannerAdClicked(final BannerAdapter p0);
    
    void onBannerAdExpanded(final BannerAdapter p0);
    
    void onBannerAdMinimized(final BannerAdapter p0);
}
