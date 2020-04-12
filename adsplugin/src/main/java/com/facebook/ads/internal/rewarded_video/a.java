// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.rewarded_video;//j

import com.bestgo.adsplugin.ads.AdAppHelper;
import com.bestgo.adsplugin.ads.AdConfig;
import com.facebook.ads.internal.util.ak;
import com.facebook.ads.internal.util.g;
import org.json.JSONObject;
import android.util.DisplayMetrics;
import android.content.Context;
import com.facebook.ads.internal.util.al;
import com.facebook.ads.internal.util.o;
import com.facebook.ads.internal.h;
import android.graphics.Rect;
import java.util.HashMap;
import java.util.Map;
import android.os.Handler;
import android.view.View;

public class a
{
    private static final String TAG;
    private final View view;//b
    private final int cc;//c
    private final int d;
    private final listener e;
    private final Handler f;
    private final Runnable g;
    private final boolean hh;//h
    private int i;
    private int j;
    private boolean k;
    private com.facebook.ads.internal.rewarded_video.b l;
    private Map<String, Integer> m;
    private long n;
    private int oo;//o
    private boolean autoShow;
    
    public a(final View view, final int n, final listener a) {
        this(view, n, 0, false, a);
    }
    
    public a(final View view, final int n, final boolean b, final listener a) {
        this(view, n, 0, b, a);
    }
    
    public a(final View b, final int c, int d, final boolean h, final listener e) {
        this.f = new Handler();
        this.g = new b(this);
        this.i = 0;
        this.j = 1000;
        this.k = true;
        this.l = new com.facebook.ads.internal.rewarded_video.b(com.facebook.ads.internal.rewarded_video.c.UNKNOWN);
        this.m = new HashMap<String, Integer>();
        this.n = 0L;
        this.oo = 0;
        this.view = b;
        this.cc = c;
        this.e = e;
        this.hh = h;
        if (d < 0) {
            d = 0;
        }
        this.d = d;
    }
    
    public void a(final int i) {
        this.i = i;
    }
    
    public void b(final int j) {
        this.j = j;
    }
    
    public void a() {
        this.f.postDelayed(this.g, (long)this.i);
        this.k = false;
        this.oo = 0;
    }
    
    public void b() {
        this.f.removeCallbacks(this.g);
        this.k = true;
        this.oo = 0;
    }

    public void setAutoShow() {
        autoShow = true;
    }
    
    private static void a(final View view, final boolean b, final String s) {
    }
    
