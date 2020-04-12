// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view;

import android.view.View;
import android.text.TextUtils;
import android.content.Context;
import android.widget.TextView;

public class m extends TextView
{
    private int a;
    private float b;
    private float c;
    
    public m(final Context context, final int maxLines) {
        super(context);
        this.c = 8.0f;
        this.setMaxLines(maxLines);
        this.setEllipsize(TextUtils.TruncateAt.END);
    }
    
    public void setMinTextSize(final float c) {
        this.c = c;
    }
    
    public void setMaxLines(final int a) {
        super.setMaxLines(this.a = a);
    }
    
    public void setTextSize(final float b) {
        super.setTextSize(this.b = b);
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        super.setMaxLines(this.a + 1);
        super.setTextSize(this.b);
        final int n5 = n3 - n;
        final int n6 = n4 - n2;
        this.measure(View.MeasureSpec.makeMeasureSpec(n5, 1073741824), View.MeasureSpec.makeMeasureSpec(n6, 0));
        if (this.getMeasuredHeight() > n6) {
            float b2 = this.b;
            while (b2 > this.c) {
                b2 -= 0.5f;
                super.setTextSize(b2);
                this.measure(View.MeasureSpec.makeMeasureSpec(n5, 1073741824), 0);
                if (this.getMeasuredHeight() <= n6 && this.getLineCount() <= this.a) {
                    break;
                }
            }
        }
        super.setMaxLines(this.a);
        this.setMeasuredDimension(n5, n6);
        super.onLayout(b, n, n2, n3, n4);
    }
}
