// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal;

public enum j
{
    REWARDED_VIDEO_COMPLETE("com.facebook.ads.rewarded_video.completed"), //a
    REWARDED_VIDEO_COMPLETE_WITHOUT_REWARD("com.facebook.ads.rewarded_video.completed.without.reward"), //b
    REWARDED_VIDEO_END_ACTIVITY("com.facebook.ads.rewarded_video.error"), //c
    REWARDED_VIDEO_ERROR("com.facebook.ads.rewarded_video.error"), //d
    REWARDED_VIDEO_AD_CLICK("com.facebook.ads.rewarded_video.ad_click"), //e
    REWARDED_VIDEO_IMPRESSION("com.facebook.ads.rewarded_video.ad_impression"), //f
    REWARDED_VIDEO_CLOSED("com.facebook.ads.rewarded_video.closed"), //g
    REWARD_SERVER_SUCCESS("com.facebook.ads.rewarded_video.server_reward_success"), //h
    REWARD_SERVER_FAILED("com.facebook.ads.rewarded_video.server_reward_failed");//i
    
    private String j;
    
    private j(final String j) {
        this.j = j;
    }
    
    public String a(final String s) {
        return this.j + ":" + s;
    }
    
    public String a() {
        return this.j;
    }
}
