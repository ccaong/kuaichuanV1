// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads;

public interface RewardedVideoAdListener extends AdListener
{
    void onRewardedVideoCompleted();
    
    void onLoggingImpression(final Ad p0);
    
    void onRewardedVideoClosed();
}
