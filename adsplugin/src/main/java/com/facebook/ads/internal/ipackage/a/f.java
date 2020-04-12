// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.ipackage.a;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.HttpURLConnection;

public abstract class f implements q
{
    private final r a;
    
    public f() {
        this(new g());
    }
    
    public f(final r a) {
        this.a = a;
    }
    
    @Override
    public HttpURLConnection a(final String s) throws IOException {
        return (HttpURLConnection)new URL(s).openConnection();
    }
    
    @Override
    public void a(final HttpURLConnection httpURLConnection, final j j, final String s) throws ProtocolException {
        httpURLConnection.setRequestMethod(j.c());
        httpURLConnection.setDoOutput(j.b());
        httpURLConnection.setDoInput(j.a());
        if (s != null) {
            httpURLConnection.setRequestProperty("Content-Type", s);
        }
        httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
    }
    
    @Override
    public OutputStream a(final HttpURLConnection httpURLConnection) throws IOException {
        return httpURLConnection.getOutputStream();
    }
    
    @Override
    public void a(final OutputStream outputStream, final byte[] array) throws IOException {
        outputStream.write(array);
    }
    
    @Override
    public InputStream b(final HttpURLConnection httpURLConnection) throws IOException {
        return httpURLConnection.getInputStream();
    }
    
    @Override
    public byte[] a(final InputStream inputStream) throws IOException {
        final byte[] array = new byte[16384];
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int read;
        while ((read = inputStream.read(array)) != -1) {
            byteArrayOutputStream.write(array, 0, read);
        }
        byteArrayOutputStream.flush();
        return byteArrayOutputStream.toByteArray();
    }
    
    @Override
    public boolean a(final m m) {
        final n a = m.a();
        if (this.a.a()) {
            this.a.a("BasicRequestHandler.onError got");
            m.printStackTrace();
        }
        return a != null && a.a() > 0;
    }
}
