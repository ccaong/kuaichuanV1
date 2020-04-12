// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.adapters;

import android.app.Activity;
import com.facebook.ads.internal.util.g;
import com.facebook.ads.internal.h;
import java.io.Serializable;
import android.content.Intent;
import com.facebook.ads.AudienceNetworkActivity;
import android.net.Uri;
import com.facebook.ads.AdSettings;
import android.content.BroadcastReceiver;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import com.facebook.ads.internal.cpackage.a;
import org.json.JSONException;
import android.util.Log;
import com.facebook.ads.AdError;
import org.json.JSONObject;
import java.util.Map;
import java.util.UUID;
import com.facebook.ads.internal.cpackage.b;
import android.content.Context;

public class m extends x
{
    private y b;
    private Context c;
    private boolean d;
    private String e;
    private String f;
    private String g;
    private String h;
    private String i;
    private String j;
    private String k;
    private String l;
    private String m;
    private String n;
    private String o;
    private z p;
    private b q;
    
    public m() {
        this.d = false;
        this.h = UUID.randomUUID().toString();
    }
    
    @Override
    public void a(final Context c, final y b, final Map<String, Object> map) {
        this.b = b;
        this.c = c;
        this.d = false;
        final JSONObject jsonObject = (JSONObject)map.get("data");
        this.i = jsonObject.optString("video_url");
        if (this.i == null || this.i.isEmpty()) {
            this.b.a(this, AdError.INTERNAL_ERROR);
            return;
        }
        this.j = jsonObject.optString("video_report_url");
        this.o = jsonObject.optString("ct");
        this.k = jsonObject.optString("end_card_markup");
        this.l = jsonObject.optString("activation_command");
        this.n = jsonObject.optString("context_switch", "endvideo");
        this.g = jsonObject.optString("title");
        this.f = jsonObject.optString("subtitle");
        if (jsonObject.has("icon") && !jsonObject.isNull("icon")) {
            try {
                this.e = jsonObject.getJSONObject("icon").getString("url");
            }
            catch (JSONException ex) {
                Log.w(m.class.toString(), "Failed to get adIconURL", (Throwable)ex);
            }
        }
        final String s = (String)map.get("placement_id");
        if (s != null) {
            this.m = s.split("_")[0];
        }
        else {
            this.m = "";
        }
        this.p = new z(this.h, this, b);
        this.c();
        (this.q = new b(c)).b(this.i);
        this.q.a(new a() {
            @Override
            public void a() {
                com.facebook.ads.internal.adapters.m.this.d = true;
                com.facebook.ads.internal.adapters.m.this.b.a(com.facebook.ads.internal.adapters.m.this);
            }
        });
    }
    
    public String a() {
        String s = "";
        if (this.q != null) {
            s = this.q.c(this.i);
        }
        if (TextUtils.isEmpty((CharSequence)s)) {
            s = this.i;
        }
        return s;
    }
    
    private void c() {
        LocalBroadcastManager.getInstance(this.c).registerReceiver((BroadcastReceiver)this.p, this.p.a());
    }
    
    private void d() {
        if (this.p != null) {
            try {
                LocalBroadcastManager.getInstance(this.c).unregisterReceiver((BroadcastReceiver)this.p);
            }
            catch (Exception ex) {}
        }
    }
    
    private String e() {
        if (this.a != null) {
            final String urlPrefix = AdSettings.getUrlPrefix();
            final Uri parse = Uri.parse((urlPrefix == null || urlPrefix.isEmpty()) ? "https://www.facebook.com/audience_network/server_side_reward" : String.format("https://www.%s.facebook.com/audience_network/server_side_reward", urlPrefix));
            final Uri.Builder builder = new Uri.Builder();
            builder.scheme(parse.getScheme());
            builder.authority(parse.getAuthority());
            builder.path(parse.getPath());
            builder.query(parse.getQuery());
            builder.fragment(parse.getFragment());
            builder.appendQueryParameter("puid", this.a.getUserID());
            builder.appendQueryParameter("pc", this.a.getCurrency());
            builder.appendQueryParameter("ptid", this.h);
            builder.appendQueryParameter("appid", this.m);
            return builder.build().toString();
        }
        return null;
    }
    
    private String f() {
        return this.n;
    }
    
    @Override
    public boolean b() {
        if (!this.d) {
            return false;
        }
        final Intent intent = new Intent(this.c, (Class)AudienceNetworkActivity.class);
        intent.putExtra("viewType", (Serializable)AudienceNetworkActivity.Type.REWARDED_VIDEO);
        intent.putExtra("videoURL", this.a());
        intent.putExtra("videoReportURL", this.j);
        if (!com.facebook.ads.internal.h.i(this.c)) {
            intent.putExtra("predefinedOrientationKey", 6);
        }
        intent.putExtra("facebookRewardedVideoEndCardActivationCommand", this.l);
        intent.putExtra("uniqueId", this.h);
        intent.putExtra("facebookRewardedVideoEndCardMarkup", com.facebook.ads.internal.util.g.a(this.k));
        intent.putExtra("clientToken", this.o);
        intent.putExtra("rewardServerURL", this.e());
        intent.putExtra("contextSwitchBehavior", this.f());
        intent.putExtra("adTitle", this.g);
        intent.putExtra("adSubtitle", this.f);
        intent.putExtra("adIconUrl", this.e);
        if (!(this.c instanceof Activity)) {
            intent.setFlags(intent.getFlags() | 0x10000000);
        }
        this.c.startActivity(intent);
        return true;
    }
    
    @Override
    public void onDestroy() {
        this.d();
    }
}
