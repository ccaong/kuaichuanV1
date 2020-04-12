// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view;

import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.util.Log;
import android.webkit.CookieManager;
import android.os.Build;
import com.facebook.ads.internal.util.h;
import android.content.Context;
import android.webkit.WebView;

public abstract class b extends WebView
{
    private static final String a;
    private boolean b;
    
    public b(final Context context) {
        super(context);
        this.d();
    }
    
    private void d() {
        this.setWebChromeClient(this.a());
        this.setWebViewClient(this.b());
        h.b(this);
        this.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= 17) {
            this.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }
        this.setHorizontalScrollBarEnabled(false);
        this.setHorizontalScrollbarOverlay(false);
        this.setVerticalScrollBarEnabled(false);
        this.setVerticalScrollbarOverlay(false);
        if (Build.VERSION.SDK_INT >= 21) {
            try {
                CookieManager.getInstance().setAcceptThirdPartyCookies((WebView)this, true);
            }
            catch (Exception ex) {
                Log.w(com.facebook.ads.internal.view.b.a, "Failed to initialize CookieManager.");
            }
        }
    }
    
    protected WebChromeClient a() {
        return new WebChromeClient();
    }
    
    protected WebViewClient b() {
        return new WebViewClient();
    }
    
    public void destroy() {
        this.b = true;
        super.destroy();
    }
    
    public boolean c() {
        return this.b;
    }
    
    static {
        a = b.class.getSimpleName();
    }
}
