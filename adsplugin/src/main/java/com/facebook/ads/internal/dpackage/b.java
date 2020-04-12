// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.dpackage;

import android.os.Process;
import com.facebook.ads.internal.util.n;
import com.facebook.ads.internal.gpackage.p;
import com.facebook.ads.internal.util.m;
import com.facebook.ads.internal.fpackage.i;
import com.facebook.ads.internal.fpackage.g;
import com.facebook.ads.internal.util.ae;
import android.support.annotation.Nullable;
import android.content.Context;

public class b implements Thread.UncaughtExceptionHandler
{
    private final Thread.UncaughtExceptionHandler a;
    private final Context b;
    
    public b(@Nullable final Thread.UncaughtExceptionHandler a, final Context context) {
        this.a = a;
        if (context == null) {
            throw new IllegalArgumentException("Missing Context");
        }
        this.b = context.getApplicationContext();
    }
    
    @Override
    public void uncaughtException(final Thread thread, final Throwable t) {
        final String a = ae.a(t);
        if (a != null && a.contains("com.facebook.ads")) {
            n.a(new p(g.b(), g.c(), new m(a, i.f)), this.b);
        }
        this.a(thread, t);
    }
    
    private void a(final Thread thread, final Throwable t) {
        if (this.a != null) {
            this.a.uncaughtException(thread, t);
        }
        else {
            try {
                Process.killProcess(Process.myPid());
            }
            catch (Throwable t2) {}
            try {
                System.exit(10);
            }
            catch (Throwable t3) {}
        }
    }
}
