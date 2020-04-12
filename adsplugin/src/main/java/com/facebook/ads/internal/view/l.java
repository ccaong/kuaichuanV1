// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view;

import com.facebook.ads.internal.gpackage.f;
import com.facebook.ads.AudienceNetworkActivity;
import android.os.Bundle;
import android.content.Intent;
import com.facebook.ads.internal.util.g;
import java.util.Map;
import java.util.HashMap;
import android.text.TextUtils;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.graphics.drawable.GradientDrawable;
import com.facebook.ads.internal.view.dpackage.b.k;
import com.facebook.ads.internal.h;
import com.facebook.ads.internal.view.dpackage.b.m;
import com.facebook.ads.internal.j;
import com.facebook.ads.internal.gpackage.q;
import android.view.View;
import android.view.ViewGroup;
import com.facebook.ads.internal.view.dpackage.b.p;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.RelativeLayout;
import android.content.Context;
import com.facebook.ads.internal.view.dpackage.a.b;
import com.facebook.ads.internal.gpackage.s;
import com.facebook.ads.internal.util.af;
import com.facebook.ads.internal.util.ab;
import com.facebook.ads.internal.rewarded_video.a;

public class l implements d
{
    private com.facebook.ads.internal.rewarded_video.a a;
    private n b;
    private ab c;
    private af d;
    private a e;
    private s<b> f;
    private s<com.facebook.ads.internal.view.dpackage.a.d> gg;
    private s<com.facebook.ads.internal.view.dpackage.a.l> h;
    private s<com.facebook.ads.internal.view.dpackage.a.s> i;
    private String jj;
    private Context k;
    private String l;
    private RelativeLayout m;
    private TextView n;
    private TextView o;
    private ImageView p;
    private p q;
    
    public l(final Context k, final a e) {
        this.k = k;
        this.e = e;
        this.i();
    }
    
