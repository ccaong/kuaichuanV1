//
// Decompiled by Procyon v0.5.30
//

package com.facebook.ads.internal.epackage;

import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

public class e extends SQLiteOpenHelper
{
    private final d a;

    public e(final Context context, final d a) {
        super(context, "ads.db", (SQLiteDatabase.CursorFactory)null, 3);
        if (a == null) {
            throw new IllegalArgumentException("AdDatabaseHelper can not be null");
        }
        this.a = a;
    }

    public void onCreate(final SQLiteDatabase sqLiteDatabase) {
        final g[] c = this.a.c();
        for (int length = c.length, i = 0; i < length; ++i) {
            c[i].a(sqLiteDatabase);
        }
    }

    public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int n, final int n2) {
        if (n == 2 && n2 == 3) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS crashes");
        }
    }

    public void onDowngrade(final SQLiteDatabase sqLiteDatabase, final int n, final int n2) {
        for (final g g : this.a.c()) {
            g.b(sqLiteDatabase);
            g.a(sqLiteDatabase);
        }
    }

    public void onOpen(final SQLiteDatabase sqLiteDatabase) {
        super.onOpen(sqLiteDatabase);
        if (!sqLiteDatabase.isReadOnly()) {
            sqLiteDatabase.execSQL("PRAGMA foreign_keys = ON;");
        }
    }
}
