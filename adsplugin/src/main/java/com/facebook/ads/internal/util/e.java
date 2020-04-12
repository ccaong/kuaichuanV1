// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.util;

import java.util.Locale;
import android.text.TextUtils;

public enum e
{
    NONE,
    INSTALLED,
    NOT_INSTALLED;
    
    public static e a(final String s) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            return e.NONE;
        }
        try {
            return valueOf(s.toUpperCase(Locale.US));
        }
        catch (IllegalArgumentException ex) {
            return e.NONE;
        }
    }
}
