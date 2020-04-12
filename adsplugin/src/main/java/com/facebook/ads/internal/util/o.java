// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.util;

import java.io.File;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import android.content.Context;

public class o
{
    private static final String TAG;
    
    public static a a() {
        try {
            if (c() || b() || a("su")) {
                return a.ROOTED;
            }
            return a.UNROOTED;
        }
        catch (Throwable t) {
            return a.UNKNOWN;
        }
    }
    
    public static boolean a(final Context context) {
        return b(context) && al.c(context);
    }
    
    public static boolean b(final Context context) {
        if (context == null) {
            Log.v(o.TAG, "Invalid context in screen interactive check, assuming interactive.");
            return true;
        }
        try {
            final PowerManager powerManager = (PowerManager)context.getSystemService("power");
            if (Build.VERSION.SDK_INT >= 20) {
                return powerManager.isInteractive();
            }
            return powerManager.isScreenOn();
        }
        catch (Exception ex) {
            Log.e(o.TAG, "Exception in screen interactive check, assuming interactive.", (Throwable)ex);
            n.a(ex, context);
            return true;
        }
    }
    
    private static boolean b() {
        final String tags = Build.TAGS;
        return tags != null && tags.contains("test-keys");
    }
    
    private static boolean c() {
        return new File("/system/app/Superuser.apk").exists();
    }
    
    private static boolean a(final String s) {
        final String[] split = System.getenv("PATH").split(":");
        for (int length = split.length, i = 0; i < length; ++i) {
            final File file = new File(split[i]);
            if (file.exists()) {
                if (file.isDirectory()) {
                    final File[] listFiles = file.listFiles();
                    if (listFiles != null) {
                        final File[] array = listFiles;
                        for (int length2 = array.length, j = 0; j < length2; ++j) {
                            if (array[j].getName().equals(s)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    
    static {
        TAG = o.class.getSimpleName();
    }
    
    public enum a
    {
        UNKNOWN(0),
        UNROOTED(1),
        ROOTED(2);
        
        public final int d;
        
        private a(final int d) {
            this.d = d;
        }
    }
}
