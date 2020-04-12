// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view;

import android.content.ActivityNotFoundException;
import com.facebook.ads.InterstitialAdActivity;
import java.io.Serializable;
import com.facebook.ads.AudienceNetworkActivity;
import android.view.WindowManager;
import android.content.Intent;
import android.util.DisplayMetrics;
import com.facebook.ads.internal.view.dpackage.c.d;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.facebook.ads.internal.view.dpackage.b.g;
import java.util.UUID;
import com.facebook.ads.internal.view.dpackage.a.b;
import android.view.MotionEvent;
import android.view.View;
import com.facebook.ads.internal.view.dpackage.a.l;
import com.facebook.ads.internal.view.dpackage.a.h;
import com.facebook.ads.internal.gpackage.q;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import com.facebook.ads.internal.util.ab;
import com.facebook.ads.internal.adapters.aa;
import com.facebook.ads.internal.rewarded_video.a;
import com.facebook.ads.internal.gpackage.f;
import com.facebook.ads.internal.view.dpackage.a.c;
import com.facebook.ads.internal.view.dpackage.a.m;
import com.facebook.ads.internal.view.dpackage.a.k;

public class i extends n
{
    private final k c;
    private final com.facebook.ads.internal.view.dpackage.a.i dd;
    private final m e;
    private final c f;
    private final String g;
    private final f h;
    private final com.facebook.ads.internal.rewarded_video.a i;
    private final aa j;
    private final com.facebook.ads.internal.view.dpackage.b.f k;
    @Nullable
    private ab l;
    @Nullable
    private String m;
    @Nullable
    private Uri n;
    @Nullable
    private String o;
    @Nullable
    private String p;
    @Nullable
    private j q;
    private boolean r;
    static final /* synthetic */ boolean a;
    
    public i(final Context context, final f h) {
        super(context);
        this.c = new k() {
            @Override
            public void a(final com.facebook.ads.internal.view.dpackage.a.j j) {
                if (com.facebook.ads.internal.view.i.this.q == null) {
                    return;
                }
                com.facebook.ads.internal.view.i.this.q.c();
            }
        };
        this.dd = new com.facebook.ads.internal.view.dpackage.a.i() {
            @Override
            public void a(final h h) {
                if (i.this.q == null) {
                    return;
                }
                i.this.q.b();
            }
        };
        this.e = new m() {
            @Override
            public void a(final l l) {
                if (com.facebook.ads.internal.view.i.this.i()) {
                    com.facebook.ads.internal.view.i.this.d();
                }
                com.facebook.ads.internal.view.i.this.setOnTouchListener((View.OnTouchListener)new View.OnTouchListener() {
                    public boolean onTouch(final View view, final MotionEvent motionEvent) {
                        if (motionEvent.getAction() == 1) {
                            com.facebook.ads.internal.view.i.this.k();
                        }
                        return true;
                    }
                });
            }
        };
        this.f = new c() {
            @Override
            public void a(final com.facebook.ads.internal.view.dpackage.a.b b) {
                if (com.facebook.ads.internal.view.i.this.q == null) {
                    return;
                }
                com.facebook.ads.internal.view.i.this.q.h();
            }
        };
        this.g = UUID.randomUUID().toString();
        this.r = false;
        this.h = h;
        this.getEventBus().a(this.c);
        this.getEventBus().a(this.dd);
        this.getEventBus().a(this.f);
        this.setAutoplay(true);
        this.setVolume(0.0f);
        this.a(this.k = new com.facebook.ads.internal.view.dpackage.b.f(context));
        final DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        final g g = new g(context);
        final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(9);
        layoutParams.addRule(12);
        g.setPadding((int)(2.0f * displayMetrics.density), (int)(25.0f * displayMetrics.density), (int)(25.0f * displayMetrics.density), (int)(2.0f * displayMetrics.density));
        g.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        this.a(g);
        this.getEventBus().a(this.e);
        this.j = new aa(this, this.getContext());
        (this.i = new com.facebook.ads.internal.rewarded_video.a((View)this, 50, true, new com.facebook.ads.internal.rewarded_video.a.listener() {
            @Override
            public void a() {
                if ((i.this.i() || i.this.b.getTargetState() == d.STARTED) && i.this.b.getTargetState() != d.PAUSED) {
                    i.this.d();
                }
            }
            
            @Override
            public void b() {
                i.this.e();
            }
        })).a(0);
        this.i.b(250);
    }
    
