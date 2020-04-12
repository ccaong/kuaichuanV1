// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view;

import android.view.View;
import android.content.Context;
import android.widget.ImageView;

public class q extends ImageView
{
    public q(final Context context) {
        super(context);
    }
    
    protected void onMeasure(final int n, final int n2) {
        int n3 = n;
        int n4 = n2;
        if (View.MeasureSpec.getMode(n) == 1073741824) {
            n4 = n;
        }
        else if (View.MeasureSpec.getMode(n2) == 1073741824) {
            n3 = n2;
        }
        super.onMeasure(n3, n4);
    }
}
