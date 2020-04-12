package com.facebook.ads.internal.adapters;

import android.view.View;
import android.util.Log;
import android.net.Uri;
import com.facebook.ads.AdError;
import com.facebook.ads.internal.fpackage.e;
import com.facebook.ads.AdSize;
import com.facebook.ads.internal.util.b;
import android.content.Context;
import java.util.Map;
import com.facebook.ads.internal.view.c;

import org.json.JSONObject;

public class h extends BannerAdapter
{
    private static final String TAG;
    private c b;
    private p c;
    private BannerAdapterListener d;
    private Map<String, Object> e;
    private Context f;
    private long g;
    private b.a h;
    
    @Override
    public void loadBannerAd(final Context f, final AdSize adSize, final BannerAdapterListener d, final Map<String, Object> e) {
        this.f = f;
        this.d = d;
        this.e = e;
        this.a((e)e.get("definition"));
    }
    
    private void a(final e e) {
        this.g = 0L;
        this.h = null;
        final o a = o.a((JSONObject)this.e.get("data"));
        if (com.facebook.ads.internal.util.f.a(this.f, a)) {
            this.d.onBannerError(this, AdError.NO_FILL);
            return;
        }
        (this.b = new c(this.f, new c.b() {
            @Override
            public void a(final String s, final Map<String, String> map) {
                final Uri parse = Uri.parse(s);
                if ("fbad".equals(parse.getScheme()) && com.facebook.ads.internal.action.b.a(parse.getAuthority()) && com.facebook.ads.internal.adapters.h.this.d != null) {
                    com.facebook.ads.internal.adapters.h.this.d.onBannerAdClicked(com.facebook.ads.internal.adapters.h.this);
                }
                final com.facebook.ads.internal.action.a aa = com.facebook.ads.internal.action.b.a(com.facebook.ads.internal.adapters.h.this.f, a.B(), parse, map);
                if (aa != null) {
                    try {
                        com.facebook.ads.internal.adapters.h.this.h = aa.a();
                        com.facebook.ads.internal.adapters.h.this.g = System.currentTimeMillis();
                        a.b();
                    }
                    catch (Exception ex) {
                        Log.e(com.facebook.ads.internal.adapters.h.TAG, "Error executing action", (Throwable)ex);
                    }
                }
            }
            
            @Override
            public void a() {
                com.facebook.ads.internal.adapters.h.this.c.b();
            }
            
            @Override
            public void b() {
                if (com.facebook.ads.internal.adapters.h.this.c != null) {
                    com.facebook.ads.internal.adapters.h.this.c.a();
                }
            }
            
            @Override
            public void a(final int n) {
                if (n == 0 && com.facebook.ads.internal.adapters.h.this.g > 0L && com.facebook.ads.internal.adapters.h.this.h != null) {
                    com.facebook.ads.internal.util.c.a(com.facebook.ads.internal.util.b.a(com.facebook.ads.internal.adapters.h.this.g, com.facebook.ads.internal.adapters.h.this.h, a.d()));
                    com.facebook.ads.internal.adapters.h.this.g = 0L;
                    com.facebook.ads.internal.adapters.h.this.h = null;
                }
            }
        }, e.e())).a(e.g(), e.h());
        (this.c = new p(this.f, this.b, this.b.getViewabilityChecker(), new com.facebook.ads.internal.adapters.b() {
            @Override
            public void d() {
                if (com.facebook.ads.internal.adapters.h.this.d != null) {
                    com.facebook.ads.internal.adapters.h.this.d.onBannerLoggingImpression(com.facebook.ads.internal.adapters.h.this);
                }
            }
        })).a(a);
        this.b.loadDataWithBaseURL(com.facebook.ads.internal.util.h.a(), a.a(), "text/html", "utf-8", (String)null);
        if (this.d != null) {
            this.d.onBannerAdLoaded(this, (View)this.b);
        }
    }
    
    @Override
    public void onDestroy() {
        if (this.b != null) {
            com.facebook.ads.internal.util.h.a(this.b);
            this.b.destroy();
            this.b = null;
        }
    }
    
    static {
        TAG = h.class.getSimpleName();
    }
}
