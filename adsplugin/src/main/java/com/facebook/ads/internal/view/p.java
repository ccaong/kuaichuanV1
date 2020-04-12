// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view;

import android.content.Context;
import android.widget.RelativeLayout;

public class p extends RelativeLayout
{
    private int a;
    private int b;
    
    public p(final Context context) {
        super(context);
        this.a = 0;
        this.b = 0;
    }
    
    public void setMinWidth(final int a) {
        this.a = a;
    }
    
    protected void setMaxWidth(final int b) {
        this.b = b;
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(n, n2);
        if (this.b > 0 && this.getMeasuredWidth() > this.b) {
            this.setMeasuredDimension(this.b, this.getMeasuredHeight());
        }
        else if (this.getMeasuredWidth() < this.a) {
            this.setMeasuredDimension(this.a, this.getMeasuredHeight());
        }
    }
}
