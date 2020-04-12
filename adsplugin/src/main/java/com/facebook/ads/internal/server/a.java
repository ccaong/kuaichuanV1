// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.server;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.Executors;
import org.json.JSONException;
import org.json.JSONObject;

import com.bestgo.adsplugin.ads.AdAppHelper;
import com.bestgo.adsplugin.ads.AdConfig;
import com.bestgo.adsplugin.ads.cache.FBCache;
import com.facebook.ads.internal.fpackage.i;
import com.facebook.ads.internal.ipackage.a.m;
import com.facebook.ads.internal.ipackage.a.n;
import com.facebook.ads.internal.util.d;
import com.facebook.ads.internal.AdErrorType;
import com.facebook.ads.internal.util.w;
import java.util.concurrent.ThreadPoolExecutor;
import com.facebook.ads.internal.util.z;
import com.facebook.ads.internal.fpackage.f;
import java.util.Map;
import com.facebook.ads.internal.h;
import android.content.Context;

public class a
{
    private final Context b;
    private final c c;
    private final h d;
    Map<String, String> aMap;
    private ainterface e;
    private f f;
    private com.facebook.ads.internal.ipackage.a.a g;
    private final String h;
    private static final z i;
    private static final ThreadPoolExecutor j;
    private boolean saveCache;
    
    public a(final Context context) {
        this.b = context.getApplicationContext();
        this.c = com.facebook.ads.internal.server.c.a();
        this.d = new h(this.b);
        this.h = com.facebook.ads.internal.server.b.a();
    }
    
    public void a(final f f) {
        this.a();
        if (w.c(this.b) == w.a.NONE) {
            this.a(new com.facebook.ads.internal.b(AdErrorType.NETWORK_ERROR, "No network connection"));
            return;
        }
        this.f = f;
        com.facebook.ads.internal.util.a.a(this.b);
        if (!com.facebook.ads.internal.util.d.a(f)) {
            com.facebook.ads.internal.server.a.j.submit(new Runnable() {
                @Override
                public void run() {
                    com.facebook.ads.internal.fpackage.i.b(com.facebook.ads.internal.server.a.this.b);
                    a.this.aMap = f.e();
                    try {
                        com.facebook.ads.internal.server.a.this.g = w.b(com.facebook.ads.internal.server.a.this.b, f.e);
                        AdConfig config = AdAppHelper.getInstance(b).getConfig();
                        if (config.ad_ctrl.fb_cache == 1) {
                            saveCache = true;
                        }
                        if (f.isAllowCacheAd()) {
                            if (config.ad_ctrl.fb_cache == 1) {
                                String placement_type = a.this.aMap.get("PLACEMENT_TYPE");
                                String placement_id = a.this.aMap.get("PLACEMENT_ID");
                                FBCache.CacheItem cacheItem = FBCache.getAdFromCache(placement_type, placement_id);
                                if (cacheItem != null) {
                                    com.facebook.ads.internal.util.d.b(com.facebook.ads.internal.server.a.this.f);
                                    com.facebook.ads.internal.server.a.this.g = null;
                                    com.facebook.ads.internal.server.a.this.a(cacheItem.data);
                                } else {
                                    com.facebook.ads.internal.util.d.b(com.facebook.ads.internal.server.a.this.f);
                                    com.facebook.ads.internal.server.a.this.g = null;
                                    com.facebook.ads.internal.server.a.this.a("{\"type\": \"error\",\"code\": 1001,\"message\": \"No fill\"}");
                                }
                            } else {
                                com.facebook.ads.internal.util.d.b(com.facebook.ads.internal.server.a.this.f);
                                com.facebook.ads.internal.server.a.this.g = null;
                                com.facebook.ads.internal.server.a.this.a("{\"type\": \"error\",\"code\": 1001,\"message\": \"No fill\"}");
                            }
                            return;
                        }
                        com.facebook.ads.internal.server.a.this.g.a(com.facebook.ads.internal.server.a.this.h, com.facebook.ads.internal.server.a.this.g.b().a(com.facebook.ads.internal.server.a.this.aMap), com.facebook.ads.internal.server.a.this.b());
                    }
                    catch (Exception ex) {
                        com.facebook.ads.internal.server.a.this.a(AdErrorType.AD_REQUEST_FAILED.getAdErrorWrapper(ex.getMessage()));
                    }
                }
            });
            return;
        }
        final String c = com.facebook.ads.internal.util.d.c(f);
        if (c != null) {
            this.a(c);
            return;
        }
        this.a(AdErrorType.LOAD_TOO_FREQUENTLY.getAdErrorWrapper(null));
    }
    
