// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.util;

import java.util.HashMap;
import java.util.Map;

public class m
{
    private final String a;
    private final String b;
    private final String c;
    
    public m(final String s, final String s2) {
        this(s, s2, false);
    }
    
    public m(final String a, final String b, final boolean b2) {
        this.a = a;
        this.b = b;
        this.c = (b2 ? "1" : "0");
    }
    
    public Map<String, String> a() {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("stacktrace", this.a);
        hashMap.put("app_crashed_version", this.b);
        hashMap.put("caught_exception", this.c);
        return hashMap;
    }
}
