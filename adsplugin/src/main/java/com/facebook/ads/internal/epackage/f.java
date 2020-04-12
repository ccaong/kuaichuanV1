// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.epackage;

abstract class f<T>
{
    private a a;
    
    protected void a(final a a) {
        this.a = a;
    }
    
    public a c() {
        return this.a;
    }
    
    abstract T b();
    
    public enum a
    {
        UNKNOWN(9000, "An unknown error has occurred."),
        DATABASE_SELECT(3001, "Failed to read from database."),
        DATABASE_INSERT(3002, "Failed to insert row into database."),
        DATABASE_UPDATE(3003, "Failed to update row in database."),
        DATABASE_DELETE(3004, "Failed to delete row from database.");
        
        private final int f;
        private final String g;
        
        private a(final int f, final String g) {
            this.f = f;
            this.g = g;
        }
        
        public int a() {
            return this.f;
        }
        
        public String b() {
            return this.g;
        }
    }
}
