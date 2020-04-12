// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view;

import com.facebook.ads.internal.view.dpackage.a.p;
import com.facebook.ads.internal.gpackage.f;
import com.facebook.ads.internal.gpackage.g;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.facebook.ads.internal.view.dpackage.b.m;
import android.content.Context;
import com.facebook.ads.internal.view.dpackage.a.b;
import com.facebook.ads.internal.view.dpackage.a.h;
import com.facebook.ads.internal.view.dpackage.a.j;
import com.facebook.ads.internal.gpackage.q;
import com.facebook.ads.internal.util.ab;
import com.facebook.ads.AudienceNetworkActivity;
import com.facebook.ads.internal.view.dpackage.a.c;
import com.facebook.ads.internal.view.dpackage.a.i;
import com.facebook.ads.internal.view.dpackage.a.k;
import com.facebook.ads.internal.view.dpackage.a.e;

public class r implements d
{
    private final e a;
    private final k b;
    private final i c;
    private final c d;
    private final AudienceNetworkActivity e;
    private final n f;
    private final a g;
    private ab h;
    private int i;
    
    public r(final AudienceNetworkActivity e, final a g) {
        this.a = new e() {
            @Override
            public void a(final com.facebook.ads.internal.view.dpackage.a.d d) {
                r.this.e.finish();
            }
        };
        this.b = new k() {
            @Override
            public void a(final j j) {
                r.this.g.a("videoInterstitalEvent", j);
            }
        };
        this.c = new i() {
            @Override
            public void a(final h h) {
                r.this.g.a("videoInterstitalEvent", h);
            }
        };
        this.d = new c() {
            @Override
            public void a(final b b) {
                r.this.g.a("videoInterstitalEvent", b);
            }
        };
        this.e = e;
        (this.f = new n((Context)e)).a(new com.facebook.ads.internal.view.dpackage.b.b((Context)e));
        this.f.getEventBus().a(this.b);
        this.f.getEventBus().a(this.c);
        this.f.getEventBus().a(this.d);
        this.f.getEventBus().a(this.a);
        this.g = g;
        this.f.setIsFullScreen(true);
        this.f.setVolume(1.0f);
        final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams.addRule(15);
        this.f.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        g.a((View)this.f);
    }
    
    @Override
    public void a(final Intent intent, final Bundle bundle, final AudienceNetworkActivity audienceNetworkActivity) {
        final boolean booleanExtra = intent.getBooleanExtra("autoplay", false);
        final String stringExtra = intent.getStringExtra("videoURL");
        final String stringExtra2 = intent.getStringExtra("videoMPD");
        final Bundle bundleExtra = intent.getBundleExtra("videoLogger");
        final String stringExtra3 = intent.getStringExtra("clientToken");
        this.i = intent.getIntExtra("videoSeekTime", 0);
        this.f.setAutoplay(booleanExtra);
        this.h = new ab((Context)audienceNetworkActivity, com.facebook.ads.internal.gpackage.g.a(audienceNetworkActivity.getApplicationContext()), this.f, stringExtra3, bundleExtra);
        this.f.setVideoMPD(stringExtra2);
        this.f.setVideoURI(stringExtra);
        if (this.i > 0) {
            this.f.a(this.i);
        }
        this.f.d();
    }
    
    @Override
    public void a(final Bundle bundle) {
    }
    
    @Override
    public void g() {
        this.g.a("videoInterstitalEvent", new com.facebook.ads.internal.view.dpackage.a.f());
        this.f.e();
    }
    
    @Override
    public void h() {
        this.g.a("videoInterstitalEvent", new com.facebook.ads.internal.view.dpackage.a.g());
        this.f.d();
    }
    
    @Override
    public void onDestroy() {
        this.g.a("videoInterstitalEvent", new p(this.i, this.f.getCurrentPosition()));
        this.h.b(this.f.getCurrentPosition());
        this.f.g();
    }
    
    @Override
    public void a(final a a) {
    }
    
    public void a(final View controlsAnchorView) {
        this.f.setControlsAnchorView(controlsAnchorView);
    }
}
