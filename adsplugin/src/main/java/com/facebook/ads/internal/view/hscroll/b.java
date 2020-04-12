// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view.hscroll;

import android.view.View;
import android.support.v7.widget.RecyclerView;
import android.content.Context;

public class b extends d implements d.a
{
    private HScrollLinearLayoutManager c;
    private a d;
    private int e;
    private int f;
    private int g;
    private int h;
    
    public b(final Context context) {
        super(context);
        this.e = -1;
        this.f = -1;
        this.g = 0;
        this.h = 0;
        this.a();
    }
    
    private void a() {
        (this.c = new HScrollLinearLayoutManager(this.getContext(), new c(), new com.facebook.ads.internal.view.hscroll.a())).setOrientation(0);
        this.setLayoutManager((RecyclerView.LayoutManager)this.c);
        this.setSnapDelegate(this);
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(n, n2);
        int n3 = Math.round(this.getMeasuredWidth() / 1.91f);
        switch (View.MeasureSpec.getMode(n2)) {
            case 1073741824: {
                n3 = View.MeasureSpec.getSize(n2);
                break;
            }
            case Integer.MIN_VALUE: {
                n3 = Math.min(View.MeasureSpec.getSize(n2), n3);
                break;
            }
        }
        final int n4 = this.getPaddingTop() + this.getPaddingBottom();
        final int b = this.b(n3 - n4);
        this.setMeasuredDimension(this.getMeasuredWidth(), b + n4);
        this.setChildWidth(b + this.h * 2);
    }
    
    private int b(final int n) {
        final int n2 = this.h * 2;
        final int n3 = this.getMeasuredWidth() - this.getPaddingLeft() - n2;
        final int itemCount = this.getAdapter().getItemCount();
        int n4;
        int i;
        for (n4 = 0, i = Integer.MAX_VALUE; i > n; i = (int)((n3 - n4 * n2) / (n4 + 0.333f))) {
            if (++n4 >= itemCount) {
                return n;
            }
        }
        return i;
    }
    
    public void setAdapter(final RecyclerView.Adapter adapter) {
        this.c.a((adapter == null) ? -1 : adapter.hashCode());
        super.setAdapter(adapter);
    }
    
    public void setChildWidth(final int g) {
        this.g = g;
        final int measuredWidth = this.getMeasuredWidth();
        this.c.b((measuredWidth - this.getPaddingLeft() - this.getPaddingRight() - this.g) / 2);
        this.c.a(this.g / measuredWidth);
    }
    
    public int getChildSpacing() {
        return this.h;
    }
    
    public void setChildSpacing(final int h) {
        this.h = h;
    }
    
    public void setOnPageChangedListener(final a d) {
        this.d = d;
    }
    
    public void setCurrentPosition(final int n) {
        this.a(n, false);
    }
    
    @Override
    protected void a(final int n, final boolean b) {
        super.a(n, b);
        this.a(n, 0);
    }
    
    @Override
    public int a(final int n) {
        final int abs = Math.abs(n);
        if (abs <= this.b) {
            return 0;
        }
        return (this.g == 0) ? 1 : (abs / this.g + 1);
    }
    
    private void a(final int e, final int f) {
        if (e == this.e && f == this.f) {
            return;
        }
        this.e = e;
        this.f = f;
        if (this.d != null) {
            this.d.a(this.e, this.f);
        }
    }
    
    public interface a
    {
        void a(final int p0, final int p1);
    }
}
