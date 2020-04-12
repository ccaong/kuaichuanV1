//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.facebook.ads.internal.cpackage;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import com.facebook.ads.internal.cpackage.c;
import com.facebook.ads.internal.cpackage.d;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class b {
    private static final String a = b.class.getSimpleName();
    private final Handler handler = new Handler();
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final c dd;
    private final d e;
    private final List<Callable<Boolean>> f;

    public b(Context var1) {
        this.dd = c.a(var1);
        this.e = d.a(var1);
        this.f = new ArrayList();
    }

    public void a(String var1) {
        this.f.add(new b.a(var1));
    }

    public void b(String var1) {
        this.f.add(new b.bb(var1));
    }

    public String c(String var1) {
        return this.e.b(var1);
    }

    public void a(final com.facebook.ads.internal.cpackage.a var1) {
        final ArrayList var2 = new ArrayList(this.f);
        this.executorService.submit(new Runnable() {
            public void run() {
                ArrayList var1x = new ArrayList(var2.size());
                Iterator var2x = var2.iterator();

                while(var2x.hasNext()) {
                    Callable var3 = (Callable)var2x.next();
                    var1x.add(b.this.executorService.submit(var3));
                }

                try {
                    var2x = var1x.iterator();

                    while(var2x.hasNext()) {
                        Future var5 = (Future)var2x.next();
                        var5.get();
                    }
                } catch (ExecutionException | InterruptedException var4) {
                    Log.e(b.a, "Exception while executing cache downloads.", var4);
                }

                b.this.handler.post(new Runnable() {
                    public void run() {
                        var1.a();
                    }
                });
            }
        });
        this.f.clear();
    }

    private class bb implements Callable<Boolean> {
        private final String b;

        public bb(String var2) {
            this.b = var2;
        }

        public Boolean call() {
            b.this.e.a(this.b);
            return Boolean.valueOf(true);
        }
    }

    private class a implements Callable<Boolean> {
        private final String b;

        public a(String var2) {
            this.b = var2;
        }

        public Boolean call() {
            b.this.dd.a(this.b);
            return Boolean.valueOf(true);
        }
    }
}
