// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.fpackage;

import android.text.TextUtils;
import java.util.Locale;
import java.util.LinkedList;
import java.util.HashMap;
import android.support.annotation.Nullable;
import org.json.JSONArray;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

public class a
{
    private final String a;
    private final JSONObject b;
    private final Map<h, List<String>> c;
    
    public a(final String a, final JSONObject b, @Nullable final JSONArray jsonArray) {
        this.c = new HashMap<h, List<String>>();
        this.a = a;
        this.b = b;
        if (jsonArray == null || jsonArray.length() == 0) {
            return;
        }
        final h[] values = h.values();
        for (int length = values.length, i = 0; i < length; ++i) {
            this.c.put(values[i], new LinkedList<String>());
        }
        for (int j = 0; j < jsonArray.length(); ++j) {
            try {
                final JSONObject jsonObject = jsonArray.getJSONObject(j);
                final String string = jsonObject.getString("type");
                final String string2 = jsonObject.getString("url");
                final h value = h.valueOf(string.toUpperCase(Locale.US));
                if (value != null && !TextUtils.isEmpty((CharSequence)string2)) {
                    this.c.get(value).add(string2);
                }
            }
            catch (Exception ex) {}
        }
    }
    
    public String a() {
        return this.a;
    }
    
    public JSONObject b() {
        return this.b;
    }
    
    public List<String> a(final h h) {
        return this.c.get(h);
    }
}
