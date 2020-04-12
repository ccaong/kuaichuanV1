// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view.bpackage;

import android.util.DisplayMetrics;
import com.facebook.ads.internal.view.k;
import android.text.TextUtils;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import com.facebook.ads.NativeAdViewAttributes;
import com.facebook.ads.NativeAd;
import android.content.Context;
import android.widget.LinearLayout;

public class d extends LinearLayout
{
    public d(final Context context, final NativeAd nativeAd, final NativeAdViewAttributes nativeAdViewAttributes) {
        super(context);
        final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        final LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setGravity(17);
        linearLayout.setVerticalGravity(16);
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
        layoutParams.setMargins(Math.round(15.0f * displayMetrics.density), Math.round(15.0f * displayMetrics.density), Math.round(15.0f * displayMetrics.density), Math.round(15.0f * displayMetrics.density));
        linearLayout.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        this.addView((View)linearLayout);
        final String adSubtitle = nativeAd.getAdSubtitle();
        final TextView textView = new TextView(this.getContext());
        textView.setText((CharSequence)(TextUtils.isEmpty((CharSequence)adSubtitle) ? nativeAd.getAdTitle() : adSubtitle));
        k.a(textView, nativeAdViewAttributes);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setSingleLine(true);
        linearLayout.addView((View)textView);
        final TextView textView2 = new TextView(this.getContext());
        textView2.setText((CharSequence)nativeAd.getAdBody());
        k.b(textView2, nativeAdViewAttributes);
        textView2.setEllipsize(TextUtils.TruncateAt.END);
        textView2.setMaxLines(2);
        linearLayout.addView((View)textView2);
    }
}
