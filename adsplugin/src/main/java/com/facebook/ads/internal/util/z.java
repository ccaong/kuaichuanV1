// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.util;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.ThreadFactory;

public class z implements ThreadFactory
{
    private int b;
    protected final AtomicLong a;
    
    public z() {
        this.a = new AtomicLong();
        this.b = Thread.currentThread().getPriority();
    }
    
    @Override
    public Thread newThread(final Runnable runnable) {
        final Thread thread = new Thread(null, runnable, this.a(), 0L);
        thread.setPriority(this.b);
        return thread;
    }
    
    protected String a() {
        return String.format(Locale.ENGLISH, "com.facebook.ads thread-%d %tF %<tT", this.a.incrementAndGet(), System.currentTimeMillis());
    }
}
