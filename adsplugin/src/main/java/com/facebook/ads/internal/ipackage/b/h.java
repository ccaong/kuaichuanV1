// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.ipackage.b;

import android.text.TextUtils;
import java.net.URL;
import java.io.Closeable;
import android.util.Log;
import java.io.InterruptedIOException;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class h implements n
{
    public final String a;
    private HttpURLConnection b;
    private InputStream c;
    private volatile int d;
    private volatile String e;
    
    public h(final String s) {
        this(s, m.a(s));
    }
    
    public h(final String s, final String e) {
        this.d = Integer.MIN_VALUE;
        this.a = j.a(s);
        this.e = e;
    }
    
    public h(final h h) {
        this.d = Integer.MIN_VALUE;
        this.a = h.a;
        this.e = h.e;
        this.d = h.d;
    }
    
    @Override
    public synchronized int a() throws l {
        if (this.d == Integer.MIN_VALUE) {
            this.d();
        }
        return this.d;
    }
    
    @Override
    public void a(final int n) throws l {
        try {
            this.b = this.a(n, -1);
            this.e = this.b.getContentType();
            this.c = new BufferedInputStream(this.b.getInputStream(), 8192);
            this.d = this.a(this.b, n, this.b.getResponseCode());
        }
        catch (IOException ex) {
            throw new l("Error opening connection for " + this.a + " with offset " + n, ex);
        }
    }
    
    private int a(final HttpURLConnection httpURLConnection, final int n, final int n2) {
        final int contentLength = httpURLConnection.getContentLength();
        return (n2 == 200) ? contentLength : ((n2 == 206) ? (contentLength + n) : this.d);
    }
    
    @Override
    public void b() throws l {
        if (this.b != null) {
            try {
                this.b.disconnect();
            }
            catch (NullPointerException ex) {
                throw new l("Error disconnecting HttpUrlConnection", ex);
            }
        }
    }
    
    @Override
    public int a(final byte[] array) throws l {
        if (this.c == null) {
            throw new l("Error reading data from " + this.a + ": connection is absent!");
        }
        try {
            return this.c.read(array, 0, array.length);
        }
        catch (InterruptedIOException ex) {
            throw new i("Reading source " + this.a + " is interrupted", ex);
        }
        catch (IOException ex2) {
            throw new l("Error reading data from " + this.a, ex2);
        }
    }
    
    private void d() throws l {
        Log.d("ProxyCache", "Read content info from " + this.a);
        HttpURLConnection a = null;
        Closeable inputStream = null;
        try {
            a = this.a(0, 10000);
            this.d = a.getContentLength();
            this.e = a.getContentType();
            inputStream = a.getInputStream();
            Log.i("ProxyCache", "Content info for `" + this.a + "`: mime: " + this.e + ", content-length: " + this.d);
        }
        catch (IOException ex) {
            Log.e("ProxyCache", "Error fetching info from " + this.a, (Throwable)ex);
        }
        finally {
            m.a(inputStream);
            if (a != null) {
                a.disconnect();
            }
        }
    }
    
    private HttpURLConnection a(final int n, final int n2) throws IOException, l {
        int n3 = 0;
        String s = this.a;
        boolean b;
        HttpURLConnection httpURLConnection;
        do {
            Log.d("ProxyCache", "Open connection " + ((n > 0) ? (" with offset " + n) : "") + " to " + s);
            httpURLConnection = (HttpURLConnection)new URL(s).openConnection();
            if (n > 0) {
                httpURLConnection.setRequestProperty("Range", "bytes=" + n + "-");
            }
            if (n2 > 0) {
                httpURLConnection.setConnectTimeout(n2);
                httpURLConnection.setReadTimeout(n2);
            }
            final int responseCode = httpURLConnection.getResponseCode();
            b = (responseCode == 301 || responseCode == 302 || responseCode == 303);
            if (b) {
                s = httpURLConnection.getHeaderField("Location");
                ++n3;
                httpURLConnection.disconnect();
            }
            if (n3 > 5) {
                throw new l("Too many redirects: " + n3);
            }
        } while (b);
        return httpURLConnection;
    }
    
    public synchronized String c() throws l {
        if (TextUtils.isEmpty((CharSequence)this.e)) {
            this.d();
        }
        return this.e;
    }
    
    @Override
    public String toString() {
        return "HttpUrlSource{url='" + this.a + "}";
    }
}
