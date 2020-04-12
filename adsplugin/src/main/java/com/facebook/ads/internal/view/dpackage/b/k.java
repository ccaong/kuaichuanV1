// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view.dpackage.b;

import com.facebook.ads.internal.view.dpackage.c.d;
import android.view.MotionEvent;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.facebook.ads.internal.view.dpackage.a.b;
import com.facebook.ads.internal.view.dpackage.a.j;
import com.facebook.ads.internal.gpackage.q;
import com.facebook.ads.internal.view.dpackage.a.h;
import android.content.Context;
import android.graphics.RectF;
import android.graphics.Paint;
import com.facebook.ads.internal.view.dpackage.a.c;
import com.facebook.ads.internal.view.dpackage.a.i;

public class k extends n
{
    private final l b;
    private final i c;
    private final com.facebook.ads.internal.view.dpackage.a.k d;
    private final c e;
    private final Paint f;
    private final RectF g;
    
    public k(final Context context) {
        super(context);
        this.c = new i() {
            @Override
            public void a(final h h) {
                k.this.b.setChecked(true);
            }
        };
        this.d = new com.facebook.ads.internal.view.dpackage.a.k() {
            @Override
            public void a(final j j) {
                k.this.b.setChecked(false);
            }
        };
        this.e = new c() {
            @Override
            public void a(final b b) {
                k.this.b.setChecked(true);
            }
        };
        this.b = new l(context);
        final DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int)(50.0f * displayMetrics.density), (int)(50.0f * displayMetrics.density));
        layoutParams.addRule(13);
        this.b.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        this.b.setChecked(true);
        (this.f = new Paint()).setStyle(Paint.Style.FILL);
        this.f.setColor(-16777216);
        this.f.setAlpha(119);
        this.g = new RectF();
        this.setBackgroundColor(0);
        this.addView((View)this.b);
        this.setGravity(17);
        final RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams((int)(75.0 * displayMetrics.density), (int)(75.0 * displayMetrics.density));
        layoutParams2.addRule(13);
        this.setLayoutParams((ViewGroup.LayoutParams)layoutParams2);
    }
    
    protected void onDraw(final Canvas canvas) {
        final float density = this.getContext().getResources().getDisplayMetrics().density;
        this.g.set(0.0f, 0.0f, (float)this.getWidth(), (float)this.getHeight());
        canvas.drawRoundRect(this.g, 5.0f * density, 5.0f * density, this.f);
        super.onDraw(canvas);
    }
    
    @Override
    protected void a_(final com.facebook.ads.internal.view.n n) {
        n.getEventBus().a(this.c);
        n.getEventBus().a(this.d);
        n.getEventBus().a(this.e);
        this.b.setOnTouchListener((View.OnTouchListener)new View.OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    return true;
                }
                if (motionEvent.getAction() == 1) {
                    if (n.getState() == com.facebook.ads.internal.view.dpackage.c.d.PREPARED) {
                        n.d();
                    }
                    else if (n.getState() == com.facebook.ads.internal.view.dpackage.c.d.IDLE) {
                        n.d();
                    }
                    else if (n.getState() == com.facebook.ads.internal.view.dpackage.c.d.PAUSED) {
                        n.d();
                    }
                    else if (n.getState() == com.facebook.ads.internal.view.dpackage.c.d.STARTED) {
                        n.e();
                    }
                    else {
                        if (n.getState() != com.facebook.ads.internal.view.dpackage.c.d.PLAYBACK_COMPLETED) {
                            return false;
                        }
                        n.d();
                    }
                    return true;
                }
                return false;
            }
        });
        super.a_(n);
    }
}