    private void i() {
        final float density = this.k.getResources().getDisplayMetrics().density;
        final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams.addRule(13);
        (this.b = new n(this.k)).h();
        this.b.setAutoplay(true);
        this.b.setIsFullScreen(true);
        this.b.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        this.b.setBackgroundColor(-16777216);
        this.i = new s<com.facebook.ads.internal.view.dpackage.a.s>() {
            @Override
            public void a(final com.facebook.ads.internal.view.dpackage.a.s s) {
                com.facebook.ads.internal.view.l.this.d.a(s.b(), (View)com.facebook.ads.internal.view.l.this.b, s.a());
            }
            
            @Override
            public Class<com.facebook.ads.internal.view.dpackage.a.s> a() {
                return com.facebook.ads.internal.view.dpackage.a.s.class;
            }
        };
        this.f = new s<b>() {
            @Override
            public void a(final b b) {
                if (com.facebook.ads.internal.view.l.this.e != null) {
                    com.facebook.ads.internal.view.l.this.e.a(com.facebook.ads.internal.j.REWARDED_VIDEO_COMPLETE.a(), b);
                }
                com.facebook.ads.internal.view.l.this.e();
            }
            
            @Override
            public Class<b> a() {
                return b.class;
            }
        };
        this.gg = new s<com.facebook.ads.internal.view.dpackage.a.d>() {
            @Override
            public void a(final com.facebook.ads.internal.view.dpackage.a.d d) {
                if (com.facebook.ads.internal.view.l.this.e != null) {
                    com.facebook.ads.internal.view.l.this.e.a(com.facebook.ads.internal.j.REWARDED_VIDEO_ERROR.a());
                }
                com.facebook.ads.internal.view.l.this.e();
            }
            
            @Override
            public Class<com.facebook.ads.internal.view.dpackage.a.d> a() {
                return com.facebook.ads.internal.view.dpackage.a.d.class;
            }
        };
        this.h = new s<com.facebook.ads.internal.view.dpackage.a.l>() {
            @Override
            public void a(final com.facebook.ads.internal.view.dpackage.a.l l) {
                if (l.this.a != null) {
                    l.this.a.a();
                }
            }
            
            @Override
            public Class<com.facebook.ads.internal.view.dpackage.a.l> a() {
                return com.facebook.ads.internal.view.dpackage.a.l.class;
            }
        };
        this.b.getEventBus().a(this.f);
        this.b.getEventBus().a(this.gg);
        this.b.getEventBus().a(this.h);
        this.b.getEventBus().a(this.i);
        this.b.a(new com.facebook.ads.internal.view.dpackage.b.j(this.k));
        this.q = new p(this.k, (int)(6.0f * density));
        final RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams2.addRule(12);
        this.q.setLayoutParams((ViewGroup.LayoutParams)layoutParams2);
        this.b.a(this.q);
        if (com.facebook.ads.internal.h.j(this.k)) {
            final k k = new k(this.k);
            this.b.a(k);
            this.b.a(new com.facebook.ads.internal.view.dpackage.b.d((View)k, com.facebook.ads.internal.view.dpackage.b.d.aType.INVISIBLE));
        }
        if (com.facebook.ads.internal.h.c(this.k)) {
            final GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] { 0, -15658735 });
            gradientDrawable.setCornerRadius(0.0f);
            final RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(-1, -2);
            layoutParams3.addRule(12);
            this.m = new RelativeLayout(this.k);
            if (Build.VERSION.SDK_INT >= 16) {
                this.m.setBackground((Drawable)gradientDrawable);
            }
            else {
                this.m.setBackgroundDrawable((Drawable)gradientDrawable);
            }
            this.m.setLayoutParams((ViewGroup.LayoutParams)layoutParams3);
            this.m.setPadding((int)(16.0f * density), 0, (int)(16.0f * density), (int)(20.0f * density));
            this.n = new TextView(this.k);
            final RelativeLayout.LayoutParams layoutParams4 = new RelativeLayout.LayoutParams(-1, -2);
            layoutParams4.addRule(12);
            layoutParams4.addRule(9);
            layoutParams4.addRule(4);
            this.n.setEllipsize(TextUtils.TruncateAt.END);
            this.n.setGravity(8388611);
            this.n.setLayoutParams((ViewGroup.LayoutParams)layoutParams4);
            this.n.setMaxLines(1);
            this.n.setPadding((int)(72.0f * density), 0, 0, (int)(30.0f * density));
            this.n.setTextColor(-1);
            this.n.setTextSize(20.0f);
            this.o = new TextView(this.k);
            final RelativeLayout.LayoutParams layoutParams5 = new RelativeLayout.LayoutParams(-1, -2);
            layoutParams5.addRule(12);
            layoutParams5.addRule(9);
            this.o.setEllipsize(TextUtils.TruncateAt.END);
            this.o.setGravity(8388611);
            this.o.setLayoutParams((ViewGroup.LayoutParams)layoutParams5);
            this.o.setMaxLines(2);
            this.o.setPadding((int)(72.0f * density), 0, 0, 0);
            this.o.setTextColor(-1);
            this.p = new ImageView(this.k);
            final RelativeLayout.LayoutParams layoutParams6 = new RelativeLayout.LayoutParams((int)(60.0f * density), (int)(60.0f * density));
            layoutParams6.addRule(12);
            layoutParams6.addRule(9);
            this.p.setLayoutParams((ViewGroup.LayoutParams)layoutParams6);
            this.m.addView((View)this.p);
            this.m.addView((View)this.n);
            this.m.addView((View)this.o);
            final com.facebook.ads.internal.view.dpackage.b.d d = new com.facebook.ads.internal.view.dpackage.b.d((View)new RelativeLayout(this.k), com.facebook.ads.internal.view.dpackage.b.d.aType.INVISIBLE);
            d.a((View)this.m, com.facebook.ads.internal.view.dpackage.b.d.aType.INVISIBLE);
            this.b.a(d);
        }
        (this.a = new com.facebook.ads.internal.rewarded_video.a((View)this.b, 1, new com.facebook.ads.internal.rewarded_video.a.listener() {
            @Override
            public void a() {
                if (!l.this.d.b()) {
                    l.this.d.a();
                    final HashMap<String, String> hashMap = new HashMap<String, String>();
                    if (!TextUtils.isEmpty((CharSequence)l.this.jj)) {
                        l.this.a.a(hashMap);
                        hashMap.put("touch", g.a(l.this.a()));
                        com.facebook.ads.internal.gpackage.g.a(l.this.k).a(l.this.jj, hashMap);
                    }
                    if (l.this.e != null) {
                        l.this.e.a(j.REWARDED_VIDEO_IMPRESSION.a());
                    }
                }
            }
        })).a(250);
        this.d = new af();
        this.e.a((View)this.b);
        if (this.m != null) {
            this.e.a((View)this.m);
        }
        this.e.a((View)this.q);
    }
    
    @Override
    public void a(final Intent intent, final Bundle bundle, final AudienceNetworkActivity audienceNetworkActivity) {
        final String stringExtra = intent.getStringExtra("videoURL");
        this.jj = intent.getStringExtra("clientToken");
        this.l = intent.getStringExtra("contextSwitchBehavior");
        if (this.n != null) {
            this.n.setText((CharSequence)intent.getStringExtra("adTitle"));
        }
        if (this.o != null) {
            this.o.setText((CharSequence)intent.getStringExtra("adSubtitle"));
        }
        if (this.p != null) {
            final String stringExtra2 = intent.getStringExtra("adIconUrl");
            if (!TextUtils.isEmpty((CharSequence)stringExtra2)) {
                new com.facebook.ads.internal.util.p(this.p).a(stringExtra2);
            }
        }
        this.c = new ab(this.k, com.facebook.ads.internal.gpackage.g.a(this.k), this.b, this.jj);
        if (!TextUtils.isEmpty((CharSequence)stringExtra)) {
            this.b.setVideoURI(stringExtra);
        }
        this.b.d();
    }
    
    @Override
    public void a(final Bundle bundle) {
    }
    
    @Override
    public void g() {
        this.c();
    }
    
    @Override
    public void h() {
        if (this.d()) {
            if (this.l.equals("restart")) {
                this.b();
            }
            else if (this.l.equals("resume")) {
                this.f();
            }
            else if (this.l.equals("skip")) {
                this.e.a(com.facebook.ads.internal.j.REWARDED_VIDEO_COMPLETE_WITHOUT_REWARD.a(), new b());
                this.e();
            }
            else if (this.l.equals("endvideo")) {
                this.e.a(com.facebook.ads.internal.j.REWARDED_VIDEO_END_ACTIVITY.a());
                final HashMap<String, String> hashMap = new HashMap<String, String>();
                if (!TextUtils.isEmpty((CharSequence)this.jj)) {
                    this.a.a(hashMap);
                    hashMap.put("touch", com.facebook.ads.internal.util.g.a(this.a()));
                    com.facebook.ads.internal.gpackage.g.a(this.k).e(this.jj, hashMap);
                }
                this.e();
            }
        }
    }
    
    @Override
    public void onDestroy() {
        this.e();
        this.n = null;
        this.o = null;
        this.p = null;
        this.m = null;
        this.l = null;
        this.f = null;
        this.gg = null;
        this.h = null;
        this.i = null;
        this.a = null;
        this.d = null;
        this.c = null;
        this.b = null;
        this.e = null;
        this.jj = null;
        this.k = null;
        this.q.a();
        this.q = null;
    }
    
    @Override
    public void a(final a a) {
    }
    
    public Map<String, String> a() {
        return this.d.e();
    }
    
    public void b() {
        this.b.a(1);
        this.b.d();
    }
    
    public void c() {
        this.b.e();
    }
    
    public boolean d() {
        return this.b.getState() == com.facebook.ads.internal.view.dpackage.c.d.PAUSED;
    }
    
    public void e() {
        if (this.b != null) {
            this.b.g();
        }
        if (this.a != null) {
            this.a.b();
        }
    }
    
    public void f() {
        this.b.a(this.b.getCurrentPosition());
        this.b.d();
    }
}
