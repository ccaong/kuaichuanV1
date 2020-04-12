// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads;

import org.json.JSONObject;
import android.view.MotionEvent;
import com.facebook.ads.internal.util.g;
import com.facebook.ads.internal.h;
import android.support.v4.content.LocalBroadcastManager;
import android.content.IntentFilter;
import android.content.Intent;
import android.content.BroadcastReceiver;
import java.util.Map;
import java.util.HashMap;
import com.facebook.ads.internal.view.s;
import android.util.Log;
import android.view.ViewGroup;
import com.facebook.ads.internal.view.n;
import com.facebook.ads.internal.util.ai;
import java.util.Iterator;
import android.text.TextUtils;
import com.facebook.ads.internal.adapters.AdAdapter;
import com.facebook.ads.internal.server.AdPlacementType;
import java.util.EnumSet;
import com.facebook.ads.internal.adapters.w;
import com.facebook.ads.internal.util.p;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.UUID;
import com.facebook.ads.internal.view.t;
import com.facebook.ads.internal.adapters.u;
import com.facebook.ads.internal.util.af;
import com.facebook.ads.internal.rewarded_video.a;
import java.util.List;
import com.facebook.ads.internal.fpackage.e;
import android.support.annotation.Nullable;
import com.facebook.ads.internal.adapters.v;
import com.facebook.ads.internal.DisplayAdController;
import com.facebook.ads.internal.cpackage.b;
import android.content.Context;
import java.lang.ref.WeakReference;
import android.view.View;
import java.util.WeakHashMap;

public class NativeAd implements Ad
{
    private static final com.facebook.ads.internal.c b;
    private static final String c;
    private static WeakHashMap<View, WeakReference<NativeAd>> d;
    private final Context e;
    private final String f;
    private final String g;
    private final com.facebook.ads.internal.cpackage.b h;
    private AdListener i;
    private DisplayAdController j;
    private volatile boolean k;
    @Nullable
    protected v a;
    private e l;
    private View m;
    private final List<View> n;
    private View.OnTouchListener o;
    private com.facebook.ads.internal.rewarded_video.a p;
    private final af q;
    @Nullable
    private u r;
    private a s;
    private b t;
    private t u;
    private NativeAdView.Type v;
    private boolean w;
    private MediaView x;
    @Deprecated
    private boolean y;
    private long z;
    private String A;
    private boolean B;
    private boolean useCache;
    
    public NativeAd(final Context e, final String f) {
        this.g = UUID.randomUUID().toString();
        this.n = new ArrayList<View>();
        this.q = new af();
        this.B = false;
        this.e = e;
        this.f = f;
        this.h = new com.facebook.ads.internal.cpackage.b(e);
    }
    
    public NativeAd(final Context context, final v a, final e l) {
        this(context, null);
        this.l = l;
        this.k = true;
        this.a = a;
    }
    
    NativeAd(final NativeAd nativeAd) {
        this(nativeAd.e, null);
        this.l = nativeAd.l;
        this.k = true;
        this.a = nativeAd.a;
    }
    
    v a() {
        return this.a;
    }
    
    public static void downloadAndDisplayImage(final Image image, final ImageView imageView) {
        if (image != null && imageView != null) {
            new p(imageView).a(image.getUrl());
        }
    }
    
    public void setAdListener(final AdListener i) {
        this.i = i;
    }
    
    protected void a(final w w) {
        this.a.a(w);
    }
    
    @Nullable
    public AdNetwork getAdNetwork() {
        if (this.isAdLoaded() && this.a != null) {
            return this.a.C();
        }
        return null;
    }
    
    @Override
    public void loadAd() {
        this.loadAd(EnumSet.of(MediaCacheFlag.NONE));
    }
    
