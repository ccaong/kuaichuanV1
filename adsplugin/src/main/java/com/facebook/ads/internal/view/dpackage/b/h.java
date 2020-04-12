// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view.dpackage.b;

import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.graphics.RectF;
import android.graphics.Paint;
import android.widget.TextView;
import android.util.Log;
import android.view.ViewGroup;
import android.view.View;
import android.widget.RelativeLayout;
import com.facebook.ads.internal.gpackage.q;
import android.content.Context;
import com.facebook.ads.internal.gpackage.s;
import java.util.concurrent.atomic.AtomicBoolean;

public class h extends n
{
    private final a b;
    private final int c;
    private final String d;
    private final String e;
    private final AtomicBoolean f;
    private final s<com.facebook.ads.internal.view.dpackage.a.n> g;
    
    public h(final Context context, final int c, final String d, final String e) {
        super(context);
        this.g = new s<com.facebook.ads.internal.view.dpackage.a.n>() {
            @Override
            public void a(final com.facebook.ads.internal.view.dpackage.a.n n) {
                if (h.this.f.get()) {
                    return;
                }
                final int n2 = h.this.c - h.this.getVideoView().getCurrentPosition() / 1000;
                if (n2 > 0) {
                    h.this.b.setText((CharSequence)(h.this.d + ' ' + n2));
                }
                else {
                    h.this.b.setText((CharSequence)h.this.e);
                    h.this.f.set(true);
                }
            }
            
            @Override
            public Class<com.facebook.ads.internal.view.dpackage.a.n> a() {
                return com.facebook.ads.internal.view.dpackage.a.n.class;
            }
        };
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = new AtomicBoolean(false);
        (this.b = new a(context)).setText((CharSequence)(this.d + ' ' + c));
        this.addView((View)this.b, (ViewGroup.LayoutParams)new RelativeLayout.LayoutParams(-2, -2));
    }
    
    public void a_(final com.facebook.ads.internal.view.n n) {
        n.getEventBus().a(this.g);
        this.b.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
                if (!h.this.f.get()) {
                    Log.i("SkipPlugin", "User clicked skip before the ads is allowed to skip.");
                    return;
                }
                n.f();
            }
        });
    }
    
    private static class a extends TextView
    {
        private final Paint a;
        private final Paint b;
        private final RectF c;
        
        public a(final Context context) {
            super(context);
            final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            this.setBackgroundColor(0);
            this.setTextColor(-3355444);
            this.setPadding((int)(9.0f * displayMetrics.density), (int)(5.0f * displayMetrics.density), (int)(9.0f * displayMetrics.density), (int)(5.0f * displayMetrics.density));
            this.setTextSize(18.0f);
            (this.a = new Paint()).setStyle(Paint.Style.STROKE);
            this.a.setColor(-10066330);
            this.a.setStrokeWidth(1.0f);
            this.a.setAntiAlias(true);
            (this.b = new Paint()).setStyle(Paint.Style.FILL);
            this.b.setColor(-1895825408);
            this.c = new RectF();
        }
        
        protected void onDraw(final Canvas canvas) {
            if (this.getText().length() == 0) {
                return;
            }
            final int n = 0;
            final int n2 = 0;
            final int width = this.getWidth();
            final int height = this.getHeight();
            this.c.set((float)n, (float)n2, (float)width, (float)height);
            canvas.drawRoundRect(this.c, 6.0f, 6.0f, this.b);
            this.c.set((float)(n + 2), (float)(n2 + 2), (float)(width - 2), (float)(height - 2));
            canvas.drawRoundRect(this.c, 6.0f, 6.0f, this.a);
            super.onDraw(canvas);
        }
    }
}
