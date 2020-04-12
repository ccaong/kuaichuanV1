// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.gpackage;

import com.facebook.ads.internal.util.x;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.database.Cursor;
import android.text.TextUtils;
import java.util.Map;
import android.util.Log;
import com.facebook.ads.internal.dpackage.a;
import android.content.Context;
import com.facebook.ads.internal.epackage.d;

public class g implements e.a, f
{
    private static final String a;
    private static g b;
    private static double c;
    private static String d;
    private final e e;
    private final d f;
    private final Context context;
    
    protected g(final Context g) {
        this.f = new d(g);
        (this.e = new e(g, this)).b();
        this.context = g;
        com.facebook.ads.internal.dpackage.a.a(context).a();
    }
    
    public static g a(final Context context) {
        if (g.b == null) {
            final Context applicationContext = context.getApplicationContext();
            synchronized (applicationContext) {
                if (g.b == null) {
                    g.b = new g(applicationContext);
                    com.facebook.ads.internal.fpackage.g.a();
                    g.c = com.facebook.ads.internal.fpackage.g.b();
                    g.d = com.facebook.ads.internal.fpackage.g.c();
                }
            }
        }
        return g.b;
    }
    
    public void b(final String s) {
        Log.e(com.facebook.ads.internal.gpackage.g.a, "AdEventManager error: " + s);
    }
    
