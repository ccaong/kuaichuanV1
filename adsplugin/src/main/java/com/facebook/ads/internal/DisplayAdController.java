// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal;

import com.facebook.ads.internal.util.ak;
import android.os.Looper;
import java.util.Iterator;
import com.facebook.ads.internal.util.o;
import com.facebook.ads.internal.adapters.w;
import java.util.List;
import com.facebook.ads.internal.fpackage.h;
import com.facebook.ads.internal.adapters.t;
import android.net.Uri;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import com.facebook.ads.internal.adapters.InterstitialAdapterListener;
import com.facebook.ads.internal.adapters.BannerAdapterListener;
import com.facebook.ads.internal.adapters.y;
import com.facebook.ads.AdError;
import java.util.Map;
import com.facebook.ads.internal.adapters.BannerAdapter;
import java.util.HashMap;
import com.facebook.ads.internal.adapters.x;
import com.facebook.ads.internal.adapters.r;
import com.facebook.ads.internal.adapters.v;
import com.facebook.ads.internal.adapters.InterstitialAdapter;
import com.facebook.ads.AdSettings;
import com.facebook.ads.internal.util.b;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import com.facebook.ads.internal.gpackage.g;
import android.util.Log;
import android.webkit.CookieSyncManager;
import android.os.Build;
import android.webkit.CookieManager;
import com.facebook.ads.AdSize;
import com.facebook.ads.internal.fpackage.f;
import com.facebook.ads.internal.fpackage.d;
import android.view.View;
import com.facebook.ads.internal.adapters.AdAdapter;
import android.os.Handler;
import com.facebook.ads.internal.server.AdPlacementType;
import android.content.Context;
import com.facebook.ads.internal.server.a;

public class DisplayAdController implements com.facebook.ads.internal.server.a.ainterface
{
    protected com.facebook.ads.internal.a a;
    private static final String b;
    private final Context c;
    private final String d;
    private final AdPlacementType e;
    private final com.facebook.ads.internal.server.a f;
    private final Handler g;
    private static final Handler h;
    private static boolean i;
    private final Runnable j;
    private final Runnable k;
    private volatile boolean l;
    private boolean m;
    private volatile boolean n;
    private AdAdapter o;
    private AdAdapter p;
    private View q;
    private d r;
    private f s;
    private e t;
    private com.facebook.ads.internal.c u;
    private AdSize v;
    private int w;
    private final c x;
    private boolean y;
    private final com.facebook.ads.internal.gpackage.f z;
    private boolean allowCache;
    
    public DisplayAdController(final Context c, final String d, final e t, final AdPlacementType e, final AdSize v, final com.facebook.ads.internal.c u, final int w, final boolean m, boolean useCache) {
        this.g = new Handler();
        this.c = c;
        this.d = d;
        this.t = t;
        this.e = e;
        this.v = v;
        this.u = u;
        this.w = w;
        this.x = new c();
        (this.f = new com.facebook.ads.internal.server.a(c)).a(this);
        this.j = new a(this);
        this.k = new b(this);
        this.m = m;
        this.j();
        try {
            CookieManager.getInstance();
            if (Build.VERSION.SDK_INT < 21) {
                CookieSyncManager.createInstance(c);
            }
        }
        catch (Exception ex) {
            Log.w(DisplayAdController.b, "Failed to initialize CookieManager.", (Throwable)ex);
        }
        com.facebook.ads.internal.dpackage.a.a(c).a();
        this.z = com.facebook.ads.internal.gpackage.g.a(c);
        allowCache = useCache;
    }
    
