// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.util;

import com.facebook.ads.internal.adapters.e;

public class v
{
    public static String a(final e e) {
        switch (e) {
            case ADMOB: {
                return "AdMob";
            }
            case YAHOO: {
                return "Flurry";
            }
            case INMOBI: {
                return "InMobi";
            }
            case AN: {
                return "Audience Network";
            }
            default: {
                return "";
            }
        }
    }
}
