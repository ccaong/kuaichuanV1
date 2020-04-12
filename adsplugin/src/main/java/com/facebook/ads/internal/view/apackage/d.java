// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view.apackage;

import java.util.HashSet;
import android.graphics.Bitmap;
import android.content.ActivityNotFoundException;
import android.util.Log;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebViewClient;
import android.text.TextUtils;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import com.facebook.ads.internal.util.h;
import android.graphics.Canvas;
import android.webkit.WebBackForwardList;
import android.webkit.ValueCallback;
import android.content.Context;
import com.facebook.ads.internal.util.l;
import java.util.Set;
import android.annotation.TargetApi;
import com.facebook.ads.internal.view.b;

@TargetApi(19)
public class d extends b
{
    private static final String a;
    private static final Set<String> b;
    private a c;
    private l dd;
    private long e;
    private long f;
    private long g;
    private long h;
    
    public d(final Context context) {
        super(context);
        this.e = -1L;
        this.f = -1L;
        this.g = -1L;
        this.h = -1L;
        this.f();
    }
    
    private void f() {
        this.getSettings().setSupportZoom(true);
        this.getSettings().setBuiltInZoomControls(true);
        this.getSettings().setDisplayZoomControls(false);
        this.getSettings().setLoadWithOverviewMode(true);
        this.getSettings().setUseWideViewPort(true);
        this.dd = new l(this);
    }
    
    public static boolean a(final String s) {
        return d.b.contains(s);
    }
    
    public void setListener(final a c) {
        this.c = c;
    }
    
    public void b(final String s) {
        try {
            this.evaluateJavascript(s, (ValueCallback)null);
        }
        catch (IllegalStateException ex) {
            this.loadUrl("javascript:" + s);
        }
    }
    
    public String getFirstUrl() {
        final WebBackForwardList copyBackForwardList = this.copyBackForwardList();
        if (copyBackForwardList.getSize() > 0) {
            return copyBackForwardList.getItemAtIndex(0).getUrl();
        }
        return this.getUrl();
    }
    
    public void a(final long e) {
        if (this.e < 0L) {
            this.e = e;
        }
    }
    
    public void b(final long f) {
        if (this.f < 0L) {
            this.f = f;
        }
        this.g();
    }
    
    public void c(final long h) {
        if (this.h < 0L) {
            this.h = h;
        }
        this.g();
    }
    
    public long getResponseEndMs() {
        return this.e;
    }
    
    public long getDomContentLoadedMs() {
        return this.f;
    }
    
    public long getScrollReadyMs() {
        return this.g;
    }
    
    public long getLoadFinishMs() {
        return this.h;
    }
    
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (this.g < 0L && this.computeVerticalScrollRange() > this.getHeight()) {
            this.g = System.currentTimeMillis();
            this.g();
        }
    }
    
    private void g() {
        if (this.f > -1L && this.g > -1L && this.h > -1L) {
            this.dd.a(false);
        }
    }
    
    @Override
    public void destroy() {
        com.facebook.ads.internal.util.h.a(this);
        super.destroy();
    }
    
    @Override
    protected WebChromeClient a() {
        return new WebChromeClient() {
            public void onReceivedTitle(final WebView webView, final String s) {
                super.onReceivedTitle(webView, s);
                if (com.facebook.ads.internal.view.apackage.d.this.c != null) {
                    com.facebook.ads.internal.view.apackage.d.this.c.b(s);
                }
            }
            
            public void onProgressChanged(final WebView webView, final int n) {
                super.onProgressChanged(webView, n);
                com.facebook.ads.internal.view.apackage.d.this.dd.a();
                if (com.facebook.ads.internal.view.apackage.d.this.c != null) {
                    com.facebook.ads.internal.view.apackage.d.this.c.a(n);
                }
            }
            
            public boolean onConsoleMessage(final ConsoleMessage consoleMessage) {
                final String message = consoleMessage.message();
                if (!TextUtils.isEmpty((CharSequence)message) && consoleMessage.messageLevel() == ConsoleMessage.MessageLevel.LOG) {
                    com.facebook.ads.internal.view.apackage.d.this.dd.a(message);
                }
                return true;
            }
        };
    }
    
    @Override
    protected WebViewClient b() {
        return new WebViewClient() {
            public boolean shouldOverrideUrlLoading(final WebView webView, final String s) {
                final Uri parse = Uri.parse(s);
                if (!com.facebook.ads.internal.view.apackage.d.b.contains(parse.getScheme())) {
                    try {
                        com.facebook.ads.internal.view.apackage.d.this.getContext().startActivity(new Intent("android.intent.action.VIEW", parse));
                        return true;
                    }
                    catch (ActivityNotFoundException ex) {
                        Log.w(com.facebook.ads.internal.view.apackage.d.a, "Activity not found to handle URI.", (Throwable)ex);
                    }
                    catch (Exception ex2) {
                        Log.e(com.facebook.ads.internal.view.apackage.d.a, "Unknown exception occurred when trying to handle URI.", (Throwable)ex2);
                    }
                }
                return false;
            }
            
            public void onPageStarted(final WebView webView, final String s, final Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
                if (com.facebook.ads.internal.view.apackage.d.this.c != null) {
                    com.facebook.ads.internal.view.apackage.d.this.c.a(s);
                }
            }
            
            public void onPageFinished(final WebView webView, final String s) {
                super.onPageFinished(webView, s);
                if (com.facebook.ads.internal.view.apackage.d.this.c != null) {
                    com.facebook.ads.internal.view.apackage.d.this.c.c(s);
                }
            }
        };
    }
    
    static {
        a = d.class.getSimpleName();
        (b = new HashSet<String>(2)).add("http");
        d.b.add("https");
    }
    
    public interface a
    {
        void a(final String p0);
        
        void b(final String p0);
        
        void a(final int p0);
        
        void c(final String p0);
    }
}