    public void loadAd(final EnumSet<MediaCacheFlag> set) {
        if (this.k) {
            throw new IllegalStateException("loadAd cannot be called more than once");
        }
        this.z = System.currentTimeMillis();
        this.k = true;
        (this.j = new DisplayAdController(this.e, this.f, com.facebook.ads.internal.e.NATIVE_UNKNOWN, AdPlacementType.NATIVE, null, NativeAd.b, 1, true, useCache)).a(new com.facebook.ads.internal.a() {
            @Override
            public void a(final com.facebook.ads.internal.b b) {
                if (NativeAd.this.i != null) {
                    NativeAd.this.i.onError(NativeAd.this, b.b());
                }
            }
            
            @Override
            public void a(final AdAdapter adAdapter) {
                if (NativeAd.this.j != null) {
                    NativeAd.this.j.c();
                }
            }
            
            @Override
            public void a(final v v) {
                com.facebook.ads.internal.util.c.a(com.facebook.ads.internal.util.b.a(com.facebook.ads.internal.util.b.bb.LOADING_AD, AdPlacementType.NATIVE, System.currentTimeMillis() - NativeAd.this.z, null));
                if (v == null) {
                    return;
                }
                if (set.contains(MediaCacheFlag.ICON) && v.k() != null) {
                    NativeAd.this.h.a(v.k().getUrl());
                }
                if (set.contains(MediaCacheFlag.IMAGE)) {
                    if (v.l() != null) {
                        NativeAd.this.h.a(v.l().getUrl());
                    }
                    if (v.A() != null) {
                        for (final NativeAd nativeAd : v.A()) {
                            if (nativeAd.getAdCoverImage() != null) {
                                NativeAd.this.h.a(nativeAd.getAdCoverImage().getUrl());
                            }
                        }
                    }
                }
                if (set.contains(MediaCacheFlag.VIDEO) && !TextUtils.isEmpty((CharSequence)v.w())) {
                    NativeAd.this.h.b(v.w());
                }
                NativeAd.this.h.a(new com.facebook.ads.internal.cpackage.a() {
                    @Override
                    public void a() {
                        NativeAd.this.a = v;
                        NativeAd.this.n();
                        NativeAd.this.o();
                        if (NativeAd.this.i != null) {
                            NativeAd.this.i.onAdLoaded(NativeAd.this);
                        }
                    }
                });
                if (NativeAd.this.i != null && v.A() != null) {
                    final w w = new w() {
                        @Override
                        public void a(final v v) {
                        }
                        
                        @Override
                        public void a(final v v, final AdError adError) {
                        }
                        
                        @Override
                        public void b(final v v) {
                        }
                        
                        @Override
                        public void c(final v v) {
                            if (NativeAd.this.i != null) {
                                NativeAd.this.i.onAdClicked(NativeAd.this);
                            }
                        }
                    };
                    final Iterator<NativeAd> iterator2 = v.A().iterator();
                    while (iterator2.hasNext()) {
                        iterator2.next().a(w);
                    }
                }
            }
            
            @Override
            public void a() {
                if (NativeAd.this.i != null) {
                    NativeAd.this.i.onAdClicked(NativeAd.this);
                }
            }
            
            @Override
            public void b() {
                throw new IllegalStateException("Native ads manager their own impressions.");
            }
        });
        this.j.b();
    }
    
    @Override
    public void destroy() {
        if (this.t != null) {
            this.t.b();
            this.t = null;
        }
        if (this.j != null) {
            this.j.d();
            this.j = null;
        }
        if (this.x != null) {
            this.x.destroy();
            this.x = null;
        }
    }
    
    @Override
    public String getPlacementId() {
        return this.f;
    }

    @Override
    public String getRequestId() {
        if (j != null) {
            return j.getRequestId();
        }
        return null;
    }

    @Override
    public void setUseCache(boolean use) {
        useCache = use;
    }

    public boolean isUseCache() {
        return useCache;
    }

    private boolean autoShow;
    public void setAutoShow() {
        if (p != null) {
            autoShow = true;
            p.setAutoShow();
        }
    }

    public boolean isAutoShow() {
        return autoShow;
    }

    public boolean isAdLoaded() {
        return this.a != null && this.a.b();
    }
    
    public boolean isNativeConfigEnabled() {
        return this.isAdLoaded() && this.a.f();
    }
    
    public Image getAdIcon() {
        if (!this.isAdLoaded()) {
            return null;
        }
        return this.a.k();
    }
    
    public Image getAdCoverImage() {
        if (!this.isAdLoaded()) {
            return null;
        }
        return this.a.l();
    }
    
    public NativeAdViewAttributes getAdViewAttributes() {
        if (!this.isAdLoaded()) {
            return null;
        }
        return this.a.m();
    }
    
    public String getAdTitle() {
        if (!this.isAdLoaded()) {
            return null;
        }
        return this.a.n();
    }
    
    public String getAdSubtitle() {
        if (!this.isAdLoaded()) {
            return null;
        }
        return this.a.o();
    }
    
    public String getAdBody() {
        if (!this.isAdLoaded()) {
            return null;
        }
        return this.a.p();
    }
    
    public String getAdCallToAction() {
        if (!this.isAdLoaded()) {
            return null;
        }
        return this.a.q();
    }
    
