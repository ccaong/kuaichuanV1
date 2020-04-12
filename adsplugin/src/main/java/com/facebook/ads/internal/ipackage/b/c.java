// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.ipackage.b;

import com.facebook.ads.internal.ipackage.b.apackage.a;
import java.io.File;

class c
{
    public final File a;
    public final com.facebook.ads.internal.ipackage.b.apackage.c b;
    public final a c;
    
    c(final File a, final com.facebook.ads.internal.ipackage.b.apackage.c b, final a c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
    
    File a(final String s) {
        return new File(this.a, this.b.a(s));
    }
}
