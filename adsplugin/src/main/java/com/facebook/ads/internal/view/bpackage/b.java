// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view.bpackage;

import android.util.DisplayMetrics;
import com.facebook.ads.internal.view.k;
import com.facebook.ads.internal.view.o;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import com.facebook.ads.NativeAdViewAttributes;
import com.facebook.ads.NativeAd;
import android.content.Context;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class b extends LinearLayout
{
    private ImageView a;
    private a b;
    private TextView c;
    private LinearLayout d;
    
    public b(final Context context, final NativeAd nativeAd, final NativeAdViewAttributes nativeAdViewAttributes, final boolean b, final int n) {
        super(context);
        final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        this.setVerticalGravity(16);
        this.setOrientation(1);
        final LinearLayout linearLayout = new LinearLayout(this.getContext());
        linearLayout.setOrientation(1);
        linearLayout.setGravity(16);
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
        layoutParams.setMargins(Math.round(15.0f * displayMetrics.density), Math.round(15.0f * displayMetrics.density), Math.round(15.0f * displayMetrics.density), Math.round(15.0f * displayMetrics.density));
        linearLayout.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        this.addView((View)linearLayout);
        this.d = new LinearLayout(this.getContext());
        final LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, 0);
        this.d.setOrientation(0);
        this.d.setGravity(16);
        layoutParams2.weight = 3.0f;
        this.d.setLayoutParams((ViewGroup.LayoutParams)layoutParams2);
        linearLayout.addView((View)this.d);
        this.a = new c(this.getContext());
        final int a = this.a(b, n);
        final LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(Math.round(a * displayMetrics.density), Math.round(a * displayMetrics.density));
        layoutParams3.setMargins(0, 0, Math.round(15.0f * displayMetrics.density), 0);
        this.a.setLayoutParams((ViewGroup.LayoutParams)layoutParams3);
        NativeAd.downloadAndDisplayImage(nativeAd.getAdIcon(), this.a);
        this.d.addView((View)this.a);
        final LinearLayout linearLayout2 = new LinearLayout(this.getContext());
        linearLayout2.setLayoutParams((ViewGroup.LayoutParams)new LinearLayout.LayoutParams(-1, -1));
        linearLayout2.setOrientation(0);
        linearLayout2.setGravity(16);
        this.d.addView((View)linearLayout2);
        this.b = new a(this.getContext(), nativeAd, nativeAdViewAttributes);
        final LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(-2, -1);
        layoutParams4.setMargins(0, 0, Math.round(15.0f * displayMetrics.density), 0);
        layoutParams4.weight = 0.5f;
        this.b.setLayoutParams((ViewGroup.LayoutParams)layoutParams4);
        linearLayout2.addView((View)this.b);
        (this.c = new TextView(this.getContext())).setPadding(Math.round(6.0f * displayMetrics.density), Math.round(6.0f * displayMetrics.density), Math.round(6.0f * displayMetrics.density), Math.round(6.0f * displayMetrics.density));
        this.c.setText((CharSequence)nativeAd.getAdCallToAction());
        this.c.setTextColor(nativeAdViewAttributes.getButtonTextColor());
        this.c.setTextSize(14.0f);
        this.c.setTypeface(nativeAdViewAttributes.getTypeface(), 1);
        this.c.setMaxLines(2);
        this.c.setEllipsize(TextUtils.TruncateAt.END);
        this.c.setGravity(17);
        final GradientDrawable backgroundDrawable = new GradientDrawable();
        backgroundDrawable.setColor(nativeAdViewAttributes.getButtonColor());
        backgroundDrawable.setCornerRadius(5.0f * displayMetrics.density);
        backgroundDrawable.setStroke(1, nativeAdViewAttributes.getButtonBorderColor());
        this.c.setBackgroundDrawable((Drawable)backgroundDrawable);
        final LinearLayout.LayoutParams layoutParams5 = new LinearLayout.LayoutParams(-2, -2);
        layoutParams5.weight = 0.25f;
        this.c.setLayoutParams((ViewGroup.LayoutParams)layoutParams5);
        linearLayout2.addView((View)this.c);
        if (b) {
            final o o = new o(this.getContext());
            o.setText((CharSequence)nativeAd.getAdBody());
            k.b(o, nativeAdViewAttributes);
            o.setMinTextSize(nativeAdViewAttributes.getDescriptionTextSize() - 1);
            final LinearLayout.LayoutParams layoutParams6 = new LinearLayout.LayoutParams(-1, 0);
            layoutParams6.weight = 1.0f;
            o.setLayoutParams((ViewGroup.LayoutParams)layoutParams6);
            o.setGravity(80);
            linearLayout.addView((View)o);
        }
    }
    
    public ImageView getIconView() {
        return this.a;
    }
    
    public TextView getCallToActionView() {
        return this.c;
    }
    
    private int a(final boolean b, final int n) {
        return (int)((n - 30) * (3.0 / (3 + (b ? 1 : 0))));
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(n, n2);
        final TextView titleTextView = this.b.getTitleTextView();
        if (titleTextView.getLayout().getLineEnd(titleTextView.getLineCount() - 1) < this.b.getMinVisibleTitleCharacters()) {
            this.d.removeView((View)this.a);
            super.onMeasure(n, n2);
        }
    }
}
