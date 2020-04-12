// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.ipackage.b;

final class j
{
    static <T> T a(final T t) {
        if (t == null) {
            throw new NullPointerException();
        }
        return t;
    }
    
    static <T> T a(final T t, final String s) {
        if (t == null) {
            throw new NullPointerException(s);
        }
        return t;
    }
    
    static void a(final boolean b, final String s) {
        if (!b) {
            throw new IllegalArgumentException(s);
        }
    }
}
