// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.ipackage.b.apackage;

import android.text.TextUtils;
import com.facebook.ads.internal.ipackage.b.m;

public class f implements c
{
    @Override
    public String a(final String s) {
        final String b = this.b(s);
        final String d = m.d(s);
        return TextUtils.isEmpty((CharSequence)b) ? d : (d + "." + b);
    }
    
    private String b(final String s) {
        final int lastIndex = s.lastIndexOf(46);
        final int lastIndex2 = s.lastIndexOf(47);
        return (lastIndex != -1 && lastIndex > lastIndex2 && lastIndex + 2 + 4 > s.length()) ? s.substring(lastIndex + 1, s.length()) : "";
    }
}
