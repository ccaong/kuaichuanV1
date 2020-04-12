// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.dpackage;

import android.util.Log;
import com.facebook.ads.internal.h;
import android.content.Context;

public class a
{
    private static final String a;
    private static a b;
    private static boolean c;
    private Context d;
    
    private a(final Context d) {
        this.d = d;
    }
    
    public static a a(final Context context) {
        if (com.facebook.ads.internal.dpackage.a.b == null) {
            final Context applicationContext = context.getApplicationContext();
            synchronized (applicationContext) {
                if (com.facebook.ads.internal.dpackage.a.b == null) {
                    com.facebook.ads.internal.dpackage.a.b = new a(applicationContext);
                }
            }
        }
        return com.facebook.ads.internal.dpackage.a.b;
    }
    
    public synchronized void a() {
        if (!com.facebook.ads.internal.dpackage.a.c) {
            if (h.e(this.d)) {
                try {
                    Thread.setDefaultUncaughtExceptionHandler(new b(Thread.getDefaultUncaughtExceptionHandler(), this.d));
                }
                catch (SecurityException ex) {
                    Log.e(com.facebook.ads.internal.dpackage.a.a, "No permissions to set the default uncaught exception handler", (Throwable)ex);
                }
            }
            com.facebook.ads.internal.dpackage.a.c = true;
        }
    }
    
    static {
        a = a.class.getName();
        com.facebook.ads.internal.dpackage.a.c = false;
    }
}