    public String getAdSocialContext() {
        if (!this.isAdLoaded()) {
            return null;
        }
        return this.a.r();
    }
    
    @Deprecated
    public Rating getAdStarRating() {
        if (!this.isAdLoaded()) {
            return null;
        }
        return this.a.s();
    }
    
    public String getId() {
        if (!this.isAdLoaded()) {
            return null;
        }
        return this.g;
    }
    
    public Image getAdChoicesIcon() {
        if (!this.isAdLoaded()) {
            return null;
        }
        return this.a.t();
    }
    
    public String getAdChoicesLinkUrl() {
        if (!this.isAdLoaded()) {
            return null;
        }
        return this.a.u();
    }
    
    String b() {
        if (!this.isAdLoaded()) {
            return null;
        }
        return this.a.v();
    }
    
    String c() {
        if (!this.isAdLoaded() || TextUtils.isEmpty((CharSequence)this.a.w())) {
            return null;
        }
        return this.h.c(this.a.w());
    }
    
    String d() {
        if (!this.isAdLoaded()) {
            return null;
        }
        return this.a.x();
    }
    
    String e() {
        if (!this.isAdLoaded()) {
            return null;
        }
        return this.a.z();
    }
    
    ai f() {
        if (!this.isAdLoaded()) {
            return ai.UNKNOWN;
        }
        return this.a.y();
    }
    
    List<NativeAd> g() {
        if (!this.isAdLoaded()) {
            return null;
        }
        return this.a.A();
    }
    
    @Nullable
    String h() {
        if (!this.isAdLoaded()) {
            return null;
        }
        return this.a.B();
    }
    
    public void registerViewForInteraction(final View view) {
        final ArrayList<View> list = new ArrayList<View>();
        this.a(list, view);
        this.registerViewForInteraction(view, list);
    }
    
    private void a(final List<View> list, final View view) {
        if (view instanceof n || view instanceof AdChoicesView || view instanceof com.facebook.ads.internal.view.hscroll.b) {
            return;
        }
        list.add(view);
        if (view instanceof ViewGroup) {
            final ViewGroup viewGroup = (ViewGroup)view;
            for (int i = 0; i < viewGroup.getChildCount(); ++i) {
                this.a(list, viewGroup.getChildAt(i));
            }
        }
    }
    
    public void registerViewForInteraction(final View m, final List<View> list) {
        if (m == null) {
            throw new IllegalArgumentException("Must provide a View");
        }
        if (list == null || list.size() == 0) {
            throw new IllegalArgumentException("Invalid set of clickable views");
        }
        if (!this.isAdLoaded()) {
            Log.e(NativeAd.c, "Ad not loaded");
            return;
        }
        if (this.m != null) {
            Log.w(NativeAd.c, "Native Ad was already registered with a View. Auto unregistering and proceeding.");
            this.unregisterView();
        }
        if (NativeAd.d.containsKey(m)) {
            Log.w(NativeAd.c, "View already registered with a NativeAd. Auto unregistering and proceeding.");
            NativeAd.d.get(m).get().unregisterView();
        }
        this.s = new a();
        this.m = m;
        if (m instanceof ViewGroup) {
            this.u = new t(m.getContext(), new s() {
                @Override
                public void a(final int n) {
                    if (NativeAd.this.a != null) {
                        NativeAd.this.a.a(n);
                    }
                }
            });
            ((ViewGroup)m).addView((View)this.u);
        }
        final Iterator<View> iterator = list.iterator();
        while (iterator.hasNext()) {
            this.a(iterator.next());
        }
        this.a.a(m, list);
        (this.p = new com.facebook.ads.internal.rewarded_video.a(this.m, this.getMinViewabilityPercentage(), this.i(), true, new com.facebook.ads.internal.rewarded_video.a.listener() {
            @Override
            public void a() {
                NativeAd.this.q.a();
                NativeAd.this.p.b();
                if (NativeAd.this.r == null) {
                    if (NativeAd.this.p != null) {
                        NativeAd.this.p.b();
                        NativeAd.this.p = null;
                    }
                    return;
                }
                NativeAd.this.r.a(NativeAd.this.m);
                NativeAd.this.r.a(NativeAd.this.v);
                NativeAd.this.r.a(NativeAd.this.w);
                NativeAd.this.r.b(NativeAd.this.x != null);
                NativeAd.this.r.c(NativeAd.this.l());
                NativeAd.this.r.a();
            }
        })).a(this.j());
        this.p.b(this.k());
        this.p.a();
        (this.r = new u(this.e, new c(), this.p, this.a)).a(list);
        NativeAd.d.put(m, new WeakReference<NativeAd>(this));
    }
    
