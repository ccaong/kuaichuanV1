//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.facebook.ads.internal.ipackage.b.apackage;

import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

abstract class e implements com.facebook.ads.internal.ipackage.b.apackage.a {
    private final ExecutorService a = Executors.newSingleThreadExecutor();

    e() {
    }

    public void a(File var1) {
        this.a.submit(new e.a(var1));
    }

    private void b(File var1) throws IOException {
        d.c(var1);
        List var2 = d.b(var1.getParentFile());
        this.a(var2);
    }

    protected abstract boolean a(File var1, long var2, int var4);

    private void a(List<File> var1) {
        long var2 = this.b(var1);
        int var4 = var1.size();
        Iterator var5 = var1.iterator();

        while(var5.hasNext()) {
            File var6 = (File)var5.next();
            boolean var7 = this.a(var6, var2, var4);
            if(!var7) {
                long var8 = var6.length();
                boolean var10 = var6.delete();
                if(var10) {
                    --var4;
                    var2 -= var8;
                    Log.i("ProxyCache", "Cache file " + var6 + " is deleted because it exceeds cache limit");
                } else {
                    Log.e("ProxyCache", "Error deleting file " + var6 + " for trimming cache");
                }
            }
        }

    }

    private long b(List<File> var1) {
        long var2 = 0L;

        File var5;
        for(Iterator var4 = var1.iterator(); var4.hasNext(); var2 += var5.length()) {
            var5 = (File)var4.next();
        }

        return var2;
    }

    private class a implements Callable<Void> {
        private final File b;

        public a(File var2) {
            this.b = var2;
        }

        public Void call() throws IOException {
            e.this.b(this.b);
            return null;
        }
    }
}
