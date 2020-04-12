// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.ipackage.a;

import android.util.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import android.os.Build;
import java.util.Set;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

public class o
{
    public static void a(final HttpsURLConnection httpsURLConnection, final Set<String> set, final Set<String> set2) throws CertificateException, SSLPeerUnverifiedException, NoSuchAlgorithmException {
        if (Build.VERSION.SDK_INT == 15 && "4.0.3".equals(Build.VERSION.RELEASE)) {
            return;
        }
        try {
            final Certificate[] serverCertificates = httpsURLConnection.getServerCertificates();
            for (int length = serverCertificates.length, i = 0; i < length; ++i) {
                final X509Certificate x509Certificate = (X509Certificate)serverCertificates[i];
                final String a = a(x509Certificate.getEncoded(), "SHA-1");
                if (set != null && set.contains(a)) {
                    return;
                }
                final String a2 = a(x509Certificate.getPublicKey().getEncoded(), "SHA-1");
                if (set2 != null && set2.contains(a2)) {
                    return;
                }
            }
            throw new CertificateException("Unable to find valid certificate or public key.");
        }
        catch (Exception ex) {
            throw ex;
        }
    }
    
    private static String a(final byte[] array, final String s) throws NoSuchAlgorithmException {
        final MessageDigest instance = MessageDigest.getInstance(s);
        instance.reset();
        return Base64.encodeToString(instance.digest(array), 0);
    }
}
