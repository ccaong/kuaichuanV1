// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view.dpackage.b;

import com.facebook.ads.internal.gpackage.r;
import android.animation.TimeInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.View;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.util.AttributeSet;
import com.facebook.ads.internal.view.dpackage.a.j;
import com.facebook.ads.internal.view.dpackage.a.k;
import com.facebook.ads.internal.view.dpackage.a.h;
import com.facebook.ads.internal.view.dpackage.a.i;
import com.facebook.ads.internal.gpackage.q;
import com.facebook.ads.internal.view.dpackage.a.o;
import android.content.Context;
import com.facebook.ads.internal.gpackage.s;
import com.facebook.ads.internal.view.n;
import android.widget.ProgressBar;
import java.util.concurrent.atomic.AtomicInteger;
import android.animation.ObjectAnimator;
import android.widget.RelativeLayout;

public class p extends RelativeLayout implements m
{
    private ObjectAnimator a;
    private AtomicInteger b;
    private ProgressBar c;
    private n d;
    private s e;
    private s f;
    private s g;
    
    public p(final Context context, final int n) {
        super(context);
        this.e = new o() {
            @Override
            public void a(final com.facebook.ads.internal.view.dpackage.a.n n) {
                if (p.this.d != null) {
                    p.this.a(p.this.d.getDuration(), p.this.d.getCurrentPosition());
                }
            }
        };
        this.f = new i() {
            @Override
            public void a(final h h) {
                p.this.b();
            }
        };
        this.g = new k() {
            @Override
            public void a(final j j) {
                if (p.this.d != null) {
                    p.this.a(p.this.d.getDuration(), p.this.d.getCurrentPosition());
                }
            }
        };
        this.b = new AtomicInteger(-1);
        (this.c = new ProgressBar(context, (AttributeSet)null, 16842872)).setLayoutParams((ViewGroup.LayoutParams)new RelativeLayout.LayoutParams(-1, n));
        final LayerDrawable progressDrawable = new LayerDrawable(new Drawable[] { new ColorDrawable(0), new ColorDrawable(0), new ScaleDrawable((Drawable)new ColorDrawable(-16711681), 8388611, 1.0f, -1.0f) });
        progressDrawable.setId(0, 16908288);
        progressDrawable.setId(1, 16908303);
        progressDrawable.setId(2, 16908301);
        this.c.setProgressDrawable((Drawable)progressDrawable);
        this.c.setMax(10000);
        this.addView((View)this.c);
    }
    
    private void b() {
        if (this.a != null) {
            this.a.cancel();
            this.a.setTarget((Object)null);
            this.a = null;
            this.c.clearAnimation();
        }
    }
    
    private void a(final int n, final int n2) {
        this.b();
        if (this.b.get() >= n2 || n <= n2) {
            return;
        }
        (this.a = ObjectAnimator.ofInt((Object)this.c, "progress", new int[] { n2 * 10000 / n, Math.min(n2 + 250, n) * 10000 / n })).setDuration((long)Math.min(250, n - n2));
        this.a.setInterpolator((TimeInterpolator)new LinearInterpolator());
        this.a.start();
        this.b.set(n2);
    }
    
    public void a(final n d) {
        this.d = d;
        final r<s, q> eventBus = d.getEventBus();
        eventBus.a(this.f);
        eventBus.a(this.g);
        eventBus.a(this.e);
    }
    
    public void a() {
        this.b();
        this.c = null;
        this.d = null;
    }
}
