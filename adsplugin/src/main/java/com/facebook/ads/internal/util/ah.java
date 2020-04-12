//
// Decompiled by Procyon v0.5.30
//

package com.facebook.ads.internal.util;

import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.graphics.Rect;
import java.util.HashMap;
import android.database.ContentObserver;
import android.provider.Settings;
import android.os.Handler;
import java.util.List;
import android.view.View;
import java.util.Map;
import com.facebook.ads.internal.bpackage.c;
import com.facebook.ads.internal.bpackage.b;
import java.util.ArrayList;
import android.support.annotation.Nullable;
import com.facebook.ads.internal.bpackage.a;
import com.facebook.ads.internal.gpackage.f;
import android.content.Context;
import android.os.Bundle;

public class ah implements ad<Bundle>
{
    private final String a;
    private boolean b;
    private final Context c;
    private final f dd;
    private final a e;
    private final com.facebook.ads.internal.bpackage.a f;
    private int g;
    private int h;
    private final aa i;

    public ah(final Context context, final f f, final a a, final String s) {
        this(context, f, a, s, null);
    }

    public ah(final Context c, final f d, final a e, final String a, @Nullable final Bundle bundle) {
        this.b = true;
        this.g = 0;
        this.h = 0;
        this.c = c;
        this.dd = d;
        this.e = e;
        this.a = a;
        final ArrayList<com.facebook.ads.internal.bpackage.b> list = new ArrayList<>();
        list.add(new com.facebook.ads.internal.bpackage.b(0.5, -1.0, 2.0, true) {
            @Override
            protected void a(final boolean b, final boolean b2, final c c) {
                dd.c(ah.this.a, ah.this.a(ah.bType.MRC));
            }
        });
        list.add(new com.facebook.ads.internal.bpackage.b(1.0E-7, -1.0, 0.001, false) {
            @Override
            protected void a(final boolean b, final boolean b2, final c c) {
                dd.c(ah.this.a, ah.this.a(bType.VIEWABLE_IMPRESSION));
            }
        });
        if (bundle != null) {
            this.f = new com.facebook.ads.internal.bpackage.a(c, (View)e, list, bundle.getBundle("adQualityManager"));
            this.g = bundle.getInt("lastProgressTimeMS");
            this.h = bundle.getInt("lastBoundaryTimeMS");
        }
        else {
            this.f = new com.facebook.ads.internal.bpackage.a(c, (View)e, list);
        }
        this.i = new aa(new Handler(), this);
    }

    public void b() {
        this.c.getContentResolver().registerContentObserver(Settings.System.CONTENT_URI, true, (ContentObserver)this.i);
    }

    public void c() {
        this.c.getContentResolver().unregisterContentObserver((ContentObserver)this.i);
    }

    private Map<String, String> a(final bType b) {
        return this.a(b, this.e.getCurrentPosition());
    }

    private Map<String, String> a(final bType b, final int n) {
        final Map<String, String> c = this.c(n);
        c.put("action", String.valueOf(b.j));
        return c;
    }

    public void a(final int n, final int n2) {
        this.a(n, true);
        this.h = n2;
        this.g = n2;
        this.f.a();
    }

    protected float d() {
        return aj.a(this.c) * this.e.getVolume();
    }

    public void a(final int n) {
        this.a(n, false);
    }

    private void a(final int g, final boolean b) {
        if (g <= 0.0 || g < this.g) {
            return;
        }
        if (g > this.g) {
            this.f.a((g - this.g) / 1000.0f, this.d());
            this.g = g;
            if (g - this.h >= 5000) {
                this.dd.c(this.a, this.a(bType.TIME, g));
                this.h = this.g;
                this.f.a();
                return;
            }
        }
        if (b) {
            this.dd.c(this.a, this.a(bType.TIME, g));
        }
    }

    public void b(final int n) {
        this.a(n, true);
        this.h = 0;
        this.g = 0;
        this.f.a();
    }

    private Map<String, String> c(final int n) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        aj.a(hashMap, this.e.a(), !this.e.c());
        this.a(hashMap);
        this.b(hashMap);
        this.a(hashMap, n);
        this.c(hashMap);
        return hashMap;
    }

    private void a(final Map<String, String> map) {
        map.put("exoplayer", String.valueOf(this.e.b()));
        map.put("prep", Long.toString(this.e.getInitialBufferTime()));
    }

    private void b(final Map<String, String> map) {
        final c b = this.f.b();
        final c.a b2 = b.b();
        map.put("vwa", String.valueOf(b2.c()));
        map.put("vwm", String.valueOf(b2.b()));
        map.put("vwmax", String.valueOf(b2.d()));
        map.put("vtime_ms", String.valueOf(b2.f() * 1000.0));
        map.put("mcvt_ms", String.valueOf(b2.g() * 1000.0));
        final c.a c = b.c();
        map.put("vla", String.valueOf(c.c()));
        map.put("vlm", String.valueOf(c.b()));
        map.put("vlmax", String.valueOf(c.d()));
        map.put("atime_ms", String.valueOf(c.f() * 1000.0));
        map.put("mcat_ms", String.valueOf(c.g() * 1000.0));
    }

    private void a(final Map<String, String> map, final int n) {
        map.put("ptime", String.valueOf(this.h / 1000.0f));
        map.put("time", String.valueOf(n / 1000.0f));
    }

    private void c(final Map<String, String> map) {
        final Rect rect = new Rect();
        this.e.getGlobalVisibleRect(rect);
        map.put("pt", String.valueOf(rect.top));
        map.put("pl", String.valueOf(rect.left));
        map.put("ph", String.valueOf(this.e.getMeasuredHeight()));
        map.put("pw", String.valueOf(this.e.getMeasuredWidth()));
        final WindowManager windowManager = (WindowManager)this.c.getSystemService("window");
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        map.put("vph", String.valueOf(displayMetrics.heightPixels));
        map.put("vpw", String.valueOf(displayMetrics.widthPixels));
    }

    public void e() {
        if (this.d() < 0.05) {
            if (this.b) {
                this.f();
                this.b = false;
            }
        }
        else if (!this.b) {
            this.g();
            this.b = true;
        }
    }

    public void f() {
        this.dd.c(this.a, this.a(bType.MUTE));
    }

    public void g() {
        this.dd.c(this.a, this.a(bType.UNMUTE));
    }

    public void h() {
        this.dd.c(this.a, this.a(bType.SKIP));
    }

    public void i() {
        this.dd.c(this.a, this.a(bType.PAUSE));
    }

    public void j() {
        this.dd.c(this.a, this.a(bType.RESUME));
    }

    @Override
    public Bundle getSaveInstanceState() {
        this.a(this.k(), this.k());
        final Bundle bundle = new Bundle();
        bundle.putInt("lastProgressTimeMS", this.g);
        bundle.putInt("lastBoundaryTimeMS", this.h);
        bundle.putBundle("adQualityManager", this.f.getSaveInstanceState());
        return bundle;
    }

    public int k() {
        return this.g;
    }

    public interface a
    {
        boolean getGlobalVisibleRect(final Rect p0);

        int getMeasuredHeight();

        int getMeasuredWidth();

        boolean a();

        boolean b();

        boolean c();

        long getInitialBufferTime();

        float getVolume();

        int getCurrentPosition();
    }

    protected enum bType
    {
        PLAY(0),
        SKIP(1),
        TIME(2),
        MRC(3),
        PAUSE(4),
        RESUME(5),
        MUTE(6),
        UNMUTE(7),
        VIEWABLE_IMPRESSION(10);

        public final int j;

        private bType(final int j) {
            this.j = j;
        }
    }
}
