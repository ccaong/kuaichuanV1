// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads;

import com.facebook.ads.internal.adapters.x;
import com.facebook.ads.internal.adapters.AdAdapter;
import com.facebook.ads.internal.b;
import com.facebook.ads.internal.a;
import com.facebook.ads.internal.c;
import com.facebook.ads.internal.server.AdPlacementType;
import com.facebook.ads.internal.e;
import android.util.Log;
import com.facebook.ads.internal.DisplayAdController;
import android.content.Context;

public class RewardedVideoAd implements Ad
{
    private static final String a;
    private final Context b;
    private final String c;
    private DisplayAdController d;
    private boolean e;
    private RewardedVideoAdListener f;
    private RewardData g;
    
    public RewardedVideoAd(final Context b, final String c) {
        this.e = false;
        this.b = b;
        this.c = c;
    }
    
    @Override
    public void loadAd() {
        try {
            this.a();
        }
        catch (Exception ex) {
            Log.e(RewardedVideoAd.a, "Error loading rewarded video ad", (Throwable)ex);
            if (this.f != null) {
                this.f.onError(this, AdError.INTERNAL_ERROR);
            }
        }
    }
    
    private void a() {
        this.b();
        this.e = false;
        (this.d = new DisplayAdController(this.b, this.c, com.facebook.ads.internal.e.REWARDED_VIDEO, AdPlacementType.REWARDED_VIDEO, AdSize.INTERSTITIAL, com.facebook.ads.internal.c.ADS, 1, true, false)).a(new a() {
            @Override
            public void a(final b b) {
                if (RewardedVideoAd.this.f != null) {
                    RewardedVideoAd.this.f.onError(RewardedVideoAd.this, b.b());
                }
            }
            
            @Override
            public void a() {
                if (RewardedVideoAd.this.f != null) {
                    RewardedVideoAd.this.f.onAdClicked(RewardedVideoAd.this);
                }
            }
            
            @Override
            public void b() {
                if (RewardedVideoAd.this.f != null) {
                    RewardedVideoAd.this.f.onLoggingImpression(RewardedVideoAd.this);
                }
            }
            
            @Override
            public void a(final AdAdapter adAdapter) {
                if (RewardedVideoAd.this.g != null) {
                    ((x)adAdapter).a(RewardedVideoAd.this.g);
                }
                RewardedVideoAd.this.e = true;
                if (RewardedVideoAd.this.f != null) {
                    RewardedVideoAd.this.f.onAdLoaded(RewardedVideoAd.this);
                }
            }
            
            @Override
            public void f() {
                RewardedVideoAd.this.f.onRewardedVideoCompleted();
            }
            
            @Override
            public void g() {
                if (RewardedVideoAd.this.f != null) {
                    RewardedVideoAd.this.f.onRewardedVideoClosed();
                }
            }
            
            @Override
            public void h() {
                if (RewardedVideoAd.this.f instanceof S2SRewardedVideoAdListener) {
                    ((S2SRewardedVideoAdListener)RewardedVideoAd.this.f).onRewardServerFailed();
                }
            }
            
            @Override
            public void i() {
                if (RewardedVideoAd.this.f instanceof S2SRewardedVideoAdListener) {
                    ((S2SRewardedVideoAdListener)RewardedVideoAd.this.f).onRewardServerSuccess();
                }
            }
        });
        this.d.b();
    }
    
    public boolean show() {
        if (!this.e) {
            if (this.f != null) {
                this.f.onError(this, AdError.INTERNAL_ERROR);
            }
            return false;
        }
        this.d.c();
        this.e = false;
        return true;
    }
    
    public void setAdListener(final RewardedVideoAdListener f) {
        this.f = f;
    }
    
    @Override
    public void destroy() {
        this.b();
    }
    
    private final void b() {
        if (this.d != null) {
            this.d.d();
            this.d = null;
        }
    }
    
    public boolean isAdLoaded() {
        return this.e;
    }
    
    @Override
    public String getPlacementId() {
        return this.c;
    }

    @Override
    public String getRequestId() {
        return null;
    }

    @Override
    public void setUseCache(boolean use) {

    }

    public void setRewardData(final RewardData g) {
        this.g = g;
    }
    
    static {
        a = RewardedVideoAd.class.getSimpleName();
    }
}
