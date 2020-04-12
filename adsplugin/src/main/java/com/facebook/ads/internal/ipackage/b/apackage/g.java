// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.ipackage.b.apackage;

import java.io.File;

public class g extends e
{
    private final long a;
    
    public g(final long a) {
        if (a <= 0L) {
            throw new IllegalArgumentException("Max size must be positive number!");
        }
        this.a = a;
    }
    
    @Override
    protected boolean a(final File file, final long n, final int n2) {
        return n <= this.a;
    }
}
