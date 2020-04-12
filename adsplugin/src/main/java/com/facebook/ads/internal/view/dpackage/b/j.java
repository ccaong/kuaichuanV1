// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view.dpackage.b;

import android.view.ViewGroup;
import android.view.View;
import android.widget.RelativeLayout;
import android.graphics.PorterDuff;
import android.util.TypedValue;
import com.facebook.ads.internal.gpackage.q;
import android.util.AttributeSet;
import android.content.Context;
import com.facebook.ads.internal.view.dpackage.a.l;
import com.facebook.ads.internal.gpackage.s;
import android.widget.ProgressBar;

public class j extends n
{
    private final ProgressBar b;
    private final s<l> c;
    
    public j(final Context context) {
        this(context, null);
    }
    
    public j(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public j(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.c = new s<l>() {
            @Override
            public void a(final l l) {
                j.this.setVisibility(8);
            }
            
            @Override
            public Class<l> a() {
                return l.class;
            }
        };
        final int n2 = (int)TypedValue.applyDimension(1, 40.0f, this.getResources().getDisplayMetrics());
        (this.b = new ProgressBar(this.getContext())).setIndeterminate(true);
        this.b.getIndeterminateDrawable().setColorFilter(-1, PorterDuff.Mode.SRC_IN);
        final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(n2, n2);
        layoutParams.addRule(13);
        this.addView((View)this.b, (ViewGroup.LayoutParams)layoutParams);
    }
    
    @Override
    protected void a_(final com.facebook.ads.internal.view.n n) {
        this.setVisibility(0);
        n.getEventBus().a(this.c);
    }
}
