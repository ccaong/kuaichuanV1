// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.util;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;

import android.content.pm.PackageManager;
import android.net.NetworkInfo;
import android.net.ConnectivityManager;
import android.annotation.TargetApi;
import android.webkit.WebSettings;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import android.webkit.WebView;
import android.os.Build;
import java.util.Locale;
import com.facebook.ads.internal.fpackage.i;
import com.facebook.ads.internal.e;
import android.content.Context;
import com.facebook.ads.internal.ipackage.a.a;
import android.text.TextUtils;
import com.facebook.ads.AdSettings;
import java.util.Set;

public class w
{
    private static String userAgent;
    private static final Set<String> b;
    private static final Set<String> c;
    
    public static boolean a() {
        final String urlPrefix = AdSettings.getUrlPrefix();
        return !TextUtils.isEmpty((CharSequence)urlPrefix) && urlPrefix.endsWith(".sb");
    }
    
    public static com.facebook.ads.internal.ipackage.a.a b() {
        return a(null);
    }
    
    public static com.facebook.ads.internal.ipackage.a.a a(final Context context) {
        return a(context, null);
    }
    
    public static com.facebook.ads.internal.ipackage.a.a a(final Context context, final e e) {
        final com.facebook.ads.internal.ipackage.a.a a = new com.facebook.ads.internal.ipackage.a.a();
        a(context, a, e);
        return a;
    }
    
    public static com.facebook.ads.internal.ipackage.a.a b(final Context context) {
        return b(context, null);
    }
    
    public static com.facebook.ads.internal.ipackage.a.a b(final Context context, final e e) {
        final com.facebook.ads.internal.ipackage.a.a a = new com.facebook.ads.internal.ipackage.a.a();
        a(context, a, e);
        if (!a()) {
            a.b(w.c);
            a.a(w.b);
        }
        return a;
    }
    
    private static void a(final Context context, final com.facebook.ads.internal.ipackage.a.a a, final e e) {
        a.c(30000);
        a.b(3);
        a.a("user-agent", c(context, e) + " [FBAN/AudienceNetworkForAndroid;FBSN/" + "Android" + ";FBSV/" + i.a + ";FBAB/" + i.d + ";FBAV/" + i.f + ";FBBV/" + i.g + ";FBVS/" + "4.23.0" + ";FBLC/" + Locale.getDefault().toString() + "]");
    }
    
    private static String c(final Context context, final e e) {
        if (context == null) {
            return "Unknown";
        }
        if (e == e.NATIVE_250 || e == e.NATIVE_UNKNOWN || e == null) {
            return System.getProperty("http.agent");
        }
        if (w.userAgent != null) {
            return w.userAgent;
        }
        synchronized (w.class) {
            if (w.userAgent != null) {
                return w.userAgent;
            }
            if (Build.VERSION.SDK_INT >= 17) {
                try {
                    return w.userAgent = d(context);
                }
                catch (Exception ex) {}
            }
            try {
                w.userAgent = a(context, "android.webkit.WebSettings", "android.webkit.WebView");
            }
            catch (Exception ex2) {
                try {
                    w.userAgent = a(context, "android.webkit.WebSettingsClassic", "android.webkit.WebViewClassic");
                }
                catch (Exception ex3) {
                    final WebView webView = new WebView(context.getApplicationContext());
                    w.userAgent = webView.getSettings().getUserAgentString();
                    webView.destroy();
                }
            }
        }
        return w.userAgent;
    }
    
    private static String a(final Context context, final String s, final String s2) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        final Class<?> forName = Class.forName(s);
        final Constructor<?> declaredConstructor = forName.getDeclaredConstructor(Context.class, Class.forName(s2));
        declaredConstructor.setAccessible(true);
        final Method method = forName.getMethod("getUserAgentString", (Class<?>[])new Class[0]);
        try {
            return (String)method.invoke(declaredConstructor.newInstance(context, null), new Object[0]);
        }
        finally {
            declaredConstructor.setAccessible(false);
        }
    }
    
    @TargetApi(17)
    private static String d(final Context context) {
        return WebSettings.getDefaultUserAgent(context);
    }
    
    public static a c(final Context context) {
        if (context.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") != PackageManager.PERMISSION_GRANTED) {
            return w.a.UNKNOWN;
        }
        final NetworkInfo activeNetworkInfo = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            return w.a.NONE;
        }
        if (activeNetworkInfo.getType() != 0) {
            return w.a.MOBILE_INTERNET;
        }
        switch (activeNetworkInfo.getSubtype()) {
            case 1:
            case 2:
            case 4:
            case 7:
            case 11: {
                return w.a.MOBILE_2G;
            }
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 12:
            case 14:
            case 15: {
                return w.a.MOBILE_3G;
            }
            case 13: {
                return w.a.MOBILE_4G;
            }
            default: {
                return a.UNKNOWN;
            }
        }
    }
    
    static {
        w.userAgent = null;
        b = new HashSet<String>(1);
        c = new HashSet<String>(2);
        w.b.add("1ww8E0AYsR2oX5lndk2hwp2Uosk=\n");
        w.c.add("toZ2GRnRjC9P5VVUdCpOrFH8lfQ=\n");
        w.c.add("3lKvjNsfmrn+WmfDhvr2iVh/yRs=\n");
        w.c.add("B08QtE4yLCdli4rptyqAEczXOeA=\n");
        w.c.add("XZXI6anZbdKf+taURdnyUH5ipgM=\n");
    }
    
    public enum a
    {
        UNKNOWN(0),//a
        NONE(0),//b
        MOBILE_INTERNET(1),//c
        MOBILE_2G(2),//d
        MOBILE_3G(3),//e
        MOBILE_4G(4);//f
        
        public final int g;
        
        private a(final int g) {
            this.g = g;
        }
    }
}