    private void j() {
        if (this.m) {
            return;
        }
        final IntentFilter intentFilter = new IntentFilter("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        this.c.registerReceiver((BroadcastReceiver)this.x, intentFilter);
        this.y = true;
    }
    
    private void k() {
        if (this.y) {
            try {
                this.c.unregisterReceiver((BroadcastReceiver)this.x);
                this.y = false;
            }
            catch (Exception ex) {
                com.facebook.ads.internal.util.c.a(com.facebook.ads.internal.util.b.a(ex, "Error unregistering screen state receiever"));
            }
        }
    }
    
    public void a(final com.facebook.ads.internal.a a) {
        this.a = a;
    }
    
    public com.facebook.ads.internal.fpackage.e a() {
        if (this.r == null) {
            return null;
        }
        return this.r.a();
    }
    
    private void a(final AdAdapter adAdapter) {
        if (adAdapter != null) {
            adAdapter.onDestroy();
        }
    }
    
    private AdPlacementType l() {
        if (this.e != null) {
            return this.e;
        }
        if (this.v == null) {
            return AdPlacementType.NATIVE;
        }
        if (this.v == AdSize.INTERSTITIAL) {
            return AdPlacementType.INTERSTITIAL;
        }
        return AdPlacementType.BANNER;
    }
    
    public void b() {
        this.m();
    }

    private void m() {
        this.s = new f(this.c, this.d, this.v, this.t, this.u, this.w, AdSettings.isTestMode(this.c), allowCache);
        this.f.a(this.s);
    }

    public String getRequestId() {
        if (s != null) {
            return s.getRequestId();
        }
        return null;
    }
    
    public void c() {
        if (this.p == null) {
            throw new IllegalStateException("no adapter ready to start");
        }
        if (this.n) {
            throw new IllegalStateException("ad already started");
        }
        this.n = true;
        switch (this.p.getPlacementType()) {
            case INTERSTITIAL: {
                ((InterstitialAdapter)this.p).show();
                break;
            }
            case BANNER: {
                if (this.q != null) {
                    this.a.a(this.q);
                    this.p();
                    break;
                }
                break;
            }
            case NATIVE: {
                final v v = (v)this.p;
                if (!v.b()) {
                    throw new IllegalStateException("ad is not ready or already displayed");
                }
                this.a.a(v);
                break;
            }
            case INSTREAM: {
                ((r)this.p).d();
                break;
            }
            case REWARDED_VIDEO: {
                ((x)this.p).b();
                break;
            }
            default: {
                Log.e(DisplayAdController.b, "start unexpected adapter type");
                break;
            }
        }
    }
    
    public void d() {
        this.k();
        if (!this.n) {
            return;
        }
        this.q();
        this.a(this.p);
        this.q = null;
        this.n = false;
    }
    
    public void e() {
        if (!this.n) {
            return;
        }
        this.q();
    }
    
    public void f() {
        if (!this.n) {
            return;
        }
        this.p();
    }
    
    public void g() {
        this.q();
        this.m();
    }
    
    public void h() {
        this.m = true;
        this.q();
    }
    
    @Override
    public synchronized void a(final com.facebook.ads.internal.server.e e) {
        this.r().post((Runnable)new Runnable() {
            @Override
            public void run() {
                final d b = e.b();
                if (b == null || b.a() == null) {
                    throw new IllegalStateException("invalid placement in response");
                }
                DisplayAdController.this.r = b;
                DisplayAdController.this.n();
            }
        });
    }
    
    private synchronized void n() {
        DisplayAdController.h.post((Runnable)new Runnable() {
            @Override
            public void run() {
                try {
                    DisplayAdController.this.o();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
    
    private void o() {
        this.o = null;
        final d r = this.r;
        final com.facebook.ads.internal.fpackage.a d = r.d();
        if (d == null) {
            this.a.a(AdErrorType.NO_FILL.getAdErrorWrapper(""));
            this.p();
            return;
        }
        final String a = d.a();
        final AdAdapter a2 = com.facebook.ads.internal.adapters.d.a(a, r.a().a());
        if (a2 == null) {
            Log.e(DisplayAdController.b, "Adapter does not exist: " + a);
            this.n();
            return;
        }
        if (this.l() != a2.getPlacementType()) {
            this.a.a(AdErrorType.INTERNAL_ERROR.getAdErrorWrapper(""));
            return;
        }
        this.o = a2;
        final HashMap<String, Object> hashMap = new HashMap<String, Object>();
        final com.facebook.ads.internal.fpackage.e a3 = r.a();
        hashMap.put("data", d.b());
        hashMap.put("definition", a3);
        if (this.s == null) {
            this.a.a(AdErrorType.UNKNOWN_ERROR.getAdErrorWrapper("environment is empty"));
            return;
        }
        switch (a2.getPlacementType()) {
            case BANNER: {
                this.a((BannerAdapter)a2, r, (Map<String, Object>)hashMap);
                break;
            }
            case INTERSTITIAL: {
                this.a((InterstitialAdapter)a2, r, (Map<String, Object>)hashMap);
                break;
            }
            case NATIVE: {
                this.a((v)a2, r, d, (Map<String, Object>)hashMap);
                break;
            }
            case INSTREAM: {
                this.a((r)a2, r, (Map<String, Object>)hashMap);
                break;
            }
            case REWARDED_VIDEO: {
                hashMap.put("placement_id", this.d);
                this.a((x)a2, r, (Map<String, Object>)hashMap);
                break;
            }
            default: {
                Log.e(DisplayAdController.b, "attempt unexpected adapter type");
                break;
            }
        }
    }
    
    private void a(final r r, final d d, final Map<String, Object> map) {
        r.a(this.c, new com.facebook.ads.a.a() {
            @Override
            public void a(final r r) {
                DisplayAdController.this.p = r;
                DisplayAdController.this.n = false;
                DisplayAdController.this.a.a(r);
            }
            
            @Override
            public void a(final r r, final View view) {
                DisplayAdController.this.a.a(view);
            }
            
            @Override
            public void a(final r r, final AdError adError) {
                DisplayAdController.this.a.a(new com.facebook.ads.internal.b(adError.getErrorCode(), adError.getErrorMessage()));
            }
            
            @Override
            public void b(final r r) {
                DisplayAdController.this.a.a();
            }
            
            @Override
            public void c(final r r) {
                DisplayAdController.this.a.b();
            }
            
            @Override
            public void d(final r r) {
                DisplayAdController.this.a.c();
            }
        }, map, this.z);
    }
    
    private void a(final x x, final d d, final Map<String, Object> map) {
        x.a(this.c, new y() {
            @Override
            public void a(final x x) {
                DisplayAdController.this.p = x;
                DisplayAdController.this.a.a(x);
            }
            
            @Override
            public void a(final x x, final AdError adError) {
                DisplayAdController.this.a.a(new com.facebook.ads.internal.b(AdErrorType.INTERNAL_ERROR, null));
                DisplayAdController.this.a(x);
                DisplayAdController.this.n();
            }
            
            @Override
            public void b(final x x) {
                DisplayAdController.this.a.a();
            }
            
            @Override
            public void c(final x x) {
                DisplayAdController.this.a.b();
            }
            
            @Override
            public void d(final x x) {
                DisplayAdController.this.a.f();
            }
            
            @Override
            public void a() {
                DisplayAdController.this.a.g();
            }
            
            @Override
            public void e(final x x) {
                DisplayAdController.this.a.h();
            }
            
            @Override
            public void f(final x x) {
                DisplayAdController.this.a.i();
            }
        }, map);
    }
    
    private void a(final BannerAdapter bannerAdapter, final d d, final Map<String, Object> map) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                DisplayAdController.this.a(bannerAdapter);
                DisplayAdController.this.n();
            }
        };
        this.g.postDelayed((Runnable)runnable, (long)d.a().i());
        bannerAdapter.loadBannerAd(this.c, this.v, new BannerAdapterListener() {
            @Override
            public void onBannerAdLoaded(final BannerAdapter bannerAdapter, final View view) {
                if (bannerAdapter != DisplayAdController.this.o) {
                    return;
                }
                DisplayAdController.this.g.removeCallbacks(runnable);
                final AdAdapter g = DisplayAdController.this.p;
                DisplayAdController.this.p = bannerAdapter;
                DisplayAdController.this.q = view;
                if (!DisplayAdController.this.n) {
                    DisplayAdController.this.a.a(bannerAdapter);
                }
                else {
                    DisplayAdController.this.a.a(view);
                    DisplayAdController.this.a(g);
                    DisplayAdController.this.p();
                }
            }
            
            @Override
            public void onBannerError(final BannerAdapter bannerAdapter, final AdError adError) {
                if (bannerAdapter != DisplayAdController.this.o) {
                    return;
                }
                DisplayAdController.this.g.removeCallbacks(runnable);
                DisplayAdController.this.a(bannerAdapter);
                DisplayAdController.this.n();
            }
            
            @Override
            public void onBannerLoggingImpression(final BannerAdapter bannerAdapter) {
                DisplayAdController.this.a.b();
            }
            
            @Override
            public void onBannerAdClicked(final BannerAdapter bannerAdapter) {
                DisplayAdController.this.a.a();
            }
            
            @Override
            public void onBannerAdExpanded(final BannerAdapter bannerAdapter) {
                DisplayAdController.this.q();
            }
            
            @Override
            public void onBannerAdMinimized(final BannerAdapter bannerAdapter) {
                DisplayAdController.this.p();
            }
        }, map);
    }
    
    private void a(final InterstitialAdapter interstitialAdapter, final d d, final Map<String, Object> map) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                DisplayAdController.this.a(interstitialAdapter);
                DisplayAdController.this.n();
            }
        };
        this.g.postDelayed((Runnable)runnable, (long)d.a().i());
        interstitialAdapter.loadInterstitialAd(this.c, new InterstitialAdapterListener() {
            @Override
            public void onInterstitialAdLoaded(final InterstitialAdapter interstitialAdapter) {
                if (interstitialAdapter != DisplayAdController.this.o) {
                    return;
                }
                DisplayAdController.this.g.removeCallbacks(runnable);
                DisplayAdController.this.p = interstitialAdapter;
                DisplayAdController.this.a.a(interstitialAdapter);
                DisplayAdController.this.p();
            }
            
            @Override
            public void onInterstitialError(final InterstitialAdapter interstitialAdapter, final AdError adError) {
                if (interstitialAdapter != DisplayAdController.this.o) {
                    return;
                }
                DisplayAdController.this.g.removeCallbacks(runnable);
                DisplayAdController.this.a(interstitialAdapter);
                DisplayAdController.this.n();
                DisplayAdController.this.a.a(new com.facebook.ads.internal.b(adError.getErrorCode(), adError.getErrorMessage()));
            }
            
            @Override
            public void onInterstitialLoggingImpression(final InterstitialAdapter interstitialAdapter) {
                DisplayAdController.this.a.b();
            }
            
            @Override
            public void onInterstitialAdClicked(final InterstitialAdapter interstitialAdapter, final String s, final boolean b) {
                DisplayAdController.this.a.a();
                final boolean b2 = !TextUtils.isEmpty((CharSequence)s);
                if (b && b2) {
                    final Intent intent = new Intent("android.intent.action.VIEW");
                    if (!(DisplayAdController.this.s.context instanceof Activity)) {
                        intent.addFlags(268435456);
                    }
                    intent.setData(Uri.parse(s));
                    try {
                        DisplayAdController.this.s.context.startActivity(intent);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            
            @Override
            public void onInterstitialAdDisplayed(final InterstitialAdapter interstitialAdapter) {
                DisplayAdController.this.a.d();
            }
            
            @Override
            public void onInterstitialAdDismissed(final InterstitialAdapter interstitialAdapter) {
                DisplayAdController.this.a.e();
            }
        }, map, this.z);
    }
    
    private void a(final v v, final d d, final com.facebook.ads.internal.fpackage.a aa, final Map<String, Object> map) {
        final long currentTimeMillis = System.currentTimeMillis();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                DisplayAdController.this.a(v);
                if (v instanceof t) {
                    com.facebook.ads.internal.util.g.a(DisplayAdController.this.c, com.facebook.ads.internal.util.v.a(((t)v).D()) + " Failed. Ad request timed out");
                }
                final Map a = DisplayAdController.this.a(currentTimeMillis);
                a.put("error", "-1");
                a.put("msg", "timeout");
                DisplayAdController.this.a(aa.a(com.facebook.ads.internal.fpackage.h.REQUEST), a);
                DisplayAdController.this.n();
            }
        };
        this.g.postDelayed((Runnable)runnable, (long)d.a().i());
        v.a(this.c, new w() {
            boolean a = false;
            boolean b = false;
            boolean c = false;
            
            @Override
            public void a(final v v) {
                if (v != DisplayAdController.this.o) {
                    return;
                }
                DisplayAdController.this.g.removeCallbacks(runnable);
                DisplayAdController.this.p = v;
                DisplayAdController.this.a.a((AdAdapter)v);
                if (!this.a) {
                    this.a = true;
                    DisplayAdController.this.a(aa.a(com.facebook.ads.internal.fpackage.h.REQUEST), DisplayAdController.this.a(currentTimeMillis));
                }
            }
            
            @Override
            public void a(final v v, final AdError adError) {
                if (v != DisplayAdController.this.o) {
                    return;
                }
                DisplayAdController.this.g.removeCallbacks(runnable);
                DisplayAdController.this.a(v);
                if (!this.a) {
                    this.a = true;
                    final Map a = DisplayAdController.this.a(currentTimeMillis);
                    a.put("error", String.valueOf(adError.getErrorCode()));
                    a.put("msg", String.valueOf(adError.getErrorMessage()));
                    DisplayAdController.this.a(aa.a(com.facebook.ads.internal.fpackage.h.REQUEST), a);
                }
                DisplayAdController.this.n();
            }
            
            @Override
            public void b(final v v) {
                if (!this.b) {
                    this.b = true;
                    DisplayAdController.this.a(aa.a(com.facebook.ads.internal.fpackage.h.IMPRESSION), null);
                }
            }
            
            @Override
            public void c(final v v) {
                if (!this.c) {
                    this.c = true;
                    DisplayAdController.this.a(aa.a(com.facebook.ads.internal.fpackage.h.CLICK), null);
                }
                if (DisplayAdController.this.a != null) {
                    DisplayAdController.this.a.a();
                }
            }
        }, this.z, map);
    }
    
    @Override
    public synchronized void a(final com.facebook.ads.internal.b b) {
        this.r().post((Runnable)new Runnable() {
            @Override
            public void run() {
                DisplayAdController.this.a.a(b);
                if (DisplayAdController.this.m || DisplayAdController.this.l) {
                    return;
                }
                Label_0134: {
                    switch (b.a().getErrorCode()) {
                        case 1000:
                        case 1002: {
                            switch (DisplayAdController.this.l()) {
                                case BANNER: {
                                    DisplayAdController.this.g.postDelayed(DisplayAdController.this.j, 30000L);
                                    DisplayAdController.this.l = true;
                                    break Label_0134;
                                }
                            }
                            break;
                        }
                    }
                }
            }
        });
    }
    
    private void p() {
        if (this.m || this.l) {
            return;
        }
        switch (this.l()) {
            case BANNER: {
                final boolean a = com.facebook.ads.internal.rewarded_video.a.a(this.q, (this.r == null) ? 1 : this.r.a().e()).a();
                if (this.q != null && !a) {
                    this.g.postDelayed(this.k, 1000L);
                    return;
                }
                break;
            }
            case INTERSTITIAL: {
                if (!com.facebook.ads.internal.util.o.a(this.c)) {
                    this.g.postDelayed(this.k, 1000L);
                    break;
                }
                break;
            }
            default: {
                return;
            }
        }
        final long n = (this.r == null) ? 30000L : this.r.a().b();
        if (n > 0L) {
            this.g.postDelayed(this.j, n);
            this.l = true;
        }
    }
    
    private void q() {
        if (!this.l) {
            return;
        }
        this.g.removeCallbacks(this.j);
        this.l = false;
    }
    
    private void a(final List<String> list, final Map<String, String> map) {
        if (list == null || list.isEmpty()) {
            return;
        }
        final Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            new com.facebook.ads.internal.util.x(map).execute(new String[] { iterator.next() });
        }
    }
    
