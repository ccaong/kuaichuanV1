// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import android.app.ActivityManager;
import android.os.AsyncTask;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import java.io.ByteArrayOutputStream;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.view.View;
import android.util.DisplayMetrics;
import android.content.ActivityNotFoundException;
import com.facebook.ads.InterstitialAdActivity;
import java.io.Serializable;
import com.facebook.ads.AudienceNetworkActivity;
import android.os.Build;
import android.content.ComponentName;
import android.content.Intent;
import com.facebook.ads.internal.h;
import com.facebook.ads.internal.view.apackage.d;
import android.util.Log;
import com.facebook.ads.AdSettings;
import android.content.Context;
import java.lang.reflect.Method;
import android.database.Cursor;
import android.content.ContentResolver;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;
import com.facebook.ads.internal.e;
import com.facebook.ads.AdSize;
import java.util.Map;
import android.net.Uri;

public class g
{
    private static final Uri a;
    private static final String b;
    private static final Map<AdSize, e> c;
    
    public static String a(final Map<String, String> map) {
        final JSONObject jsonObject = new JSONObject();
        if (map != null) {
            for (final Map.Entry<String, String> entry : map.entrySet()) {
                try {
                    jsonObject.put((String)entry.getKey(), (Object)entry.getValue());
                }
                catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return jsonObject.toString();
    }
    
    public static String a(final JSONObject jsonObject, final String s) {
        return a(jsonObject, s, null);
    }
    
    public static String a(final JSONObject jsonObject, final String s, final String s2) {
        final String optString = jsonObject.optString(s, s2);
        return "null".equals(optString) ? "null" : optString;
    }
    
    public static a a(final ContentResolver contentResolver) {
        Cursor query = null;
        try {
            query = contentResolver.query(g.a, new String[] { "aid", "androidid", "limit_tracking" }, (String)null, (String[])null, (String)null);
            if (query == null || !query.moveToFirst()) {
                return new a(null, null, false);
            }
            return new a(query.getString(query.getColumnIndex("aid")), query.getString(query.getColumnIndex("androidid")), Boolean.valueOf(query.getString(query.getColumnIndex("limit_tracking"))));
        }
        catch (Exception ex) {
            return new a(null, null, false);
        }
        finally {
            if (query != null) {
                query.close();
            }
        }
    }
    
    public static boolean a(final String s, final String s2) {
        try {
            Class.forName(s + "." + s2);
            return true;
        }
        catch (ClassNotFoundException ex) {
            return false;
        }
    }
    
    public static Method a(final Class<?> clazz, final String s, final Class<?>... array) {
        try {
            return clazz.getMethod(s, (Class[])array);
        }
        catch (NoSuchMethodException ex) {
            return null;
        }
    }
    
    public static Method a(final String s, final String s2, final Class<?>... array) {
        try {
            return a(Class.forName(s), s2, array);
        }
        catch (ClassNotFoundException ex) {
            return null;
        }
    }
    
    public static Object a(final Object o, final Method method, final Object... array) {
        try {
            return method.invoke(o, array);
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public static void a(final Context context, final String s) {
        if (AdSettings.isTestMode(context)) {
            Log.d("FBAudienceNetworkLog", s + " (displayed for test ads only)");
        }
    }
    
    public static void a(final Context context, final Uri uri, final String s) {
        if (d.a(uri.getScheme()) && h.d(context)) {
            b(context, uri, s);
        }
        else {
            a(context, uri);
        }
    }
    
    private static Intent a(final Uri uri) {
        final Intent intent = new Intent("android.intent.action.VIEW", uri);
        intent.setComponent((ComponentName)null);
        if (Build.VERSION.SDK_INT >= 15) {
            intent.setSelector((Intent)null);
        }
        return intent;
    }
    
    private static void a(final Context context, final Uri uri) {
        final Intent a = a(uri);
        a.addCategory("android.intent.category.BROWSABLE");
        a.addFlags(268435456);
        a.putExtra("com.android.browser.application_id", context.getPackageName());
        a.putExtra("create_new_tab", false);
        try {
            context.startActivity(a);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private static void b(final Context context, final Uri uri, final String s) {
        final Intent intent = new Intent(context, (Class)AudienceNetworkActivity.class);
        intent.addFlags(268435456);
        intent.putExtra("viewType", (Serializable)AudienceNetworkActivity.Type.BROWSER);
        intent.putExtra("browserURL", uri.toString());
        intent.putExtra("clientToken", s);
        intent.putExtra("handlerTime", System.currentTimeMillis());
        try {
            context.startActivity(intent);
        }
        catch (ActivityNotFoundException ex) {
            intent.setClass(context, (Class)InterstitialAdActivity.class);
            try {
                context.startActivity(intent);
            }
            catch (ActivityNotFoundException ex2) {
                a(context, uri);
            }
        }
    }
    
    public static void a(final DisplayMetrics displayMetrics, final View view, final AdSize adSize) {
        final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(((int)(displayMetrics.widthPixels / displayMetrics.density) >= adSize.getWidth()) ? displayMetrics.widthPixels : ((int)Math.ceil(adSize.getWidth() * displayMetrics.density)), (int)Math.ceil(adSize.getHeight() * displayMetrics.density));
        layoutParams.addRule(14, -1);
        view.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    }
    
    public static e a(final AdSize adSize) {
        final e e = g.c.get(adSize);
        if (e == null) {
            return com.facebook.ads.internal.e.WEBVIEW_BANNER_LEGACY;
        }
        return e;
    }
    
    public static byte[] a(final String s) {
        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(s.length());
            final GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gzipOutputStream.write(s.getBytes());
            gzipOutputStream.close();
            final byte[] byteArray = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            return byteArray;
        }
        catch (Exception ex) {
            com.facebook.ads.internal.util.c.a(com.facebook.ads.internal.util.b.a(ex, "Error compressing data"));
            ex.printStackTrace();
            return new byte[0];
        }
    }
    
    public static String a(final byte[] array) {
        try {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(array);
            final GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
            final String a = a(gzipInputStream);
            gzipInputStream.close();
            byteArrayInputStream.close();
            return a;
        }
        catch (Exception ex) {
            com.facebook.ads.internal.util.c.a(com.facebook.ads.internal.util.b.a(ex, "Error decompressing data"));
            ex.printStackTrace();
            return "";
        }
    }
    
    public static String a(final InputStream inputStream) throws IOException {
        final StringWriter stringWriter = new StringWriter();
        final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        final char[] array = new char[4096];
        int read;
        while ((read = inputStreamReader.read(array)) != -1) {
            stringWriter.write(array, 0, read);
        }
        final String string = stringWriter.toString();
        stringWriter.close();
        inputStreamReader.close();
        return string;
    }
    
    public static final <P, PR, R> AsyncTask<P, PR, R> a(final AsyncTask<P, PR, R> asyncTask, final P... array) {
        if (Build.VERSION.SDK_INT >= 11) {
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (P[])array);
        }
        else {
            asyncTask.execute((P[])array);
        }
        return asyncTask;
    }
    
    public static boolean b(final String s) {
        try {
            Class.forName(s);
            return true;
        }
        catch (Throwable t) {
            return false;
        }
    }
    
    public static boolean a(final Context context) {
        try {
            final ActivityManager.RunningTaskInfo runningTaskInfo = ((ActivityManager)context.getSystemService("activity")).getRunningTasks(2).get(0);
            final String string = runningTaskInfo.topActivity.getPackageName() + ".UnityPlayerActivity";
            final Boolean value = runningTaskInfo.topActivity.getClassName() == string || b(string);
            Log.d("IS_UNITY", Boolean.toString(value));
            return value;
        }
        catch (Throwable t) {
            return false;
        }
    }
    
    public static String a(final long n) {
        return a(n / 1000.0);
    }
    
    public static String a(final double n) {
        return String.format(Locale.US, "%.3f", n);
    }
    
    static {
        a = Uri.parse("content://com.facebook.katana.provider.AttributionIdProvider");
        b = g.class.getSimpleName();
        (c = new HashMap<AdSize, e>()).put(AdSize.INTERSTITIAL, e.WEBVIEW_INTERSTITIAL_UNKNOWN);
        g.c.put(AdSize.RECTANGLE_HEIGHT_250, e.WEBVIEW_BANNER_250);
        g.c.put(AdSize.BANNER_HEIGHT_90, e.WEBVIEW_BANNER_90);
        g.c.put(AdSize.BANNER_HEIGHT_50, e.WEBVIEW_BANNER_50);
    }
    
    public static class a
    {
        public String a;
        public String b;
        public boolean c;
        
        public a(final String a, final String b, final boolean c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }
}
