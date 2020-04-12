// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.ipackage.a;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

public class n
{
    private int a;
    private String b;
    private Map<String, List<String>> c;
    private byte[] d;
    
    public n(final HttpURLConnection httpURLConnection, final byte[] d) {
        try {
            this.a = httpURLConnection.getResponseCode();
            this.b = httpURLConnection.getURL().toString();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        this.c = httpURLConnection.getHeaderFields();
        this.d = d;
    }
    
    public int a() {
        return this.a;
    }
    
    public String b() {
        return this.b;
    }
    
    public Map<String, List<String>> c() {
        return this.c;
    }
    
    public byte[] d() {
        return this.d;
    }
    
    public String e() {
        if (this.d != null) {
            return new String(this.d);
        }
        return null;
    }
}
