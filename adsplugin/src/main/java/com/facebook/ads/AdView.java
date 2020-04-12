// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads;

import android.content.res.Configuration;
import com.facebook.ads.internal.adapters.AdAdapter;
import com.facebook.ads.internal.b;
import com.facebook.ads.internal.a;
import com.facebook.ads.internal.server.AdPlacementType;
import com.facebook.ads.internal.util.g;
import android.content.Context;
import android.view.View;
import com.facebook.ads.internal.DisplayAdController;
import android.util.DisplayMetrics;
import com.facebook.ads.internal.c;
import android.widget.RelativeLayout;

public class AdView extends RelativeLayout implements Ad
{
    private static final c a;
    private final DisplayMetrics b;
    private final AdSize adSize;
    private final String d;
    private DisplayAdController e;
    private AdListener f;
    private View g;
    private volatile boolean h;
    private boolean useCache;
    
    public AdView(final Context context, final String d, final AdSize c) {
        super(context);
        if (c == null || c == AdSize.INTERSTITIAL) {
            throw new IllegalArgumentException("adSize");
        }
        this.b = this.getContext().getResources().getDisplayMetrics();
        this.adSize = c;
        this.d = d;
        (this.e = new DisplayAdController(context, d, com.facebook.ads.internal.util.g.a(c), AdPlacementType.BANNER, c, AdView.a, 1, false, useCache)).a(new a() {
            @Override
            public void a(final b b) {
                if (AdView.this.f != null) {
                    AdView.this.f.onError(AdView.this, b.b());
                }
            }
            
            @Override
            public void a(final AdAdapter adAdapter) {
                if (AdView.this.e != null) {
                    AdView.this.e.c();
                }
            }
            
            @Override
            public void a(final View view) {
                if (view == null) {
                    throw new IllegalStateException("Cannot present null view");
                }
                AdView.this.g = view;
                AdView.this.removeAllViews();
                AdView.this.addView(AdView.this.g);
                if (AdView.this.g instanceof com.facebook.ads.internal.view.c) {
                    com.facebook.ads.internal.util.g.a(AdView.this.b, AdView.this.g, AdView.this.adSize);
                }
                if (AdView.this.f != null) {
                    AdView.this.f.onAdLoaded(AdView.this);
                }
            }
            
            @Override
            public void a() {
                if (AdView.this.f != null) {
                    AdView.this.f.onAdClicked(AdView.this);
                }
            }
            
            @Override
            public void b() {
                if (AdView.this.f != null) {
                    AdView.this.f.onLoggingImpression(AdView.this);
                }
            }
        });
    }
    
    public void setAdListener(final AdListener f) {
        this.f = f;
    }
    
    public void loadAd() {
        if (!this.h) {
            this.e.b();
            this.h = true;
        }
        else if (this.e != null) {
            this.e.g();
        }
    }
    
    public void destroy() {
        if (this.e != null) {
            this.e.d();
            this.e = null;
        }
        this.removeAllViews();
        this.g = null;
    }
    
    public String getPlacementId() {
        return this.d;
    }

    @Override
    public String getRequestId() {
        if (e != null) {
            return e.getRequestId();
        }
        return null;
    }

    @Override
    public void setUseCache(boolean use) {
        useCache = use;
    }

    public void disableAutoRefresh() {
        if (this.e != null) {
            this.e.h();
        }
    }
    
    protected void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.g != null) {
            com.facebook.ads.internal.util.g.a(this.b, this.g, this.adSize);
        }
    }
    
    protected void onWindowVisibilityChanged(final int n) {
        super.onWindowVisibilityChanged(n);
        if (this.e == null) {
            return;
        }
        if (n == 0) {
            this.e.f();
        }
        else if (n == 8) {
            this.e.e();
        }
    }
    
    static {
        a = c.ADS;
    }
}
