// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view;

import com.facebook.ads.internal.view.bpackage.b;
import com.facebook.ads.internal.view.bpackage.d;
import com.facebook.ads.MediaView;
import com.facebook.ads.AdChoicesView;
import java.util.List;
import android.widget.LinearLayout;
import android.view.ViewGroup;
import com.facebook.ads.NativeAdView;
import android.content.Context;
import android.view.View;
import java.util.ArrayList;
import android.util.DisplayMetrics;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdViewAttributes;
import android.widget.RelativeLayout;

public class a extends RelativeLayout
{
    private final NativeAdViewAttributes a;
    private final NativeAd b;
    private final DisplayMetrics c;
    private ArrayList<View> d;
    
    public a(final Context context, final NativeAd b, final NativeAdView.Type type, final NativeAdViewAttributes a) {
        super(context);
        this.setBackgroundColor(a.getBackgroundColor());
        this.a = a;
        this.b = b;
        this.c = context.getResources().getDisplayMetrics();
        this.setLayoutParams((ViewGroup.LayoutParams)new RelativeLayout.LayoutParams(-1, Math.round(type.getHeight() * this.c.density)));
        final p p4 = new p(context);
        p4.setMinWidth(Math.round(280.0f * this.c.density));
        p4.setMaxWidth(Math.round(375.0f * this.c.density));
        final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams.addRule(13, -1);
        p4.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        this.addView((View)p4);
        final LinearLayout linearLayout = new LinearLayout(context);
        this.d = new ArrayList<View>();
        linearLayout.setOrientation(1);
        linearLayout.setLayoutParams((ViewGroup.LayoutParams)new RelativeLayout.LayoutParams(-1, -1));
        p4.addView((View)linearLayout);
        switch (type) {
            case HEIGHT_400: {
                this.b((ViewGroup)linearLayout);
            }
            case HEIGHT_300: {
                this.a((ViewGroup)linearLayout);
                break;
            }
        }
        this.a((ViewGroup)linearLayout, type);
        b.registerViewForInteraction((View)this, this.d);
        final AdChoicesView adChoicesView = new AdChoicesView(this.getContext(), b, true);
        final RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams)adChoicesView.getLayoutParams();
        layoutParams2.addRule(11);
        layoutParams2.setMargins(Math.round(4.0f * this.c.density), Math.round(4.0f * this.c.density), Math.round(4.0f * this.c.density), Math.round(4.0f * this.c.density));
        p4.addView((View)adChoicesView);
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.b.unregisterView();
    }
    
    private void a(final ViewGroup viewGroup) {
        final RelativeLayout relativeLayout = new RelativeLayout(this.getContext());
        relativeLayout.setLayoutParams((ViewGroup.LayoutParams)new LinearLayout.LayoutParams(-1, Math.round(180.0f * this.c.density)));
        relativeLayout.setBackgroundColor(this.a.getBackgroundColor());
        final MediaView mediaView = new MediaView(this.getContext());
        relativeLayout.addView((View)mediaView);
        final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, (int)(180.0f * this.c.density));
        layoutParams.addRule(13, -1);
        mediaView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        mediaView.setAutoplay(this.a.getAutoplay());
        mediaView.setAutoplayOnMobile(this.a.getAutoplayOnMobile());
        mediaView.setNativeAd(this.b);
        viewGroup.addView((View)relativeLayout);
        this.d.add((View)mediaView);
    }
    
    private void b(final ViewGroup viewGroup) {
        final d d = new d(this.getContext(), this.b, this.a);
        d.setLayoutParams((ViewGroup.LayoutParams)new LinearLayout.LayoutParams(-1, Math.round(110.0f * this.c.density)));
        viewGroup.addView((View)d);
    }
    
    private void a(final ViewGroup viewGroup, final NativeAdView.Type type) {
        final b b = new b(this.getContext(), this.b, this.a, this.a(type), this.b(type));
        b.setLayoutParams((ViewGroup.LayoutParams)new LinearLayout.LayoutParams(-1, Math.round(this.b(type) * this.c.density)));
        viewGroup.addView((View)b);
        this.d.add((View)b.getIconView());
        this.d.add((View)b.getCallToActionView());
    }
    
    private boolean a(final NativeAdView.Type type) {
        return type == NativeAdView.Type.HEIGHT_300 || type == NativeAdView.Type.HEIGHT_120;
    }
    
    private int b(final NativeAdView.Type type) {
        switch (type) {
            case HEIGHT_100:
            case HEIGHT_120: {
                return type.getHeight();
            }
            case HEIGHT_300: {
                return type.getHeight() - 180;
            }
            case HEIGHT_400: {
                return (type.getHeight() - 180) / 2;
            }
            default: {
                return 0;
            }
        }
    }
}
