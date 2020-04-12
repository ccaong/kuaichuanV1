// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.fpackage;

import com.facebook.ads.internal.e;

public enum c
{
    UNKNOWN,
    BANNER,
    INTERSTITIAL,
    NATIVE,
    REWARDED_VIDEO;

    public static c a(final e e) {
        switch (e) {
            case NATIVE_UNKNOWN: {
                return c.NATIVE;
            }
            case WEBVIEW_BANNER_50:
            case WEBVIEW_BANNER_90:
            case WEBVIEW_BANNER_LEGACY:
            case WEBVIEW_BANNER_250: {
                return c.BANNER;
            }
            case WEBVIEW_INTERSTITIAL_HORIZONTAL:
            case WEBVIEW_INTERSTITIAL_VERTICAL:
            case WEBVIEW_INTERSTITIAL_TABLET:
            case WEBVIEW_INTERSTITIAL_UNKNOWN: {
                return c.INTERSTITIAL;
            }
            case REWARDED_VIDEO: {
                return c.REWARDED_VIDEO;
            }
            default: {
                return c.UNKNOWN;
            }
        }
    }
}
