// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads;

import java.util.HashSet;
import android.util.Log;
import android.content.SharedPreferences;
import java.util.UUID;
import com.facebook.ads.internal.util.s;
import com.facebook.ads.internal.util.g;
import android.text.TextUtils;
import android.os.Build;
import com.facebook.ads.internal.util.AdInternalSettings;
import android.content.Context;
import java.util.Collection;

public class AdSettings
{
    private static final String b;
    public static final boolean DEBUG = false;
    private static final Collection<String> c;
    private static final Collection<String> d;
    private static String e;
    private static boolean f;
    private static boolean g;
    private static String h;
    private static boolean i;
    private static String j;
    private static TestAdType k;
    static volatile boolean a;
    
    public static void addTestDevice(final String s) {
        AdSettings.c.add(s);
    }
    
    public static void addTestDevices(final Collection<String> collection) {
        AdSettings.c.addAll(collection);
    }
    
    public static void clearTestDevices() {
        AdSettings.c.clear();
    }
    
    public static boolean isTestMode(final Context context) {
        if (AdInternalSettings.a || AdSettings.d.contains(Build.PRODUCT)) {
            return true;
        }
        if (AdSettings.j == null) {
            final SharedPreferences sharedPreferences = context.getSharedPreferences("FBAdPrefs", 0);
            AdSettings.j = sharedPreferences.getString("deviceIdHash", (String)null);
            if (TextUtils.isEmpty((CharSequence)AdSettings.j)) {
                final g.a a = com.facebook.ads.internal.util.g.a(context.getContentResolver());
                if (!TextUtils.isEmpty((CharSequence)a.b)) {
                    AdSettings.j = s.a(a.b);
                }
                else if (!TextUtils.isEmpty((CharSequence)a.a)) {
                    AdSettings.j = s.a(a.a);
                }
                else {
                    AdSettings.j = s.a(UUID.randomUUID().toString());
                }
                sharedPreferences.edit().putString("deviceIdHash", AdSettings.j).apply();
            }
        }
        if (AdSettings.c.contains(AdSettings.j)) {
            return true;
        }
        a(AdSettings.j);
        return false;
    }
    
    private static void a(final String s) {
        if (AdSettings.a) {
            return;
        }
        AdSettings.a = true;
        Log.d(AdSettings.b, "Test mode device hash: " + s);
        Log.d(AdSettings.b, "When testing your app with Facebook's ad units you must specify the device hashed ID to ensure the delivery of test ads, add the following code before loading an ad: AdSettings.addTestDevice(\"" + s + "\");");
    }
    
    public static void setUrlPrefix(final String e) {
        AdSettings.e = e;
    }
    
    public static String getUrlPrefix() {
        return AdSettings.e;
    }
    
    public static boolean isVideoAutoplay() {
        return AdSettings.f;
    }
    
    public static boolean isVideoAutoplayOnMobile() {
        return AdSettings.g;
    }
    
    public static void setVideoAutoplay(final boolean f) {
        AdSettings.f = f;
    }
    
    public static void setVideoAutoplayOnMobile(final boolean g) {
        AdSettings.g = g;
    }
    
    public static void setMediationService(final String h) {
        AdSettings.h = h;
    }
    
    public static String getMediationService() {
        return AdSettings.h;
    }
    
    public static void setIsChildDirected(final boolean i) {
        AdSettings.i = i;
    }
    
    public static boolean isChildDirected() {
        return AdSettings.i;
    }
    
    public static void setTestAdType(final TestAdType k) {
        AdSettings.k = k;
    }
    
    public static TestAdType getTestAdType() {
        return AdSettings.k;
    }
    
    static {
        b = AdSettings.class.getSimpleName();
        AdSettings.k = TestAdType.DEFAULT;
        c = new HashSet<String>();
        (d = new HashSet<String>()).add("sdk");
        AdSettings.d.add("google_sdk");
        AdSettings.d.add("vbox86p");
        AdSettings.d.add("vbox86tp");
        AdSettings.a = false;
    }
    
    public enum TestAdType
    {
        DEFAULT("DEFAULT"), 
        IMG_16_9_APP_INSTALL("IMG_16_9_APP_INSTALL"), 
        IMG_16_9_LINK("IMG_16_9_LINK"), 
        VIDEO_HD_16_9_46S_APP_INSTALL("VID_HD_16_9_46S_APP_INSTALL"), 
        VIDEO_HD_16_9_46S_LINK("VID_HD_16_9_46S_LINK"), 
        VIDEO_HD_16_9_15S_APP_INSTALL("VID_HD_16_9_15S_APP_INSTALL"), 
        VIDEO_HD_16_9_15S_LINK("VID_HD_16_9_15S_LINK"), 
        VIDEO_HD_9_16_39S_APP_INSTALL("VID_HD_9_16_39S_APP_INSTALL"), 
        VIDEO_HD_9_16_39S_LINK("VID_HD_9_16_39S_LINK"), 
        CAROUSEL_IMG_SQUARE_APP_INSTALL("CAROUSEL_IMG_SQUARE_APP_INSTALL"), 
        CAROUSEL_IMG_SQUARE_LINK("CAROUSEL_IMG_SQUARE_LINK");
        
        private final String a;
        
        private TestAdType(final String a) {
            this.a = a;
        }
        
        public String getAdTypeString() {
            return this.a;
        }
    }
}