    private void a(final String s) {
        try {
            final com.facebook.ads.internal.server.d a = this.c.a(s);
            final com.facebook.ads.internal.fpackage.d b = a.b();
            if (b != null) {
                this.d.a(b.b());
                com.facebook.ads.internal.util.d.a(b.a().c(), this.f);
            }
            switch (a.a()) {
                case ADS: {
                    try {
                        String placement_type = this.aMap.get("PLACEMENT_TYPE");
                        String placement_id = this.aMap.get("PLACEMENT_ID");
                        try {
                            String request_id = new JSONObject(s).getJSONArray("placements").getJSONObject(0).getJSONArray("ads").getJSONObject(0).getJSONObject("data").getString("request_id");
                            if (f != null) {
                                f.setRequestId(request_id);
                            }
                            if (!"banner".equals(placement_type) && saveCache) {
                                FBCache.saveToCache(request_id, placement_type, placement_id, s);
                            }
                        } catch (OutOfMemoryError error) {
                        }
                    } catch (Exception ex) {
                    }
                    final e e = (e)a;
                    if (b != null && b.a().d()) {
                        com.facebook.ads.internal.util.d.a(s, this.f);
                    }
                    this.a(e);
                    break;
                }
                case ERROR: {
                    final com.facebook.ads.internal.server.f f = (com.facebook.ads.internal.server.f)a;
                    final String c = f.c();
                    this.a(AdErrorType.adErrorTypeFromCode(f.d(), AdErrorType.ERROR_MESSAGE).getAdErrorWrapper((c != null) ? c : s));
                    break;
                }
                default: {
                    this.a(AdErrorType.UNKNOWN_RESPONSE.getAdErrorWrapper(s));
                    break;
                }
            }
        }
        catch (Exception ex) {
            this.a(AdErrorType.PARSER_FAILURE.getAdErrorWrapper(ex.getMessage()));
        }
    }
    
    private com.facebook.ads.internal.ipackage.a.b b() {
        return new com.facebook.ads.internal.ipackage.a.b() {
            @Override
            public void a(final n n) {
                if (n != null) {
                    final String e = n.e();
                    com.facebook.ads.internal.util.d.b(com.facebook.ads.internal.server.a.this.f);
                    com.facebook.ads.internal.server.a.this.g = null;
                    com.facebook.ads.internal.server.a.this.a(e);
                }
            }

            @Override
            public void a(final Exception ex) {
                if (m.class.equals(ex.getClass())) {
                    this.a((m)ex);
                }
                else {
                    com.facebook.ads.internal.server.a.this.a(new com.facebook.ads.internal.b(AdErrorType.NETWORK_ERROR, ex.getMessage()));
                }
            }
            
            public void a(final m m) {
                com.facebook.ads.internal.util.d.b(com.facebook.ads.internal.server.a.this.f);
                com.facebook.ads.internal.server.a.this.g = null;
                try {
                    final n a = m.a();
                    if (a != null) {
                        final String e = a.e();
                        final com.facebook.ads.internal.server.d a2 = com.facebook.ads.internal.server.a.this.c.a(e);
                        if (a2.a() == com.facebook.ads.internal.server.d.a.ERROR) {
                            final com.facebook.ads.internal.server.f f = (com.facebook.ads.internal.server.f)a2;
                            final String c = f.c();
                            com.facebook.ads.internal.server.a.this.a(AdErrorType.adErrorTypeFromCode(f.d(), AdErrorType.ERROR_MESSAGE).getAdErrorWrapper((c == null) ? e : c));
                            return;
                        }
                    }
                }
                catch (JSONException ex) {}
                com.facebook.ads.internal.server.a.this.a(new com.facebook.ads.internal.b(AdErrorType.NETWORK_ERROR, m.getMessage()));
            }
        };
    }
    
    private void a(final e e) {
        if (this.e != null) {
            this.e.a(e);
        }
        this.a();
    }
    
    private void a(final com.facebook.ads.internal.b b) {
        if (this.e != null) {
            this.e.a(b);
        }
        this.a();
    }
    
    public void a() {
        if (this.g != null) {
            this.g.c(1);
            this.g.b(1);
            this.g = null;
        }
    }
    
    public void a(final ainterface e) {
        this.e = e;
    }
    
    static {
        i = new z();
        j = (ThreadPoolExecutor)Executors.newCachedThreadPool(a.i);
    }
    
    public interface ainterface
    {
        void a(final e p0);
        
        void a(final com.facebook.ads.internal.b p0);
    }
}
