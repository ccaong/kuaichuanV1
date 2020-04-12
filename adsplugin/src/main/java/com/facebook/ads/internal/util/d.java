// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.util;

import java.util.concurrent.ConcurrentHashMap;
import com.facebook.ads.internal.fpackage.c;
import com.facebook.ads.internal.fpackage.f;
import java.util.Map;

public class d
{
    private static Map<String, Long> a;
    private static Map<String, Long> b;
    private static Map<String, String> c;
    
    public static boolean a(final f f) {
        final String d = d(f);
        return com.facebook.ads.internal.util.d.b.containsKey(d) && System.currentTimeMillis() - com.facebook.ads.internal.util.d.b.get(d) < a(d, f.b());
    }
    
    public static void b(final f f) {
        d.b.put(d(f), System.currentTimeMillis());
    }
    
    public static void a(final long n, final f f) {
        d.a.put(d(f), n);
    }
    
    private static long a(final String s, final c c) {
        if (d.a.containsKey(s)) {
            return d.a.get(s);
        }
        switch (c) {
            case BANNER: {
                return 15000L;
            }
            case INTERSTITIAL:
            case NATIVE: {
                return -1000L;
            }
            default: {
                return -1000L;
            }
        }
    }
    
    public static void a(final String s, final f f) {
        d.c.put(d(f), s);
    }
    
    public static String c(final f f) {
        return d.c.get(d(f));
    }
    
    private static String d(final f f) {
        return String.format("%s:%s:%s:%d:%d:%d", f.a(), f.b(), f.e, (f.c() == null) ? 0 : f.c().getHeight(), (f.c() == null) ? 0 : f.c().getWidth(), f.d());
    }
    
    static {
        d.a = new ConcurrentHashMap<String, Long>();
        d.b = new ConcurrentHashMap<String, Long>();
        d.c = new ConcurrentHashMap<String, String>();
    }
}
