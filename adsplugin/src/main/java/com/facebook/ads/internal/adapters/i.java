// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.adapters;

import android.util.Log;
import com.facebook.ads.internal.view.dpackage.b.h;
import com.facebook.ads.internal.view.dpackage.b.e;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.facebook.ads.internal.view.dpackage.b.c;
import com.facebook.ads.internal.view.dpackage.b.k;
import com.facebook.ads.internal.view.dpackage.b.m;
import com.facebook.ads.internal.view.dpackage.b.j;
import android.view.View;
import com.facebook.ads.internal.util.ab;
import org.json.JSONException;
import java.util.Map;
import java.util.HashMap;
import com.facebook.ads.AdError;
import com.facebook.ads.internal.gpackage.q;
import android.content.Context;
import org.json.JSONObject;
import com.facebook.ads.internal.util.ah;
import com.facebook.ads.internal.view.n;
import com.facebook.ads.internal.gpackage.f;
import android.support.annotation.Nullable;
import com.facebook.ads.internal.view.dpackage.a.a;
import com.facebook.ads.internal.view.dpackage.a.d;
import com.facebook.ads.internal.view.dpackage.a.l;
import com.facebook.ads.internal.view.dpackage.a.b;
import com.facebook.ads.internal.gpackage.s;
import android.os.Bundle;
import com.facebook.ads.internal.util.ad;

public class i extends r implements ad<Bundle>
{
    private final s<b> e;
    private final s<l> f;
    private final s<d> g;
    private final s<a> h;
    @Nullable
    private com.facebook.ads.a.a i;
    @Nullable
    private f j;
    @Nullable
    private String k;
    private boolean l;
    @Nullable
    protected n a;
    @Nullable
    private ah m;
    @Nullable
    protected JSONObject b;
    @Nullable
    protected Context c;
    @Nullable
    private String n;
    static final /* synthetic */ boolean d;
    
    public i() {
        this.e = new s<b>() {
            @Override
            public void a(final b b) {
                if (com.facebook.ads.internal.adapters.i.this.i == null) {
                    return;
                }
                com.facebook.ads.internal.adapters.i.this.i.d(com.facebook.ads.internal.adapters.i.this);
            }
            
            @Override
            public Class<b> a() {
                return b.class;
            }
        };
        this.f = new s<l>() {
            @Override
            public void a(final l l) {
                if (com.facebook.ads.internal.adapters.i.this.i == null) {
                    return;
                }
                com.facebook.ads.internal.adapters.i.this.l = true;
                com.facebook.ads.internal.adapters.i.this.i.a(com.facebook.ads.internal.adapters.i.this);
            }
            
            @Override
            public Class<l> a() {
                return l.class;
            }
        };
        this.g = new s<d>() {
            @Override
            public void a(final d d) {
                if (com.facebook.ads.internal.adapters.i.this.i == null) {
                    return;
                }
                com.facebook.ads.internal.adapters.i.this.i.a(com.facebook.ads.internal.adapters.i.this, AdError.INTERNAL_ERROR);
            }
            
            @Override
            public Class<d> a() {
                return d.class;
            }
        };
        this.h = new s<a>() {
            @Override
            public void a(final a a) {
                if (com.facebook.ads.internal.adapters.i.this.i != null) {
                    com.facebook.ads.internal.adapters.i.this.i.b(com.facebook.ads.internal.adapters.i.this);
                }
                if (com.facebook.ads.internal.adapters.i.this.j != null) {
                    com.facebook.ads.internal.adapters.i.this.j.b(com.facebook.ads.internal.adapters.i.this.n, new HashMap<String, String>());
                }
            }
            
            @Override
            public Class<a> a() {
                return a.class;
            }
        };
        this.l = false;
    }
    
    public final void a(final Context context, final com.facebook.ads.a.a a, final f f, final Bundle bundle) {
        try {
            this.a(context, a, new JSONObject(bundle.getString("ad_response")), f, bundle);
        }
        catch (JSONException ex) {
            a.a(this, AdError.INTERNAL_ERROR);
        } catch (OutOfMemoryError error) {
            a.a(this, AdError.INTERNAL_ERROR);
        }
    }
    
    @Override
    public final void a(final Context context, final com.facebook.ads.a.a a, final Map<String, Object> map, final f f) {
        try {
            this.a(context, a, (JSONObject)map.get("data"), f, null);
        }
        catch (JSONException ex) {
            a.a(this, AdError.INTERNAL_ERROR);
        }
    }
    
    private void a(final Context c, final com.facebook.ads.a.a i, final JSONObject b, final f j, @Nullable final Bundle bundle) throws JSONException {
        this.c = c;
        this.i = i;
        this.j = j;
        this.b = b;
        this.l = false;
        final JSONObject jsonObject = b.getJSONObject("video");
        this.n = b.optString("ct");
        this.k = jsonObject.getString("videoURL");
        this.a = new n(c);
        this.a();
        this.a.getEventBus().a(this.e);
        this.a.getEventBus().a(this.g);
        this.a.getEventBus().a(this.f);
        this.a.getEventBus().a(this.h);
        if (bundle != null) {
            this.m = new ab(c, j, this.a, this.n, bundle.getBundle("logger"));
        }
        else {
            this.m = new ab(c, j, this.a, this.n);
        }
        this.i.a(this, (View)this.a);
        this.a.setVideoURI(this.k);
    }
    
