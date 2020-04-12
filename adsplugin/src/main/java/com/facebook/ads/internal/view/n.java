// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view;

import android.view.TextureView;
import java.util.Iterator;
import android.net.Uri;
import android.view.ViewGroup;
import com.facebook.ads.internal.view.dpackage.c.a;
import android.view.MotionEvent;
import java.util.ArrayList;
import android.util.AttributeSet;
import android.support.annotation.Nullable;
import android.content.Context;
import android.support.annotation.NonNull;
import com.facebook.ads.internal.view.dpackage.a.p;
import android.view.View;
import android.os.Handler;
import com.facebook.ads.internal.view.dpackage.a.v;
import com.facebook.ads.internal.view.dpackage.a.w;
import com.facebook.ads.internal.view.dpackage.a.t;
import com.facebook.ads.internal.view.dpackage.a.j;
import com.facebook.ads.internal.view.dpackage.a.h;
import com.facebook.ads.internal.view.dpackage.a.b;
import com.facebook.ads.internal.view.dpackage.a.d;
import com.facebook.ads.internal.view.dpackage.a.l;
import com.facebook.ads.internal.util.ai;
import com.facebook.ads.internal.view.dpackage.b.m;
import java.util.List;
import com.facebook.ads.internal.gpackage.q;
import com.facebook.ads.internal.gpackage.s;
import com.facebook.ads.internal.gpackage.r;
import com.facebook.ads.internal.view.dpackage.c.c;
import com.facebook.ads.internal.view.dpackage.c.e;
import com.facebook.ads.internal.util.ah;
import android.widget.RelativeLayout;

public class n extends RelativeLayout implements ah.a, e
{
    protected final c b;
    private final r<s, q> a;
    private final List<m> c;
    private boolean d;
    @Deprecated
    private boolean e;
    @Deprecated
    private boolean f;
    private ai g;
    private boolean h;
    private static final l i;
    private static final d j;
    private static final com.facebook.ads.internal.view.dpackage.a.b k;
    private static final com.facebook.ads.internal.view.dpackage.a.n l;
    private static final com.facebook.ads.internal.view.dpackage.a.q m;
    private static final h n;
    private static final com.facebook.ads.internal.view.dpackage.a.r o;
    private static final j p;
    private static final t q;
    private static final w r;
    private static final v s;
    private final Handler t;
    private final View.OnTouchListener u;
    
    public void a(final com.facebook.ads.internal.view.dpackage.c.d d) {
        if (d == com.facebook.ads.internal.view.dpackage.c.d.PREPARED) {
            this.a.a(com.facebook.ads.internal.view.n.i);
            if (this.i() && !this.d) {
                this.d();
            }
        }
        else if (d == com.facebook.ads.internal.view.dpackage.c.d.ERROR) {
            this.d = true;
            this.a.a(com.facebook.ads.internal.view.n.j);
        }
        else if (d == com.facebook.ads.internal.view.dpackage.c.d.PLAYBACK_COMPLETED) {
            this.d = true;
            this.t.removeCallbacksAndMessages((Object)null);
            this.a.a(com.facebook.ads.internal.view.n.k);
        }
        else if (d == com.facebook.ads.internal.view.dpackage.c.d.STARTED) {
            this.a.a(com.facebook.ads.internal.view.n.p);
            this.t.removeCallbacksAndMessages((Object)null);
            this.t.postDelayed((Runnable)new Runnable() {
                @Override
                public void run() {
                    if (!com.facebook.ads.internal.view.n.this.d) {
                        com.facebook.ads.internal.view.n.this.a.a(com.facebook.ads.internal.view.n.l);
                        com.facebook.ads.internal.view.n.this.t.postDelayed((Runnable)this, 250L);
                    }
                }
            }, 250L);
        }
        else if (d == com.facebook.ads.internal.view.dpackage.c.d.PAUSED) {
            this.a.a(com.facebook.ads.internal.view.n.n);
            this.t.removeCallbacksAndMessages((Object)null);
        }
        else if (d == com.facebook.ads.internal.view.dpackage.c.d.IDLE) {
            this.a.a(com.facebook.ads.internal.view.n.o);
            this.t.removeCallbacksAndMessages((Object)null);
        }
    }
    
    public void a(final int n, final int n2) {
        this.a.a(new p(n, n2));
    }
    
    @NonNull
    public r<s, q> getEventBus() {
        return this.a;
    }
    
    public n(@Nullable final Context context) {
        this(context, null);
    }
    
    public n(@Nullable final Context context, @Nullable final AttributeSet set) {
        this(context, set, 0);
    }
    
