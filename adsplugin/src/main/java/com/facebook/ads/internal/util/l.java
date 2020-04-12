// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.util;

import android.text.TextUtils;
import com.facebook.ads.internal.view.apackage.d;

public class l
{
    private final d a;
    private boolean b;
    
    public l(final d a) {
        this.b = true;
        this.a = a;
    }
    
    public void a(final boolean b) {
        this.b = b;
    }
    
    public void a() {
        if (!this.b) {
            return;
        }
        if (this.a.canGoBack() || this.a.canGoForward()) {
            this.b = false;
            return;
        }
        this.a.b("void((function() {try {  if (!window.performance || !window.performance.timing || !document ||       !document.body || document.body.scrollHeight <= 0 ||       !document.body.children || document.body.children.length < 1) {    return;  }  var nvtiming__an_t = window.performance.timing;  if (nvtiming__an_t.responseEnd > 0) {    console.log('ANNavResponseEnd:'+nvtiming__an_t.responseEnd);  }  if (nvtiming__an_t.domContentLoadedEventStart > 0) {    console.log('ANNavDomContentLoaded:' + nvtiming__an_t.domContentLoadedEventStart);  }  if (nvtiming__an_t.loadEventEnd > 0) {    console.log('ANNavLoadEventEnd:' + nvtiming__an_t.loadEventEnd);  }} catch(err) {  console.log('an_navigation_timing_error:' + err.message);}})());");
    }
    
    public void a(final String s) {
        if (!this.b) {
            return;
        }
        if (s.startsWith("ANNavResponseEnd:")) {
            this.a.a(a(s, "ANNavResponseEnd:"));
        }
        else if (s.startsWith("ANNavDomContentLoaded:")) {
            this.a.b(a(s, "ANNavDomContentLoaded:"));
        }
        else if (s.startsWith("ANNavLoadEventEnd:")) {
            this.a.c(a(s, "ANNavLoadEventEnd:"));
        }
    }
    
    private static long a(final String s, final String s2) {
        final String substring = s.substring(s2.length());
        if (TextUtils.isEmpty((CharSequence)substring)) {
            return -1L;
        }
        try {
            final Long value = Long.parseLong(substring);
            return (value < 0L) ? -1L : value;
        }
        catch (NumberFormatException ex) {
            return -1L;
        }
    }
}
