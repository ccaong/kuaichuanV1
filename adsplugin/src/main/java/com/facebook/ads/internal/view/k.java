// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view;

import android.view.View;
import android.widget.TextView;
import android.widget.LinearLayout;
import com.facebook.ads.NativeAdViewAttributes;
import com.facebook.ads.NativeAd;
import android.content.Context;

public abstract class k
{
    public static LinearLayout a(final Context context, final NativeAd nativeAd, final NativeAdViewAttributes nativeAdViewAttributes) {
        final LinearLayout linearLayout = new LinearLayout(context);
        final o o = new o(context);
        o.setText((CharSequence)nativeAd.getAdSocialContext());
        b(o, nativeAdViewAttributes);
        linearLayout.addView((View)o);
        return linearLayout;
    }
    
    public static void a(final TextView textView, final NativeAdViewAttributes nativeAdViewAttributes) {
        textView.setTextColor(nativeAdViewAttributes.getTitleTextColor());
        textView.setTextSize((float)nativeAdViewAttributes.getTitleTextSize());
        textView.setTypeface(nativeAdViewAttributes.getTypeface(), 1);
    }
    
    public static void b(final TextView textView, final NativeAdViewAttributes nativeAdViewAttributes) {
        textView.setTextColor(nativeAdViewAttributes.getDescriptionTextColor());
        textView.setTextSize((float)nativeAdViewAttributes.getDescriptionTextSize());
        textView.setTypeface(nativeAdViewAttributes.getTypeface());
    }
}
