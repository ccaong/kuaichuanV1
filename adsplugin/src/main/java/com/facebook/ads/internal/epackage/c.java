//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.facebook.ads.internal.epackage;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.WorkerThread;
import com.facebook.ads.internal.epackage.b;
import com.facebook.ads.internal.epackage.d;
import com.facebook.ads.internal.epackage.g;
import java.util.Map;
import java.util.UUID;
import org.json.JSONObject;

public class c extends g {
    public static final b a = new b(0, "event_id", "TEXT PRIMARY KEY");
    public static final b b = new b(1, "token_id", "TEXT REFERENCES tokens ON UPDATE CASCADE ON DELETE RESTRICT");
    public static final b c = new b(2, "priority", "INTEGER");
    public static final b d = new b(3, "type", "TEXT");
    public static final b e = new b(4, "time", "REAL");
    public static final b f = new b(5, "session_time", "REAL");
    public static final b g = new b(6, "session_id", "TEXT");
    public static final b h = new b(7, "data", "TEXT");
    public static final b[] i;
    private static final String k;

    public c(d var1) {
        super(var1);
    }

    public String a() {
        return "events";
    }

    public b[] b() {
        return i;
    }

    @WorkerThread
    Cursor c() {
        return this.f().rawQuery("SELECT count(*) FROM events", (String[])null);
    }

    @WorkerThread
    Cursor d() {
        return this.f().rawQuery(k, (String[])null);
    }

    @WorkerThread
    String a(String var1, int var2, String var3, double var4, double var6, String var8, Map<String, String> var9) {
        String var10 = UUID.randomUUID().toString();
        ContentValues var11 = new ContentValues(7);
        var11.put(a.b, var10);
        var11.put(b.b, var1);
        var11.put(c.b, Integer.valueOf(var2));
        var11.put(d.b, var3);
        var11.put(e.b, Double.valueOf(var4));
        var11.put(f.b, Double.valueOf(var6));
        var11.put(g.b, var8);
        var11.put(h.b, var9 != null?(new JSONObject(var9)).toString():null);
        this.f().insertOrThrow("events", (String)null, var11);
        return var10;
    }

    boolean a(String var1) {
        return this.f().delete("events", a.b + " = ?", new String[]{var1}) > 0;
    }

    static {
        i = new b[]{a, b, c, d, e, f, g, h};
        k = a("events", i);
    }
}
