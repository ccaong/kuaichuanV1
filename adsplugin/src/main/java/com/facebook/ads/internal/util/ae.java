// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.util;

import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ae
{
    public static final String a(final Throwable t) {
        if (t == null) {
            return null;
        }
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        t.printStackTrace(printWriter);
        printWriter.close();
        return stringWriter.toString();
    }
}
