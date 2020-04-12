package com.facebook.ads.internal.adapters;

import java.util.ArrayList;
import com.facebook.ads.internal.server.AdPlacementType;
import java.util.List;

public enum f
{
    ANBANNER((Class<?>)h.class, com.facebook.ads.internal.adapters.e.AN, AdPlacementType.BANNER),//a
    ANINTERSTITIAL((Class<?>)j.class, com.facebook.ads.internal.adapters.e.AN, AdPlacementType.INTERSTITIAL),//b
    ADMOBNATIVE((Class<?>)c.class, com.facebook.ads.internal.adapters.e.ADMOB, AdPlacementType.NATIVE),//c
    ANNATIVE((Class<?>)l.class, com.facebook.ads.internal.adapters.e.AN, AdPlacementType.NATIVE),//d
    ANINSTREAMVIDEO((Class<?>)i.class, com.facebook.ads.internal.adapters.e.AN, AdPlacementType.INSTREAM),//e
    ANREWARDEDVIDEO((Class<?>)m.class, com.facebook.ads.internal.adapters.e.AN, AdPlacementType.REWARDED_VIDEO),//f
    INMOBINATIVE((Class<?>)q.class, com.facebook.ads.internal.adapters.e.INMOBI, AdPlacementType.NATIVE),//g
    YAHOONATIVE((Class<?>)n.class, com.facebook.ads.internal.adapters.e.YAHOO, AdPlacementType.NATIVE);//h
    
    private static List<f> m;
    public Class<?> i;
    public String j;
    public e k;
    public AdPlacementType l;
    
    private f(final Class<?> i, final e k, final AdPlacementType l) {
        this.i = i;
        this.k = k;
        this.l = l;
    }
    
    public static List<f> a() {
        if (f.m == null) {
            synchronized (f.class) {
                (f.m = new ArrayList<f>()).add(f.ANBANNER);
                f.m.add(f.ANINTERSTITIAL);
                f.m.add(f.ANNATIVE);
                f.m.add(f.ANINSTREAMVIDEO);
                f.m.add(f.ANREWARDEDVIDEO);
                if (com.facebook.ads.internal.hpackage.a.a(com.facebook.ads.internal.adapters.e.YAHOO)) {
                    f.m.add(f.YAHOONATIVE);
                }
                if (com.facebook.ads.internal.hpackage.a.a(com.facebook.ads.internal.adapters.e.INMOBI)) {
                    f.m.add(f.INMOBINATIVE);
                }
                if (com.facebook.ads.internal.hpackage.a.a(com.facebook.ads.internal.adapters.e.ADMOB)) {
                    f.m.add(f.ADMOBNATIVE);
                }
            }
        }
        return f.m;
    }
}
