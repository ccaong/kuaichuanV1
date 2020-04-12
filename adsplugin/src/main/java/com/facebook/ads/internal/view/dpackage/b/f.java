// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view.dpackage.b;

import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.facebook.ads.internal.util.p;
import android.support.annotation.Nullable;
import com.facebook.ads.internal.gpackage.q;
import android.content.Context;
import com.facebook.ads.internal.view.dpackage.a.b;
import com.facebook.ads.internal.view.dpackage.a.j;
import com.facebook.ads.internal.gpackage.s;
import android.widget.ImageView;
import android.view.View;

public class f extends n implements View.OnLayoutChangeListener
{
    private final ImageView b;
    private final s<j> c;
    private final s<b> d;
    
    public f(final Context context) {
        super(context);
        this.c = new s<j>() {
            @Override
            public void a(final j j) {
                f.this.setVisibility(8);
            }
            
            @Override
            public Class<j> a() {
                return j.class;
            }
        };
        this.d = new s<b>() {
            @Override
            public void a(final b b) {
                f.this.setVisibility(0);
            }
            
            @Override
            public Class<b> a() {
                return b.class;
            }
        };
        (this.b = new ImageView(context)).setScaleType(ImageView.ScaleType.FIT_CENTER);
        this.b.setBackgroundColor(-16777216);
    }
    
    public void setImage(@Nullable final String s) {
        if (s == null) {
            this.setVisibility(8);
            return;
        }
        this.setVisibility(0);
        new p(this.b).a(s);
    }
    
    @Override
    protected void a_(final com.facebook.ads.internal.view.n n) {
        n.getEventBus().a(this.c);
        n.getEventBus().a(this.d);
        n.addOnLayoutChangeListener((View.OnLayoutChangeListener)this);
        super.a_(n);
    }
    
    public void onLayoutChange(final View view, final int leftMargin, final int topMargin, final int n, final int n2, final int n3, final int n4, final int n5, final int n6) {
        final RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)this.getLayoutParams();
        final int n7 = n2 - topMargin;
        final int n8 = n - leftMargin;
        if (layoutParams.height == n7 && layoutParams.width == n8 && layoutParams.topMargin == topMargin && layoutParams.leftMargin == leftMargin) {
            return;
        }
        final RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(n8, n7);
        layoutParams2.topMargin = topMargin;
        layoutParams2.leftMargin = leftMargin;
        this.b.setLayoutParams((ViewGroup.LayoutParams)new RelativeLayout.LayoutParams(n8, n7));
        if (this.b.getParent() == null) {
            this.addView((View)this.b);
        }
        this.setLayoutParams((ViewGroup.LayoutParams)layoutParams2);
    }
}