    private int getMinViewabilityPercentage() {
        int n = 1;
        if (this.l != null) {
            n = this.l.e();
        }
        else if (this.j != null && this.j.a() != null) {
            n = this.j.a().e();
        }
        return n;
    }
    
    private int i() {
        int n = 0;
        if (this.l != null) {
            n = this.l.f();
        }
        else if (this.j != null && this.j.a() != null) {
            n = this.j.a().f();
        }
        return n;
    }
    
    private int j() {
        if (this.l != null) {
            return this.l.g();
        }
        if (this.a != null) {
            return this.a.i();
        }
        if (this.j != null && this.j.a() != null) {
            return this.j.a().g();
        }
        return 0;
    }
    
    private int k() {
        if (this.l != null) {
            return this.l.h();
        }
        if (this.a != null) {
            return this.a.j();
        }
        if (this.j != null && this.j.a() != null) {
            return this.j.a().h();
        }
        return 1000;
    }
    
    void a(final NativeAdView.Type v) {
        this.v = v;
    }
    
    void a(final boolean w) {
        this.w = w;
    }
    
    void a(final MediaView x) {
        this.x = x;
    }
    
    @Deprecated
    public void setMediaViewAutoplay(final boolean y) {
        this.y = y;
    }
    
    private boolean l() {
        return (this.f() == ai.UNKNOWN) ? this.y : (this.f() == ai.ON);
    }
    
    private void a(final View view) {
        this.n.add(view);
        view.setOnClickListener((View.OnClickListener)this.s);
        view.setOnTouchListener((View.OnTouchListener)this.s);
    }
    
    public void unregisterView() {
        if (this.m == null) {
            return;
        }
        if (!NativeAd.d.containsKey(this.m) || NativeAd.d.get(this.m).get() != this) {
            throw new IllegalStateException("View not registered with this NativeAd");
        }
        if (this.m instanceof ViewGroup && this.u != null) {
            ((ViewGroup)this.m).removeView((View)this.u);
            this.u = null;
        }
        if (this.a != null) {
            this.a.a();
        }
        NativeAd.d.remove(this.m);
        this.m();
        this.m = null;
        if (this.p != null) {
            this.p.b();
            this.p = null;
        }
        this.r = null;
    }
    
    private void m() {
        for (final View view : this.n) {
            view.setOnClickListener((View.OnClickListener)null);
            view.setOnTouchListener((View.OnTouchListener)null);
        }
        this.n.clear();
    }
    
    private void n() {
        if (this.a != null && this.a.c()) {
            (this.t = new b()).a();
            this.r = new u(this.e, new com.facebook.ads.internal.adapters.b() {
                @Override
                public boolean a() {
                    return true;
                }
            }, this.p, this.a);
        }
    }
    
    private void registerExternalLogReceiver(final String a) {
        this.B = true;
        this.A = a;
    }
    
    private void o() {
        if (this.B) {
            this.r = new u(this.e, new c() {
                @Override
                public boolean b() {
                    return true;
                }
                
                @Override
                public String c() {
                    return NativeAd.this.A;
                }
            }, this.p, this.a);
        }
    }
    
    private void logExternalImpression() {
        if (this.r == null) {
            return;
        }
        this.r.a();
    }
    
    private void logExternalClick(final String s) {
        if (this.a == null) {
            return;
        }
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("eil", String.valueOf(true));
        hashMap.put("eil_source", s);
        this.a.b(hashMap);
    }
    
    public void setOnTouchListener(final View.OnTouchListener o) {
        this.o = o;
    }
    
    static {
        b = com.facebook.ads.internal.c.ADS;
        c = NativeAd.class.getSimpleName();
        NativeAd.d = new WeakHashMap<View, WeakReference<NativeAd>>();
    }
    
    private class b extends BroadcastReceiver
    {
        private boolean b;
        
        public void onReceive(final Context context, final Intent intent) {
            final String s = intent.getAction().split(":")[0];
            if ("com.facebook.ads.native.impression".equals(s) && NativeAd.this.r != null) {
                NativeAd.this.r.a();
            }
            else if ("com.facebook.ads.native.click".equals(s) && NativeAd.this.a != null) {
                final HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("mil", String.valueOf(true));
                NativeAd.this.a.b(hashMap);
            }
        }
        
