// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal;

public enum e
{
    UNKNOWN(0),//a
    WEBVIEW_BANNER_LEGACY(4),//b
    WEBVIEW_BANNER_50(5),//c
    WEBVIEW_BANNER_90(6),//d
    WEBVIEW_BANNER_250(7),//e
    WEBVIEW_INTERSTITIAL_UNKNOWN(100),//f
    WEBVIEW_INTERSTITIAL_HORIZONTAL(101),//g
    WEBVIEW_INTERSTITIAL_VERTICAL(102),//h
    WEBVIEW_INTERSTITIAL_TABLET(103),//i
    NATIVE_UNKNOWN(200),//j
    REWARDED_VIDEO(400),//k
    NATIVE_250(201),//l
    INSTREAM_VIDEO(300);//m
    
    private final int n;
    
    private e(final int n2) {
        this.n = n2;
    }
    
    public int a() {
        return this.n;
    }
}
