// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view.dpackage.b;

import android.graphics.Canvas;
import com.facebook.ads.internal.view.dpackage.a.b;
import com.facebook.ads.internal.gpackage.q;
import android.content.Context;
import android.support.annotation.Nullable;
import com.facebook.ads.internal.view.n;
import com.facebook.ads.internal.view.dpackage.a.c;
import android.graphics.Rect;
import android.graphics.Paint;
import android.view.View;

public class o extends View implements m
{
    private final Paint a;
    private final Rect b;
    private float c;
    private final com.facebook.ads.internal.view.dpackage.a.o d;
    private final c e;
    @Nullable
    private n f;
    
    public o(final Context context) {
        super(context);
        this.d = new com.facebook.ads.internal.view.dpackage.a.o() {
            @Override
            public void a(final com.facebook.ads.internal.view.dpackage.a.n n) {
                if (o.this.f != null) {
                    final int duration = o.this.f.getDuration();
                    if (duration > 0) {
                        o.this.c = o.this.f.getCurrentPosition() / duration;
                    }
                    else {
                        o.this.c = 0.0f;
                    }
                    o.this.postInvalidate();
                }
            }
        };
        this.e = new c() {
            @Override
            public void a(final b b) {
                if (o.this.f != null) {
                    o.this.c = 0.0f;
                    o.this.postInvalidate();
                }
            }
        };
        (this.a = new Paint()).setStyle(Paint.Style.FILL);
        this.a.setColor(-9528840);
        this.b = new Rect();
    }
    
    public void a(final n f) {
        this.f = f;
        f.getEventBus().a(this.d);
        f.getEventBus().a(this.e);
    }
    
    public void draw(final Canvas canvas) {
        this.b.set(0, 0, (int)(this.getWidth() * this.c), this.getHeight());
        canvas.drawRect(this.b, this.a);
        super.draw(canvas);
    }
}
