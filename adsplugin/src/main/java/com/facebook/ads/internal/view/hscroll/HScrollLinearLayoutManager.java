// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view.hscroll;

import android.util.DisplayMetrics;
import android.graphics.PointF;
import android.support.v7.widget.LinearSmoothScroller;
import android.view.View;
import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

public class HScrollLinearLayoutManager extends LinearLayoutManager
{
    private final c a;
    private final com.facebook.ads.internal.view.hscroll.a b;
    private final Context c;
    private int[] d;
    private int e;
    private float f;
    private a g;
    private int h;
    
    public HScrollLinearLayoutManager(final Context c, final c a, final com.facebook.ads.internal.view.hscroll.a b) {
        super(c);
        this.e = 0;
        this.f = 50.0f;
        this.c = c;
        this.a = a;
        this.b = b;
        this.h = -1;
        this.g = new a(this.c);
    }
    
    public void onMeasure(final RecyclerView.Recycler recycler, final RecyclerView.State state, final int n, final int n2) {
        final int mode = View.MeasureSpec.getMode(n);
        final int mode2 = View.MeasureSpec.getMode(n2);
        if ((mode == 1073741824 && this.getOrientation() == 1) || (mode2 == 1073741824 && this.getOrientation() == 0)) {
            super.onMeasure(recycler, state, n, n2);
            return;
        }
        final int size = View.MeasureSpec.getSize(n);
        final int size2 = View.MeasureSpec.getSize(n2);
        int[] a;
        if (this.b.b(this.h)) {
            a = this.b.a(this.h);
        }
        else {
            a = new int[] { 0, 0 };
            if (state.getItemCount() >= 1) {
                for (int n3 = 1, i = 0; i < n3; ++i) {
                    this.d = this.a.a(recycler, i, View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
                    if (this.getOrientation() == 0) {
                        final int[] array = a;
                        final int n4 = 0;
                        array[n4] += this.d[0];
                        if (i == 0) {
                            a[1] = this.d[1] + this.getPaddingTop() + this.getPaddingBottom();
                        }
                    }
                    else {
                        final int[] array2 = a;
                        final int n5 = 1;
                        array2[n5] += this.d[1];
                        if (i == 0) {
                            a[0] = this.d[0] + this.getPaddingLeft() + this.getPaddingRight();
                        }
                    }
                }
                if (this.h != -1) {
                    this.b.a(this.h, a);
                }
            }
        }
        if (mode == 1073741824) {
            a[0] = size;
        }
        if (mode2 == 1073741824) {
            a[1] = size2;
        }
        this.setMeasuredDimension(a[0], a[1]);
    }
    
    void a(final int h) {
        this.h = h;
    }
    
    public void scrollToPosition(final int n) {
        super.scrollToPositionWithOffset(n, this.e);
    }
    
    public void smoothScrollToPosition(final RecyclerView recyclerView, final RecyclerView.State state, final int targetPosition) {
        this.g.setTargetPosition(targetPosition);
        this.startSmoothScroll((RecyclerView.SmoothScroller)this.g);
    }
    
    public void b(final int e) {
        this.e = e;
    }
    
    public void a(final double n) {
        this.f = (float)(50.0 / ((n <= 0.0) ? 1.0 : n));
        this.g = new a(this.c);
    }
    
    private class a extends LinearSmoothScroller
    {
        public a(final Context context) {
            super(context);
        }
        
        public PointF computeScrollVectorForPosition(final int n) {
            return HScrollLinearLayoutManager.this.computeScrollVectorForPosition(n);
        }
        
        protected float calculateSpeedPerPixel(final DisplayMetrics displayMetrics) {
            return HScrollLinearLayoutManager.this.f / displayMetrics.densityDpi;
        }
        
        protected int getHorizontalSnapPreference() {
            return -1;
        }
        
        public int calculateDxToMakeVisible(final View view, final int n) {
            final RecyclerView.LayoutManager layoutManager = this.getLayoutManager();
            if (!layoutManager.canScrollHorizontally()) {
                return 0;
            }
            final RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)view.getLayoutParams();
            return this.calculateDtToFit(layoutManager.getDecoratedLeft(view) - layoutParams.leftMargin, layoutManager.getDecoratedRight(view) + layoutParams.rightMargin, layoutManager.getPaddingLeft(), layoutManager.getWidth() - layoutManager.getPaddingRight(), n) + HScrollLinearLayoutManager.this.e;
        }
    }
}
