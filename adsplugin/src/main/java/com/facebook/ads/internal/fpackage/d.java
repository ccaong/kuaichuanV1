// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.fpackage;

import java.util.ArrayList;
import android.support.annotation.Nullable;
import java.util.List;

public class d
{
    private List<a> a;
    private int b;
    private e c;
    @Nullable
    private String d;
    
    public d(final e c, @Nullable final String d) {
        this.b = 0;
        this.a = new ArrayList<a>();
        this.c = c;
        this.d = d;
    }
    
    public void a(final a a) {
        this.a.add(a);
    }
    
    public e a() {
        return this.c;
    }
    
    @Nullable
    public String b() {
        return this.d;
    }
    
    public int c() {
        return this.a.size();
    }
    
    public a d() {
        if (this.b < this.a.size()) {
            ++this.b;
            return this.a.get(this.b - 1);
        }
        return null;
    }
}
