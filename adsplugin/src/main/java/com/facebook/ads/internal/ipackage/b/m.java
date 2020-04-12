// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.ipackage.b;

import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.io.IOException;
import android.util.Log;
import java.io.Closeable;
import java.net.URLDecoder;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

public class m
{
    static String a(final String s) {
        final MimeTypeMap singleton = MimeTypeMap.getSingleton();
        final String fileExtensionFromUrl = MimeTypeMap.getFileExtensionFromUrl(s);
        return TextUtils.isEmpty((CharSequence)fileExtensionFromUrl) ? null : singleton.getMimeTypeFromExtension(fileExtensionFromUrl);
    }
    
    static void a(final byte[] array, final long n, final int n2) {
        j.a(array, "Buffer must be not null!");
        j.a(n >= 0L, "Data offset must be positive!");
        j.a(n2 >= 0 && n2 <= array.length, "Length must be in range [0..buffer.length]");
    }
    
    static String b(final String s) {
        try {
            return URLEncoder.encode(s, "utf-8");
        }
        catch (UnsupportedEncodingException ex) {
            throw new RuntimeException("Error encoding url", ex);
        }
    }
    
    static String c(final String s) {
        try {
            return URLDecoder.decode(s, "utf-8");
        }
        catch (UnsupportedEncodingException ex) {
            throw new RuntimeException("Error decoding url", ex);
        }
    }
    
    static void a(final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            }
            catch (IOException ex) {
                Log.e("ProxyCache", "Error closing resource", (Throwable)ex);
            }
        }
    }
    
    public static String d(final String s) {
        try {
            return a(MessageDigest.getInstance("MD5").digest(s.getBytes()));
        }
        catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException(ex);
        }
    }
    
    private static String a(final byte[] array) {
        final StringBuffer sb = new StringBuffer();
        for (int length = array.length, i = 0; i < length; ++i) {
            sb.append(String.format("%02x", array[i]));
        }
        return sb.toString();
    }
}
