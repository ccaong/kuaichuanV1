// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.fpackage;

import android.net.NetworkInfo;
import android.net.ConnectivityManager;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.content.pm.PackageManager;
import android.content.SharedPreferences;
import com.facebook.ads.internal.util.c;
import com.facebook.ads.internal.util.b;
import com.facebook.ads.internal.util.g;
import com.facebook.ads.internal.f;
import android.content.Context;

public class i
{
    private static boolean r;
    public static final String a;
    public static String b;
    public static String c;
    public static String d;
    public static String e;
    public static String f;
    public static int g;
    public static String h;
    public static String i;
    public static int j;
    public static String k;
    public static int l;
    public static String m;
    public static String n;
    public static String o;
    public static boolean p;
    public static String q;
    
    public static synchronized void a(final Context context) {
        if (!com.facebook.ads.internal.fpackage.i.r) {
            com.facebook.ads.internal.fpackage.i.r = true;
            c(context);
        }
        d(context);
    }
    
    public static void b(final Context context) {
        if (!com.facebook.ads.internal.fpackage.i.r) {
            return;
        }
        try {
            f a = null;
            g.a a2 = null;
            final SharedPreferences sharedPreferences = context.getSharedPreferences("SDKIDFA", 0);
            if (sharedPreferences.contains("attributionId")) {
                com.facebook.ads.internal.fpackage.i.n = sharedPreferences.getString("attributionId", "");
            }
            if (sharedPreferences.contains("advertisingId")) {
                com.facebook.ads.internal.fpackage.i.o = sharedPreferences.getString("advertisingId", "");
                com.facebook.ads.internal.fpackage.i.p = sharedPreferences.getBoolean("limitAdTracking", com.facebook.ads.internal.fpackage.i.p);
                com.facebook.ads.internal.fpackage.i.q = com.facebook.ads.internal.f.c.SHARED_PREFS.name();
            }
            try {
                a2 = com.facebook.ads.internal.util.g.a(context.getContentResolver());
            }
            catch (Exception ex) {
                com.facebook.ads.internal.util.c.a(com.facebook.ads.internal.util.b.a(ex, "Error retrieving attribution id from fb4a"));
            }
            if (a2 != null) {
                final String a3 = a2.a;
                if (a3 != null) {
                    com.facebook.ads.internal.fpackage.i.n = a3;
                }
            }
            try {
                a = com.facebook.ads.internal.f.a(context, a2);
            }
            catch (Exception ex2) {
                com.facebook.ads.internal.util.c.a(com.facebook.ads.internal.util.b.a(ex2, "Error retrieving advertising id from Google Play Services"));
            }
            if (a != null) {
                final String a4 = a.a();
                final Boolean value = a.b();
                if (a4 != null) {
                    com.facebook.ads.internal.fpackage.i.o = a4;
                    com.facebook.ads.internal.fpackage.i.p = value;
                    com.facebook.ads.internal.fpackage.i.q = a.c().name();
                }
            }
            final SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString("attributionId", com.facebook.ads.internal.fpackage.i.n);
            edit.putString("advertisingId", com.facebook.ads.internal.fpackage.i.o);
            edit.putBoolean("limitAdTracking", com.facebook.ads.internal.fpackage.i.p);
            edit.apply();
        }
        catch (Exception ex3) {
            ex3.printStackTrace();
        }
    }
    
    private static void c(final Context context) {
        final PackageManager packageManager = context.getPackageManager();
        try {
            final PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            com.facebook.ads.internal.fpackage.i.d = packageInfo.packageName;
            com.facebook.ads.internal.fpackage.i.f = packageInfo.versionName;
            com.facebook.ads.internal.fpackage.i.g = packageInfo.versionCode;
        }
        catch (PackageManager.NameNotFoundException ex) {}
        try {
            if (com.facebook.ads.internal.fpackage.i.d != null && com.facebook.ads.internal.fpackage.i.d.length() >= 0) {
                final String installerPackageName = packageManager.getInstallerPackageName(com.facebook.ads.internal.fpackage.i.d);
                if (installerPackageName != null && installerPackageName.length() > 0) {
                    com.facebook.ads.internal.fpackage.i.h = installerPackageName;
                }
            }
        }
        catch (Exception ex2) {}
        try {
            final CharSequence applicationLabel = packageManager.getApplicationLabel(packageManager.getApplicationInfo(context.getPackageName(), 0));
            if (applicationLabel != null && applicationLabel.length() > 0) {
                com.facebook.ads.internal.fpackage.i.e = applicationLabel.toString();
            }
        }
        catch (PackageManager.NameNotFoundException ex3) {}
        final TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            final String networkOperatorName = telephonyManager.getNetworkOperatorName();
            if (networkOperatorName != null && networkOperatorName.length() > 0) {
                com.facebook.ads.internal.fpackage.i.i = networkOperatorName;
            }
        }
        final String manufacturer = Build.MANUFACTURER;
        if (manufacturer != null && manufacturer.length() > 0) {
            com.facebook.ads.internal.fpackage.i.b = manufacturer;
        }
        final String model = Build.MODEL;
        if (model != null && model.length() > 0) {
            com.facebook.ads.internal.fpackage.i.c = Build.MODEL;
        }
    }
    
    private static void d(final Context context) {
        try {
            final NetworkInfo activeNetworkInfo = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting()) {
                com.facebook.ads.internal.fpackage.i.j = activeNetworkInfo.getType();
                com.facebook.ads.internal.fpackage.i.k = activeNetworkInfo.getTypeName();
                com.facebook.ads.internal.fpackage.i.l = activeNetworkInfo.getSubtype();
                com.facebook.ads.internal.fpackage.i.m = activeNetworkInfo.getSubtypeName();
            }
        }
        catch (Exception ex) {}
    }
    
    static {
        com.facebook.ads.internal.fpackage.i.r = false;
        a = Build.VERSION.RELEASE;
        com.facebook.ads.internal.fpackage.i.b = "";
        com.facebook.ads.internal.fpackage.i.c = "";
        com.facebook.ads.internal.fpackage.i.d = "";
        com.facebook.ads.internal.fpackage.i.e = "";
        com.facebook.ads.internal.fpackage.i.f = "";
        com.facebook.ads.internal.fpackage.i.g = 0;
        com.facebook.ads.internal.fpackage.i.h = "";
        com.facebook.ads.internal.fpackage.i.i = "";
        com.facebook.ads.internal.fpackage.i.j = 0;
        com.facebook.ads.internal.fpackage.i.k = "";
        com.facebook.ads.internal.fpackage.i.l = 0;
        com.facebook.ads.internal.fpackage.i.m = "";
        com.facebook.ads.internal.fpackage.i.n = "";
        com.facebook.ads.internal.fpackage.i.o = "";
        com.facebook.ads.internal.fpackage.i.p = false;
        com.facebook.ads.internal.fpackage.i.q = "";
    }
}
