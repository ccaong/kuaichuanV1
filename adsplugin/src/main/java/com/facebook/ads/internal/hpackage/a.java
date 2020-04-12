// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.hpackage;

import com.facebook.ads.internal.adapters.e;

public class a
{
    private static final String[] a;
    private static final String[] b;
    private static final String[] c;
    
    public static boolean a(final e e) {
        switch (e) {
            case AN: {
                return true;
            }
            case YAHOO: {
                return a(com.facebook.ads.internal.hpackage.a.a);
            }
            case INMOBI: {
                return a(com.facebook.ads.internal.hpackage.a.b);
            }
            case ADMOB: {
                return a(com.facebook.ads.internal.hpackage.a.c);
            }
            default: {
                return false;
            }
        }
    }
    
    private static boolean a(final String[] array) {
        if (array == null) {
            return false;
        }
        for (int length = array.length, i = 0; i < length; ++i) {
            if (!a(array[i])) {
                return false;
            }
        }
        return true;
    }
    
    private static boolean a(final String s) {
        try {
            Class.forName(s);
            return true;
        }
        catch (Throwable t) {
            return false;
        }
    }
    
    static {
        a = new String[] { "com.flurry.android.FlurryAgent", "com.flurry.android.ads.FlurryAdErrorType", "com.flurry.android.ads.FlurryAdNative", "com.flurry.android.ads.FlurryAdNativeAsset", "com.flurry.android.ads.FlurryAdNativeListener" };
        b = new String[] { "com.inmobi.ads.InMobiNative", "com.inmobi.sdk.InMobiSdk" };
        c = new String[] { "com.google.android.gms.ads.formats.NativeAdView" };
    }
}
