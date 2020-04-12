// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.adapters;

import java.util.Iterator;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;
import android.content.Intent;
import com.facebook.ads.internal.util.g;
import android.os.Bundle;
import java.util.Map;
import java.util.Collection;
import com.facebook.ads.internal.util.e;
import com.facebook.ads.internal.util.f;

public class o implements f.a
{
    private final String a;
    private final String b;
    private final e c;
    private final Collection<String> d;
    private final Map<String, String> ee;
    private final String ff;
    private final int gg;
    private final int h;
    private final int i;
    private final String j;
    
    private o(final String a, final String b, final e c, final Collection<String> d, final Map<String, String> e, final String f, final int g, final int h, final int i, final String j) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.ee = e;
        this.ff = f;
        this.gg = g;
        this.h = h;
        this.i = i;
        this.j = j;
    }
    
    public String a() {
        return this.a;
    }
    
    public String b() {
        return this.b;
    }
    
    @Override
    public e D() {
        return this.c;
    }
    
    @Override
    public Collection<String> E() {
        return this.d;
    }
    
    public Map<String, String> c() {
        return this.ee;
    }
    
    public String d() {
        return this.ff;
    }
    
    public int e() {
        return this.gg;
    }
    
    public int f() {
        return this.h;
    }
    
    @Override
    public String B() {
        return this.j;
    }
    
    public Bundle g() {
        final Bundle bundle = new Bundle();
        bundle.putByteArray("markup", com.facebook.ads.internal.util.g.a(this.a));
        bundle.putString("request_id", this.ff);
        bundle.putInt("viewability_check_initial_delay", this.gg);
        bundle.putInt("viewability_check_interval", this.h);
        bundle.putInt("skip_after_seconds", this.i);
        bundle.putString("ct", this.j);
        return bundle;
    }
    
    public void a(final Intent intent) {
        intent.putExtra("markup", com.facebook.ads.internal.util.g.a(this.a));
        intent.putExtra("activation_command", this.b);
        intent.putExtra("request_id", this.ff);
        intent.putExtra("viewability_check_initial_delay", this.gg);
        intent.putExtra("viewability_check_interval", this.h);
        intent.putExtra("skipAfterSeconds", this.i);
        intent.putExtra("ct", this.j);
    }
    
    public static o a(final Bundle bundle) {
        return new o(g.a(bundle.getByteArray("markup")), null, e.NONE, null, null, bundle.getString("request_id"), bundle.getInt("viewability_check_initial_delay"), bundle.getInt("viewability_check_interval"), bundle.getInt("skip_after_seconds", 0), bundle.getString("ct"));
    }
    
    public static o b(final Intent intent) {
        return new o(g.a(intent.getByteArrayExtra("markup")), intent.getStringExtra("activation_command"), e.NONE, null, null, intent.getStringExtra("request_id"), intent.getIntExtra("viewability_check_initial_delay", 0), intent.getIntExtra("viewability_check_interval", 1000), intent.getIntExtra("skipAfterSeconds", 0), intent.getStringExtra("ct"));
    }
    
    public static o a(final JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        final String optString = jsonObject.optString("markup");
        final String optString2 = jsonObject.optString("activation_command");
        final String optString3 = jsonObject.optString("request_id");
        final String a = g.a(jsonObject, "ct");
        final e a2 = e.a(jsonObject.optString("invalidation_behavior"));
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(jsonObject.optString("detection_strings"));
        }
        catch (JSONException ex) {
            ex.printStackTrace();
        }
        final Collection<String> a3 = f.a(jsonArray);
        final JSONObject optJSONObject = jsonObject.optJSONObject("metadata");
        final HashMap<String, String> hashMap = new HashMap<>();
        if (optJSONObject != null) {
            final Iterator<String> keys = optJSONObject.keys();
            while (keys.hasNext()) {
                final String s = keys.next();
                hashMap.put(s, optJSONObject.optString(s));
            }
        }
        int int1 = 0;
        int int2 = 1000;
        int int3 = 0;
        if (hashMap.containsKey("viewability_check_initial_delay")) {
            int1 = Integer.parseInt(hashMap.get("viewability_check_initial_delay"));
        }
        if (hashMap.containsKey("viewability_check_interval")) {
            int2 = Integer.parseInt(hashMap.get("viewability_check_interval"));
        }
        if (hashMap.containsKey("skip_after_seconds")) {
            int3 = Integer.parseInt(hashMap.get("skip_after_seconds"));
        }
        return new o(optString, optString2, a2, a3, hashMap, optString3, int1, int2, int3, a);
    }
}
