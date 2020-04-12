// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.adapters;

import com.facebook.ads.AdNetwork;
import com.facebook.ads.internal.util.ai;
import com.facebook.ads.NativeAdViewAttributes;
import java.util.List;
import android.view.View;
import com.flurry.android.ads.FlurryAdErrorType;
import com.flurry.android.ads.FlurryAdNativeAsset;
import com.facebook.ads.AdError;
import com.flurry.android.ads.FlurryAdNativeListener;
import com.flurry.android.FlurryAgent;
import com.facebook.ads.internal.util.g;
import org.json.JSONObject;
import java.util.Map;
import com.facebook.ads.internal.gpackage.f;
import android.content.Context;
import com.facebook.ads.NativeAd;
import com.flurry.android.ads.FlurryAdNative;

public class n extends v implements t
{
    private static volatile boolean a;
    private w b;
    private FlurryAdNative c;
    private boolean d;
    private String e;
    private String f;
    private String g;
    private String h;
    private String i;
    private NativeAd.Image j;
    private NativeAd.Image k;
    private NativeAd.Image l;
    
    @Override
    public void a(final Context context, final w b, final f f, final Map<String, Object> map) {
        final JSONObject jsonObject = (JSONObject)map.get("data");
        final String optString = jsonObject.optString("api_key");
        final String optString2 = jsonObject.optString("placement_id");
        synchronized (n.class) {
            if (!n.a) {
                com.facebook.ads.internal.util.g.a(context, com.facebook.ads.internal.util.v.a(this.D()) + " Initializing");
                FlurryAgent.setLogEnabled(true);
                FlurryAgent.init(context, optString);
                n.a = true;
            }
        }
        com.facebook.ads.internal.util.g.a(context, com.facebook.ads.internal.util.v.a(this.D()) + " Loading");
        this.b = b;
        (this.c = new FlurryAdNative(context, optString2)).setListener((FlurryAdNativeListener)new FlurryAdNativeListener() {
            public void onFetched(final FlurryAdNative flurryAdNative) {
                if (n.this.b == null) {
                    return;
                }
                if (flurryAdNative.isVideoAd()) {
                    com.facebook.ads.internal.util.g.a(context, com.facebook.ads.internal.util.v.a(n.this.D()) + " Failed. AN does not support Flurry video ads");
                    n.this.b.a(n.this, new AdError(3001, "video ad"));
                    return;
                }
                n.this.d = true;
                final FlurryAdNativeAsset asset;
                if ((asset = flurryAdNative.getAsset("headline")) != null) {
                    n.this.e = asset.getValue();
                }
                final FlurryAdNativeAsset asset2;
                if ((asset2 = flurryAdNative.getAsset("summary")) != null) {
                    n.this.f = asset2.getValue();
                }
                final FlurryAdNativeAsset asset3;
                if ((asset3 = flurryAdNative.getAsset("source")) != null) {
                    n.this.g = asset3.getValue();
                }
                final FlurryAdNativeAsset asset4;
                if ((asset4 = flurryAdNative.getAsset("appCategory")) != null) {
                    n.this.i = asset4.getValue();
                }
                final FlurryAdNativeAsset asset5;
                if ((asset5 = flurryAdNative.getAsset("callToAction")) != null) {
                    n.this.h = asset5.getValue();
                }
                else if (flurryAdNative.getAsset("appRating") != null) {
                    n.this.h = "Install Now";
                }
                else {
                    n.this.h = "Learn More";
                }
                final FlurryAdNativeAsset asset6;
                if ((asset6 = flurryAdNative.getAsset("secImage")) != null) {
                    n.this.j = new NativeAd.Image(asset6.getValue(), 82, 82);
                }
                final FlurryAdNativeAsset asset7;
                if ((asset7 = flurryAdNative.getAsset("secHqImage")) != null) {
                    n.this.k = new NativeAd.Image(asset7.getValue(), 1200, 627);
                }
                final FlurryAdNativeAsset asset8;
                if ((asset8 = flurryAdNative.getAsset("secBrandingLogo")) != null) {
                    n.this.l = new NativeAd.Image(asset8.getValue(), 20, 20);
                }
                com.facebook.ads.internal.util.g.a(context, com.facebook.ads.internal.util.v.a(n.this.D()) + " Loaded");
                n.this.b.a(n.this);
            }
            
            public void onShowFullscreen(final FlurryAdNative flurryAdNative) {
            }
            
            public void onCloseFullscreen(final FlurryAdNative flurryAdNative) {
            }
            
            public void onAppExit(final FlurryAdNative flurryAdNative) {
            }
            
            public void onClicked(final FlurryAdNative flurryAdNative) {
                if (n.this.b != null) {
                    n.this.b.c(n.this);
                }
            }
            
            public void onImpressionLogged(final FlurryAdNative flurryAdNative) {
                if (n.this.b != null) {
                    n.this.b.b(n.this);
                }
            }
            
            public void onExpanded(final FlurryAdNative flurryAdNative) {
            }
            
            public void onCollapsed(final FlurryAdNative flurryAdNative) {
            }
            
            public void onError(final FlurryAdNative flurryAdNative, final FlurryAdErrorType flurryAdErrorType, final int n) {
                com.facebook.ads.internal.util.g.a(context, com.facebook.ads.internal.util.v.a(n.this.D()) + " Failed with FlurryError: " + flurryAdErrorType.toString());
                if (n.this.b != null) {
                    n.this.b.a(n.this, new AdError(3001, flurryAdErrorType.toString()));
                }
            }
        });
        this.c.fetchAd();
    }
    
