// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.server;

import org.json.JSONException;
import org.json.JSONArray;
import com.facebook.ads.internal.fpackage.a;
import org.json.JSONObject;
import android.text.TextUtils;

public class c
{
    private static c a;
    
    public static synchronized c a() {
        return c.a;
    }
    
    public d a(final String s) throws JSONException {
        if (!TextUtils.isEmpty((CharSequence)s)) {
            final JSONObject jsonObject = new JSONObject(s);
            final String optString = jsonObject.optString("type");
            switch (optString) {
                case "ads": {
                    return this.a(jsonObject);
                }
                case "error": {
                    return this.b(jsonObject);
                }
                default: {
                    final JSONObject optJSONObject = jsonObject.optJSONObject("error");
                    if (optJSONObject != null) {
                        return this.c(optJSONObject);
                    }
                    break;
                }
            }
        }
        return new d(d.a.UNKNOWN, null);
    }
    
    private e a(final JSONObject jsonObject) throws JSONException {
        final JSONObject jsonObject2 = jsonObject.getJSONArray("placements").getJSONObject(0);
        final com.facebook.ads.internal.fpackage.d d = new com.facebook.ads.internal.fpackage.d(com.facebook.ads.internal.fpackage.e.a(jsonObject2.getJSONObject("definition")), jsonObject2.optString("feature_config"));
        if (jsonObject2.has("ads")) {
            final JSONArray jsonArray = jsonObject2.getJSONArray("ads");
            for (int i = 0; i < jsonArray.length(); ++i) {
                final JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                d.a(new a(jsonObject3.optString("adapter"), jsonObject3.optJSONObject("data"), jsonObject3.optJSONArray("trackers")));
            }
        }
        return new e(d);
    }
    
    private f b(final JSONObject jsonObject) {
        try {
            final JSONObject jsonObject2 = jsonObject.getJSONArray("placements").getJSONObject(0);
            return new f(jsonObject.optString("message", ""), jsonObject.optInt("code", 0), new com.facebook.ads.internal.fpackage.d(com.facebook.ads.internal.fpackage.e.a(jsonObject2.getJSONObject("definition")), jsonObject2.optString("feature_config")));
        }
        catch (JSONException ex) {
            return this.c(jsonObject);
        }
    }
    
    private f c(final JSONObject jsonObject) {
        return new f(jsonObject.optString("message", ""), jsonObject.optInt("code", 0), null);
    }
    
    static {
        c.a = new c();
    }
}
