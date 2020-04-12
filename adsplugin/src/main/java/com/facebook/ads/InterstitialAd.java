// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads;

import android.view.View;
import com.facebook.ads.internal.adapters.AdAdapter;
import com.facebook.ads.internal.b;
import com.facebook.ads.internal.a;
import com.facebook.ads.internal.server.AdPlacementType;
import com.facebook.ads.internal.util.g;
import com.facebook.ads.internal.DisplayAdController;
import android.content.Context;
import com.facebook.ads.internal.c;

public class InterstitialAd implements Ad
{
    private static final c a;
    private final Context b;
    private final String cc;
    private DisplayAdController d;
    private boolean e;
    private boolean f;
    private InterstitialAdListener g;
    private boolean useCache;
    
    public InterstitialAd(final Context b, final String c) {
        this.b = b;
        this.cc = c;
    }
    
    public void setAdListener(final InterstitialAdListener g) {
        this.g = g;
    }
    
    @Override
    public void loadAd() {
        this.e = false;
        if (this.f) {
            this.f = false;
            if (InterstitialAd.this.d != null) {
                InterstitialAd.this.d.d();
                InterstitialAd.this.d = null;
            }
            if (InterstitialAd.this.g != null) {
                InterstitialAd.this.g.onInterstitialDismissed(InterstitialAd.this);
            }
        }
        if(this.f) {
            throw new IllegalStateException("InterstitialAd cannot be loaded while being displayed. Make sure your adapter calls adapterListener.onInterstitialDismissed().");
        }
        if (this.d != null) {
            this.d.d();
            this.d = null;
        }
        (this.d = new DisplayAdController(this.b, this.cc, com.facebook.ads.internal.util.g.a(AdSize.INTERSTITIAL), AdPlacementType.INTERSTITIAL, AdSize.INTERSTITIAL, InterstitialAd.a, 1, true, useCache)).a(new a() {
            @Override
            public void a(final b b) {
                if (InterstitialAd.this.g != null) {
                    InterstitialAd.this.g.onError(InterstitialAd.this, b.b());
                }
            }
            
            @Override
            public void a(final AdAdapter adAdapter) {
                InterstitialAd.this.e = true;
                if (InterstitialAd.this.g != null) {
                    InterstitialAd.this.g.onAdLoaded(InterstitialAd.this);
                }
            }
            
            @Override
            public void a(final View view) {
            }
            
            @Override
            public void a() {
                if (InterstitialAd.this.g != null) {
                    InterstitialAd.this.g.onAdClicked(InterstitialAd.this);
                }
            }
            
            @Override
            public void b() {
                if (InterstitialAd.this.g != null) {
                    InterstitialAd.this.g.onLoggingImpression(InterstitialAd.this);
                }
            }
            
            @Override
            public void d() {
                if (InterstitialAd.this.g != null) {
                    InterstitialAd.this.g.onInterstitialDisplayed(InterstitialAd.this);
                }
            }
            
            @Override
            public void e() {
                InterstitialAd.this.f = false;
                if (InterstitialAd.this.d != null) {
                    InterstitialAd.this.d.d();
                    InterstitialAd.this.d = null;
                }
                if (InterstitialAd.this.g != null) {
                    InterstitialAd.this.g.onInterstitialDismissed(InterstitialAd.this);
                }
            }
        });
        this.d.b();
    }
    
    @Override
    public void destroy() {
        if (this.d != null) {
            this.d.d();
            this.d = null;
        }
    }
    
    @Override
    public String getPlacementId() {
        return this.cc;
    }

    @Override
    public String getRequestId() {
        if (d != null) {
            return d.getRequestId();
        }
        return null;
    }

    @Override
    public void setUseCache(boolean use) {
        useCache = use;
    }

    public boolean isAdLoaded() {
        return this.e;
    }
    
    public boolean show() {
        if (!this.e) {
            if (this.g != null) {
                this.g.onError(this, AdError.INTERNAL_ERROR);
            }
            return false;
        }
        this.d.c();
        this.f = true;
        this.e = false;
        return true;
    }
    
    static {
        a = c.ADS;
    }
}
