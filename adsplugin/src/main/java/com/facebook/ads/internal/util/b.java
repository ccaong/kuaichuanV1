// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.util;

import com.facebook.ads.internal.server.AdPlacementType;
import java.util.HashMap;
import android.support.annotation.Nullable;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;

public class b
{
    public static String aa;
    private String bb;
    private Map<String, String> c;
    private int d;
    private String e;
    
    public b(final String b, final Map<String, String> c, final int d, final String e) {
        this.bb = b;
        this.c = c;
        this.d = d;
        this.e = e;
    }
    
    public JSONObject a() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", (Object)this.bb);
            jsonObject.put("data", (Object)new JSONObject((Map)this.c));
            jsonObject.put("time", this.d);
            jsonObject.put("request_id", (Object)this.e);
        }
        catch (JSONException ex) {
            ex.printStackTrace();
        }
        return jsonObject;
    }
    
    public static b a(@Nullable final Throwable t, @Nullable final String s) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        if (t != null) {
            hashMap.put("ex", t.getClass().getSimpleName());
            hashMap.put("ex_msg", t.getMessage());
        }
        return new b("error", hashMap, (int)(System.currentTimeMillis() / 1000L), (s != null) ? s : b.aa);
    }
    
    public static b a(final long n, final a a, final String s) {
        final long currentTimeMillis = System.currentTimeMillis();
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("Time", String.valueOf(currentTimeMillis - n));
        hashMap.put("AdAction", String.valueOf(a.f));
        return new b("bounceback", hashMap, (int)(currentTimeMillis / 1000L), s);
    }
    
    public static b a(final bb bb, final AdPlacementType adPlacementType, final long n, final String s) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("LatencyType", String.valueOf(bb.b));
        hashMap.put("AdPlacementType", adPlacementType.toString());
        hashMap.put("Time", String.valueOf(n));
        return new b("latency", hashMap, (int)(System.currentTimeMillis() / 1000L), (s != null) ? s : b.aa);
    }
    
    static {
        b.aa = null;
    }
    
    public enum bb
    {
        LOADING_AD(0);
        
        int b;
        
        private bb(final int b) {
            this.b = b;
        }
    }
    
    public enum a
    {
        OPEN_STORE(0),
        OPEN_LINK(1),
        XOUT(2),
        OPEN_URL(3),
        SHOW_INTERSTITIAL(4);
        
        int f;
        
        private a(final int f) {
            this.f = f;
        }
    }
}
