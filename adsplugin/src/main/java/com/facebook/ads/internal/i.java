// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal;

import com.facebook.ads.internal.util.o;
import com.facebook.ads.internal.util.ak;
import com.facebook.ads.internal.adapters.AdAdapter;
import java.util.Map;
import com.facebook.ads.internal.gpackage.g;
import com.facebook.ads.AdError;
import com.facebook.ads.internal.adapters.w;
import java.util.HashMap;
import com.facebook.ads.internal.server.AdPlacementType;
import java.util.ArrayList;
import com.facebook.ads.internal.adapters.v;
import java.util.List;
import com.facebook.ads.internal.fpackage.f;
import com.facebook.ads.AdSettings;
import com.facebook.ads.NativeAd;
import java.util.EnumSet;
import com.facebook.ads.internal.fpackage.d;
import android.os.Handler;
import com.facebook.ads.AdSize;
import android.content.Context;
import com.facebook.ads.internal.server.a;

public class i implements com.facebook.ads.internal.server.a.ainterface
{
    private final Context a;
    private final String b;
    private final com.facebook.ads.internal.server.a c;
    private final e d;
    private final c e;
    private final AdSize f;
    private final int g;
    private boolean h;
    private final Handler i;
    private final Runnable j;
    private a k;
    private d l;
    
    public i(final Context a, final String b, final e d, final AdSize f, final c e, final int g, final EnumSet<NativeAd.MediaCacheFlag> set) {
        this.a = a;
        this.b = b;
        this.d = d;
        this.f = f;
        this.e = e;
        this.g = g;
        (this.c = new com.facebook.ads.internal.server.a(a)).a(this);
        this.h = true;
        this.i = new Handler();
        this.j = new b(this);
        com.facebook.ads.internal.dpackage.a.a(a).a();
    }
    
    public void a(final a k) {
        this.k = k;
    }
    
    public void a() {
        this.c.a(new f(this.a, this.b, this.f, this.d, this.e, this.g, AdSettings.isTestMode(this.a), false));
    }
    
    private List<v> d() {
        final d l = this.l;
        com.facebook.ads.internal.fpackage.a a = l.d();
        final ArrayList list = new ArrayList<v>(l.c());
        while (a != null) {
            final AdAdapter a2 = com.facebook.ads.internal.adapters.d.a(a.a(), AdPlacementType.NATIVE);
            if (a2 != null && a2.getPlacementType() == AdPlacementType.NATIVE) {
                final HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("data", a.b());
                hashMap.put("definition", l.a());
                ((v)a2).a(this.a, new w() {
                    @Override
                    public void a(final v v) {
                        list.add(v);
                    }
                    
                    @Override
                    public void a(final v v, final AdError adError) {
                    }
                    
                    @Override
                    public void b(final v v) {
                    }
                    
                    @Override
                    public void c(final v v) {
                    }
                }, com.facebook.ads.internal.gpackage.g.a(this.a), hashMap);
            }
            a = l.d();
        }
        return (List<v>)list;
    }
    
    @Override
    public void a(final com.facebook.ads.internal.server.e e) {
        final d b = e.b();
        if (b == null) {
            throw new IllegalStateException("no placement in response");
        }
        if (this.h) {
            long b2 = b.a().b();
            if (b2 == 0L) {
                b2 = 1800000L;
            }
            this.i.postDelayed(this.j, b2);
        }
        this.l = b;
        final List<v> d = this.d();
        if (this.k != null) {
            if (d.isEmpty()) {
                this.k.a(AdErrorType.NO_FILL.getAdErrorWrapper(""));
                return;
            }
            this.k.a(d);
        }
    }
    
    @Override
    public void a(final com.facebook.ads.internal.b b) {
        if (this.h) {
            this.i.postDelayed(this.j, 1800000L);
        }
        if (this.k != null) {
            this.k.a(b);
        }
    }
    
    public void b() {
    }
    
    public void c() {
        this.h = false;
        this.i.removeCallbacks(this.j);
    }
    
    private static final class b extends ak<i>
    {
        public b(final i i) {
            super(i);
        }
        
        @Override
        public void run() {
            final i i = this.a();
            if (i == null) {
                return;
            }
            if (o.a(i.a)) {
                i.a();
            }
            else {
                i.i.postDelayed(i.j, 5000L);
            }
        }
    }
    
    public interface a
    {
        void a(final List<v> p0);
        
        void a(final com.facebook.ads.internal.b p0);
    }
}
