// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view;

import android.webkit.WebView;
import com.facebook.ads.internal.util.h;
import com.facebook.ads.internal.util.k;
import com.facebook.ads.internal.gpackage.g;
import android.os.Bundle;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.view.View;
import android.content.Context;
import com.facebook.ads.internal.view.apackage.b;
import com.facebook.ads.internal.view.apackage.a;
import com.facebook.ads.AudienceNetworkActivity;
import android.annotation.TargetApi;

@TargetApi(19)
public class f implements d
{
    private static final String a;
    private final AudienceNetworkActivity b;
    private final com.facebook.ads.internal.view.apackage.a c;
    private final com.facebook.ads.internal.view.apackage.d d;
    private final b e;
    private final AudienceNetworkActivity.BackButtonInterceptor f;
    private String g;
    private String h;
    private long i;
    private boolean j;
    private long k;
    private boolean l;
    
    public f(final AudienceNetworkActivity b, final a a) {
        this.f = new AudienceNetworkActivity.BackButtonInterceptor() {
            @Override
            public boolean interceptBackButton() {
                if (f.this.d.canGoBack()) {
                    f.this.d.goBack();
                    return true;
                }
                return false;
            }
        };
        this.j = true;
        this.k = -1L;
        this.l = true;
        this.b = b;
        final int n = (int)(2.0f * b.getResources().getDisplayMetrics().density);
        (this.c = new com.facebook.ads.internal.view.apackage.a((Context)b)).setId(View.generateViewId());
        final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams.addRule(10);
        this.c.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        this.c.setListener(new com.facebook.ads.internal.view.apackage.a.aListner() {
            @Override
            public void a() {
                b.finish();
            }
        });
        a.a((View)this.c);
        this.d = new com.facebook.ads.internal.view.apackage.d((Context)b);
        final RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams2.addRule(3, this.c.getId());
        layoutParams2.addRule(12);
        this.d.setLayoutParams((ViewGroup.LayoutParams)layoutParams2);
        this.d.setListener(new com.facebook.ads.internal.view.apackage.d.a() {
            @Override
            public void a(final String url) {
                f.this.j = true;
                f.this.c.setUrl(url);
            }
            
            @Override
            public void b(final String title) {
                f.this.c.setTitle(title);
            }
            
            @Override
            public void a(final int progress) {
                if (f.this.j) {
                    f.this.e.setProgress(progress);
                }
            }
            
            @Override
            public void c(final String s) {
                f.this.e.setProgress(100);
                f.this.j = false;
            }
        });
        a.a((View)this.d);
        this.e = new b((Context)b, null, 16842872);
        final RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(-1, n);
        layoutParams3.addRule(3, this.c.getId());
        this.e.setLayoutParams((ViewGroup.LayoutParams)layoutParams3);
        this.e.setProgress(0);
        a.a((View)this.e);
        b.addBackButtonInterceptor(this.f);
    }
    
    @Override
    public void a(final Intent intent, final Bundle bundle, final AudienceNetworkActivity audienceNetworkActivity) {
        if (this.k < 0L) {
            this.k = System.currentTimeMillis();
        }
        if (bundle == null) {
            this.g = intent.getStringExtra("browserURL");
            this.h = intent.getStringExtra("clientToken");
            this.i = intent.getLongExtra("handlerTime", -1L);
        }
        else {
            this.g = bundle.getString("browserURL");
            this.h = bundle.getString("clientToken");
            this.i = bundle.getLong("handlerTime", -1L);
        }
        final String url = (this.g != null) ? this.g : "about:blank";
        this.c.setUrl(url);
        this.d.loadUrl(url);
    }
    
    @Override
    public void a(final Bundle bundle) {
        bundle.putString("browserURL", this.g);
    }
    
    @Override
    public void g() {
        this.d.onPause();
        if (this.l) {
            this.l = false;
            com.facebook.ads.internal.gpackage.g.a((Context)this.b).a(this.h, new k.a(this.d.getFirstUrl()).a(this.i).b(this.k).c(this.d.getResponseEndMs()).d(this.d.getDomContentLoadedMs()).e(this.d.getScrollReadyMs()).f(this.d.getLoadFinishMs()).g(System.currentTimeMillis()).a());
        }
    }
    
    @Override
    public void h() {
        this.d.onResume();
    }
    
    @Override
    public void onDestroy() {
        this.b.removeBackButtonInterceptor(this.f);
        com.facebook.ads.internal.util.h.a(this.d);
        this.d.destroy();
    }
    
    @Override
    public void a(final a a) {
    }
    
    static {
        a = f.class.getSimpleName();
    }
}
