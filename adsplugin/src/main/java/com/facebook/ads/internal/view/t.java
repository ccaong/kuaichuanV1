// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view;

import android.view.ViewGroup;
import android.content.Context;
import android.view.View;

public class t extends View
{
    private s a;
    
    public t(final Context context, final s a) {
        super(context);
        this.a = a;
        this.setLayoutParams(new ViewGroup.LayoutParams(0, 0));
    }
    
    public void onWindowVisibilityChanged(final int n) {
        if (this.a != null) {
            this.a.a(n);
        }
    }
}
