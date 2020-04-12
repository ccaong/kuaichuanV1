// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.adapters;

import com.facebook.ads.AdNetwork;
import com.facebook.ads.internal.util.ai;
import com.facebook.ads.NativeAdViewAttributes;
import java.util.List;
import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.sdk.InMobiSdk;
import com.facebook.ads.AdError;
import android.text.TextUtils;
import org.json.JSONObject;
import com.facebook.ads.internal.util.g;
import java.util.Map;
import com.facebook.ads.internal.gpackage.f;
import android.content.Context;
import com.facebook.ads.NativeAd;
import android.view.View;
import com.inmobi.ads.InMobiNative;

public class q extends v implements t
{
    private w a;
    private InMobiNative b;
    private boolean c;
    private View d;
    private String e;
    private String f;
    private String g;
    private NativeAd.Rating h;
    private NativeAd.Image i;
    private NativeAd.Image j;
    
    @Override
    public void a(final Context context, final w a, final f f, final Map<String, Object> map) {
        com.facebook.ads.internal.util.g.a(context, com.facebook.ads.internal.util.v.a(this.D()) + " Loading");
        final JSONObject jsonObject = (JSONObject)map.get("data");
        final String optString = jsonObject.optString("account_id");
        final Long value = jsonObject.optLong("placement_id");
        if (TextUtils.isEmpty((CharSequence)optString) || value == null) {
            a.a(this, AdError.MEDIATION_ERROR);
            return;
        }
        this.a = a;
        InMobiSdk.init(context, optString);
        (this.b = new InMobiNative((long)value, (InMobiNative.NativeAdListener)new InMobiNative.NativeAdListener() {
            public void onAdLoadSucceeded(final InMobiNative inMobiNative) {
                try {
                    final JSONObject jsonObject = new JSONObject((String)inMobiNative.getAdContent());
                    q.this.e = jsonObject.optString("title");
                    q.this.f = jsonObject.optString("description");
                    q.this.g = jsonObject.optString("cta");
                    final JSONObject optJSONObject = jsonObject.optJSONObject("icon");
                    if (optJSONObject != null) {
                        q.this.i = new NativeAd.Image(optJSONObject.optString("url"), optJSONObject.optInt("width"), optJSONObject.optInt("height"));
                    }
                    final JSONObject optJSONObject2 = jsonObject.optJSONObject("screenshots");
                    if (optJSONObject2 != null) {
                        q.this.j = new NativeAd.Image(optJSONObject2.optString("url"), optJSONObject2.optInt("width"), optJSONObject2.optInt("height"));
                    }
                    final String optString = jsonObject.optString("rating");
                    try {
                        q.this.h = new NativeAd.Rating(Double.parseDouble(optString), 5.0);
                    }
                    catch (Exception ex) {}
                    q.this.c = true;
                    if (q.this.d != null) {
                        InMobiNative.bind(q.this.d, inMobiNative);
                    }
                    if (q.this.a != null) {
                        com.facebook.ads.internal.util.g.a(context, com.facebook.ads.internal.util.v.a(q.this.D()) + " Loaded");
                        q.this.a.a(q.this);
                    }
                }
                catch (Exception ex2) {
                    if (q.this.a != null) {
                        com.facebook.ads.internal.util.g.a(context, com.facebook.ads.internal.util.v.a(q.this.D()) + " Failed. Internal AN SDK error");
                        q.this.a.a(q.this, AdError.INTERNAL_ERROR);
                    }
                }
            }
            
            public void onAdLoadFailed(final InMobiNative inMobiNative, final InMobiAdRequestStatus inMobiAdRequestStatus) {
                com.facebook.ads.internal.util.g.a(context, com.facebook.ads.internal.util.v.a(q.this.D()) + " Failed with InMobi error: " + inMobiAdRequestStatus.getMessage());
                if (q.this.a != null) {
                    q.this.a.a(q.this, new AdError(3001, inMobiAdRequestStatus.getMessage()));
                }
            }
            
            public void onAdDismissed(final InMobiNative inMobiNative) {
            }
            
            public void onAdDisplayed(final InMobiNative inMobiNative) {
            }
            
            public void onUserLeftApplication(final InMobiNative inMobiNative) {
            }
        })).load();
    }
    
    @Override
    public void a(final Map<String, String> map) {
        this.a.b(this);
    }
    
    @Override
    public void b(final Map<String, String> map) {
        if (this.b()) {
            this.a.c(this);
            this.b.reportAdClickAndOpenLandingPage((Map)null);
        }
    }
    
    @Override
    public void a(final int n) {
    }
    
    @Override
    public void a(final View d, final List<View> list) {
        this.d = d;
        if (this.b()) {
            final InMobiNative b = this.b;
            InMobiNative.bind(this.d, this.b);
        }
    }
    
    @Override
    public void a() {
        if (this.b()) {
            final InMobiNative b = this.b;
            InMobiNative.unbind(this.d);
        }
        this.d = null;
    }
    
    @Override
    public boolean b() {
        return this.b != null && this.c;
    }
    
    @Override
    public boolean c() {
        return false;
    }
    
    @Override
    public boolean d() {
        return false;
    }
    
    @Override
    public boolean e() {
        return false;
    }
    
    @Override
    public boolean f() {
        return false;
    }
    
    @Override
    public boolean g() {
        return true;
    }
    
    @Override
    public void a(final w a) {
        this.a = a;
    }
    
    @Override
    public int h() {
        return 0;
    }
    
    @Override
    public int i() {
        return 0;
    }
    
    @Override
    public int j() {
        return 0;
    }
    
    @Override
    public NativeAd.Image k() {
        return this.i;
    }
    
    @Override
    public NativeAd.Image l() {
        return this.j;
    }
    
    @Override
    public NativeAdViewAttributes m() {
        return null;
    }
    
    @Override
    public String n() {
        return this.e;
    }
    
    @Override
    public String o() {
        return null;
    }
    
    @Override
    public String p() {
        return this.f;
    }
    
    @Override
    public String q() {
        return this.g;
    }
    
    @Override
    public String r() {
        return null;
    }
    
    @Override
    public NativeAd.Rating s() {
        return null;
    }
    
    @Override
    public NativeAd.Image t() {
        return null;
    }
    
    @Override
    public String u() {
        return null;
    }
    
    @Override
    public String v() {
        return "Ad";
    }
    
    @Override
    public String w() {
        return null;
    }
    
    @Override
    public String x() {
        return null;
    }
    
    @Override
    public ai y() {
        return ai.UNKNOWN;
    }
    
    @Override
    public String z() {
        return null;
    }
    
    @Override
    public List<NativeAd> A() {
        return null;
    }
    
    @Override
    public String B() {
        return null;
    }
    
    @Override
    public AdNetwork C() {
        return AdNetwork.INMOBI;
    }
    
    @Override
    public void onDestroy() {
        this.a();
        this.b = null;
        this.a = null;
    }
    
    @Override
    public e D() {
        return com.facebook.ads.internal.adapters.e.INMOBI;
    }
}
