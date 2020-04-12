package com.facebook.ads.internal.adapters;

import android.graphics.Color;
import com.facebook.ads.internal.util.p;
import android.view.View;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.facebook.ads.internal.view.q;
import android.view.ViewGroup;
import com.facebook.ads.internal.view.hscroll.b;
import com.facebook.ads.NativeAd;
import java.util.List;
import android.support.v7.widget.RecyclerView;

public class g extends RecyclerView.Adapter<com.facebook.ads.internal.view.g>
{
    private static final int a;
    private final List<NativeAd> b;
    private final int c;
    private final int d;
    
    public g(final b b, final List<NativeAd> b2) {
        final float density = b.getContext().getResources().getDisplayMetrics().density;
        this.b = b2;
        this.c = Math.round(density * 1.0f);
        this.d = b.getChildSpacing();
    }
    
    public com.facebook.ads.internal.view.g onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        final q q = new q(viewGroup.getContext());
        q.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new com.facebook.ads.internal.view.g(q);
    }
    
    public void onBindViewHolder(final com.facebook.ads.internal.view.g g, final int n) {
        final ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(-2, -1);
        layoutParams.setMargins((n == 0) ? (this.d * 2) : this.d, 0, (n >= this.b.size() - 1) ? (this.d * 2) : this.d, 0);
        g.a.setBackgroundColor(0);
        g.a.setImageDrawable((Drawable)null);
        g.a.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        g.a.setPadding(this.c, this.c, this.c, this.c);
        final NativeAd nativeAd = this.b.get(n);
        nativeAd.registerViewForInteraction((View)g.a);
        final NativeAd.Image adCoverImage = nativeAd.getAdCoverImage();
        if (adCoverImage != null) {
            final p p2 = new p(g.a);
            p2.a(new com.facebook.ads.internal.util.q() {
                @Override
                public void a() {
                    g.a.setBackgroundColor(a);
                }
            });
            p2.a(adCoverImage.getUrl());
        }
    }

    public int getItemCount() {
        return this.b.size();
    }
    
    static {
        a = Color.argb(51, 0, 0, 0);
    }
}
