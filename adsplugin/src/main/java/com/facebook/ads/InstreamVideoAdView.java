// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads;

import java.io.Serializable;
import com.facebook.ads.internal.adapters.r;
import android.view.ViewGroup;
import com.facebook.ads.internal.adapters.AdAdapter;
import com.facebook.ads.internal.b;
import com.facebook.ads.internal.a;
import com.facebook.ads.internal.c;
import com.facebook.ads.internal.server.AdPlacementType;
import com.facebook.ads.internal.e;
import android.content.Context;
import android.view.View;
import android.support.annotation.Nullable;
import com.facebook.ads.internal.adapters.i;
import com.facebook.ads.internal.DisplayAdController;
import android.os.Bundle;
import com.facebook.ads.internal.util.ad;
import android.widget.RelativeLayout;

public class InstreamVideoAdView extends RelativeLayout implements Ad, ad<Bundle>
{
    private final String a;
    private final AdSize b;
    private DisplayAdController c;
    @Nullable
    private i d;
    private boolean e;
    @Nullable
    private InstreamVideoAdListener f;
    @Nullable
    private View g;
    @Nullable
    private Bundle h;
    
    public InstreamVideoAdView(final Context context, final Bundle h) {
        this(context, h.getString("placementID"), (AdSize)h.get("adSize"));
        this.h = h;
    }
    
    public InstreamVideoAdView(final Context context, final String a, final AdSize b) {
        super(context);
        this.e = false;
        this.a = a;
        this.b = b;
        this.c = this.getController();
    }
    
    private DisplayAdController getController() {
        (this.c = new DisplayAdController(this.getContext(), this.a, com.facebook.ads.internal.e.INSTREAM_VIDEO, AdPlacementType.INSTREAM, this.b, com.facebook.ads.internal.c.ADS, 1, true, false)).a(new a() {
            @Override
            public void a(final b b) {
                if (InstreamVideoAdView.this.f == null) {
                    return;
                }
                InstreamVideoAdView.this.f.onError(InstreamVideoAdView.this, b.b());
            }
            
            @Override
            public void a(final AdAdapter adAdapter) {
                if (InstreamVideoAdView.this.c == null) {
                    return;
                }
                InstreamVideoAdView.this.e = true;
                if (InstreamVideoAdView.this.f == null) {
                    return;
                }
                InstreamVideoAdView.this.f.onAdLoaded(InstreamVideoAdView.this);
            }
            
            @Override
            public void a(final View view) {
                if (view == null) {
                    throw new IllegalStateException("Cannot present null view");
                }
                InstreamVideoAdView.this.g = view;
                InstreamVideoAdView.this.removeAllViews();
                InstreamVideoAdView.this.g.setLayoutParams((ViewGroup.LayoutParams)new RelativeLayout.LayoutParams(-1, -1));
                InstreamVideoAdView.this.addView(InstreamVideoAdView.this.g);
            }
            
            @Override
            public void a() {
                if (InstreamVideoAdView.this.f == null) {
                    return;
                }
                InstreamVideoAdView.this.f.onAdClicked(InstreamVideoAdView.this);
            }
            
            @Override
            public void b() {
            }
            
            @Override
            public void c() {
                if (InstreamVideoAdView.this.f == null) {
                    return;
                }
                InstreamVideoAdView.this.f.onAdVideoComplete(InstreamVideoAdView.this);
            }
        });
        return this.c;
    }
    
    public void loadAd() {
        if (this.h != null) {
            (this.d = new i()).a(this.getContext(), new com.facebook.ads.a.a() {
                @Override
                public void a(final r r) {
                    InstreamVideoAdView.this.e = true;
                    if (InstreamVideoAdView.this.f == null) {
                        return;
                    }
                    InstreamVideoAdView.this.f.onAdLoaded(InstreamVideoAdView.this);
                }
                
                @Override
                public void a(final r r, final View view) {
                    if (view == null) {
                        throw new IllegalStateException("Cannot present null view");
                    }
                    InstreamVideoAdView.this.g = view;
                    InstreamVideoAdView.this.removeAllViews();
                    InstreamVideoAdView.this.g.setLayoutParams((ViewGroup.LayoutParams)new RelativeLayout.LayoutParams(-1, -1));
                    InstreamVideoAdView.this.addView(InstreamVideoAdView.this.g);
                }
                
                @Override
                public void a(final r r, final AdError adError) {
                    if (InstreamVideoAdView.this.f == null) {
                        return;
                    }
                    InstreamVideoAdView.this.f.onError(InstreamVideoAdView.this, adError);
                }
                
                @Override
                public void b(final r r) {
                    if (InstreamVideoAdView.this.f == null) {
                        return;
                    }
                    InstreamVideoAdView.this.f.onAdClicked(InstreamVideoAdView.this);
                }
                
                @Override
                public void c(final r r) {
                }
                
                @Override
                public void d(final r r) {
                    if (InstreamVideoAdView.this.f == null) {
                        return;
                    }
                    InstreamVideoAdView.this.f.onAdVideoComplete(InstreamVideoAdView.this);
                }
            }, com.facebook.ads.internal.gpackage.g.a(this.getContext()), this.h.getBundle("adapter"));
        }
        else {
            this.c.b();
        }
    }
    
    public boolean show() {
        if (!this.e || (this.c == null && this.d == null)) {
            if (this.f != null) {
                this.f.onError(this, AdError.INTERNAL_ERROR);
            }
            return false;
        }
        if (this.d != null) {
            this.d.d();
        }
        else {
            this.c.c();
        }
        this.e = false;
        return true;
    }
    
    public void setAdListener(final InstreamVideoAdListener f) {
        this.f = f;
    }
    
    public void destroy() {
        this.a();
    }
    
    private final void a() {
        if (this.c != null) {
            this.c.d();
            this.c = null;
            this.c = this.getController();
            this.d = null;
            this.e = false;
            this.removeAllViews();
        }
    }
    
    public boolean isAdLoaded() {
        return this.e;
    }
    
    public String getPlacementId() {
        return this.a;
    }

    @Override
    public String getRequestId() {
        return null;
    }

    @Override
    public void setUseCache(boolean use) {

    }

    public Bundle getSaveInstanceState() {
        final r r = (this.d != null) ? this.d : ((r)this.c.i());
        if (r == null) {
            return null;
        }
        final Bundle saveInstanceState = r.getSaveInstanceState();
        if (saveInstanceState == null) {
            return null;
        }
        final Bundle bundle = new Bundle();
        bundle.putBundle("adapter", saveInstanceState);
        bundle.putString("placementID", this.a);
        bundle.putSerializable("adSize", (Serializable)this.b);
        return bundle;
    }
}
