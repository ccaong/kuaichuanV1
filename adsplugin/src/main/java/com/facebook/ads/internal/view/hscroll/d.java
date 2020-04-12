// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view.hscroll;

import android.view.MotionEvent;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.support.v7.widget.RecyclerView;

public class d extends RecyclerView implements View.OnTouchListener
{
    protected int a;
    protected int b;
    private int c;
    private boolean d;
    private boolean e;
    private LinearLayoutManager f;
    private a g;
    
    public d(final Context context) {
        super(context);
        this.a = 0;
        this.c = 0;
        this.d = true;
        this.e = false;
        this.a(context);
    }
    
    private void a(final Context context) {
        this.setOnTouchListener((View.OnTouchListener)this);
        this.b = (int)context.getResources().getDisplayMetrics().density * 10;
    }
    
    public void setLayoutManager(final RecyclerView.LayoutManager layoutManager) {
        if (!(layoutManager instanceof LinearLayoutManager)) {
            throw new IllegalArgumentException("SnapRecyclerView only supports LinearLayoutManager");
        }
        super.setLayoutManager(layoutManager);
        this.f = (LinearLayoutManager)layoutManager;
    }
    
    public void setSnapDelegate(final a g) {
        this.g = g;
    }
    
    protected void a(final int a, final boolean b) {
        if (this.getAdapter() == null) {
            return;
        }
        this.a = a;
        if (b) {
            this.smoothScrollToPosition(a);
        }
        else {
            this.scrollToPosition(a);
        }
    }
    
    public int getCurrentPosition() {
        return this.a;
    }
    
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        final int c = (int)motionEvent.getRawX();
        final int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 1 || actionMasked == 6 || actionMasked == 3 || actionMasked == 4) {
            if (this.e) {
                this.a(this.a(c), true);
            }
            this.d = true;
            this.e = false;
            return true;
        }
        if (actionMasked == 0 || actionMasked == 5 || (this.d && actionMasked == 2)) {
            this.c = c;
            if (this.d) {
                this.d = false;
            }
            this.e = true;
        }
        return false;
    }
    
    private int a(final int n) {
        final int n2 = this.c - n;
        final int a = this.g.a(n2);
        if (n2 > this.b) {
            return this.a(this.a, a);
        }
        if (n2 < -this.b) {
            return this.b(this.a, a);
        }
        return this.a;
    }
    
    private int getItemCount() {
        return (this.getAdapter() == null) ? 0 : this.getAdapter().getItemCount();
    }
    
    private int a(final int n, final int n2) {
        return Math.min(n + n2, this.getItemCount() - 1);
    }
    
    private int b(final int n, final int n2) {
        return Math.max(n - n2, 0);
    }
    
    public interface a
    {
        int a(final int p0);
    }
}
