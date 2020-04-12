// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view.dpackage.b;

import android.graphics.Canvas;
import com.facebook.ads.internal.view.dpackage.a.b;
import com.facebook.ads.internal.gpackage.q;
import android.content.Context;
import com.facebook.ads.internal.view.dpackage.a.c;
import com.facebook.ads.internal.view.dpackage.a.o;
import java.util.concurrent.atomic.AtomicInteger;
import com.facebook.ads.internal.view.n;
import android.graphics.RectF;
import android.graphics.Paint;
import android.view.View;

public class i extends View implements m
{
    private final Paint a;
    private final Paint b;
    private final Paint c;
    private final RectF d;
    private n e;
    private int f;
    private final AtomicInteger g;
    private final o h;
    private final c i;
    
    public i(final Context context, final int f, final int color) {
        super(context);
        this.g = new AtomicInteger(0);
        this.h = new o() {
            @Override
            public void a(final com.facebook.ads.internal.view.dpackage.a.n n) {
                com.facebook.ads.internal.view.dpackage.b.i.this.g.set(com.facebook.ads.internal.view.dpackage.b.i.this.f * 1000 - com.facebook.ads.internal.view.dpackage.b.i.this.e.getCurrentPosition());
                com.facebook.ads.internal.view.dpackage.b.i.this.postInvalidate();
            }
        };
        this.i = new c() {
            @Override
            public void a(final b b) {
                com.facebook.ads.internal.view.dpackage.b.i.this.f = 0;
            }
        };
        final float density = this.getResources().getDisplayMetrics().density;
        this.f = f;
        (this.a = new Paint()).setStyle(Paint.Style.FILL);
        this.a.setColor(color);
        (this.b = new Paint()).setColor(-3355444);
        this.b.setStyle(Paint.Style.FILL);
        this.b.setStrokeWidth(1.0f * density);
        this.b.setAntiAlias(true);
        (this.c = new Paint()).setColor(-10066330);
        this.c.setStyle(Paint.Style.STROKE);
        this.c.setStrokeWidth(2.0f * density);
        this.c.setAntiAlias(true);
        this.d = new RectF();
    }
    
    public void a(final n e) {
        this.e = e;
        this.e.getEventBus().a(this.h);
        this.e.getEventBus().a(this.i);
    }
    
    protected void onDraw(final Canvas canvas) {
        final int min = Math.min(this.getWidth() - this.getPaddingLeft() - this.getPaddingRight(), this.getHeight() - this.getPaddingTop() - this.getPaddingBottom());
        final int n = min / 2;
        canvas.drawCircle((float)(n + this.getPaddingLeft()), (float)(min / 2 + this.getPaddingTop()), (float)n, this.b);
        if (this.g.get() <= 0) {
            final int n2 = min / 3;
            final int n3 = min / 3;
            canvas.drawLine((float)(n2 + this.getPaddingLeft()), (float)(n3 + this.getPaddingTop()), (float)(n2 * 2 + this.getPaddingLeft()), (float)(n3 * 2 + this.getPaddingTop()), this.c);
            canvas.drawLine((float)(n2 * 2 + this.getPaddingLeft()), (float)(n3 + this.getPaddingTop()), (float)(n2 + this.getPaddingLeft()), (float)(n3 * 2 + this.getPaddingTop()), this.c);
        }
        else {
            this.d.set((float)this.getPaddingLeft(), (float)this.getPaddingTop(), (float)(this.getWidth() - this.getPaddingRight()), (float)(this.getHeight() - this.getPaddingBottom()));
            canvas.drawArc(this.d, -90.0f, -(this.g.get() * 360) / (this.f * 1000), true, this.a);
        }
        super.onDraw(canvas);
    }
    
    public boolean a() {
        return this.e != null && (this.f <= 0 || this.g.get() < 0);
    }
    
    public int getSkipSeconds() {
        return this.f;
    }
}
