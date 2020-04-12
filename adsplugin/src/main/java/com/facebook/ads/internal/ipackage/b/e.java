// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.ipackage.b;

import android.text.TextUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import com.facebook.ads.internal.ipackage.b.apackage.b;

class e extends k
{
    private final h a;
    private final b b;
    private com.facebook.ads.internal.ipackage.b.b c;
    
    public e(final h a, final b b) {
        super(a, b);
        this.b = b;
        this.a = a;
    }
    
    public void a(final com.facebook.ads.internal.ipackage.b.b c) {
        this.c = c;
    }
    
    public void a(final d d, final Socket socket) throws IOException, l {
        final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
        bufferedOutputStream.write(this.b(d).getBytes("UTF-8"));
        final long b = d.b;
        if (this.a(d)) {
            this.a(bufferedOutputStream, b);
        }
        else {
            this.b(bufferedOutputStream, b);
        }
    }
    
    private boolean a(final d d) throws l {
        final int a = this.a.a();
        final boolean b = a > 0;
        final int a2 = this.b.a();
        return !b || !d.c || d.b <= a2 + a * 0.2f;
    }
    
    private String b(final d d) throws l {
        final String c = this.a.c();
        final boolean b = !TextUtils.isEmpty((CharSequence)c);
        final int n = this.b.d() ? this.b.a() : this.a.a();
        final boolean b2 = n >= 0;
        final long n2 = d.c ? (n - d.b) : n;
        return (d.c ? "HTTP/1.1 206 PARTIAL CONTENT\n" : "HTTP/1.1 200 OK\n") + "Accept-Ranges: bytes\n" + (b2 ? String.format("Content-Length: %d\n", n2) : "") + ((b2 && d.c) ? String.format("Content-Range: bytes %d-%d/%d\n", d.b, n - 1, n) : "") + (b ? String.format("Content-Type: %s\n", c) : "") + "\n";
    }
    
    private void a(final OutputStream outputStream, long n) throws IOException, l {
        int a;
        for (byte[] array = new byte[8192]; (a = this.a(array, n, array.length)) != -1; n += a) {
            outputStream.write(array, 0, a);
        }
        outputStream.flush();
    }
    
    private void b(final OutputStream outputStream, long n) throws IOException, l {
        try {
            final h h = new h(this.a);
            h.a((int)n);
            final byte[] array = new byte[8192];
            int a;
            while ((a = h.a(array)) != -1) {
                outputStream.write(array, 0, a);
                n += a;
            }
            outputStream.flush();
        }
        finally {
            this.a.b();
        }
    }
    
    @Override
    protected void a(final int n) {
        if (this.c != null) {
            this.c.a(this.b.a, this.a.a, n);
        }
    }
}
