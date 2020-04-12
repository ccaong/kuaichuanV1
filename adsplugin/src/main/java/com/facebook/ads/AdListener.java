// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads;

public interface AdListener
{
    void onError(final Ad p0, final AdError p1);
    
    void onAdLoaded(final Ad p0);
    
    void onAdClicked(final Ad p0);
    
    void onLoggingImpression(final Ad p0);
}
