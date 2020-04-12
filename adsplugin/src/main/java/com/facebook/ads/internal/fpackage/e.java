// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.fpackage;

import java.util.HashMap;
import org.json.JSONObject;
import java.util.Iterator;
import android.util.Log;
import android.webkit.CookieSyncManager;
import android.os.Build;
import android.webkit.CookieManager;
import java.util.Map;
import java.util.List;
import com.facebook.ads.internal.server.AdPlacementType;

public class e
{
    public int a;
    public int b;
    private static final String c;
    private static final AdPlacementType d;
    private AdPlacementType e;
    private int f;
    private int g;
    private int h;
    private int i;
    private int j;
    private int k;
    private int l;
    private boolean m;
    private List<b> n;
    
    private e(final Map<String, String> map) {
        this.a = -1;
        this.b = -1;
        this.e = com.facebook.ads.internal.fpackage.e.d;
        this.f = 1;
        this.g = 0;
        this.h = 0;
        this.i = 20;
        this.j = 0;
        this.k = 1000;
        this.l = 10000;
        this.m = false;
        this.n = null;
        for (final Map.Entry<String, String> entry : map.entrySet()) {
            final String s = entry.getKey();
            switch (s) {
                case "type": {
                    this.e = AdPlacementType.fromString(entry.getValue());
                    continue;
                }
                case "min_viewability_percentage": {
                    this.f = Integer.parseInt(entry.getValue());
                    continue;
                }
                case "viewability_check_ticker": {
                    this.g = Integer.parseInt(entry.getValue());
                    continue;
                }
                case "refresh": {
                    this.h = Integer.parseInt(entry.getValue());
                    continue;
                }
                case "refresh_threshold": {
                    this.i = Integer.parseInt(entry.getValue());
                    continue;
                }
                case "cacheable": {
                    this.m = Boolean.valueOf(entry.getValue());
                    continue;
                }
                case "placement_width": {
                    this.a = Integer.parseInt(entry.getValue());
                    continue;
                }
                case "placement_height": {
                    this.b = Integer.parseInt(entry.getValue());
                    continue;
                }
                case "viewability_check_initial_delay": {
                    this.j = Integer.parseInt(entry.getValue());
                    continue;
                }
                case "viewability_check_interval": {
                    this.k = Integer.parseInt(entry.getValue());
                    continue;
                }
                case "request_timeout": {
                    this.l = Integer.parseInt(entry.getValue());
                    continue;
                }
                case "conv_tracking_data": {
                    this.n = com.facebook.ads.internal.fpackage.b.a(entry.getValue());
                    try {
                        final CookieManager instance = CookieManager.getInstance();
                        final boolean acceptCookie = instance.acceptCookie();
                        instance.setAcceptCookie(true);
                        for (final b b : this.n) {
                            if (b.b()) {
                                instance.setCookie(b.a, b.b + "=" + b.c + ";Domain=" + b.a + ";Expires=" + b.a() + ";path=/");
                            }
                        }
                        if (Build.VERSION.SDK_INT < 21) {
                            CookieSyncManager.getInstance().startSync();
                        }
                        instance.setAcceptCookie(acceptCookie);
                    }
                    catch (Exception ex) {
                        Log.w(com.facebook.ads.internal.fpackage.e.c, "Failed to set cookie.", (Throwable)ex);
                    }
                    continue;
                }
            }
        }
    }
    
    public AdPlacementType a() {
        return this.e;
    }
    
    public long b() {
        return this.h * 1000;
    }
    
    public long c() {
        return this.i * 1000;
    }
    
    public boolean d() {
        return this.m;
    }
    
    public int e() {
        return this.f;
    }
    
    public int f() {
        return this.g;
    }
    
    public int g() {
        return this.j;
    }
    
    public int h() {
        return this.k;
    }
    
    public int i() {
        return this.l;
    }
    
    public static e a(final JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        final Iterator<String> keys = jsonObject.keys();
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        while (keys.hasNext()) {
            final String s = keys.next();
            hashMap.put(s, String.valueOf(jsonObject.opt(s)));
        }
        return new e(hashMap);
    }
    
    static {
        c = e.class.getSimpleName();
        d = AdPlacementType.UNKNOWN;
    }
}
