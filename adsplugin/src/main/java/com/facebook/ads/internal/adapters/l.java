// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.adapters;

import com.facebook.ads.AdNetwork;
import android.view.View;

import com.facebook.ads.internal.util.al;

import android.os.Handler;
import android.text.TextUtils;
import java.util.HashMap;
import com.facebook.ads.AdError;
import java.util.Map;
import android.util.Log;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;
import android.support.annotation.Nullable;
import com.facebook.ads.internal.util.b;
import java.util.List;
import com.facebook.ads.NativeAdViewAttributes;
import com.facebook.ads.internal.util.ai;
import java.util.Collection;
import com.facebook.ads.internal.util.e;
import com.facebook.ads.NativeAd;
import android.net.Uri;
import android.content.Context;
import com.facebook.ads.internal.util.f;

public class l extends v implements f.a
{
    private static final String a;
    private Context context;
    private w c;
    private Uri d;
    private String e;
    private String f;
    private String g;
    private String h;
    private String i;
    private NativeAd.Image j;
    private NativeAd.Image k;
    private NativeAd.Rating l;
    private String m;
    private e n;
    private Collection<String> o;
    private boolean p;
    private boolean q;
    private boolean r;
    private int s;
    private int t;
    private int u;
    private int v;
    private String w;
    private String x;
    private ai y;
    private String z;
    private NativeAd.Image A;
    private String B;
    private String C;
    private NativeAdViewAttributes D;
    private List<NativeAd> E;
    private int F;
    private int G;
    private String H;
    private boolean I;
    private boolean J;
    private boolean K;
    private boolean L;
    private boolean M;
    private long N;
    private b.a O;
    @Nullable
    private com.facebook.ads.internal.gpackage.f P;
    
    public l() {
        this.N = 0L;
        this.O = null;
    }
    
