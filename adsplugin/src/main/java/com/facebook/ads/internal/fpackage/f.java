//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.facebook.ads.internal.fpackage;

import android.content.Context;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdSettings.TestAdType;
import com.facebook.ads.internal.d;
import com.facebook.ads.internal.e;
import com.facebook.ads.internal.fpackage.c;
import com.facebook.ads.internal.fpackage.g;
import com.facebook.ads.internal.fpackage.i;
import com.facebook.ads.internal.server.AdPlacementType;
import com.facebook.ads.internal.util.al;
import com.facebook.ads.internal.util.o;
import com.facebook.ads.internal.util.s;
import com.facebook.ads.internal.util.w;
import com.facebook.ads.internal.util.o.a;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class f {
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static String h = null;
    private static a ii = o.a();
    protected String a;
    protected AdPlacementType b;
    protected c c;
    public Context context;
    public e e;
    private com.facebook.ads.internal.c j;
    public boolean ff;
    private int k;
    private AdSize l;
    private boolean allowCached;
    private String requestId;

    public f(Context var1, String var2, AdSize var3, e var4, com.facebook.ads.internal.c var5, int var6, boolean var7, boolean allowCached) {
        this.a = var2;
        this.l = var3;
        this.e = var4;
        this.c = c.a(var4);
        this.j = var5;
        this.k = var6;
        this.ff = var7;
        this.allowCached = allowCached;
        this.a(var1);
    }

    private void a(final Context var1) {
        this.context = var1;
        g.a();
        i.a(var1);
        this.g();
        executorService.submit(new Runnable() {
            public void run() {
                if(f.h == null) {
                    f.h = s.a(var1, var1.getPackageName());
                }

            }
        });
    }

    private void g() {
        if(this.c == null) {
            this.c = c.UNKNOWN;
        }

        switch(c) {
            case INTERSTITIAL:
                this.b = AdPlacementType.INTERSTITIAL;
                break;
            case BANNER:
                this.b = AdPlacementType.BANNER;
                break;
            case NATIVE:
                this.b = AdPlacementType.NATIVE;
                break;
            case REWARDED_VIDEO:
                this.b = AdPlacementType.REWARDED_VIDEO;
                break;
            default:
                this.b = AdPlacementType.UNKNOWN;
        }

    }

    public void setRequestId(String id) {
        requestId = id;
    }

    public String getRequestId() {
        return requestId;
    }

    public boolean isAllowCacheAd() {
        return allowCached;
    }

    public String a() {
        return this.a;
    }

    public c b() {
        return this.c;
    }

    public AdSize c() {
        return this.l;
    }

    public int d() {
        return this.k;
    }

    private static Map<String, String> b(Context var0) {
        HashMap var1 = new HashMap();
        var1.put("SDK", "android");
        var1.put("SDK_VERSION", "4.23.0");
        var1.put("LOCALE", Locale.getDefault().toString());
        float var2 = var0.getResources().getDisplayMetrics().density;
        int var3 = var0.getResources().getDisplayMetrics().widthPixels;
        int var4 = var0.getResources().getDisplayMetrics().heightPixels;
        var1.put("DENSITY", String.valueOf(var2));
        var1.put("SCREEN_WIDTH", String.valueOf((int)((float)var3 / var2)));
        var1.put("SCREEN_HEIGHT", String.valueOf((int)((float)var4 / var2)));
        var1.put("IDFA", i.o);
        var1.put("IDFA_FLAG", i.p?"0":"1");
        var1.put("ATTRIBUTION_ID", i.n);
        var1.put("ID_SOURCE", i.q);
        var1.put("OS", "Android");
        var1.put("OSVERS", i.a);
        var1.put("BUNDLE", i.d);
        var1.put("APPNAME", i.e);
        var1.put("APPVERS", i.f);
        var1.put("APPBUILD", String.valueOf(i.g));
        var1.put("CARRIER", i.i);
        var1.put("MAKE", i.b);
        var1.put("MODEL", i.c);
        var1.put("ROOTED", String.valueOf(ii.d));
        var1.put("COPPA", String.valueOf(AdSettings.isChildDirected()));
        var1.put("INSTALLER", i.h);
        var1.put("SDK_CAPABILITY", d.b());
        var1.put("NETWORK_TYPE", String.valueOf(w.c(var0).g));
        var1.put("REQUEST_TIME", com.facebook.ads.internal.util.g.a(System.currentTimeMillis()));
        var1.put("SESSION_TIME", com.facebook.ads.internal.util.g.a(g.b()));
        var1.put("SESSION_ID", g.c());
        return var1;
    }

    public Map<String, String> e() {
        HashMap var1 = new HashMap();
        this.a(var1, "PLACEMENT_ID", this.a);
        if(this.b != AdPlacementType.UNKNOWN) {
            this.a(var1, "PLACEMENT_TYPE", this.b.toString().toLowerCase());
        }

        Map var2 = b(this.context);
        Iterator var3 = var2.entrySet().iterator();

        while(var3.hasNext()) {
            Entry var4 = (Entry)var3.next();
            this.a(var1, (String)var4.getKey(), (String)var4.getValue());
        }

        if(this.l != null) {
            this.a(var1, "WIDTH", String.valueOf(this.l.getWidth()));
            this.a(var1, "HEIGHT", String.valueOf(this.l.getHeight()));
        }

        this.a(var1, "ADAPTERS", com.facebook.ads.internal.adapters.d.a(this.b));
        if(this.e != null) {
            this.a(var1, "TEMPLATE_ID", String.valueOf(this.e.a()));
        }

        if(this.j != null) {
            this.a(var1, "REQUEST_TYPE", String.valueOf(this.j.a()));
        }

        if(this.ff) {
            this.a(var1, "TEST_MODE", "1");
        }

        if(AdSettings.getTestAdType() != TestAdType.DEFAULT) {
            this.a(var1, "DEMO_AD_ID", AdSettings.getTestAdType().getAdTypeString());
        }

        if(this.k != 0) {
            this.a(var1, "NUM_ADS_REQUESTED", String.valueOf(this.k));
        }

        String var5 = AdSettings.getMediationService();
        if(var5 != null) {
            this.a(var1, "MEDIATION_SERVICE", var5);
        }

        this.a(var1, "CLIENT_EVENTS", com.facebook.ads.internal.util.c.a());
        if(h != null) {
            this.a(var1, "AFP", h);
        }

        this.a(var1, "UNITY", String.valueOf(com.facebook.ads.internal.util.g.a(this.context)));
        this.a(var1, "KG_RESTRICTED", String.valueOf(al.b(this.context)));
        return var1;
    }

    private void a(Map<String, String> var1, String var2, String var3) {
        var1.put(var2, var3);
    }
}