        public void a() {
            final IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.facebook.ads.native.impression:" + NativeAd.this.g);
            intentFilter.addAction("com.facebook.ads.native.click:" + NativeAd.this.g);
            LocalBroadcastManager.getInstance(NativeAd.this.e).registerReceiver((BroadcastReceiver)this, intentFilter);
            this.b = true;
        }
        
        public void b() {
            if (!this.b) {
                return;
            }
            try {
                LocalBroadcastManager.getInstance(NativeAd.this.e).unregisterReceiver((BroadcastReceiver)this);
            }
            catch (Exception ex) {}
        }
    }
    
    private class a implements View.OnClickListener, View.OnTouchListener
    {
        public void onClick(final View view) {
            if (!NativeAd.this.q.d()) {
                Log.e("FBAudienceNetworkLog", "No touch data recorded, please ensure touch events reach the ad View by returning false if you intercept the event.");
            }
            final int k = com.facebook.ads.internal.h.k(NativeAd.this.e);
            if (k >= 0 && NativeAd.this.q.c() < k) {
                if (!NativeAd.this.q.b()) {
                    Log.e("FBAudienceNetworkLog", "Ad cannot be clicked before it is viewed.");
                }
                else {
                    Log.e("FBAudienceNetworkLog", "Clicks happened too fast.");
                }
                return;
            }
            if (!(view instanceof AdChoicesView) && NativeAd.this.q.a(com.facebook.ads.internal.h.l(NativeAd.this.e))) {
                Log.e("FBAudienceNetworkLog", "Clicks are too close to the border of the view.");
                return;
            }
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("touch", com.facebook.ads.internal.util.g.a(NativeAd.this.q.e()));
            if (NativeAd.this.v != null) {
                hashMap.put("nti", String.valueOf(NativeAd.this.v.getValue()));
            }
            if (NativeAd.this.w) {
                hashMap.put("nhs", String.valueOf(NativeAd.this.w));
            }
            NativeAd.this.p.a(hashMap);
            NativeAd.this.a.b(hashMap);
        }
        
        public boolean onTouch(final View view, final MotionEvent motionEvent) {
            NativeAd.this.q.a(motionEvent, NativeAd.this.m, view);
            return NativeAd.this.o != null && NativeAd.this.o.onTouch(view, motionEvent);
        }
    }
    
    private class c extends com.facebook.ads.internal.adapters.b
    {
        @Override
        public void d() {
            if (NativeAd.this.i != null) {
                NativeAd.this.i.onLoggingImpression(NativeAd.this);
            }
        }
        
        @Override
        public void e() {
        }
        
        @Override
        public boolean a() {
            return false;
        }
    }
    
    public enum MediaCacheFlag
    {
        NONE(0L), 
        ICON(1L), 
        IMAGE(2L), 
        VIDEO(3L);
        
        public static final EnumSet<MediaCacheFlag> ALL;
        private final long a;
        
        private MediaCacheFlag(final long a) {
            this.a = a;
        }
        
        public long getCacheFlagValue() {
            return this.a;
        }
        
        static {
            ALL = EnumSet.allOf(MediaCacheFlag.class);
        }
    }
    
    public static class Rating
    {
        private final double a;
        private final double b;
        
        public Rating(final double a, final double b) {
            this.a = a;
            this.b = b;
        }
        
        public double getValue() {
            return this.a;
        }
        
        public double getScale() {
            return this.b;
        }
        
        public static Rating fromJSONObject(final JSONObject jsonObject) {
            if (jsonObject == null) {
                return null;
            }
            final double optDouble = jsonObject.optDouble("value", 0.0);
            final double optDouble2 = jsonObject.optDouble("scale", 0.0);
            if (optDouble == 0.0 || optDouble2 == 0.0) {
                return null;
            }
            return new Rating(optDouble, optDouble2);
        }
    }
    
    public static class Image
    {
        private final String a;
        private final int b;
        private final int c;
        
        public Image(final String a, final int b, final int c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
        
        public String getUrl() {
            return this.a;
        }
        
        public int getWidth() {
            return this.b;
        }
        
        public int getHeight() {
            return this.c;
        }
        
        public static Image fromJSONObject(final JSONObject jsonObject) {
            if (jsonObject == null) {
                return null;
            }
            final String optString = jsonObject.optString("url");
            if (optString == null) {
                return null;
            }
            return new Image(optString, jsonObject.optInt("width", 0), jsonObject.optInt("height", 0));
        }
    }
}
