//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.facebook.ads.internal.cpackage;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import com.facebook.ads.internal.ipackage.b.f;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class d {
    private static final String a = d.class.getSimpleName();
    private static d b;
    private final Future<f> c;

    public static d a(Context var0) {
        if(b == null) {
            Context var1 = var0.getApplicationContext();
            synchronized(var1) {
                if(b == null) {
                    b = new d(var1);
                }
            }
        }

        return b;
    }

    private d(final Context var1) {
        this.c = Executors.newSingleThreadExecutor().submit(new Callable() {
            public f call() {
                return new f(var1);
            }
        });
    }

    public void a(String var1) {
        f var2 = this.a();
        if(var2 != null) {
            var2.a(var1);
        }

    }

    @Nullable
    public String b(String var1) {
        f var2 = this.a();
        return var2 == null?null:var2.b(var1);
    }

    @Nullable
    private f a() {
        try {
            return (f)this.c.get(500L, TimeUnit.MILLISECONDS);
        } catch (ExecutionException | TimeoutException | InterruptedException var2) {
            Log.e(a, "Timed out waiting for cache server.", var2);
            return null;
        }
    }
}
