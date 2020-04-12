// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.util;

import java.util.Iterator;
import org.json.JSONArray;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public class c
{
    private static final List<b> a;
    
    public static void a(final b b) {
        synchronized (c.a) {
            c.a.add(b);
        }
    }
    
    public static String a() {
        final ArrayList<b> list;
        synchronized (c.a) {
            if (c.a.isEmpty()) {
                return "";
            }
            list = (ArrayList<b>)new ArrayList<b>(c.a);
            c.a.clear();
        }
        final JSONArray jsonArray = new JSONArray();
        final Iterator<b> iterator = list.iterator();
        while (iterator.hasNext()) {
            jsonArray.put((Object)iterator.next().a());
        }
        return jsonArray.toString();
    }
    
    static {
        a = new ArrayList<b>();
    }
}
