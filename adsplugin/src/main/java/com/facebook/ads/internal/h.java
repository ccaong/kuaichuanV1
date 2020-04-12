// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;
import android.support.annotation.Nullable;
import com.facebook.ads.internal.util.g;
import android.os.Build;
import android.content.Context;
import android.content.SharedPreferences;

public class h
{
    private static h a;
    private final SharedPreferences b;
    
    public static boolean a(final Context context) {
        return Build.VERSION.SDK_INT >= 14 && g.a("com.google.android.exoplayer2", "ExoPlayer") && o(context).a("adnw_enable_exoplayer", false);
    }
    
    public static boolean b(final Context context) {
        return o(context).a("adnw_block_lockscreen", false);
    }
    
    public static boolean c(final Context context) {
        return o(context).a("show_metadata_rewarded_video", false);
    }
    
    public static boolean d(final Context context) {
        return Build.VERSION.SDK_INT >= 19 && o(context).a("adnw_enable_iab", false);
    }
    
    public static boolean e(final Context context) {
        return o(context).a("adnw_debug_logging", false);
    }
    
    public static long f(final Context context) {
        return o(context).a("unified_logging_immediate_delay_ms", 500L);
    }
    
    public static long g(final Context context) {
        return o(context).a("unified_logging_dispatch_interval_seconds", 300) * 1000L;
    }
    
    public static int h(final Context context) {
        return o(context).a("unified_logging_event_limit", -1);
    }
    
    public static boolean i(final Context context) {
        return o(context).a("video_and_endcard_autorotate", "autorotate_disabled").equals("autorotate_enabled");
    }
    
    public static boolean j(final Context context) {
        return o(context).a("show_play_pause_rewarded_video", false);
    }
    
    public static int k(final Context context) {
        return o(context).a("minimum_elapsed_time_after_impression", -1);
    }
    
    public static int l(final Context context) {
        return o(context).a("ad_viewability_tap_margin", 0);
    }
    
    public static boolean m(final Context context) {
        return o(context).a("visible_area_check_enabled", false);
    }
    
    public static int n(final Context context) {
        return o(context).a("visible_area_percentage", 50);
    }
    
    private static h o(final Context context) {
        if (h.a == null) {
            synchronized (h.class) {
                if (h.a == null) {
                    h.a = new h(context);
                }
            }
        }
        return h.a;
    }
    
    public h(final Context context) {
        this.b = context.getApplicationContext().getSharedPreferences("com.facebook.ads.FEATURE_CONFIG", 0);
    }
    
    public void a(@Nullable final String s) throws JSONException {
        if (s == null || s.isEmpty() || s.equals("[]")) {
            return;
        }
        final SharedPreferences.Editor edit = this.b.edit();
        final JSONObject jsonObject = new JSONObject(s);
        final Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            final String s2 = keys.next();
            edit.putString(s2, jsonObject.getString(s2));
        }
        edit.commit();
    }
    
    @Nullable
    public String a(final String s, final String s2) {
        final String string = this.b.getString(s, s2);
        return (string == null || string.equals("null")) ? s2 : string;
    }
    
    public boolean a(final String s, final boolean b) {
        final String string = this.b.getString(s, String.valueOf(b));
        return (string == null || string.equals("null")) ? b : Boolean.valueOf(string);
    }
    
    public int a(final String s, final int n) {
        final String string = this.b.getString(s, String.valueOf(n));
        return (string == null || string.equals("null")) ? n : Integer.valueOf(string);
    }
    
    public long a(final String s, final long n) {
        final String string = this.b.getString(s, String.valueOf(n));
        return (string == null || string.equals("null")) ? n : Long.valueOf(string);
    }
}