    @Override
    public void c(final String s, final Map<String, String> map) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            return;
        }
        this.a(new o(this.context, s, com.facebook.ads.internal.gpackage.g.c, com.facebook.ads.internal.gpackage.g.d, map));
    }
    
    @Override
    public void a(final String s, final Map<String, String> map) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            return;
        }
        this.a(new i(this.context, s, com.facebook.ads.internal.gpackage.g.c, com.facebook.ads.internal.gpackage.g.d, map));
    }
    
    public void f(final String s, final Map<String, String> map) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            return;
        }
        this.a(new j(this.context, s, com.facebook.ads.internal.gpackage.g.c, com.facebook.ads.internal.gpackage.g.d, map));
    }
    
    public void g(final String s, final Map<String, String> map) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            return;
        }
        this.a(new n(this.context, s, com.facebook.ads.internal.gpackage.g.c, com.facebook.ads.internal.gpackage.g.d, map));
    }
    
    @Override
    public void b(final String s, final Map<String, String> map) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            return;
        }
        this.a(new k(this.context, s, com.facebook.ads.internal.gpackage.g.c, com.facebook.ads.internal.gpackage.g.d, map));
    }
    
    @Override
    public void e(final String s, final Map<String, String> map) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            return;
        }
        this.a(new b(this.context, s, com.facebook.ads.internal.gpackage.g.c, com.facebook.ads.internal.gpackage.g.d, map));
    }
    
    @Override
    public void d(final String s, final Map<String, String> map) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            return;
        }
        this.a(new m(s, com.facebook.ads.internal.gpackage.g.c, com.facebook.ads.internal.gpackage.g.d, map));
    }
    
    public void a(final String s, final com.facebook.ads.internal.util.k k) {
        this.a(new com.facebook.ads.internal.gpackage.a(s, com.facebook.ads.internal.gpackage.g.c, com.facebook.ads.internal.gpackage.g.d, k));
    }
    
    public void a(final String s, final Map<String, String> map, final String s2, final h h) {
        this.a(new l(this.context, s, com.facebook.ads.internal.gpackage.g.c, com.facebook.ads.internal.gpackage.g.d, map, s2, h));
    }
    
    private void a(final com.facebook.ads.internal.gpackage.d d) {
        this.f.a(d, new com.facebook.ads.internal.epackage.a<String>() {
            @Override
            public void a(final String s) {
                super.a(s);
                if (d.i()) {
                    com.facebook.ads.internal.gpackage.g.this.e.a();
                }
                else {
                    com.facebook.ads.internal.gpackage.g.this.e.b();
                }
            }
            
            @Override
            public void a(final int n, final String s) {
                super.a(n, s);
                if (!(d instanceof c)) {
                    com.facebook.ads.internal.gpackage.g.this.b(s);
                }
            }
        });
    }
    
    @Override
    public boolean c() {
        final int h = com.facebook.ads.internal.h.h(this.context);
        if (h < 1) {
            return false;
        }
        Cursor d = null;
        try {
            d = this.f.d();
            return d.moveToFirst() && d.getInt(0) > h;
        }
        finally {
            if (d != null) {
                d.close();
            }
        }
    }
    
    @Override
    public JSONObject a() {
        final int h = com.facebook.ads.internal.h.h(this.context);
        return (h > 0) ? this.a(h) : this.d();
    }
    
    private JSONObject d() {
        Cursor f = null;
        Cursor e = null;
        try {
            JSONObject jsonObject = null;
            f = this.f.f();
            e = this.f.e();
            if (f.getCount() > 0 && e.getCount() > 0) {
                jsonObject = new JSONObject();
                jsonObject.put("tokens", (Object)this.a(f));
                jsonObject.put("events", (Object)this.b(e));
            }
            if (com.facebook.ads.internal.h.e(this.context)) {
                final JSONArray a = com.facebook.ads.internal.util.n.a(this.context);
                if (a != null && a.length() > 0) {
                    if (jsonObject == null) {
                        jsonObject = new JSONObject();
                    }
                    jsonObject.put("debug", (Object)a);
                }
            }
            return jsonObject;
        }
        catch (JSONException ex) {
            return null;
        }
        finally {
            if (f != null) {
                f.close();
            }
            if (e != null) {
                e.close();
            }
        }
    }
    
    private JSONObject a(final int n) {
        Cursor d = null;
        Cursor a = null;
        try {
            JSONObject jsonObject = null;
            d = this.f.d();
            a = this.f.a(n);
            if (a.getCount() > 0) {
                jsonObject = new JSONObject();
                jsonObject.put("tokens", (Object)this.a(a));
                jsonObject.put("events", (Object)this.c(a));
            }
            if (com.facebook.ads.internal.h.e(this.context)) {
                final JSONArray a2 = com.facebook.ads.internal.util.n.a(this.context);
                if (a2 != null && a2.length() > 0) {
                    if (jsonObject == null) {
                        jsonObject = new JSONObject();
                    }
                    jsonObject.put("debug", (Object)a2);
                }
            }
            return jsonObject;
        }
        catch (JSONException ex) {
            return null;
        }
        finally {
            if (d != null) {
                d.close();
            }
            if (a != null) {
                a.close();
            }
        }
    }
    
    @Override
    public boolean a(final JSONArray jsonArray) {
        boolean b = true;
        final boolean e = com.facebook.ads.internal.h.e(this.context);
        boolean b2 = false;
        for (int i = 0; i < jsonArray.length(); ++i) {
            try {
                final JSONObject jsonObject = jsonArray.getJSONObject(i);
                final String string = jsonObject.getString("id");
                final int int1 = jsonObject.getInt("code");
                if (int1 == 1) {
                    if (e && jsonObject.optInt("dbtype", 0) == 1) {
                        b2 = true;
                    }
                    else {
                        this.f.a(string);
                    }
                }
                else if (int1 >= 1000 && int1 < 2000) {
                    b = false;
                }
                else if (int1 >= 2000 && int1 < 3000) {
                    if (e && jsonObject.optInt("dbtype", 0) == 1) {
                        b2 = true;
                    }
                    else {
                        this.f.a(string);
                    }
                }
            }
            catch (JSONException ex) {}
        }
        if (b2) {
            com.facebook.ads.internal.util.n.b(this.context);
        }
        return b;
    }
    
    @Override
    public void b() {
        this.f.g();
        this.f.b();
    }
    
    private JSONObject a(final Cursor cursor) throws JSONException {
        final JSONObject jsonObject = new JSONObject();
        while (cursor.moveToNext()) {
            jsonObject.put(cursor.getString(0), (Object)cursor.getString(1));
        }
        return jsonObject;
    }
    
    private JSONArray b(final Cursor cursor) throws JSONException {
        final JSONArray jsonArray = new JSONArray();
        cursor.moveToPosition(-1);
        while (cursor.moveToNext()) {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", (Object)cursor.getString(com.facebook.ads.internal.epackage.c.a.a));
            jsonObject.put("token_id", (Object)cursor.getString(com.facebook.ads.internal.epackage.c.b.a));
            jsonObject.put("type", (Object)cursor.getString(com.facebook.ads.internal.epackage.c.d.a));
            jsonObject.put("time", (Object)com.facebook.ads.internal.util.g.a(cursor.getDouble(com.facebook.ads.internal.epackage.c.e.a)));
            jsonObject.put("session_time", (Object)com.facebook.ads.internal.util.g.a(cursor.getDouble(com.facebook.ads.internal.epackage.c.f.a)));
            jsonObject.put("session_id", (Object)cursor.getString(com.facebook.ads.internal.epackage.c.g.a));
            final String string = cursor.getString(com.facebook.ads.internal.epackage.c.h.a);
            jsonObject.put("data", (Object)((string != null) ? new JSONObject(string) : new JSONObject()));
            jsonArray.put((Object)jsonObject);
        }
        return jsonArray;
    }
    
    private JSONArray c(final Cursor cursor) throws JSONException {
        final JSONArray jsonArray = new JSONArray();
        cursor.moveToPosition(-1);
        while (cursor.moveToNext()) {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", (Object)cursor.getString(2));
            jsonObject.put("token_id", (Object)cursor.getString(0));
            jsonObject.put("type", (Object)cursor.getString(4));
            jsonObject.put("time", (Object)com.facebook.ads.internal.util.g.a(cursor.getDouble(5)));
            jsonObject.put("session_time", (Object)com.facebook.ads.internal.util.g.a(cursor.getDouble(6)));
            jsonObject.put("session_id", (Object)cursor.getString(7));
            final String string = cursor.getString(8);
            jsonObject.put("data", (Object)((string != null) ? new JSONObject(string) : new JSONObject()));
            jsonArray.put((Object)jsonObject);
        }
        return jsonArray;
    }
    
    @Override
    public void a(final String s) {
        new x().execute((String[])new String[] { s });
    }
    
    static {
        a = g.class.getSimpleName();
    }
}
