// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads;

import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.support.v4.view.PagerAdapter;
import android.content.Context;
import android.widget.LinearLayout;

public class NativeAdScrollView extends LinearLayout
{
    public static final int DEFAULT_MAX_ADS = 10;
    public static final int DEFAULT_INSET = 20;
    private final Context a;
    private final NativeAdsManager b;
    private final AdViewProvider c;
    private final NativeAdView.Type d;
    private final int e;
    private final b f;
    private final NativeAdViewAttributes g;
    
    private NativeAdScrollView(final Context a, final NativeAdsManager b, final AdViewProvider c, final NativeAdView.Type d, final NativeAdViewAttributes g, final int e) {
        super(a);
        if (!b.isLoaded()) {
            throw new IllegalStateException("NativeAdsManager not loaded");
        }
        if (d == null && c == null) {
            throw new IllegalArgumentException("Must provide a NativeAdView.Type or AdViewProvider");
        }
        this.a = a;
        this.b = b;
        this.g = g;
        this.c = c;
        this.d = d;
        this.e = e;
        final a adapter = new a();
        (this.f = new b(a)).setAdapter((PagerAdapter)adapter);
        this.setInset(20);
        adapter.a();
        this.addView((View)this.f);
    }
    
    public NativeAdScrollView(final Context context, final NativeAdsManager nativeAdsManager, final AdViewProvider adViewProvider) {
        this(context, nativeAdsManager, adViewProvider, null, null, 10);
    }
    
    public NativeAdScrollView(final Context context, final NativeAdsManager nativeAdsManager, final AdViewProvider adViewProvider, final int n) {
        this(context, nativeAdsManager, adViewProvider, null, null, n);
    }
    
    public NativeAdScrollView(final Context context, final NativeAdsManager nativeAdsManager, final NativeAdView.Type type) {
        this(context, nativeAdsManager, null, type, new NativeAdViewAttributes(), 10);
    }
    
    public NativeAdScrollView(final Context context, final NativeAdsManager nativeAdsManager, final NativeAdView.Type type, final NativeAdViewAttributes nativeAdViewAttributes) {
        this(context, nativeAdsManager, null, type, nativeAdViewAttributes, 10);
    }
    
    public NativeAdScrollView(final Context context, final NativeAdsManager nativeAdsManager, final NativeAdView.Type type, final NativeAdViewAttributes nativeAdViewAttributes, final int n) {
        this(context, nativeAdsManager, null, type, nativeAdViewAttributes, n);
    }
    
    public void setInset(final int n) {
        if (n > 0) {
            final DisplayMetrics displayMetrics = this.a.getResources().getDisplayMetrics();
            final int round = Math.round(n * displayMetrics.density);
            this.f.setPadding(round, 0, round, 0);
            this.f.setPageMargin(Math.round(n / 2 * displayMetrics.density));
            this.f.setClipToPadding(false);
        }
    }
    
    private class b extends ViewPager
    {
        public b(final Context context) {
            super(context);
        }
        
        protected void onMeasure(final int n, int measureSpec) {
            int n2 = 0;
            for (int i = 0; i < this.getChildCount(); ++i) {
                final View child = this.getChildAt(i);
                child.measure(n, View.MeasureSpec.makeMeasureSpec(0, 0));
                final int measuredHeight = child.getMeasuredHeight();
                if (measuredHeight > n2) {
                    n2 = measuredHeight;
                }
            }
            measureSpec = View.MeasureSpec.makeMeasureSpec(n2, 1073741824);
            super.onMeasure(n, measureSpec);
        }
    }
    
    private class a extends PagerAdapter
    {
        private List<NativeAd> b;
        
        public a() {
            this.b = new ArrayList<NativeAd>();
        }
        
        public void a() {
            this.b.clear();
            for (int min = Math.min(NativeAdScrollView.this.e, NativeAdScrollView.this.b.getUniqueNativeAdCount()), i = 0; i < min; ++i) {
                final NativeAd nextNativeAd = NativeAdScrollView.this.b.nextNativeAd();
                nextNativeAd.a(true);
                this.b.add(nextNativeAd);
            }
            this.notifyDataSetChanged();
        }
        
        public int getCount() {
            return this.b.size();
        }
        
        public boolean isViewFromObject(final View view, final Object o) {
            return view == o;
        }
        
        public Object instantiateItem(final ViewGroup viewGroup, final int n) {
            View view;
            if (NativeAdScrollView.this.d != null) {
                view = NativeAdView.render(NativeAdScrollView.this.a, this.b.get(n), NativeAdScrollView.this.d, NativeAdScrollView.this.g);
            }
            else {
                view = NativeAdScrollView.this.c.createView(this.b.get(n), n);
            }
            viewGroup.addView(view);
            return view;
        }
        
        public void destroyItem(final ViewGroup viewGroup, final int n, final Object o) {
            if (n < this.b.size()) {
                if (NativeAdScrollView.this.d != null) {
                    this.b.get(n).unregisterView();
                }
                else {
                    NativeAdScrollView.this.c.destroyView(this.b.get(n), (View)o);
                }
            }
            viewGroup.removeView((View)o);
        }
        
        public int getItemPosition(final Object o) {
            final int index = this.b.indexOf(o);
            return (index >= 0) ? index : -2;
        }
    }
    
    public interface AdViewProvider
    {
        View createView(final NativeAd p0, final int p1);
        
        void destroyView(final NativeAd p0, final View p1);
    }
}