    public static com.facebook.ads.internal.rewarded_video.b a(final View view, final int n) {
        if (view == null) {
            a(view, false, "adView is null.");
            return new com.facebook.ads.internal.rewarded_video.b(c.AD_IS_NULL);
        }
        if (view.getParent() == null) {
            a(view, false, "adView has no parent.");
            return new com.facebook.ads.internal.rewarded_video.b(c.INVALID_PARENT);
        }
        if (view.getWindowVisibility() != View.VISIBLE) {
            a(view, false, "adView window is not set to VISIBLE.");
            return new com.facebook.ads.internal.rewarded_video.b(c.INVALID_WINDOW);
        }
        if (view.getVisibility() != View.VISIBLE) {
            a(view, false, "adView is not set to VISIBLE.");
            return new com.facebook.ads.internal.rewarded_video.b(c.AD_IS_NOT_VISIBLE);
        }
        if (view.getMeasuredWidth() <= 0 || view.getMeasuredHeight() <= 0) {
            a(view, false, "adView has invisible dimensions (w=" + view.getMeasuredWidth() + ", h=" + view.getMeasuredHeight());
            return new com.facebook.ads.internal.rewarded_video.b(c.INVALID_DIMENSIONS);
        }
        if (view.getAlpha() < 0.9f) {
            a(view, false, "adView is too transparent.");
            return new com.facebook.ads.internal.rewarded_video.b(c.AD_IS_TRANSPARENT);
        }
        final int width = view.getWidth();
        final int height = view.getHeight();
        final int[] array = new int[2];
        try {
            view.getLocationOnScreen(array);
        }
        catch (NullPointerException ex) {
            a(view, false, "Cannot get location on screen.");
            return new com.facebook.ads.internal.rewarded_video.b(c.INVALID_DIMENSIONS);
        }
        final Context context = view.getContext();
        final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        final Rect rect = new Rect(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        float n2 = 0.0f;
        if (rect.intersect(array[0], array[1], array[0] + width, array[1] + height)) {
            n2 = rect.width() * rect.height() * 1.0f / (width * height);
        }
        final boolean m = h.m(context);
        final int n3 = h.n(context);
        if (m) {
            n2 *= 100.0f;
            if (n2 < n3) {
                a(view, false, String.format("adView visible area is too small [%.2f%% visible, current threshold %d%%]", n2, n3));
                return new com.facebook.ads.internal.rewarded_video.b(c.AD_INSUFFICIENT_VISIBLE_AREA, n2);
            }
        }
        else {
            if (array[0] < 0 || displayMetrics.widthPixels - array[0] < width) {
                a(view, false, "adView is not fully on screen horizontally.");
                return new com.facebook.ads.internal.rewarded_video.b(c.AD_OFFSCREEN_HORIZONTALLY, n2);
            }
            final int n4 = (int)(height * (100.0 - n) / 100.0);
            if (array[1] < 0 && Math.abs(array[1]) > n4) {
                a(view, false, "adView is not visible from the top.");
                return new com.facebook.ads.internal.rewarded_video.b(c.AD_OFFSCREEN_TOP, n2);
            }
            if (array[1] + height - displayMetrics.heightPixels > n4) {
                a(view, false, "adView is not visible from the bottom.");
                return new com.facebook.ads.internal.rewarded_video.b(c.AD_OFFSCREEN_BOTTOM, n2);
            }
        }
        if (!o.b(context)) {
            a(view, false, "Screen is not interactive.");
            return new com.facebook.ads.internal.rewarded_video.b(c.SCREEN_NOT_INTERACTIVE, n2);
        }
        final Map<String, String> a = al.a(context);
        if (al.a(a)) {
            a(view, false, "Keyguard is obstructing view.");
            return new com.facebook.ads.internal.rewarded_video.b(c.AD_IS_OBSTRUCTED_BY_KEYGUARD, n2);
        }
        if (h.b(context) && al.b(a)) {
            a(view, false, "Ad is on top of the Lockscreen.");
            return new com.facebook.ads.internal.rewarded_video.b(c.AD_IN_LOCKSCREEN, n2, a);
        }
        a(view, true, "adView is visible.");
        return new com.facebook.ads.internal.rewarded_video.b(c.IS_VIEWABLE, n2, a);
    }
    
    public void a(final Map<String, String> map) {
        map.put("vrc", String.valueOf(this.l.b()));
        map.put("vp", String.valueOf(this.l.c()));
        map.put("vh", new JSONObject((Map)this.m).toString());
        map.put("vt", com.facebook.ads.internal.util.g.a(this.n));
        map.putAll(this.l.d());
    }
    
    static {
        TAG = a.class.getSimpleName();
    }
    
    private static final class b extends ak<a>
    {
        private long initTime;
        private boolean immediateShow;

        public b(final a a) {
            super(a);
            initTime = System.currentTimeMillis();
            AdConfig config = AdAppHelper.getInstance(a.view != null ? a.view.getContext() : null).getConfig();
            immediateShow = config.ad_ctrl.immediate_show == 1;
        }
        
        @Override
        public void run() {
            final a a = this.a();
            if (a == null) {
                return;
            }
            final View a2 = a.view;
            final listener b = a.e;
            if (a2 == null || b == null) {
                return;
            }
            final com.facebook.ads.internal.rewarded_video.b a3 = com.facebook.ads.internal.rewarded_video.a.a(a2, a.cc);
            if (a3.a()) {
                a.oo++;
            }
            else {
                a.oo = 0;
            }
            final boolean b2 = a.oo > a.d;
            final boolean b3 = a.l != null && a.l.a();
            if (b2 || !a3.a()) {
                a.l = a3;
            }
            final String value = String.valueOf(a3.b());
            synchronized (a) {
                a.m.put(value, (a.m.containsKey(value) ? a.m.get(value) : 0) + 1);
            }
            if (a.autoShow || (b2 && !b3) || (immediateShow && (System.currentTimeMillis() - initTime) > 2000)) {
                a.n = System.currentTimeMillis();
                b.a();
                if (!a.hh) {
                    return;
                }
            }
            else if (!b2 && b3) {
                b.b();
            }
            if (!a.k) {
                a.f.postDelayed(a.g, (long)a.j);
            }
        }
    }
    
    public abstract static class listener
    {
        public abstract void a();
        
        public void b() {
        }
    }
}
