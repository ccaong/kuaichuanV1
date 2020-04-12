// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view.dpackage.b;

import android.os.Build;
import android.graphics.Canvas;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Path;
import android.widget.Button;

public class l extends Button
{
    private final Path a;
    private final Path b;
    private final Paint c;
    private final Path d;
    private boolean e;
    
    public l(final Context context) {
        super(context);
        this.e = false;
        this.a = new Path();
        this.b = new Path();
        this.d = new Path();
        this.c = new Paint() {
            {
                this.setStyle(Paint.Style.FILL_AND_STROKE);
                this.setStrokeCap(Paint.Cap.ROUND);
                this.setStrokeWidth(3.0f);
                this.setAntiAlias(true);
                this.setColor(-1);
            }
        };
        this.setClickable(true);
        this.setBackgroundColor(0);
    }
    
    public void setChecked(final boolean e) {
        this.e = e;
        this.refreshDrawableState();
        this.invalidate();
    }
    
    protected void onDraw(final Canvas canvas) {
        if (canvas.isHardwareAccelerated() && Build.VERSION.SDK_INT < 17) {
            this.setLayerType(1, (Paint)null);
        }
        final float n = Math.max(canvas.getWidth(), canvas.getHeight()) / 100.0f;
        if (this.e) {
            this.d.rewind();
            this.d.moveTo(26.5f * n, 15.5f * n);
            this.d.lineTo(26.5f * n, 84.5f * n);
            this.d.lineTo(82.5f * n, 50.5f * n);
            this.d.lineTo(26.5f * n, 15.5f * n);
            this.d.close();
            canvas.drawPath(this.d, this.c);
        }
        else {
            this.a.rewind();
            this.a.moveTo(29.0f * n, 21.0f * n);
            this.a.lineTo(29.0f * n, 79.0f * n);
            this.a.lineTo(45.0f * n, 79.0f * n);
            this.a.lineTo(45.0f * n, 21.0f * n);
            this.a.lineTo(29.0f * n, 21.0f * n);
            this.a.close();
            this.b.rewind();
            this.b.moveTo(55.0f * n, 21.0f * n);
            this.b.lineTo(55.0f * n, 79.0f * n);
            this.b.lineTo(71.0f * n, 79.0f * n);
            this.b.lineTo(71.0f * n, 21.0f * n);
            this.b.lineTo(55.0f * n, 21.0f * n);
            this.b.close();
            canvas.drawPath(this.a, this.c);
            canvas.drawPath(this.b, this.c);
        }
        super.onDraw(canvas);
    }
}
