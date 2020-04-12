// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads;

import java.util.Iterator;
import com.facebook.ads.internal.cpackage.a;
import android.text.TextUtils;
import com.facebook.ads.internal.cpackage.b;
import com.facebook.ads.internal.adapters.v;
import com.facebook.ads.internal.e;
import java.util.EnumSet;
import android.util.Log;
import android.webkit.CookieSyncManager;
import android.os.Build;
import android.webkit.CookieManager;
import java.util.ArrayList;
import com.facebook.ads.internal.i;
import java.util.List;
import android.content.Context;
import com.facebook.ads.internal.c;

public class NativeAdsManager
{
    private static final String a;
    private static final c b;
    private final Context context;
    private final String d;
    private final int e;
    private final List<NativeAd> f;
    private int g;
    private Listener h;
    private i i;
    private boolean j;
    private boolean k;
    
    public NativeAdsManager(final Context c, final String d, final int n) {
        if (c == null) {
            throw new IllegalArgumentException("context can not be null");
        }
        this.context = c;
        this.d = d;
        this.e = Math.max(n, 0);
        this.f = new ArrayList<NativeAd>(n);
        this.g = -1;
        this.k = false;
        this.j = false;
        try {
            CookieManager.getInstance();
            if (Build.VERSION.SDK_INT < 21) {
                CookieSyncManager.createInstance(c);
            }
        }
        catch (Exception ex) {
            Log.w(NativeAdsManager.a, "Failed to initialize CookieManager.", (Throwable)ex);
        }
    }
    
    public void setListener(final Listener h) {
        this.h = h;
    }
    
    public void loadAds() {
        this.loadAds(EnumSet.of(NativeAd.MediaCacheFlag.NONE));
    }
    
    public void loadAds(final EnumSet<NativeAd.MediaCacheFlag> set) {
        final e j = com.facebook.ads.internal.e.NATIVE_UNKNOWN;
        final int e = this.e;
        if (this.i != null) {
            this.i.b();
        }
        this.i = new i(this.context, this.d, j, null, NativeAdsManager.b, e, set);
        if (this.j) {
            this.i.c();
        }
        this.i.a(new i.a() {
            @Override
            public void a(final List<v> list) {
                final com.facebook.ads.internal.cpackage.b b = new com.facebook.ads.internal.cpackage.b(NativeAdsManager.this.context);
                for (final v v : list) {
                    if (set.contains(NativeAd.MediaCacheFlag.ICON) && v.k() != null) {
                        b.a(v.k().getUrl());
                    }
                    if (set.contains(NativeAd.MediaCacheFlag.IMAGE) && v.l() != null) {
                        b.a(v.l().getUrl());
                    }
                    if (set.contains(NativeAd.MediaCacheFlag.VIDEO) && !TextUtils.isEmpty((CharSequence)v.w())) {
                        b.b(v.w());
                    }
                }
                b.a(new com.facebook.ads.internal.cpackage.a() {
                    @Override
                    public void a() {
                        NativeAdsManager.this.k = true;
                        NativeAdsManager.this.f.clear();
                        NativeAdsManager.this.g = 0;
                        final Iterator<v> iterator = list.iterator();
                        while (iterator.hasNext()) {
                            NativeAdsManager.this.f.add(new NativeAd(NativeAdsManager.this.context, iterator.next(), null));
                        }
                        if (NativeAdsManager.this.h != null) {
                            NativeAdsManager.this.h.onAdsLoaded();
                        }
                    }
                });
            }
            
            @Override
            public void a(final com.facebook.ads.internal.b b) {
                if (NativeAdsManager.this.h != null) {
                    NativeAdsManager.this.h.onAdError(b.b());
                }
            }
        });
        this.i.a();
    }
    
    public int getUniqueNativeAdCount() {
        return this.f.size();
    }
    
    public NativeAd nextNativeAd() {
        if (this.f.size() == 0) {
            return null;
        }
        final int n = this.g++;
        final NativeAd nativeAd = this.f.get(n % this.f.size());
        if (n >= this.f.size()) {
            return new NativeAd(nativeAd);
        }
        return nativeAd;
    }
    
    public boolean isLoaded() {
        return this.k;
    }
    
    public void disableAutoRefresh() {
        this.j = true;
        if (this.i != null) {
            this.i.c();
        }
    }
    
    static {
        a = NativeAdsManager.class.getSimpleName();
        b = c.ADS;
    }
    
    public interface Listener
    {
        void onAdsLoaded();
        
        void onAdError(final AdError p0);
    }
}