    public n(@Nullable final Context context, @Nullable final AttributeSet set, final int n) {
        super(context, set, n);
        this.c = new ArrayList<m>();
        this.d = false;
        this.e = false;
        this.f = false;
        this.g = ai.UNKNOWN;
        this.h = false;
        this.u = (View.OnTouchListener)new View.OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                com.facebook.ads.internal.view.n.this.a.a(new com.facebook.ads.internal.view.dpackage.a.s(view, motionEvent));
                return true;
            }
        };
        if (com.facebook.ads.internal.h.a(this.getContext())) {
            this.b = new com.facebook.ads.internal.view.dpackage.c.a(this.getContext());
        }
        else {
            this.b = new com.facebook.ads.internal.view.dpackage.c.b(this.getContext());
        }
        this.b.setRequestedVolume(1.0f);
        this.b.setVideoStateChangeListener(this);
        final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams.addRule(13);
        this.addView((View)this.b, (ViewGroup.LayoutParams)layoutParams);
        this.t = new Handler();
        this.a = new r<s, q>();
        this.setOnTouchListener(this.u);
    }
    
    public void setVideoURI(final String s) {
        this.setVideoURI(Uri.parse(s));
    }
    
    public void setVideoURI(final Uri uri) {
        for (final m m : this.c) {
            if (m instanceof com.facebook.ads.internal.view.dpackage.b.n) {
                final com.facebook.ads.internal.view.dpackage.b.n n = (com.facebook.ads.internal.view.dpackage.b.n)m;
                if (n.getParent() != null) {
                    continue;
                }
                this.addView((View)n);
                n.a(this);
            }
            else {
                m.a(this);
            }
        }
        this.d = false;
        this.b.setup(uri);
    }
    
    public void setVideoMPD(final String videoMPD) {
        this.b.setVideoMPD(videoMPD);
    }
    
    public boolean a() {
        return this.i();
    }
    
    @Deprecated
    public void setAutoplay(final boolean e) {
        this.e = e;
    }
    
    @Deprecated
    public void setIsAutoplayOnMobile(final boolean f) {
        this.f = f;
    }
    
    public ai getIsAutoPlayFromServer() {
        return this.g;
    }
    
    public void setIsAutoPlayFromServer(final ai g) {
        this.g = g;
    }
    
    public void d() {
        if (this.d && this.b.getState() == com.facebook.ads.internal.view.dpackage.c.d.PLAYBACK_COMPLETED) {
            this.d = false;
        }
        this.b.start();
    }
    
    public void e() {
        this.b.pause();
    }
    
    public void f() {
        this.getEventBus().a(com.facebook.ads.internal.view.n.m);
        this.b.a();
    }
    
    public int getCurrentPosition() {
        return this.b.getCurrentPosition();
    }
    
    public long getInitialBufferTime() {
        return this.b.getInitialBufferTime();
    }
    
    public void a(final m m) {
        this.c.add(m);
    }
    
    public int getDuration() {
        return this.b.getDuration();
    }
    
    public com.facebook.ads.internal.view.dpackage.c.d getState() {
        return this.b.getState();
    }
    
    public void g() {
        this.b.b();
    }
    
    public boolean b() {
        return com.facebook.ads.internal.h.a(this.getContext());
    }
    
    public TextureView getTextureView() {
        return (TextureView)this.b;
    }
    
    public void setVolume(final float requestedVolume) {
        this.b.setRequestedVolume(requestedVolume);
        this.getEventBus().a(com.facebook.ads.internal.view.n.q);
    }
    
    public float getVolume() {
        return this.b.getVolume();
    }
    
    public void setIsFullScreen(final boolean b) {
        this.h = b;
        this.b.setFullScreen(b);
    }
    
    public void h() {
        this.b.a(true);
    }
    
    public boolean c() {
        return this.h;
    }
    
    public void a(final int n) {
        this.b.seekTo(n);
    }
    
    public void setControlsAnchorView(final View controlsAnchorView) {
        if (this.b != null) {
            this.b.setControlsAnchorView(controlsAnchorView);
        }
    }
    
    public View getVideoView() {
        return this.b.getView();
    }
    
    protected boolean i() {
        final boolean b = this.e && (!this.f || com.facebook.ads.internal.util.w.c(this.getContext()) == com.facebook.ads.internal.util.w.a.MOBILE_INTERNET);
        return (this.getIsAutoPlayFromServer() == ai.UNKNOWN) ? b : (this.getIsAutoPlayFromServer() == ai.ON);
    }
    
    protected void onAttachedToWindow() {
        this.a.a(com.facebook.ads.internal.view.n.s);
        super.onAttachedToWindow();
    }
    
    protected void onDetachedFromWindow() {
        this.a.a(com.facebook.ads.internal.view.n.r);
        super.onDetachedFromWindow();
    }
    
    public void setLayoutParams(final ViewGroup.LayoutParams layoutParams) {
        super.setLayoutParams(layoutParams);
    }
    
    public int getVideoWidth() {
        return this.b.getVideoWidth();
    }
    
    public int getVideoHeight() {
        return this.b.getVideoHeight();
    }
    
    static {
        i = new l();
        j = new d();
        k = new com.facebook.ads.internal.view.dpackage.a.b();
        l = new com.facebook.ads.internal.view.dpackage.a.n();
        m = new com.facebook.ads.internal.view.dpackage.a.q();
        n = new h();
        o = new com.facebook.ads.internal.view.dpackage.a.r();
        p = new j();
        q = new t();
        r = new w();
        s = new v();
    }
}
