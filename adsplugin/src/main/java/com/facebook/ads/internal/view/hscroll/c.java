// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view.hscroll;

import android.view.ViewGroup;
import android.view.View;
import android.support.v7.widget.RecyclerView;

public class c
{
    public int[] a(final RecyclerView.Recycler recycler, final int n, final int n2, final int n3) {
        final View viewForPosition = recycler.getViewForPosition(n);
        final int[] a = this.a(viewForPosition, n2, n3);
        recycler.recycleView(viewForPosition);
        return a;
    }
    
    public int[] a(final View view, final int n, final int n2) {
        final RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)view.getLayoutParams();
        view.measure(ViewGroup.getChildMeasureSpec(n, view.getPaddingLeft() + view.getPaddingRight(), layoutParams.width), ViewGroup.getChildMeasureSpec(n2, view.getPaddingTop() + view.getPaddingBottom(), layoutParams.height));
        return new int[] { view.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin, view.getMeasuredHeight() + layoutParams.bottomMargin + layoutParams.topMargin };
    }
}
