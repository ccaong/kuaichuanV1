// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.epackage;

import android.database.sqlite.SQLiteDatabase;

public abstract class g
{
    protected final d j;
    
    protected g(final d j) {
        this.j = j;
    }
    
    public static String a(final String s, final b[] array) {
        final StringBuilder sb = new StringBuilder("SELECT ");
        for (int i = 0; i < array.length - 1; ++i) {
            sb.append(array[i].b);
            sb.append(", ");
        }
        sb.append(array[array.length - 1].b);
        sb.append(" FROM ");
        sb.append(s);
        return sb.toString();
    }
    
    public static String a(final String s, final b[] array, final b b) {
        final StringBuilder sb = new StringBuilder(a(s, array));
        sb.append(" WHERE ");
        sb.append(b.b);
        sb.append(" = ?");
        return sb.toString();
    }
    
    public abstract String a();
    
    public abstract b[] b();
    
    public void a(final SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + this.a() + " (" + this.c() + ")");
    }
    
    public void b(final SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + this.a());
    }
    
    public void e() {
    }
    
    protected SQLiteDatabase f() {
        return this.j.a();
    }
    
    private String c() {
        final b[] b = this.b();
        if (b.length < 1) {
            return null;
        }
        String string = "";
        for (int i = 0; i < b.length - 1; ++i) {
            string = string + b[i].a() + ", ";
        }
        return string + b[b.length - 1].a();
    }
}
