// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.gpackage;

import java.util.Map;
import android.content.Context;

public class j extends d
{
    public j(final Context context, final String s, final double n, final String s2, final Map<String, String> map) {
        super(context, s, n, s2, map);
    }
    
    @Override
    public h a() {
        return h.IMMEDIATE;
    }
    
    @Override
    public String b() {
        return "invalidation";
    }
    
    @Override
    public boolean c() {
        return false;
    }
}
