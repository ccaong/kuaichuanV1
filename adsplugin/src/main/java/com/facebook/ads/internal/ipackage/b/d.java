// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.ipackage.b;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import android.text.TextUtils;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.regex.Pattern;

class d
{
    private static final Pattern d;
    private static final Pattern e;
    public final String a;
    public final long b;
    public final boolean c;
    
    public d(final String s) {
        j.a(s);
        final long a = this.a(s);
        this.b = Math.max(0L, a);
        this.c = (a >= 0L);
        this.a = this.b(s);
    }
    
    public static d a(final InputStream inputStream) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        final StringBuilder sb = new StringBuilder();
        String line;
        while (!TextUtils.isEmpty((CharSequence)(line = bufferedReader.readLine()))) {
            sb.append(line).append('\n');
        }
        return new d(sb.toString());
    }
    
    private long a(final String s) {
        final Matcher matcher = com.facebook.ads.internal.ipackage.b.d.d.matcher(s);
        if (matcher.find()) {
            return Long.parseLong(matcher.group(1));
        }
        return -1L;
    }
    
    private String b(final String s) {
        final Matcher matcher = com.facebook.ads.internal.ipackage.b.d.e.matcher(s);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new IllegalArgumentException("Invalid request `" + s + "`: url not found!");
    }
    
    @Override
    public String toString() {
        return "GetRequest{rangeOffset=" + this.b + ", partial=" + this.c + ", uri='" + this.a + '\'' + '}';
    }
    
    static {
        d = Pattern.compile("[R,r]ange:[ ]?bytes=(\\d*)-");
        e = Pattern.compile("GET /(.*) HTTP");
    }
}
