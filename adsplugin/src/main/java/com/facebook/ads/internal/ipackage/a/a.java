// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.ipackage.a;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ProtocolException;
import java.util.Iterator;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;
import android.util.Log;
import javax.net.ssl.HttpsURLConnection;
import java.net.CookieManager;
import java.net.CookieHandler;
import android.os.Build;
import java.util.TreeMap;
import java.util.Set;
import java.util.Map;

public class a
{
    private static int[] f;
    private static final String g;
    protected final q aa;
    protected final d b;
    protected r c;
    protected int d;
    protected int e;
    private int h;
    private Map<String, String> i;
    private boolean j;
    private Set<String> k;
    private Set<String> l;
    
    public a() {
        this.b = new e();
        this.c = new g();
        this.d = 2000;
        this.e = 8000;
        this.h = 3;
        this.i = new TreeMap<String, String>();
        this.aa = new f() {};
    }
    
    private static void c() {
        if (Build.VERSION.SDK_INT < 8) {
            System.setProperty("http.keepAlive", "false");
        }
    }
    
    public static void a() {
        if (CookieHandler.getDefault() == null) {
            CookieHandler.setDefault(new CookieManager());
        }
    }
    
    public void a(final String s, final p p3, final b b) {
        this.a(new k(s, p3), b);
    }
    
    protected void a(final l l, final b b) {
        this.b.a(this, b).a(l);
    }
    
    public n a(final l l) throws m {
        int i = 0;
        long n = System.currentTimeMillis();
        while (i < this.h) {
            Label_0196: {
                try {
                    this.c(this.a(i));
                    if (this.c.a()) {
                        this.c.a(i + 1 + "of" + this.h + ", trying " + l.a());
                    }
                    n = System.currentTimeMillis();
                    final n a = this.a(l.a(), l.b(), l.c(), l.d());
                    if (a != null) {
                        return a;
                    }
                }
                catch (m m) {
                    if (!this.a(m, n) || i >= this.h - 1) {
                        if (this.aa.a(m) && i < this.h - 1) {
                            try {
                                Thread.sleep(this.d);
                                break Label_0196;
                            }
                            catch (InterruptedException ex) {
                                throw m;
                            }
                        }
                        throw m;
                    }
                }
            }
            ++i;
        }
        return null;
    }
    
    protected int a(final int n) {
        return 1000 * com.facebook.ads.internal.ipackage.a.a.f[n + 2];
    }
    
    public void b(final int h) {
        if (h < 1 || h > 18) {
            throw new IllegalArgumentException("Maximum retries must be between 1 and 18");
        }
        this.h = h;
    }
    
    public void a(final Set<String> l) {
        this.l = l;
    }
    
    public void b(final Set<String> k) {
        this.k = k;
    }
    
    public n a(final String s, final p p2) {
        return this.b(new i(s, p2));
    }
    
    public n b(final String s, final p p2) {
        return this.b(new k(s, p2));
    }
    
    public n b(final l l) {
        n a = null;
        try {
            a = this.a(l.a(), l.b(), l.c(), l.d());
        }
        catch (m m) {
            this.aa.a(m);
        }
        catch (Exception ex) {
            this.aa.a(new m(ex, a));
        }
        return a;
    }
    
