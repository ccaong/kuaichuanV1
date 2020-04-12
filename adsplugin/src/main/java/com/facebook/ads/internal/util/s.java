// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.util;

import android.content.Context;
import java.io.FileInputStream;
import java.io.File;
import android.support.annotation.Nullable;
import java.security.MessageDigest;

public class s
{
    @Nullable
    public static String a(final String s) {
        try {
            return a(MessageDigest.getInstance("MD5").digest(s.getBytes("utf-8")));
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    @Nullable
    public static final String b(final String s) {
        return a(new File(s));
    }
    
    @Nullable
    public static final String a(final File file) {
        try {
            final MessageDigest instance = MessageDigest.getInstance("MD5");
            final byte[] array = new byte[1024];
            final FileInputStream fileInputStream = new FileInputStream(file);
            int i;
            do {
                i = fileInputStream.read(array);
                if (i > 0) {
                    instance.update(array, 0, i);
                }
            } while (i != -1);
            fileInputStream.close();
            return a(instance.digest());
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    @Nullable
    public static final String a(final Context context, final String s) {
        try {
            return b(context.getPackageManager().getApplicationInfo(s, 0).sourceDir);
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    private static final String a(final byte[] array) {
        final StringBuilder sb = new StringBuilder();
        for (int length = array.length, i = 0; i < length; ++i) {
            sb.append(Integer.toString((array[i] & 0xFF) + 256, 16).substring(1));
        }
        return sb.toString();
    }
}
