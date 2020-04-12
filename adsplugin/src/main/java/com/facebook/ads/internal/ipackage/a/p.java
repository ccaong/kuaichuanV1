//
// Decompiled by Procyon v0.5.30
//

package com.facebook.ads.internal.ipackage.a;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

public class p implements Map<String, String>
{
    private Map<String, String> a;

    public p() {
        this.a = new HashMap<String, String>();
    }

    @Override
    public void clear() {
        this.a.clear();
    }

    @Override
    public boolean containsKey(final Object o) {
        return this.a.containsKey(o);
    }

    @Override
    public boolean containsValue(final Object o) {
        return this.a.containsValue(o);
    }

    @Override
    public Set<Entry<String, String>> entrySet() {
        return this.a.entrySet();
    }

    public String get(final Object o) {
        return this.a.get(o);
    }

    public String a(String var1, String var2) {
        return (String)this.a.put(var1, var2);
    }
    
    @Override
    public boolean isEmpty() {
        return this.a.isEmpty();
    }

    @Override
    public Set<String> keySet() {
        return this.a.keySet();
    }

    public String put(final String s, final String s2) {
        return this.a.put(s, s2);
    }

    @Override
    public void putAll(final Map<? extends String, ? extends String> map) {
        this.a.putAll(map);
    }

    public String remove(final Object o) {
        return this.a.remove(o);
    }

    @Override
    public int size() {
        return this.a.size();
    }

    @Override
    public Collection<String> values() {
        return this.a.values();
    }

    public p a(final Map<? extends String, ? extends String> map) {
        this.putAll(map);
        return this;
    }

    public String a() {
        final StringBuilder sb = new StringBuilder();
        for (final String s : this.a.keySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(s);
            final String s2 = this.a.get(s);
            if (s2 != null) {
                sb.append("=");
                try {
                    sb.append(URLEncoder.encode(s2, "UTF-8"));
                }
                catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    public byte[] b() {
        byte[] bytes = null;
        try {
            bytes = this.a().getBytes("UTF-8");
        }
        catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }
}