    protected n a(final String s, final j j, final String s2, final byte[] array) throws m {
        HttpURLConnection a = null;
        n n = null;
        try {
            this.j = false;
            a = this.a(s);
            this.a(a, j, s2);
            this.c(a);
            if (this.c.a()) {
                this.c.a(a, array);
            }
            a.connect();
            this.j = true;
            final boolean b = this.l != null && !this.l.isEmpty();
            final boolean b2 = this.k != null && !this.k.isEmpty();
            Label_0169: {
                if (a instanceof HttpsURLConnection) {
                    if (!b) {
                        if (!b2) {
                            break Label_0169;
                        }
                    }
                    try {
                        o.a((HttpsURLConnection)a, this.l, this.k);
                    }
                    catch (Exception ex) {
                        Log.e(com.facebook.ads.internal.ipackage.a.a.g, "Unable to validate SSL certificates.", (Throwable)ex);
                    }
                }
            }
            if (a.getDoOutput() && array != null) {
                this.a(a, array);
            }
            if (a.getDoInput()) {
                n = this.a(a);
            }
            else {
                n = new n(a, null);
            }
        }
        catch (Exception ex2) {
            try {
                n = this.b(a);
                return n;
            }
            catch (Exception ex3) {
                ex2.printStackTrace();
            }
            finally {
                if (n != null && n.a() > 0) {
                    final n n2 = n;
                    if (this.c.a()) {
                        this.c.a(n);
                    }
                    if (a != null) {
                        a.disconnect();
                    }
                    return n2;
                }
                throw new m(ex2, n);
            }
        }
        finally {
            if (this.c.a()) {
                this.c.a(n);
            }
            if (a != null) {
                a.disconnect();
            }
        }
        return n;
    }
    
    protected HttpURLConnection a(final String s) throws IOException {
        try {
            new URL(s);
        }
        catch (MalformedURLException ex) {
            throw new IllegalArgumentException(s + " is not a valid URL", ex);
        }
        return this.aa.a(s);
    }
    
    protected void a(final HttpURLConnection httpURLConnection, final j j, final String s) throws ProtocolException {
        httpURLConnection.setConnectTimeout(this.d);
        httpURLConnection.setReadTimeout(this.e);
        this.aa.a(httpURLConnection, j, s);
    }
    
    private void c(final HttpURLConnection httpURLConnection) {
        for (final String s : this.i.keySet()) {
            httpURLConnection.setRequestProperty(s, this.i.get(s));
        }
    }
    
    protected int a(final HttpURLConnection httpURLConnection, final byte[] array) throws IOException {
        OutputStream a = null;
        try {
            a = this.aa.a(httpURLConnection);
            if (a != null) {
                this.aa.a(a, array);
            }
            return httpURLConnection.getResponseCode();
        }
        finally {
            if (a != null) {
                try {
                    a.close();
                }
                catch (Exception ex) {}
            }
        }
    }
    
    protected n a(final HttpURLConnection httpURLConnection) throws IOException {
        InputStream b = null;
        byte[] a = null;
        try {
            b = this.aa.b(httpURLConnection);
            if (b != null) {
                a = this.aa.a(b);
            }
            return new n(httpURLConnection, a);
        }
        finally {
            if (b != null) {
                try {
                    b.close();
                }
                catch (Exception ex) {}
            }
        }
    }
    
    protected n b(final HttpURLConnection httpURLConnection) throws IOException {
        InputStream errorStream = null;
        byte[] a = null;
        try {
            errorStream = httpURLConnection.getErrorStream();
            if (errorStream != null) {
                a = this.aa.a(errorStream);
            }
            return new n(httpURLConnection, a);
        }
        finally {
            if (errorStream != null) {
                try {
                    errorStream.close();
                }
                catch (Exception ex) {}
            }
        }
    }
    
    public p b() {
        return new p();
    }
    
    public a a(final String s, final String s2) {
        this.i.put(s, s2);
        return this;
    }
    
    protected boolean a(final Throwable t, final long n) {
        final long n2 = System.currentTimeMillis() - n + 10L;
        if (this.c.a()) {
            this.c.a("ELAPSED TIME = " + n2 + ", CT = " + this.d + ", RT = " + this.e);
        }
        if (this.j) {
            return n2 >= this.e;
        }
        return n2 >= this.d;
    }
    
    public void c(final int d) {
        this.d = d;
    }
    
    static {
        a.f = new int[20];
        g = a.class.getSimpleName();
        c();
        if (Build.VERSION.SDK_INT > 8) {
            a();
        }
    }
}
