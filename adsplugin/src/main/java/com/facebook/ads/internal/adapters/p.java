// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.adapters;

import com.facebook.ads.internal.gpackage.g;
import java.util.Map;
import android.util.Log;
import android.text.TextUtils;
import android.content.Context;
import com.facebook.ads.internal.view.b;

public class p extends a
{
    private static final String c;
    private final b d;
    private final Context e;
    private o f;
    private boolean g;
    
    public p(final Context context, final b d, final com.facebook.ads.internal.rewarded_video.a a, final com.facebook.ads.internal.adapters.b b) {
        super(context, b, a);
        this.e = context.getApplicationContext();
        this.d = d;
    }
    
    public void a(final o f) {
        this.f = f;
    }
    
    public synchronized void b() {
        if (this.g || this.f == null) {
            return;
        }
        this.g = true;
        if (this.d != null && !TextUtils.isEmpty((CharSequence)this.f.b())) {
            this.d.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    if (p.this.d.c()) {
                        Log.w(p.c, "Webview already destroyed, cannot activate");
                    }
                    else {
                        p.this.d.loadUrl("javascript:" + p.this.f.b());
                    }
                }
            });
        }
    }
    
    @Override
    protected void a(final Map<String, String> map) {
        if (this.f != null && !TextUtils.isEmpty((CharSequence)this.f.B())) {
            com.facebook.ads.internal.gpackage.g.a(this.e).a(this.f.B(), map);
        }
    }
    
    static {
        c = p.class.getSimpleName();
    }
}
