//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.facebook.ads.internal.ipackage.b;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import com.facebook.ads.internal.ipackage.b.g;
import com.facebook.ads.internal.ipackage.b.h;
import com.facebook.ads.internal.ipackage.b.j;
import com.facebook.ads.internal.ipackage.b.l;
import com.facebook.ads.internal.ipackage.b.m;
import com.facebook.ads.internal.ipackage.b.o;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class f {
    private final Object a;
    private final ExecutorService b;
    private final Map<String, g> c;
    private final ServerSocket d;
    private final int e;
    private final Thread f;
    private final com.facebook.ads.internal.ipackage.b.c g;
    private boolean h;

    public f(Context var1) {
        this((new f.a(var1)).a());
    }

    private f(com.facebook.ads.internal.ipackage.b.c var1) {
        this.a = new Object();
        this.b = Executors.newFixedThreadPool(8);
        this.c = new ConcurrentHashMap();
        this.g = j.a(var1);

        try {
            InetAddress var2 = InetAddress.getByName("127.0.0.1");
            this.d = new ServerSocket(0, 8, var2);
            this.e = this.d.getLocalPort();
            CountDownLatch var3 = new CountDownLatch(1);
            this.f = new Thread(new f.e(var3));
            this.f.start();
            var3.await();
            Log.i("ProxyCache", "Proxy cache server started. Ping it...");
            this.b();
        } catch (InterruptedException | IOException var4) {
            this.b.shutdown();
            throw new IllegalStateException("Error starting local proxy server", var4);
        }
    }

    private void b() {
        byte var1 = 3;
        int var2 = 300;

        int var3;
        for(var3 = 0; var3 < var1; var2 *= 2) {
            try {
                Future var4 = this.b.submit(new f.b());
                this.h = ((Boolean)var4.get((long)var2, TimeUnit.MILLISECONDS)).booleanValue();
                if(this.h) {
                    return;
                }

                SystemClock.sleep((long)var2);
            } catch (ExecutionException | TimeoutException | InterruptedException var5) {
                Log.e("ProxyCache", "Error pinging server [attempt: " + var3 + ", timeout: " + var2 + "]. ", var5);
            }

            ++var3;
        }

        Log.e("ProxyCache", "Shutdown server... Error pinging server [attempts: " + var3 + ", max timeout: " + var2 / 2 + "].");
        this.a();
    }

    private boolean c() throws l {
        String var1 = this.d("ping");
        h var2 = new h(var1);

        boolean var4;
        try {
            byte[] var3 = "ping ok".getBytes();
            var2.a(0);
            byte[] var12 = new byte[var3.length];
            var2.a(var12);
            boolean var5 = Arrays.equals(var3, var12);
            Log.d("ProxyCache", "Ping response: `" + new String(var12) + "`, pinged? " + var5);
            boolean var6 = var5;
            return var6;
        } catch (l var10) {
            Log.e("ProxyCache", "Error reading ping response", var10);
            var4 = false;
        } finally {
            var2.b();
        }

        return var4;
    }

    public void a(String var1) {
        byte var2 = 3;
        int var3 = 300;

        int var4;
        for(var4 = 0; var4 < var2; var3 *= 2) {
            try {
                Future var5 = this.b.submit(new f.c(var1));
                if(((Boolean)var5.get()).booleanValue()) {
                    return;
                }

                SystemClock.sleep((long)var3);
            } catch (ExecutionException | InterruptedException var6) {
                Log.e("ProxyCache", "Error precaching url [attempt: " + var4 + ", url: " + var1 + "]. ", var6);
            }

            ++var4;
        }

        Log.e("ProxyCache", "Shutdown server... Error precaching url [attempts: " + var4 + ", url: " + var1 + "].");
        this.a();
    }

    private boolean c(String var1) throws l {
        h var2 = new h(this.d(var1));

        boolean var4;
        try {
            var2.a(0);
            byte[] var3 = new byte[8192];

            while(var2.a(var3) != -1) {
                ;
            }

            return true;
        } catch (l var8) {
            Log.e("ProxyCache", "Error reading url", var8);
            var4 = false;
        } finally {
            var2.b();
        }

        return var4;
    }

    public String b(String var1) {
        if(!this.h) {
            Log.e("ProxyCache", "Proxy server isn\'t pinged. Caching doesn\'t work.");
        }

        return this.h?this.d(var1):var1;
    }

    private String d(String var1) {
        return String.format("http://%s:%d/%s", new Object[]{"127.0.0.1", Integer.valueOf(this.e), m.b(var1)});
    }

    public void a() {
        Log.i("ProxyCache", "Shutdown proxy server");
        this.d();
        this.f.interrupt();

        try {
            if(!this.d.isClosed()) {
                this.d.close();
            }
        } catch (IOException var2) {
            this.a((Throwable)(new l("Error shutting down proxy server", var2)));
        }

    }

    private void d() {
        Object var1 = this.a;
        synchronized(this.a) {
            Iterator var2 = this.c.values().iterator();

            while(var2.hasNext()) {
                g var3 = (g)var2.next();
                var3.a();
            }

            this.c.clear();
        }
    }

    private void e() {
        while(true) {
            try {
                if(!Thread.currentThread().isInterrupted()) {
                    Socket var1 = this.d.accept();
                    Log.d("ProxyCache", "Accept new socket " + var1);
                    this.b.submit(new f.d(var1));
                    continue;
                }
            } catch (IOException var2) {
                this.a((Throwable)(new l("Error during waiting connection", var2)));
            }

            return;
        }
    }

    private void a(Socket var1) {
        try {
            com.facebook.ads.internal.ipackage.b.d var2 = com.facebook.ads.internal.ipackage.b.d.a(var1.getInputStream());
            Log.i("ProxyCache", "Request to cache proxy:" + var2);
            String var3 = m.c(var2.a);
            if("ping".equals(var3)) {
                this.b(var1);
            } else {
                g var4 = this.e(var3);
                var4.a(var2, var1);
            }
        } catch (SocketException var9) {
            Log.d("ProxyCache", "Closing socket... Socket is closed by client.");
        } catch (IOException | l var10) {
            this.a((Throwable)(new l("Error processing request", var10)));
        } finally {
            this.c(var1);
            Log.d("ProxyCache", "Opened connections: " + this.f());
        }

    }

    private void b(Socket var1) throws IOException {
        OutputStream var2 = var1.getOutputStream();
        var2.write("HTTP/1.1 200 OK\n\n".getBytes());
        var2.write("ping ok".getBytes());
    }

    private g e(String var1) {
        Object var2 = this.a;
        synchronized(this.a) {
            g var3 = (g)this.c.get(var1);
            if(var3 == null) {
                var3 = new g(var1, this.g);
                this.c.put(var1, var3);
            }

            return var3;
        }
    }

    private int f() {
        Object var1 = this.a;
        synchronized(this.a) {
            int var2 = 0;

            g var4;
            for(Iterator var3 = this.c.values().iterator(); var3.hasNext(); var2 += var4.b()) {
                var4 = (g)var3.next();
            }

            return var2;
        }
    }

    private void c(Socket var1) {
        this.d(var1);
        this.e(var1);
        this.f(var1);
    }

    private void d(Socket var1) {
        try {
            if(!var1.isInputShutdown()) {
                var1.shutdownInput();
            }
        } catch (SocketException var3) {
            Log.d("ProxyCache", "Releasing input stream... Socket is closed by client.");
        } catch (IOException var4) {
            this.a((Throwable)(new l("Error closing socket input stream", var4)));
        }

    }

    private void e(Socket var1) {
        try {
            if(var1.isOutputShutdown()) {
                var1.shutdownOutput();
            }
        } catch (IOException var3) {
            this.a((Throwable)(new l("Error closing socket output stream", var3)));
        }

    }

    private void f(Socket var1) {
        try {
            if(!var1.isClosed()) {
                var1.close();
            }
        } catch (IOException var3) {
            this.a((Throwable)(new l("Error closing socket", var3)));
        }

    }

    private void a(Throwable var1) {
        Log.e("ProxyCache", "HttpProxyCacheServer error", var1);
    }

    public static final class a {
        private File a;
        private com.facebook.ads.internal.ipackage.b.apackage.c b;
        private com.facebook.ads.internal.ipackage.b.apackage.a c;

        public a(Context var1) {
            this.a = o.a(var1);
            this.c = new com.facebook.ads.internal.ipackage.b.apackage.g(67108864L);
            this.b = new com.facebook.ads.internal.ipackage.b.apackage.f();
        }

        private com.facebook.ads.internal.ipackage.b.c a() {
            return new com.facebook.ads.internal.ipackage.b.c(this.a, this.b, this.c);
        }
    }

    private class c implements Callable<Boolean> {
        private final String b;

        public c(String var2) {
            this.b = var2;
        }

        public Boolean call() throws l {
            return Boolean.valueOf(f.this.c(this.b));
        }
    }

    private class b implements Callable<Boolean> {
        private b() {
        }

        public Boolean call() throws l {
            return Boolean.valueOf(f.this.c());
        }
    }

    private final class d implements Runnable {
        private final Socket b;

        public d(Socket var2) {
            this.b = var2;
        }

        public void run() {
            f.this.a(this.b);
        }
    }

    private final class e implements Runnable {
        private final CountDownLatch b;

        public e(CountDownLatch var2) {
            this.b = var2;
        }

        public void run() {
            this.b.countDown();
            f.this.e();
        }
    }
}