    public void setImage(final String image) {
        this.k.setImage(image);
    }
    
    public void a(final String m, @Nullable String o) {
        if (this.l != null) {
            this.l.a();
        }
        o = ((o == null) ? "" : o);
        this.l = new ab(this.getContext(), this.h, this, o);
        this.o = o;
        this.m = m;
    }
    
    @Override
    public void setVideoURI(final Uri n) {
        if (!com.facebook.ads.internal.view.i.a && this.l == null) {
            throw new AssertionError();
        }
        super.setVideoURI(this.n = n);
    }
    
    @Override
    public void setVideoMPD(final String p) {
        if (!com.facebook.ads.internal.view.i.a && this.l == null) {
            throw new AssertionError();
        }
        super.setVideoMPD(this.p = p);
    }
    
    private void a(final Context context, final Intent intent) {
        if (!com.facebook.ads.internal.view.i.a && this.m == null) {
            throw new AssertionError();
        }
        if (!com.facebook.ads.internal.view.i.a && this.n == null && this.p == null) {
            throw new AssertionError();
        }
        ((WindowManager)context.getSystemService("window")).getDefaultDisplay().getMetrics(new DisplayMetrics());
        intent.putExtra("useNativeCloseButton", true);
        intent.putExtra("viewType", (Serializable)AudienceNetworkActivity.Type.VIDEO);
        intent.putExtra("videoURL", this.n.toString());
        intent.putExtra("clientToken", (this.o == null) ? "" : this.o);
        intent.putExtra("videoMPD", this.p);
        intent.putExtra("videoReportURL", this.m);
        intent.putExtra("predefinedOrientationKey", 13);
        intent.putExtra("autoplay", this.a());
        intent.putExtra("videoSeekTime", this.getCurrentPosition());
        intent.putExtra("uniqueId", this.g);
        intent.putExtra("videoLogger", this.l.getSaveInstanceState());
        intent.addFlags(268435456);
    }
    
    private void k() {
        final Context context = this.getContext();
        final Intent intent = new Intent(context, (Class)AudienceNetworkActivity.class);
        this.a(context, intent);
        try {
            this.e();
            this.setVisibility(8);
            context.startActivity(intent);
        }
        catch (ActivityNotFoundException ex3) {
            try {
                intent.setClass(context, (Class)InterstitialAdActivity.class);
                context.startActivity(intent);
            }
            catch (Exception ex) {
                com.facebook.ads.internal.util.c.a(com.facebook.ads.internal.util.b.a(ex, "Error occurred while loading fullscreen video activity."));
            }
        }
        catch (Exception ex2) {
            com.facebook.ads.internal.util.c.a(com.facebook.ads.internal.util.b.a(ex2, "Error occurred while loading fullscreen video activity."));
        }
    }
    
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.r = true;
        this.j.a();
        this.l();
    }
    
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.r = false;
        this.j.b();
        this.l();
    }
    
    protected void onVisibilityChanged(final View view, final int n) {
        this.l();
        super.onVisibilityChanged(view, n);
    }
    
    private void l() {
        if (this.getVisibility() == 0 && this.r) {
            this.i.a();
        }
        else {
            this.i.b();
        }
    }
    
    @Override
    public void d() {
        if (com.facebook.ads.internal.rewarded_video.a.a((View)this, 50).a()) {
            super.d();
        }
    }
    
    public void setListener(@Nullable final j q) {
        this.q = q;
    }
    
    @Nullable
    public j getListener() {
        return this.q;
    }
    
    public String getUniqueId() {
        return this.g;
    }
    
    static {
        a = !i.class.desiredAssertionStatus();
    }
}