    private void a(final JSONObject jsonObject, final String h) {
        if (this.J) {
            throw new IllegalStateException("Adapter already loaded data");
        }
        if (jsonObject == null) {
            return;
        }
        com.facebook.ads.internal.util.g.a(this.context, "Audience Network Loaded");
        this.H = h;
        this.d = Uri.parse(com.facebook.ads.internal.util.g.a(jsonObject, "fbad_command"));
        this.e = com.facebook.ads.internal.util.g.a(jsonObject, "title");
        this.f = com.facebook.ads.internal.util.g.a(jsonObject, "subtitle");
        this.g = com.facebook.ads.internal.util.g.a(jsonObject, "body");
        this.h = com.facebook.ads.internal.util.g.a(jsonObject, "call_to_action");
        this.i = com.facebook.ads.internal.util.g.a(jsonObject, "social_context");
        this.j = NativeAd.Image.fromJSONObject(jsonObject.optJSONObject("icon"));
        this.k = NativeAd.Image.fromJSONObject(jsonObject.optJSONObject("image"));
        this.l = NativeAd.Rating.fromJSONObject(jsonObject.optJSONObject("star_rating"));
        this.m = com.facebook.ads.internal.util.g.a(jsonObject, "used_report_url");
        this.p = jsonObject.optBoolean("manual_imp");
        this.q = jsonObject.optBoolean("enable_view_log");
        this.r = jsonObject.optBoolean("enable_snapshot_log");
        this.s = jsonObject.optInt("snapshot_log_delay_second", 4);
        this.t = jsonObject.optInt("snapshot_compress_quality", 0);
        this.u = jsonObject.optInt("viewability_check_initial_delay", 0);
        this.v = jsonObject.optInt("viewability_check_interval", 1000);
        final JSONObject optJSONObject = jsonObject.optJSONObject("ad_choices_icon");
        final JSONObject optJSONObject2 = jsonObject.optJSONObject("native_ui_config");
        this.D = ((optJSONObject2 == null || optJSONObject2.length() == 0) ? null : new NativeAdViewAttributes(optJSONObject2));
        if (optJSONObject != null) {
            this.A = NativeAd.Image.fromJSONObject(optJSONObject);
        }
        this.B = com.facebook.ads.internal.util.g.a(jsonObject, "ad_choices_link_url");
        this.C = com.facebook.ads.internal.util.g.a(jsonObject, "request_id");
        this.n = com.facebook.ads.internal.util.e.a(jsonObject.optString("invalidation_behavior"));
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(jsonObject.optString("detection_strings"));
        }
        catch (JSONException ex) {
            ex.printStackTrace();
        }
        this.o = com.facebook.ads.internal.util.f.a(jsonArray);
        this.w = com.facebook.ads.internal.util.g.a(jsonObject, "video_url");
        this.x = com.facebook.ads.internal.util.g.a(jsonObject, "video_mpd");
        if (!jsonObject.has("video_autoplay_enabled")) {
            this.y = ai.UNKNOWN;
        }
        else {
            this.y = (jsonObject.optBoolean("video_autoplay_enabled") ? ai.ON : ai.OFF);
        }
        this.z = com.facebook.ads.internal.util.g.a(jsonObject, "video_report_url");
        try {
            final JSONArray optJSONArray = jsonObject.optJSONArray("carousel");
            if (optJSONArray != null && optJSONArray.length() > 0) {
                final int length = optJSONArray.length();
                final ArrayList e = new ArrayList<NativeAd>(length);
                for (int i = 0; i < length; ++i) {
                    final l l = new l();
                    l.a(this.context, optJSONArray.getJSONObject(i), this.P, h, i, length);
                    e.add(new NativeAd(this.context, l, null));
                }
                this.E = (List<NativeAd>)e;
            }
        }
        catch (JSONException ex2) {
            Log.e(com.facebook.ads.internal.adapters.l.a, "Unable to parse carousel data.", (Throwable)ex2);
        }
        this.J = true;
        this.K = this.F();
    }
    
    private void a(final Context context, final JSONObject jsonObject, final com.facebook.ads.internal.gpackage.f p6, final String s, final int f, final int g) {
        this.I = true;
        this.context = context;
        this.P = p6;
        this.F = f;
        this.G = g;
        this.a(jsonObject, s);
    }
    
    private boolean F() {
        return this.e != null && this.e.length() > 0 && this.h != null && this.h.length() > 0 && (this.j != null || this.I) && this.k != null;
    }
    
    @Override
    public e D() {
        return this.n;
    }
    
    @Override
    public Collection<String> E() {
        return this.o;
    }
    
    @Override
    public void a(final Context context, final w c, final com.facebook.ads.internal.gpackage.f p4, final Map<String, Object> map) {
        this.context = context;
        this.c = c;
        this.P = p4;
        final JSONObject jsonObject = (JSONObject)map.get("data");
        this.a(jsonObject, com.facebook.ads.internal.util.g.a(jsonObject, "ct"));
        if (com.facebook.ads.internal.util.f.a(context, this)) {
            c.a(this, AdError.NO_FILL);
            return;
        }
        if (c != null) {
            c.a(this);
        }
        b.aa = this.C;
    }
    
    private Map<String, String> c(final Map<String, String> map) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        if (map.containsKey("view")) {
            hashMap.put("view", map.get("view"));
        }
        if (map.containsKey("snapshot")) {
            hashMap.put("snapshot", map.get("snapshot"));
        }
        return hashMap;
    }
    
    private void G() {
        if (!this.M) {
            if (this.P != null) {
                this.P.a(this.m);
            }
            this.M = true;
        }
    }
    
    @Override
    public void a(final Map<String, String> map) {
        if (!this.b()) {
            return;
        }
        if (!this.L) {
            if (this.c != null) {
                this.c.b(this);
            }
            final HashMap<String, String> hashMap = new HashMap<>();
            if (map != null) {
                hashMap.putAll(map);
            }
            if (this.I) {
                hashMap.put("cardind", String.valueOf(this.F));
                hashMap.put("cardcnt", String.valueOf(this.G));
            }
            if (!TextUtils.isEmpty((CharSequence)this.B()) && this.P != null) {
                this.P.a(this.B(), hashMap);
            }
            if (this.e() || this.d()) {
                this.a(map, hashMap);
            }
            this.L = true;
        }
    }
    
    private void a(final Map<String, String> map, final Map<String, String> map2) {
        try {
            new Handler().postDelayed((Runnable)new Runnable() {
                final /* synthetic */ Map b = com.facebook.ads.internal.adapters.l.this.c(map);
                
                @Override
                public void run() {
                    if (!TextUtils.isEmpty((CharSequence)com.facebook.ads.internal.adapters.l.this.H)) {
                        final HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.putAll(map2);
                        hashMap.putAll(this.b);
                        if (com.facebook.ads.internal.adapters.l.this.P != null) {
                            com.facebook.ads.internal.adapters.l.this.P.d(com.facebook.ads.internal.adapters.l.this.H, hashMap);
                        }
                    }
                }
            }, (long)(this.s * 1000));
        }
        catch (Exception ex) {}
    }
    
    @Override
    public void b(final Map<String, String> map) {
        if (!this.b()) {
            return;
        }
        if (com.facebook.ads.internal.h.b(this.context) && al.b(map)) {
            Log.e(com.facebook.ads.internal.adapters.l.a, "Click happened on lockscreen ad");
            return;
        }
        final HashMap<String, String> hashMap = new HashMap<>();
        if (map != null) {
            hashMap.putAll(map);
        }
        com.facebook.ads.internal.util.g.a(this.context, "Click logged");
        if (this.c != null) {
            this.c.c(this);
        }
        if (this.I) {
            hashMap.put("cardind", String.valueOf(this.F));
            hashMap.put("cardcnt", String.valueOf(this.G));
        }
        final com.facebook.ads.internal.action.a a = com.facebook.ads.internal.action.b.a(this.context, this.H, this.d, hashMap);
        if (a != null) {
            try {
                this.N = System.currentTimeMillis();
                this.O = a.a();
                a.b();
            }
            catch (Exception ex) {
                Log.e(com.facebook.ads.internal.adapters.l.a, "Error executing action", (Throwable)ex);
            }
        }
    }
    
    @Override
    public void a(final int n) {
        if (!this.b()) {
            return;
        }
        if (n == 0 && this.N > 0L && this.O != null) {
            com.facebook.ads.internal.util.c.a(com.facebook.ads.internal.util.b.a(this.N, this.O, this.C));
            this.N = 0L;
            this.O = null;
        }
    }
    
    @Override
    public void a(final View view, final List<View> list) {
    }
    
    @Override
    public void a() {
    }
    
    @Override
    public boolean c() {
        return this.b() && this.p;
    }
    
    @Override
    public boolean e() {
        return this.b() && this.q;
    }
    
    @Override
    public boolean f() {
        return this.b() && this.D != null;
    }
    
    @Override
    public boolean g() {
        return true;
    }
    
    @Override
    public boolean d() {
        return this.b() && this.r;
    }
    
    @Override
    public void a(final w c) {
        this.c = c;
    }
    
    @Override
    public int h() {
        if (this.t < 0 || this.t > 100) {
            return 0;
        }
        return this.t;
    }
    
    @Override
    public int i() {
        return this.u;
    }
    
    @Override
    public int j() {
        return this.v;
    }
    
    @Override
    public NativeAd.Image k() {
        if (!this.b()) {
            return null;
        }
        return this.j;
    }
    
    @Override
    public NativeAd.Image l() {
        if (!this.b()) {
            return null;
        }
        return this.k;
    }
    
    @Override
    public NativeAdViewAttributes m() {
        if (!this.b()) {
            return null;
        }
        return this.D;
    }
    
    @Override
    public String n() {
        if (!this.b()) {
            return null;
        }
        this.G();
        return this.e;
    }
    
    @Override
    public String o() {
        if (!this.b()) {
            return null;
        }
        this.G();
        return this.f;
    }
    
    @Override
    public String p() {
        if (!this.b()) {
            return null;
        }
        this.G();
        return this.g;
    }
    
    @Override
    public String q() {
        if (!this.b()) {
            return null;
        }
        this.G();
        return this.h;
    }
    
    @Override
    public String r() {
        if (!this.b()) {
            return null;
        }
        this.G();
        return this.i;
    }
    
    @Override
    public NativeAd.Rating s() {
        if (!this.b()) {
            return null;
        }
        this.G();
        return this.l;
    }
    
    @Override
    public NativeAd.Image t() {
        if (!this.b()) {
            return null;
        }
        return this.A;
    }
    
    @Override
    public String u() {
        if (!this.b()) {
            return null;
        }
        return this.B;
    }
    
    @Override
    public String v() {
        if (!this.b()) {
            return null;
        }
        return "AdChoices";
    }
    
    @Override
    public String w() {
        if (!this.b()) {
            return null;
        }
        return this.w;
    }
    
    @Override
    public String x() {
        if (!this.b()) {
            return null;
        }
        return this.x;
    }
    
    @Override
    public ai y() {
        if (!this.b()) {
            return ai.UNKNOWN;
        }
        return this.y;
    }
    
    @Override
    public List<NativeAd> A() {
        if (!this.b()) {
            return null;
        }
        return this.E;
    }
    
    @Override
    public String B() {
        return this.H;
    }
    
    @Override
    public boolean b() {
        return this.J && this.K;
    }
    
    @Override
    public void onDestroy() {
    }
    
    @Override
    public AdNetwork C() {
        return AdNetwork.AN;
    }
    
    @Override
    public String z() {
        return this.z;
    }
    
    static {
        a = l.class.getSimpleName();
    }
}
