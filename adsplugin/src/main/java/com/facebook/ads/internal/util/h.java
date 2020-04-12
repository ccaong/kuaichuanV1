// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.util;

import android.annotation.TargetApi;
import android.webkit.WebSettings;
import android.os.Build;
import android.text.TextUtils;
import com.facebook.ads.AdSettings;
import android.webkit.WebView;

public class h
{
    public static void a(final WebView webView) {
        webView.loadUrl("about:blank");
        webView.clearCache(true);
    }
    
    public static String a() {
        final String urlPrefix = AdSettings.getUrlPrefix();
        if (TextUtils.isEmpty((CharSequence)urlPrefix)) {
            return "https://www.facebook.com/";
        }
        return String.format("https://www.%s.facebook.com", urlPrefix);
    }
    
    @TargetApi(21)
    public static void b(final WebView webView) {
        final WebSettings settings = webView.getSettings();
        if (Build.VERSION.SDK_INT >= 21) {
            settings.setMixedContentMode(0);
        }
        else {
            try {
                WebSettings.class.getMethod("setMixedContentMode", (Class<?>[])new Class[0]).invoke(settings, 0);
            }
            catch (Exception ex) {}
        }
    }
}
