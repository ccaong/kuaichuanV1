// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view;

import java.util.HashMap;
import android.text.TextUtils;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.util.Log;
import android.content.Context;
import android.net.Uri;
import java.util.Map;
import com.facebook.ads.AudienceNetworkActivity;
import com.facebook.ads.internal.util.b;
import com.facebook.ads.internal.adapters.o;
import com.facebook.ads.internal.adapters.p;

public class h implements d
{
    private static final String a;
    private final a b;
    private final c c;
    private final p d;
    private o e;
    private long f;
    private long g;
    private b.a h;
    
    public h(final AudienceNetworkActivity audienceNetworkActivity, final a b) {
        this.b = b;
        this.f = System.currentTimeMillis();
        (this.c = new c((Context)audienceNetworkActivity, new c.b() {
            @Override
            public void a(final String s, final Map<String, String> map) {
                final Uri parse = Uri.parse(s);
                if ("fbad".equals(parse.getScheme()) && "close".equals(parse.getAuthority())) {
                    audienceNetworkActivity.finish();
                    return;
                }
                if ("fbad".equals(parse.getScheme()) && com.facebook.ads.internal.action.b.a(parse.getAuthority())) {
                    com.facebook.ads.internal.view.h.this.b.a("com.facebook.ads.interstitial.clicked");
                }
                final com.facebook.ads.internal.action.a a = com.facebook.ads.internal.action.b.a((Context)audienceNetworkActivity, com.facebook.ads.internal.view.h.this.e.B(), parse, map);
                if (a != null) {
                    try {
                        com.facebook.ads.internal.view.h.this.h = a.a();
                        com.facebook.ads.internal.view.h.this.g = System.currentTimeMillis();
                        a.b();
                    }
                    catch (Exception ex) {
                        Log.e(com.facebook.ads.internal.view.h.a, "Error executing action", (Throwable)ex);
                    }
                }
            }
            
            @Override
            public void a() {
                com.facebook.ads.internal.view.h.this.d.b();
            }
            
            @Override
            public void b() {
                com.facebook.ads.internal.view.h.this.d.a();
            }
            
            @Override
            public void a(final int n) {
            }
        }, 1)).setLayoutParams((ViewGroup.LayoutParams)new RelativeLayout.LayoutParams(-1, -1));
        this.d = new p((Context)audienceNetworkActivity, this.c, this.c.getViewabilityChecker(), new com.facebook.ads.internal.adapters.b() {
            @Override
            public void d() {
                com.facebook.ads.internal.view.h.this.b.a("com.facebook.ads.interstitial.impression.logged");
            }
        });
        b.a((View)this.c);
    }
    
    @Override
    public void a(final Intent intent, final Bundle bundle, final AudienceNetworkActivity audienceNetworkActivity) {
        if (bundle != null && bundle.containsKey("dataModel")) {
            this.e = o.a(bundle.getBundle("dataModel"));
            if (this.e != null) {
                this.c.loadDataWithBaseURL(com.facebook.ads.internal.util.h.a(), this.e.a(), "text/html", "utf-8", (String)null);
                this.c.a(this.e.e(), this.e.f());
            }
            return;
        }
        this.e = o.b(intent);
        if (this.e != null) {
            this.d.a(this.e);
            this.c.loadDataWithBaseURL(com.facebook.ads.internal.util.h.a(), this.e.a(), "text/html", "utf-8", (String)null);
            this.c.a(this.e.e(), this.e.f());
        }
    }
    
    @Override
    public void a(final Bundle bundle) {
        if (this.e != null) {
            bundle.putBundle("dataModel", this.e.g());
        }
    }
    
    @Override
    public void g() {
        this.c.onPause();
    }
    
    @Override
    public void h() {
        if (this.g > 0L && this.h != null && this.e != null) {
            com.facebook.ads.internal.util.c.a(com.facebook.ads.internal.util.b.a(this.g, this.h, this.e.d()));
        }
        this.c.onResume();
    }
    
    @Override
    public void onDestroy() {
        if (this.e != null) {
            com.facebook.ads.internal.util.c.a(com.facebook.ads.internal.util.b.a(this.f, com.facebook.ads.internal.util.b.a.XOUT, this.e.d()));
            if (!TextUtils.isEmpty((CharSequence)this.e.B())) {
                final HashMap<String, String> hashMap = new HashMap<String, String>();
                this.c.getViewabilityChecker().a(hashMap);
                hashMap.put("touch", com.facebook.ads.internal.util.g.a(this.c.getTouchData()));
                com.facebook.ads.internal.gpackage.g.a(this.c.getContext()).e(this.e.B(), hashMap);
            }
        }
        com.facebook.ads.internal.util.h.a(this.c);
        this.c.destroy();
    }
    
    @Override
    public void a(final a a) {
    }
    
    static {
        a = h.class.getSimpleName();
    }
}
