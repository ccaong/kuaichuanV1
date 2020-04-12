// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal;

import org.json.JSONArray;

public enum d
{
    APP_AD(0), //a
    LINK_AD(1), //b
    APP_AD_V2(2), //c
    LINK_AD_V2(3), //d
    APP_ENGAGEMENT_AD(4), //e
    AD_CHOICES(5), //f
    JS_TRIGGER(6), //g
    JS_TRIGGER_NO_AUTO_IMP_LOGGING(7), //h
    VIDEO_AD(8), //i
    INLINE_VIDEO_AD(9), //j
    BANNER_TO_INTERSTITIAL(10), //k
    NATIVE_CLOSE_BUTTON(11), //l
    UNIFIED_LOGGING(16), //m
    HTTP_LINKS(17);//n
    
    private final int p;
    public static final d[] o;
    private static final String q;
    
    private d(final int p4) {
        this.p = p4;
    }
    
    public int a() {
        return this.p;
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.p);
    }
    
    public static String b() {
        return d.q;
    }
    
    static {
        o = new d[] { LINK_AD_V2, APP_ENGAGEMENT_AD, AD_CHOICES, JS_TRIGGER_NO_AUTO_IMP_LOGGING, NATIVE_CLOSE_BUTTON, UNIFIED_LOGGING, HTTP_LINKS };
        final JSONArray jsonArray = new JSONArray();
        final d[] o2 = d.o;
        for (int length = o2.length, n2 = 0; n2 < length; ++n2) {
            jsonArray.put(o2[n2].a());
        }
        q = jsonArray.toString();
    }
}
