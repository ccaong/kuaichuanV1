// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.util;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

public class ag
{
    public static String a(final Set<String> set, final String s) {
        final StringBuilder sb = new StringBuilder();
        final Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next());
            sb.append(s);
        }
        return (sb.length() > 0) ? sb.substring(0, sb.length() - 1) : "";
    }
}
