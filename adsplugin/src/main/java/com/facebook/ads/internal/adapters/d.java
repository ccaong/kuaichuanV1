package com.facebook.ads.internal.adapters;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import com.facebook.ads.internal.util.ag;
import java.util.HashSet;
import java.util.Iterator;
import com.facebook.ads.internal.server.AdPlacementType;
import java.util.Map;
import java.util.Set;

public class d
{
    private static final Set<f> a;
    private static final Map<AdPlacementType, String> b;
    
    public static AdAdapter a(final String s, final AdPlacementType adPlacementType) {
        return a(e.a(s), adPlacementType);
    }
    
    public static AdAdapter a(final e e, final AdPlacementType adPlacementType) {
        AdAdapter adAdapter = null;
        try {
            final f b = b(e, adPlacementType);
            if (b != null && d.a.contains(b)) {
                Class<?> clazz = b.i;
                if (clazz == null) {
                    clazz = Class.forName(b.j);
                }
                adAdapter = (AdAdapter)clazz.newInstance();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return adAdapter;
    }
    
    private static f b(final e e, final AdPlacementType adPlacementType) {
        for (final f f : d.a) {
            if (f.k == e && f.l == adPlacementType) {
                return f;
            }
        }
        return null;
    }
    
    public static String a(final AdPlacementType adPlacementType) {
        if (d.b.containsKey(adPlacementType)) {
            return d.b.get(adPlacementType);
        }
        final HashSet<String> set = new HashSet<String>();
        for (final f f : d.a) {
            if (f.l == adPlacementType) {
                set.add(f.k.toString());
            }
        }
        final String a = ag.a(set, ",");
        d.b.put(adPlacementType, a);
        return a;
    }
    
    static {
        a = new HashSet<f>();
        b = new ConcurrentHashMap<AdPlacementType, String>();
        for (final f ff : f.a()) {
            Serializable s = null;
            switch (ff.l) {
                case BANNER: {
                    s = BannerAdapter.class;
                    break;
                }
                case INTERSTITIAL: {
                    s = InterstitialAdapter.class;
                    break;
                }
                case NATIVE: {
                    s = v.class;
                    break;
                }
                case INSTREAM: {
                    s = r.class;
                    break;
                }
                case REWARDED_VIDEO: {
                    s = x.class;
                    break;
                }
            }
            if (s != null) {
                Class<?> clazz = ff.i;
                if (clazz == null) {
                    try {
                        clazz = Class.forName(ff.j);
                    }
                    catch (ClassNotFoundException ex) {}
                }
                if (clazz == null || !((Class)s).isAssignableFrom(clazz)) {
                    continue;
                }
                d.a.add(ff);
            }
        }
    }
}
