// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view.dpackage.b;

import android.view.ViewGroup;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.RelativeLayout;

public abstract class n extends RelativeLayout implements m
{
    private com.facebook.ads.internal.view.n b;
    static final /* synthetic */ boolean a;
    
    public n(final Context context) {
        super(context);
    }
    
    public n(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.setLayoutParams((ViewGroup.LayoutParams)new RelativeLayout.LayoutParams(-1, -1));
    }
    
    protected com.facebook.ads.internal.view.n getVideoView() {
        if (!n.a && this.b == null) {
            throw new AssertionError();
        }
        return this.b;
    }
    
    protected void a_(final com.facebook.ads.internal.view.n n) {
    }
    
    public void a(final com.facebook.ads.internal.view.n b) {
        this.a_(this.b = b);
    }
    
    static {
        a = !n.class.desiredAssertionStatus();
    }
}
