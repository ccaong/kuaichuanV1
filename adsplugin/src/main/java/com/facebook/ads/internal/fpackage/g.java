// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.fpackage;

import java.util.UUID;

public class g
{
    private static final String a;
    private static volatile boolean b;
    private static double c;
    private static String d;
    
    public static void a() {
        if (!g.b) {
            synchronized (g.a) {
                if (!g.b) {
                    g.b = true;
                    g.c = System.currentTimeMillis() / 1000.0;
                    g.d = UUID.randomUUID().toString();
                }
            }
        }
    }
    
    public static double b() {
        return g.c;
    }
    
    public static String c() {
        return g.d;
    }
    
    static {
        a = g.class.getSimpleName();
        g.b = false;
    }
}