    protected void a() throws JSONException {
        if (!com.facebook.ads.internal.adapters.i.d && this.c == null) {
            throw new AssertionError();
        }
        if (!com.facebook.ads.internal.adapters.i.d && this.b == null) {
            throw new AssertionError();
        }
        final JSONObject jsonObject = this.b.getJSONObject("video");
        JSONObject optJSONObject = this.b.optJSONObject("text");
        if (optJSONObject == null) {
            optJSONObject = new JSONObject();
        }
        this.a.a(new j(this.c));
        final k k = new k(this.c);
        this.a.a(k);
        this.a.a(new com.facebook.ads.internal.view.dpackage.b.d((View)k, com.facebook.ads.internal.view.dpackage.b.d.aType.INVISIBLE));
        this.a.a(new com.facebook.ads.internal.view.dpackage.b.b(this.c));
        final String b = this.b();
        if (b != null) {
            final c c = new c(this.c, b);
            final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
            layoutParams.addRule(12);
            layoutParams.addRule(9);
            c.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
            c.setCountdownTextColor(-1);
            this.a.a(c);
        }
        if (jsonObject.has("destinationURL") && !jsonObject.isNull("destinationURL")) {
            final String string = jsonObject.getString("destinationURL");
            if (!TextUtils.isEmpty((CharSequence)string)) {
                final e e = new e(this.c, string, this.n, optJSONObject.optString("learnMore", "Learn More"));
                final RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-2, -2);
                layoutParams2.addRule(10);
                layoutParams2.addRule(11);
                e.setLayoutParams((ViewGroup.LayoutParams)layoutParams2);
                this.a.a(e);
            }
        }
        this.a.a(new com.facebook.ads.internal.view.dpackage.b.a(this.c, "http://m.facebook.com/ads/ad_choices", this.n, new float[] { 0.0f, 0.0f, 8.0f, 0.0f }));
        final int c2 = this.c();
        if (c2 > 0) {
            final h h = new h(this.c, c2, optJSONObject.optString("skipAdIn", "Skip Ad in"), optJSONObject.optString("skipAd", "Skip Ad"));
            final RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(-2, -2);
            layoutParams3.addRule(12);
            layoutParams3.addRule(11);
            h.setLayoutParams((ViewGroup.LayoutParams)layoutParams3);
            h.setPadding(0, 0, 0, 30);
            this.a.a(h);
        }
    }
    
    protected String b() {
        if (!com.facebook.ads.internal.adapters.i.d && this.b == null) {
            throw new AssertionError();
        }
        try {
            final JSONObject jsonObject = this.b.getJSONObject("capabilities");
            if (!jsonObject.has("countdown") || jsonObject.isNull("countdown")) {
                return null;
            }
            final JSONObject jsonObject2 = jsonObject.getJSONObject("countdown");
            if (jsonObject2.has("format")) {
                return jsonObject2.optString("format");
            }
        }
        catch (Exception ex) {
            Log.w(String.valueOf(i.class), "Invalid JSON", (Throwable)ex);
            return null;
        }
        return null;
    }
    
    protected int c() {
        if (!com.facebook.ads.internal.adapters.i.d && this.b == null) {
            throw new AssertionError();
        }
        try {
            final JSONObject jsonObject = this.b.getJSONObject("capabilities");
            if (!jsonObject.has("skipButton") || jsonObject.isNull("skipButton")) {
                return -1;
            }
            final JSONObject jsonObject2 = jsonObject.getJSONObject("skipButton");
            if (jsonObject2.has("skippableSeconds")) {
                return jsonObject2.getInt("skippableSeconds");
            }
        }
        catch (Exception ex) {
            Log.w(String.valueOf(i.class), "Invalid JSON", (Throwable)ex);
            return -1;
        }
        return -1;
    }
    
    @Override
    public void onDestroy() {
        if (this.a != null) {
            this.a.g();
        }
        this.i = null;
        this.j = null;
        this.k = null;
        this.l = false;
        this.n = null;
        this.a = null;
        this.m = null;
        this.b = null;
        this.c = null;
    }
    
    @Override
    public boolean d() {
        if (!this.l || this.a == null) {
            return false;
        }
        if (this.m.k() > 0) {
            this.a.a(this.m.k());
            this.a.d();
        }
        else {
            this.a.d();
            this.e();
        }
        return true;
    }
    
    protected void e() {
        if (this.j == null) {
            return;
        }
        this.j.a(this.n, new HashMap<String, String>());
        if (this.i != null) {
            this.i.c(this);
        }
    }
    
    @Override
    public Bundle getSaveInstanceState() {
        if (this.m == null || this.b == null) {
            return null;
        }
        final Bundle bundle = new Bundle();
        bundle.putBundle("logger", this.m.getSaveInstanceState());
        bundle.putString("ad_response", this.b.toString());
        return bundle;
    }
    
    static {
        d = !i.class.desiredAssertionStatus();
    }
}
