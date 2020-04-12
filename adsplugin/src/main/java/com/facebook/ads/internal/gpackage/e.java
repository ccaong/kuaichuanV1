// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.gpackage;

import com.facebook.ads.internal.server.b;
import android.support.annotation.WorkerThread;
import com.facebook.ads.internal.ipackage.a.n;
import android.net.NetworkInfo;
import org.json.JSONArray;
import android.text.TextUtils;
import com.facebook.ads.internal.ipackage.a.p;
import org.json.JSONObject;
import com.facebook.ads.internal.h;
import android.os.Looper;
import com.facebook.ads.internal.util.w;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executor;
import android.os.AsyncTask;
import android.content.Context;
import android.os.Handler;
import com.facebook.ads.internal.ipackage.a.a;
import android.net.ConnectivityManager;
import java.util.concurrent.ThreadPoolExecutor;

public class e
{
    private static final String a;
    private static final String b;
    private final a c;
    private final ThreadPoolExecutor d;
    private final ConnectivityManager e;
    private final com.facebook.ads.internal.ipackage.a.a f;
    private final Handler g;
    private final long h;
    private final long i;
    private final Runnable j;
    private volatile boolean k;
    private int l;
    private long m;
    
    e(final Context context, final a c) {
        this.j = new Runnable() {
            @Override
            public void run() {
                com.facebook.ads.internal.gpackage.e.this.k = false;
                if (com.facebook.ads.internal.gpackage.e.this.d.getQueue().isEmpty()) {
                    new AsyncTask<Void, Void, Void>() {
                        protected Void doInBackground(final Void... array) {
                            ++com.facebook.ads.internal.gpackage.e.this.l;
                            if (com.facebook.ads.internal.gpackage.e.this.m > 0L) {
                                try {
                                    Thread.sleep(com.facebook.ads.internal.gpackage.e.this.m);
                                }
                                catch (InterruptedException ex) {}
                            }
                            com.facebook.ads.internal.gpackage.e.this.d();
                            return null;
                        }
                    }.executeOnExecutor((Executor)com.facebook.ads.internal.gpackage.e.this.d, (Void[])new Void[0]);
                }
            }
        };
        this.c = c;
        this.d = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        this.e = (ConnectivityManager)context.getSystemService("connectivity");
        this.f = w.b(context);
        this.g = new Handler(Looper.getMainLooper());
        this.h = com.facebook.ads.internal.h.f(context);
        this.i = com.facebook.ads.internal.h.g(context);
    }
    
    void a() {
        this.k = true;
        this.g.removeCallbacks(this.j);
        this.a(this.h);
    }
    
    void b() {
        if (this.k) {
            return;
        }
        this.k = true;
        this.g.removeCallbacks(this.j);
        this.a(this.i);
    }
    
    private void a(final long n) {
        this.g.postDelayed(this.j, n);
    }
    
    private void c() {
        if (this.l >= 5) {
            this.e();
            this.b();
            return;
        }
        if (this.l == 1) {
            this.m = 2000L;
        }
        else {
            this.m *= 2L;
        }
        this.a();
    }
    
    @WorkerThread
    private void d() {
        try {
            final NetworkInfo activeNetworkInfo = this.e.getActiveNetworkInfo();
            if (activeNetworkInfo == null || !activeNetworkInfo.isConnectedOrConnecting()) {
                this.a(this.i);
                return;
            }
            final JSONObject a = this.c.a();
            if (a == null) {
                this.e();
                return;
            }
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("attempt", (Object)String.valueOf(this.l));
            a.put("data", (Object)jsonObject);
            final p p = new p();
            p.a("payload", a.toString());
            final n b = this.f.b(com.facebook.ads.internal.gpackage.e.b, p);
            final String s = (b != null) ? b.e() : null;
            if (TextUtils.isEmpty((CharSequence)s)) {
                this.c();
            }
            else if (b.a() != 200) {
                this.c();
            }
            else if (!this.c.a(new JSONArray(s))) {
                this.c();
            }
            else if (this.c.c()) {
                this.c();
            }
            else {
                this.e();
            }
        }
        catch (Exception ex) {
            this.c();
        }
    }
    
    private void e() {
        this.l = 0;
        this.m = 0L;
        if (this.d.getQueue().size() == 0) {
            this.c.b();
        }
    }
    
    static {
        a = e.class.getSimpleName();
        b = com.facebook.ads.internal.server.b.b();
    }
    
    interface a
    {
        JSONObject a();
        
        boolean a(final JSONArray p0);
        
        void b();
        
        boolean c();
    }
}
