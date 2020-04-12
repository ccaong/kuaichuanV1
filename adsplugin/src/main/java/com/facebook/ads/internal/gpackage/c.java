// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.gpackage;

import android.content.Context;

import java.util.Map;

public class c extends d
{
    public c(String s, double n, String s2, Map<String, String> map) {
        super(s, n, s2, map);
    }

    public c(Context context, String a, double c, String d, Map<String, String> map) {
        super(context, a, c, d, map);
    }

    @Override
    public h a() {
        return h.DEFERRED;
    }
    
    @Override
    public String b() {
        return "error";
    }
    
    @Override
    public boolean c() {
        return false;
    }
}
