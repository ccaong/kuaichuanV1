// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.ipackage.a;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;

public interface q
{
    HttpURLConnection a(final String p0) throws IOException;
    
    void a(final HttpURLConnection p0, final j p1, final String p2) throws ProtocolException;
    
    OutputStream a(final HttpURLConnection p0) throws IOException;
    
    void a(final OutputStream p0, final byte[] p1) throws IOException;
    
    InputStream b(final HttpURLConnection p0) throws IOException;
    
    byte[] a(final InputStream p0) throws IOException;
    
    boolean a(final m p0);
}
