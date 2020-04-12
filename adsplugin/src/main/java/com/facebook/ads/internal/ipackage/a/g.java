// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.ipackage.a;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.net.HttpURLConnection;

public class g implements r
{
    @Override
    public boolean a() {
        return false;
    }
    
    @Override
    public void a(final String s) {
        System.out.println(s);
    }
    
    @Override
    public void a(final HttpURLConnection httpURLConnection, final Object o) {
        this.a("=== HTTP Request ===");
        this.a(httpURLConnection.getRequestMethod() + " " + httpURLConnection.getURL().toString());
        if (o instanceof String) {
            this.a("Content: " + (String)o);
        }
        this.a(httpURLConnection.getRequestProperties());
    }
    
    @Override
    public void a(final n n) {
        if (n != null) {
            this.a("=== HTTP Response ===");
            this.a("Receive url: " + n.b());
            this.a("Status: " + n.a());
            this.a(n.c());
            this.a("Content:\n" + n.e());
        }
    }
    
    private void a(final Map<String, List<String>> map) {
        if (map != null) {
            for (final String s : map.keySet()) {
                final Iterator<String> iterator2 = map.get(s).iterator();
                while (iterator2.hasNext()) {
                    this.a(s + ":" + iterator2.next());
                }
            }
        }
    }
}
