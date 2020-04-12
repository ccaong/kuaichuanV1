// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.fpackage;

import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONArray;
import java.util.List;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import org.json.JSONObject;
import java.util.Date;

public class b
{
    public String a;
    public String b;
    public String c;
    public Date d;
    public boolean e;
    
    public b(final JSONObject jsonObject) {
        this(jsonObject.optString("url"), jsonObject.optString("key"), jsonObject.optString("value"), new Date(jsonObject.optLong("expiration") * 1000L));
    }
    
    public b(final String a, final String b, final String c, final Date d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = (b != null && c != null);
    }
    
    public String a() {
        Date d = this.d;
        if (d == null) {
            d = new Date();
            d.setTime(d.getTime() + 3600000L);
        }
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss zzz");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return simpleDateFormat.format(d);
    }
    
    public boolean b() {
        return this.b != null && this.c != null && this.a != null;
    }
    
    public static List<b> a(final String s) {
        if (s == null) {
            return null;
        }
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(s);
        }
        catch (JSONException ex) {}
        if (jsonArray == null) {
            return null;
        }
        final ArrayList<b> list = new ArrayList<b>();
        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject jsonObject = null;
            try {
                jsonObject = jsonArray.getJSONObject(i);
            }
            catch (JSONException ex2) {}
            if (jsonObject != null) {
                final b b = new b(jsonObject);
                if (b != null) {
                    list.add(b);
                }
            }
        }
        return list;
    }
}
