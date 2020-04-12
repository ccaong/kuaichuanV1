// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.server;

import android.text.TextUtils;
import com.facebook.ads.AdSettings;

public class b
{
    public static String a() {
        final String urlPrefix = AdSettings.getUrlPrefix();
        if (TextUtils.isEmpty((CharSequence)urlPrefix)) {
            return "https://graph.facebook.com/network_ads_common";
        }
        return String.format("https://graph.%s.facebook.com/network_ads_common", urlPrefix);
    }
    
    public static String b() {
        final String urlPrefix = AdSettings.getUrlPrefix();
        if (TextUtils.isEmpty((CharSequence)urlPrefix)) {
            return "https://www.facebook.com/adnw_logging/";
        }
        return String.format("https://www.%s.facebook.com/adnw_logging/", urlPrefix);
    }
}
