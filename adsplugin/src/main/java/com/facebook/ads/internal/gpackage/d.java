// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.gpackage;

import java.util.Iterator;
import com.facebook.ads.internal.util.g;
import com.facebook.ads.internal.util.a;
import java.util.HashMap;
import android.content.Context;
import java.util.Map;

public abstract class d
{
    protected final String a;
    protected final double b;
    protected final double c;
    protected final String d;
    protected final Map<String, String> e;
    
    public d(final String s, final double n, final String s2, final Map<String, String> map) {
        this(null, s, n, s2, map);
    }
    
    public d(final Context context, final String a, final double c, final String d, final Map<String, String> map) {
        this.a = a;
        this.b = System.currentTimeMillis() / 1000.0;
        this.c = c;
        this.d = d;
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        if (map != null && !map.isEmpty()) {
            hashMap.putAll(map);
        }
        if (this.c()) {
            hashMap.put("analog", g.a(com.facebook.ads.internal.util.a.a()));
        }
        this.e = a(hashMap);
    }
    
    public String d() {
        return this.a;
    }
    
    public double e() {
        return this.b;
    }
    
    public double f() {
        return this.c;
    }
    
    public String g() {
        return this.d;
    }
    
    public Map<String, String> h() {
        return this.e;
    }
    
    public abstract h a();
    
    public abstract String b();
    
    public abstract boolean c();
    
    public final boolean i() {
        return this.a() == h.IMMEDIATE;
    }
    
    private static Map<String, String> a(final Map<String, String> map) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        for (final Map.Entry<String, String> entry : map.entrySet()) {
            final String s = entry.getKey();
            final String s2 = entry.getValue();
            if (s2 != null) {
                hashMap.put(s, s2);
            }
        }
        return hashMap;
    }
}