    @Override
    public void a(final Map<String, String> map) {
    }
    
    @Override
    public void b(final Map<String, String> map) {
    }
    
    @Override
    public void a(final int n) {
    }
    
    @Override
    public void a(final View trackingView, final List<View> list) {
        if (this.c != null) {
            this.c.setTrackingView(trackingView);
        }
    }
    
    @Override
    public void a() {
        if (this.c != null) {
            this.c.removeTrackingView();
        }
    }
    
    @Override
    public boolean b() {
        return this.d;
    }
    
    @Override
    public boolean c() {
        return false;
    }
    
    @Override
    public boolean d() {
        return false;
    }
    
    @Override
    public boolean e() {
        return false;
    }
    
    @Override
    public boolean f() {
        return false;
    }
    
    @Override
    public boolean g() {
        return true;
    }
    
    @Override
    public void a(final w b) {
        this.b = b;
    }
    
    @Override
    public int h() {
        return 0;
    }
    
    @Override
    public int i() {
        return 0;
    }
    
    @Override
    public int j() {
        return 0;
    }
    
    @Override
    public NativeAd.Image k() {
        return this.j;
    }
    
    @Override
    public NativeAd.Image l() {
        return this.k;
    }
    
    @Override
    public NativeAdViewAttributes m() {
        return null;
    }
    
    @Override
    public String n() {
        return this.e;
    }
    
    @Override
    public String o() {
        return this.g;
    }
    
    @Override
    public String p() {
        return this.f;
    }
    
    @Override
    public String q() {
        return this.h;
    }
    
    @Override
    public String r() {
        return this.i;
    }
    
    @Override
    public NativeAd.Rating s() {
        return null;
    }
    
    @Override
    public NativeAd.Image t() {
        return this.l;
    }
    
    @Override
    public String u() {
        return null;
    }
    
    @Override
    public String v() {
        return "Ad";
    }
    
    @Override
    public String w() {
        return null;
    }
    
    @Override
    public String x() {
        return null;
    }
    
    @Override
    public ai y() {
        return ai.UNKNOWN;
    }
    
    @Override
    public String z() {
        return null;
    }
    
    @Override
    public List<NativeAd> A() {
        return null;
    }
    
    @Override
    public String B() {
        return null;
    }
    
    @Override
    public AdNetwork C() {
        return AdNetwork.FLURRY;
    }
    
    @Override
    public void onDestroy() {
        this.a();
        this.b = null;
        if (this.c != null) {
            this.c.destroy();
            this.c = null;
        }
    }
    
    @Override
    public e D() {
        return com.facebook.ads.internal.adapters.e.YAHOO;
    }
}