    private Map<String, String> a(final long n) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("delay", String.valueOf(System.currentTimeMillis() - n));
        return hashMap;
    }
    
    private Handler r() {
        if (!s()) {
            return this.g;
        }
        return DisplayAdController.h;
    }
    
    private static synchronized boolean s() {
        return DisplayAdController.i;
    }
    
    protected static synchronized void setMainThreadForced(final boolean i) {
        Log.d(DisplayAdController.b, "DisplayAdController changed main thread forced from " + DisplayAdController.i + " to " + i);
        DisplayAdController.i = i;
    }
    
    public AdAdapter i() {
        return this.p;
    }
    
    static {
        b = DisplayAdController.class.getSimpleName();
        h = new Handler(Looper.getMainLooper());
        DisplayAdController.i = false;
    }
    
    private class c extends BroadcastReceiver
    {
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            if ("android.intent.action.SCREEN_OFF".equals(action)) {
                DisplayAdController.this.q();
            }
            else if ("android.intent.action.SCREEN_ON".equals(action)) {
                DisplayAdController.this.p();
            }
        }
    }
    
    private static final class b extends ak<DisplayAdController>
    {
        public b(final DisplayAdController displayAdController) {
            super(displayAdController);
        }
        
        @Override
        public void run() {
            final DisplayAdController displayAdController = this.a();
            if (displayAdController == null) {
                return;
            }
            displayAdController.p();
        }
    }
    
    private static final class a extends ak<DisplayAdController>
    {
        public a(final DisplayAdController displayAdController) {
            super(displayAdController);
        }
        
        @Override
        public void run() {
            final DisplayAdController displayAdController = this.a();
            if (displayAdController == null) {
                return;
            }
            displayAdController.l = false;
            displayAdController.m();
        }
    }
}
