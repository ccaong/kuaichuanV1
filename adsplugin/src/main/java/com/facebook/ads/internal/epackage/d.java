//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.facebook.ads.internal.epackage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.annotation.WorkerThread;
import com.facebook.ads.internal.epackage.a;
import com.facebook.ads.internal.epackage.c;
import com.facebook.ads.internal.epackage.e;
import com.facebook.ads.internal.epackage.f;
import com.facebook.ads.internal.epackage.g;
import com.facebook.ads.internal.epackage.h;
import com.facebook.ads.internal.epackage.i;

public class d {
    private static final String a;
    private final Context b;
    private final h cc;
    private final c d;
    private SQLiteOpenHelper e;

    public d(Context var1) {
        this.b = var1;
        this.cc = new h(this);
        this.d = new c(this);
    }

    public SQLiteDatabase a() {
        if(Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalStateException("Cannot call getDatabase from the UI thread!");
        } else {
            return this.h();
        }
    }

    private synchronized SQLiteDatabase h() {
        if(this.e == null) {
            this.e = new e(this.b, this);
        }

        return this.e.getWritableDatabase();
    }

    public void b() {
        g[] var1 = this.c();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            g var4 = var1[var3];
            var4.e();
        }

        if(this.e != null) {
            this.e.close();
            this.e = null;
        }

    }

    public g[] c() {
        return new g[]{this.cc, this.d};
    }

    public <T> AsyncTask a(final f<T> var1, final a<T> var2) {
        return com.facebook.ads.internal.util.g.a(new AsyncTask<Void, Void, T>() {
            private com.facebook.ads.internal.epackage.f.a d;

            protected T doInBackground(Void... var1x) {
                T var2x = null;

                try {
                    var2x = var1.b();
                    this.d = var1.c();
                } catch (SQLiteException var4) {
                    this.d = com.facebook.ads.internal.epackage.f.a.UNKNOWN;
                }

                return var2x;
            }

            protected void onPostExecute(T var1x) {
                if(this.d == null) {
                    var2.a(var1x);
                } else {
                    var2.a(this.d.a(), this.d.b());
                }

                var2.a();
            }
        }, new Void[0]);
    }

    public Cursor d() {
        return this.d.c();
    }

    @WorkerThread
    public Cursor e() {
        return this.d.d();
    }

    @WorkerThread
    public Cursor f() {
        return this.cc.c();
    }

    @WorkerThread
    public Cursor a(int var1) {
        return this.a().rawQuery(a + " LIMIT " + var1, (String[])null);
    }

    public AsyncTask a(final com.facebook.ads.internal.gpackage.d var1, a<String> var2) {
        return this.a((f)(new i() {
            public String b() {
                try {
                    SQLiteDatabase var1x = d.this.a();
                    var1x.beginTransaction();
                    String var2 = null;
                    if(var1.d() != null) {
                        var2 = d.this.d.a(d.this.cc.a(var1.d()), var1.a().c, var1.b(), var1.e(), var1.f(), var1.g(), var1.h());
                    }

                    var1x.setTransactionSuccessful();
                    var1x.endTransaction();
                    return var2;
                } catch (Exception var3) {
                    this.a(com.facebook.ads.internal.epackage.f.a.DATABASE_INSERT);
                    return null;
                }
            }
        }), var2);
    }

    @WorkerThread
    public boolean a(String var1) {
        return this.d.a(var1);
    }

    @WorkerThread
    public void g() {
        this.cc.d();
    }

    static {
        a = "SELECT tokens." + h.a.b + ", " + "tokens" + "." + h.b.b + ", " + "events" + "." + c.a.b + ", " + "events" + "." + c.c.b + ", " + "events" + "." + c.d.b + ", " + "events" + "." + c.e.b + ", " + "events" + "." + c.f.b + ", " + "events" + "." + c.g.b + ", " + "events" + "." + c.h.b + " FROM " + "events" + " JOIN " + "tokens" + " ON " + "events" + "." + c.b.b + " = " + "tokens" + "." + h.a.b + " ORDER BY " + "events" + "." + c.e.b + " ASC";
    }
}
