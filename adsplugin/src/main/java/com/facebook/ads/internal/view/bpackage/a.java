// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view.bpackage;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.facebook.ads.internal.view.k;
import com.facebook.ads.NativeAdViewAttributes;
import com.facebook.ads.NativeAd;
import android.content.Context;
import com.facebook.ads.internal.view.m;
import android.widget.LinearLayout;

public class a extends LinearLayout
{
    private m a;
    private int b;
    
    public a(final Context context, final NativeAd nativeAd, final NativeAdViewAttributes nativeAdViewAttributes) {
        super(context);
        this.setOrientation(1);
        this.setVerticalGravity(16);
        (this.a = new m(this.getContext(), 2)).setMinTextSize(nativeAdViewAttributes.getTitleTextSize() - 2);
        this.a.setText((CharSequence)nativeAd.getAdTitle());
        k.a(this.a, nativeAdViewAttributes);
        this.a.setLayoutParams((ViewGroup.LayoutParams)new LinearLayout.LayoutParams(-2, -2));
        this.addView((View)this.a);
        this.b = ((nativeAd.getAdTitle() != null) ? Math.min(nativeAd.getAdTitle().length(), 21) : 21);
        this.addView((View)k.a(context, nativeAd, nativeAdViewAttributes));
    }
    
    public TextView getTitleTextView() {
        return this.a;
    }
    
    public int getMinVisibleTitleCharacters() {
        return this.b;
    }
}
