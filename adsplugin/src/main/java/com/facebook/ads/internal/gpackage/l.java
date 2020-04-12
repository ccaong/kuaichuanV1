// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.gpackage;

import java.util.Map;
import android.content.Context;

public class l extends d
{
    protected String f;
    protected h g;
    
    public l(final Context context, final String s, final double n, final String s2, final Map<String, String> map, final String f, final h g) {
        super(context, s, n, s2, map);
        this.f = f;
        this.g = g;
    }
    
    @Override
    public h a() {
        return this.g;
    }
    
    @Override
    public String b() {
        return this.f;
    }
    
    @Override
    public boolean c() {
        return true;
    }
}
