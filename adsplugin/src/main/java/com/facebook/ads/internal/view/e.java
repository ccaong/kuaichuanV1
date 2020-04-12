// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view;

import android.view.ViewGroup;
import android.view.View;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.widget.LinearLayout;

public class e extends LinearLayout
{
    private Bitmap a;
    private Bitmap b;
    private ImageView c;
    private ImageView d;
    private ImageView e;
    private Bitmap f;
    private Bitmap g;
    private int h;
    private int i;
    private int j;
    private int k;
    private int l;
    private int m;
    private double n;
    private double o;
    
    public void a(final Bitmap a, final Bitmap b) {
        if (b == null) {
            this.c.setImageDrawable((Drawable)null);
            this.e.setImageDrawable((Drawable)null);
        }
        if (a == null) {
            this.d.setImageDrawable((Drawable)null);
            return;
        }
        Bitmap bm = null;
        try {
            bm = Bitmap.createBitmap(a);
        } catch (OutOfMemoryError error) {
        }
        this.d.setImageBitmap(bm);
        this.a = a;
        this.b = b;
        this.a();
    }
    
    private void a() {
        if (this.getHeight() <= 0 || this.getWidth() <= 0) {
            return;
        }
        this.o = this.getMeasuredWidth() / this.getMeasuredHeight();
        this.n = this.a.getWidth() / this.a.getHeight();
        if (this.n > this.o) {
            this.b();
        }
        else {
            this.c();
        }
    }
    
    private void b() {
        this.j = this.a(this.n);
        this.k = this.getWidth();
        this.h = (int)Math.ceil((this.getHeight() - this.j) / 2.0f);
        if (this.b == null) {
            return;
        }
        final Matrix matrix = new Matrix();
        matrix.preScale(1.0f, -1.0f);
        this.i = (int)Math.floor((this.getHeight() - this.j) / 2.0f);
        final float n = this.a.getHeight() / this.j;
        final int min = Math.min(Math.round(this.h * n), this.b.getHeight());
        if (min > 0) {
            try {
                this.f = Bitmap.createBitmap(this.b, 0, 0, this.b.getWidth(), min, matrix, true);
            } catch (OutOfMemoryError error) {
            }
            this.c.setImageBitmap(this.f);
        }
        final int min2 = Math.min(Math.round(this.i * n), this.b.getHeight());
        if (min2 > 0) {
            try {
                this.g = Bitmap.createBitmap(this.b, 0, this.b.getHeight() - min2, this.b.getWidth(), min2, matrix, true);
            } catch (OutOfMemoryError error) {
            }
            this.e.setImageBitmap(this.g);
        }
    }
    
    private void c() {
        this.k = this.b(this.n);
        this.j = this.getHeight();
        this.l = (int)Math.ceil((this.getWidth() - this.k) / 2.0f);
        if (this.b == null) {
            return;
        }
        final Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        this.m = (int)Math.floor((this.getWidth() - this.k) / 2.0f);
        final float n = this.a.getWidth() / this.k;
        final int min = Math.min(Math.round(this.l * n), this.b.getWidth());
        if (min > 0) {
            try {
                this.f = Bitmap.createBitmap(this.b, 0, 0, min, this.b.getHeight(), matrix, true);
            } catch (OutOfMemoryError error) {
            }
            this.c.setImageBitmap(this.f);
        }
        final int min2 = Math.min(Math.round(this.m * n), this.b.getWidth());
        if (min2 > 0) {
            try {
                this.g = Bitmap.createBitmap(this.b, this.b.getWidth() - min2, 0, min2, this.b.getHeight(), matrix, true);
            } catch (OutOfMemoryError error) {
            }
            this.e.setImageBitmap(this.g);
        }
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        if (this.a == null) {
            super.onLayout(b, n, n2, n3, n4);
        }
        else {
            this.a(this.n);
            this.b(this.n);
            if (this.f == null || this.e()) {
                this.a();
            }
            if (this.n > this.o) {
                this.c.layout(n, n2, n3, this.h);
                this.d.layout(n, n2 + this.h, n3, this.h + this.j);
                this.e.layout(n, n2 + this.h + this.j, n3, n4);
            }
            else {
                this.c.layout(n, n2, this.l, n4);
                this.d.layout(n + this.l, n2, this.l + this.k, n4);
                this.e.layout(n + this.l + this.k, n2, n3, n4);
            }
        }
    }
    
    public e(final Context context) {
        super(context);
        this.d();
    }
    
    private void d() {
        this.getContext().getResources().getDisplayMetrics();
        this.setOrientation(1);
        (this.c = new ImageView(this.getContext())).setScaleType(ImageView.ScaleType.FIT_XY);
        this.addView((View)this.c);
        (this.d = new ImageView(this.getContext())).setLayoutParams((ViewGroup.LayoutParams)new LinearLayout.LayoutParams(-1, -1));
        this.d.setScaleType(ImageView.ScaleType.FIT_XY);
        this.addView((View)this.d);
        (this.e = new ImageView(this.getContext())).setScaleType(ImageView.ScaleType.FIT_XY);
        this.addView((View)this.e);
    }
    
    private int a(final double n) {
        return (int)Math.round(this.getWidth() / n);
    }
    
    private int b(final double n) {
        return (int)Math.round(this.getHeight() * n);
    }
    
    private boolean e() {
        return this.h + this.j + this.i != this.getMeasuredHeight() || this.l + this.k + this.m != this.getMeasuredWidth();
    }
}
