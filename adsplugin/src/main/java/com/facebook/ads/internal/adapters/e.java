package com.facebook.ads.internal.adapters;

import java.util.Locale;
import android.text.TextUtils;

public enum e
{
    UNKNOWN,//a
    AN,//b
    ADMOB,//c
    INMOBI,//d
    YAHOO;//e
    
    public static e a(final String s) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            return e.UNKNOWN;
        }
        try {
            return Enum.valueOf(e.class, s.toUpperCase(Locale.getDefault()));
        }
        catch (Exception ex) {
            return e.UNKNOWN;
        }
    }
}
