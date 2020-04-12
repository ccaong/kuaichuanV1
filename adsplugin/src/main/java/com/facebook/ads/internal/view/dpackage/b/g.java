// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view.dpackage.b;

import com.facebook.ads.internal.gpackage.s;
import com.facebook.ads.internal.gpackage.r;
import com.facebook.ads.internal.view.dpackage.c.d;
import android.view.MotionEvent;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.facebook.ads.internal.view.dpackage.a.b;
import com.facebook.ads.internal.view.dpackage.a.j;
import com.facebook.ads.internal.view.dpackage.a.h;
import com.facebook.ads.internal.gpackage.q;
import android.util.AttributeSet;
import android.content.Context;
import com.facebook.ads.internal.view.dpackage.a.c;
import com.facebook.ads.internal.view.dpackage.a.k;
import com.facebook.ads.internal.view.dpackage.a.i;
import com.facebook.ads.internal.view.dpackage.a.m;
import android.view.View;

public class g extends n implements View.OnTouchListener
{
    private final com.facebook.ads.internal.view.dpackage.a.m b;
    private final i c;
    private final k d;
    private final c e;
    private final l f;
    
    public g(final Context context) {
        this(context, null);
    }
    
    public g(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public g(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.b = new com.facebook.ads.internal.view.dpackage.a.m() {
            @Override
            public void a(final com.facebook.ads.internal.view.dpackage.a.l l) {
                g.this.setVisibility(0);
            }
        };
        this.c = new i() {
            @Override
            public void a(final h h) {
                g.this.f.setChecked(true);
            }
        };
        this.d = new k() {
            @Override
            public void a(final j j) {
                g.this.f.setChecked(false);
            }
        };
        this.e = new c() {
            @Override
            public void a(final b b) {
                g.this.f.setChecked(true);
            }
        };
        final DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        (this.f = new l(context)).setChecked(true);
        final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int)(25.0f * displayMetrics.density), (int)(25.0f * displayMetrics.density));
        this.setVisibility(8);
        this.addView((View)this.f, (ViewGroup.LayoutParams)layoutParams);
        this.setClickable(true);
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
    }
    
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        if (motionEvent.getAction() != 1) {
            return false;
        }
        final com.facebook.ads.internal.view.n videoView = this.getVideoView();
        if (videoView == null) {
            return false;
        }
        if (videoView.getState() == com.facebook.ads.internal.view.dpackage.c.d.PREPARED || videoView.getState() == com.facebook.ads.internal.view.dpackage.c.d.PAUSED || videoView.getState() == com.facebook.ads.internal.view.dpackage.c.d.PLAYBACK_COMPLETED) {
            videoView.d();
            return true;
        }
        if (videoView.getState() == com.facebook.ads.internal.view.dpackage.c.d.STARTED) {
            videoView.e();
            return false;
        }
        return false;
    }
    
    @Override
    protected void a_(final com.facebook.ads.internal.view.n n) {
        this.f.setOnTouchListener((View.OnTouchListener)this);
        this.setOnTouchListener((View.OnTouchListener)this);
        final r<s, q> eventBus = n.getEventBus();
        eventBus.a(this.b);
        eventBus.a(this.e);
        eventBus.a(this.c);
        eventBus.a(this.d);
    }
}
