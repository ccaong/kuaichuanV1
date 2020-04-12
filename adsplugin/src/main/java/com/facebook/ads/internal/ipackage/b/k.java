// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.ipackage.b;

import android.util.Log;
import java.util.concurrent.atomic.AtomicInteger;

class k
{
    private final n a;
    private final com.facebook.ads.internal.ipackage.b.a b;
    private final Object c;
    private final Object d;
    private final AtomicInteger e;
    private volatile Thread f;
    private volatile boolean g;
    private volatile int h;
    
    public k(final n n, final com.facebook.ads.internal.ipackage.b.a a) {
        this.c = new Object();
        this.d = new Object();
        this.h = -1;
        this.a = j.a(n);
        this.b = j.a(a);
        this.e = new AtomicInteger();
    }
    
    public int a(final byte[] array, final long n, final int n2) throws l {
        m.a(array, n, n2);
        while (!this.b.d() && this.b.a() < n + n2 && !this.g) {
            this.c();
            this.d();
            this.b();
        }
        final int a = this.b.a(array, n, n2);
        if (this.b.d() && this.h != 100) {
            this.a(this.h = 100);
        }
        return a;
    }
    
    private void b() throws l {
        final int value = this.e.get();
        if (value >= 1) {
            this.e.set(0);
            throw new l("Error reading source " + value + " times");
        }
    }
    
    public void a() {
        synchronized (this.d) {
            Log.d("ProxyCache", "Shutdown proxy for " + this.a);
            try {
                this.g = true;
                if (this.f != null) {
                    this.f.interrupt();
                }
                this.b.b();
            }
            catch (l l) {
                this.a(l);
            }
        }
    }
    
    private synchronized void c() {
        final boolean b = this.f != null && this.f.getState() != Thread.State.TERMINATED;
        if (!this.g && !this.b.d() && !b) {
            (this.f = new Thread(new a(), "Source reader for " + this.a)).start();
        }
    }
    
    private void d() throws l {
        synchronized (this.c) {
            try {
                this.c.wait(1000L);
            }
            catch (InterruptedException ex) {
                throw new l("Waiting source data is interrupted!", ex);
            }
        }
    }
    
    private void b(final long n, final long n2) {
        this.a(n, n2);
        synchronized (this.c) {
            this.c.notifyAll();
        }
    }
    
    protected void a(final long n, final long n2) {
        final int h = (n2 == 0L) ? 100 : ((int)(n * 100L / n2));
        final boolean b = h != this.h;
        if (n2 >= 0L && b) {
            this.a(h);
        }
        this.h = h;
    }
    
    protected void a(final int n) {
    }
    
    private void e() {
        int a = -1;
        int a2 = 0;
        try {
            a2 = this.b.a();
            this.a.a(a2);
            a = this.a.a();
            final byte[] array = new byte[8192];
            int a3;
            while ((a3 = this.a.a(array)) != -1) {
                synchronized (this.d) {
                    if (this.g()) {
                        return;
                    }
                    this.b.a(array, a3);
                }
                a2 += a3;
                this.b(a2, a);
            }
            this.f();
        }
        catch (Throwable t) {
            this.e.incrementAndGet();
            this.a(t);
        }
        finally {
            this.h();
            this.b(a2, a);
        }
    }
    
    private void f() throws l {
        synchronized (this.d) {
            if (!this.g() && this.b.a() == this.a.a()) {
                this.b.c();
            }
        }
    }
    
    private boolean g() {
        return Thread.currentThread().isInterrupted() || this.g;
    }
    
    private void h() {
        try {
            this.a.b();
        }
        catch (l l) {
            this.a(new l("Error closing source " + this.a, l));
        }
    }
    
    protected final void a(final Throwable t) {
        if (t instanceof i) {
            Log.d("ProxyCache", "ProxyCache is interrupted");
        }
        else {
            Log.e("ProxyCache", "ProxyCache error", t);
        }
    }
    
    private class a implements Runnable
    {
        @Override
        public void run() {
            k.this.e();
        }
    }
}
