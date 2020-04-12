// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.server;

import java.util.Locale;
import android.text.TextUtils;

public enum AdPlacementType
{
    UNKNOWN("unknown"), 
    BANNER("banner"), 
    INTERSTITIAL("interstitial"), 
    NATIVE("native"), 
    INSTREAM("instream"), 
    REWARDED_VIDEO("rewarded_video");
    
    private String a;
    
    private AdPlacementType(final String a) {
        this.a = a;
    }
    
    @Override
    public String toString() {
        return this.a;
    }
    
    public static AdPlacementType fromString(final String s) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            return AdPlacementType.UNKNOWN;
        }
        try {
            return valueOf(s.toUpperCase(Locale.US));
        }
        catch (Exception ex) {
            return AdPlacementType.UNKNOWN;
        }
    }
}
