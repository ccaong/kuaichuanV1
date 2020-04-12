// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view.apackage;

import android.graphics.Color;
import android.graphics.Canvas;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.annotation.TargetApi;
import android.widget.ProgressBar;

@TargetApi(19)
public class b extends ProgressBar
{
    private static final int a;
    private static final int b;
    private Rect c;
    private Paint d;
    
    public b(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.a();
    }
    
    private void a() {
        this.setIndeterminate(false);
        this.setMax(100);
        this.setProgressDrawable(this.b());
        this.c = new Rect();
        (this.d = new Paint()).setStyle(Paint.Style.FILL);
        this.d.setColor(com.facebook.ads.internal.view.apackage.b.a);
    }
    
    private Drawable b() {
        return (Drawable)new LayerDrawable(new Drawable[] { new ColorDrawable(0), new ClipDrawable((Drawable)new ColorDrawable(com.facebook.ads.internal.view.apackage.b.b), 3, 1) });
    }
    
    protected synchronized void onDraw(final Canvas canvas) {
        canvas.drawRect(this.c, this.d);
        super.onDraw(canvas);
    }
    
    protected synchronized void onMeasure(final int n, final int n2) {
        super.onMeasure(n, n2);
        this.c.set(0, 0, this.getMeasuredWidth(), 2);
    }
    
    public synchronized void setProgress(final int n) {
        super.setProgress((n == 100) ? 0 : Math.max(n, 5));
    }
    
    static {
        a = Color.argb(26, 0, 0, 0);
        b = Color.rgb(88, 144, 255);
    }
}
