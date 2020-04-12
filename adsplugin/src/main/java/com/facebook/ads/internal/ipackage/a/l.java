// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.ipackage.a;

public abstract class l
{
    protected String a;
    protected j b;
    protected String c;
    protected byte[] d;
    
    public l(final String a, final p p2) {
        this.a = "";
        if (a != null) {
            this.a = a;
        }
        if (p2 != null) {
            this.a = this.a + "?" + p2.a();
        }
    }
    
    public String a() {
        return this.a;
    }
    
    public j b() {
        return this.b;
    }
    
    public String c() {
        return this.c;
    }
    
    public byte[] d() {
        return this.d;
    }
}
