// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.epackage;

import android.database.SQLException;
import android.content.ContentValues;
import java.util.UUID;
import android.text.TextUtils;
import android.support.annotation.WorkerThread;
import android.database.Cursor;

public class h extends g
{
    public static final b a;
    public static final b b;
    public static final b[] c;
    private static final String d;
    private static final String e;
    private static final String f;
    private static final String g;
    
    public h(final d d) {
        super(d);
    }
    
    @Override
    public String a() {
        return "tokens";
    }
    
    @Override
    public b[] b() {
        return h.c;
    }
    
    @WorkerThread
    Cursor c() {
        return this.f().rawQuery(h.e, (String[])null);
    }
    
    @WorkerThread
    String a(final String s) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            throw new IllegalArgumentException("Invalid token.");
        }
        Cursor rawQuery = null;
        try {
            rawQuery = this.f().rawQuery(h.f, new String[] { s });
            final String s2 = rawQuery.moveToNext() ? rawQuery.getString(h.a.a) : null;
            if (!TextUtils.isEmpty((CharSequence)s2)) {
                return s2;
            }
            final String string = UUID.randomUUID().toString();
            final ContentValues contentValues = new ContentValues(2);
            contentValues.put(h.a.b, string);
            contentValues.put(h.b.b, s);
            this.f().insertOrThrow("tokens", (String)null, contentValues);
            return string;
        }
        finally {
            if (rawQuery != null) {
                rawQuery.close();
            }
        }
    }
    
    @WorkerThread
    public void d() {
        try {
            this.f().execSQL(h.g);
        }
        catch (SQLException ex) {}
    }
    
    static {
        a = new b(0, "token_id", "TEXT PRIMARY KEY");
        b = new b(1, "token", "TEXT");
        c = new b[] { h.a, h.b };
        d = h.class.getSimpleName();
        e = com.facebook.ads.internal.epackage.g.a("tokens", h.c);
        f = com.facebook.ads.internal.epackage.g.a("tokens", h.c, h.b);
        g = "DELETE FROM tokens WHERE NOT EXISTS (SELECT 1 FROM events WHERE tokens." + h.a.b + " = " + "events" + "." + com.facebook.ads.internal.epackage.c.b.b + ")";
    }
}
