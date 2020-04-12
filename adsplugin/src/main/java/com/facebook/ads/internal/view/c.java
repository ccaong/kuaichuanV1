// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view;

import android.util.Log;
import android.webkit.JavascriptInterface;
import com.facebook.ads.internal.util.h;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import com.facebook.ads.internal.util.g;
import java.util.HashMap;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import java.util.Map;
import android.view.MotionEvent;
import android.view.View;
import android.content.Context;
import com.facebook.ads.internal.util.af;

public class c extends com.facebook.ads.internal.view.b
{
    private static final String a;
    private final b b;
    private af c;
    private com.facebook.ads.internal.rewarded_video.a d;
    
    public c(final Context context, final b b, final int n) {
        super(context);
        this.b = b;
        this.getSettings().setSupportZoom(false);
        this.addJavascriptInterface(new a(), "AdControl");
        this.c = new af();
        this.d = new com.facebook.ads.internal.rewarded_video.a((View)this, n, new com.facebook.ads.internal.rewarded_video.a.listener() {
            @Override
            public void a() {
                c.this.c.a();
                b.b();
            }
        });
    }
    
    public void a(final int n, final int n2) {
        this.d.a(n);
        this.d.b(n2);
    }
    
    protected void onWindowVisibilityChanged(final int n) {
        super.onWindowVisibilityChanged(n);
        if (this.b != null) {
            this.b.a(n);
        }
        if (this.d != null) {
            if (n == 0) {
                this.d.a();
            }
            else if (n == 8) {
                this.d.b();
            }
        }
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        this.c.a(motionEvent, (View)this, (View)this);
        return super.onTouchEvent(motionEvent);
    }
    
    public com.facebook.ads.internal.rewarded_video.a getViewabilityChecker() {
        return this.d;
    }
    
    public Map<String, String> getTouchData() {
        return this.c.e();
    }
    
    @Override
    protected WebChromeClient a() {
        return new WebChromeClient() {
            public boolean onConsoleMessage(final ConsoleMessage consoleMessage) {
                return true;
            }
        };
    }
    
    @Override
    protected WebViewClient b() {
        return new WebViewClient() {
            public boolean shouldOverrideUrlLoading(final WebView webView, final String s) {
                final HashMap<String, String> hashMap = new HashMap<String, String>();
                com.facebook.ads.internal.view.c.this.d.a(hashMap);
                hashMap.put("touch", g.a(com.facebook.ads.internal.view.c.this.getTouchData()));
                com.facebook.ads.internal.view.c.this.b.a(s, hashMap);
                return true;
            }
            
            public void onReceivedSslError(final WebView webView, final SslErrorHandler sslErrorHandler, final SslError sslError) {
                sslErrorHandler.cancel();
            }
        };
    }
    
    @Override
    public void destroy() {
        if (this.d != null) {
            this.d.b();
            this.d = null;
        }
        h.a(this);
        super.destroy();
    }
    
    static {
        a = c.class.getSimpleName();
    }
    
    public class a
    {
        private final String b;
        
        public a() {
            this.b = a.class.getSimpleName();
        }
        
        @JavascriptInterface
        public String getAnalogInfo() {
            return g.a(com.facebook.ads.internal.util.a.a());
        }
        
        @JavascriptInterface
        public void alert(final String s) {
            Log.e(this.b, s);
        }
        
        @JavascriptInterface
        public void onPageInitialized() {
            if (com.facebook.ads.internal.view.c.this.c()) {
                return;
            }
            com.facebook.ads.internal.view.c.this.b.a();
            if (com.facebook.ads.internal.view.c.this.d != null) {
                com.facebook.ads.internal.view.c.this.d.a();
            }
        }
    }
    
    public interface b
    {
        void a(final String p0, final Map<String, String> p1);
        
        void a();
        
        void b();
        
        void a(final int p0);
    }
}
