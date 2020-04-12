// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.util;

import java.lang.ref.WeakReference;

public abstract class ak<T> implements Runnable
{
    private final WeakReference<T> a;
    
    public ak(final T t) {
        this.a = new WeakReference<T>(t);
    }
    
    public T a() {
        return this.a.get();
    }
}
