// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.util;

import java.util.concurrent.Executors;
import android.graphics.Bitmap;
import java.util.concurrent.ExecutorService;

public class ac
{
    static final int a;
    static final ExecutorService b;
    private static volatile boolean c;
    private final Bitmap d;
    private Bitmap e;
    private final j f;
    
    public ac(final Bitmap d) {
        this.d = d;
        this.f = new u();
    }
    
    public Bitmap a(final int n) {
        return this.e = this.f.a(this.d, n);
    }
    
    public Bitmap a() {
        return this.e;
    }
    
    static {
        a = Runtime.getRuntime().availableProcessors();
        b = Executors.newFixedThreadPool(ac.a);
        ac.c = true;
    }
}
