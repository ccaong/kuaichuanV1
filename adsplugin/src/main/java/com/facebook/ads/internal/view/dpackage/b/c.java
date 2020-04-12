// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view.dpackage.b;

import java.util.concurrent.TimeUnit;
import android.view.View;
import com.facebook.ads.internal.gpackage.q;
import android.content.Context;
import com.facebook.ads.internal.gpackage.s;
import android.widget.TextView;

public class c extends n
{
    private final TextView b;
    private final String c;
    private final s<com.facebook.ads.internal.view.dpackage.a.n> d;
    
    public c(final Context context, final String c) {
        super(context);
        this.d = new s<com.facebook.ads.internal.view.dpackage.a.n>() {
            @Override
            public void a(final com.facebook.ads.internal.view.dpackage.a.n n) {
                com.facebook.ads.internal.view.dpackage.b.c.this.b.setText((CharSequence)com.facebook.ads.internal.view.dpackage.b.c.this.a(com.facebook.ads.internal.view.dpackage.b.c.this.getVideoView().getDuration() - com.facebook.ads.internal.view.dpackage.b.c.this.getVideoView().getCurrentPosition()));
            }
            
            @Override
            public Class<com.facebook.ads.internal.view.dpackage.a.n> a() {
                return com.facebook.ads.internal.view.dpackage.a.n.class;
            }
        };
        this.b = new TextView(context);
        this.c = c;
        this.addView((View)this.b);
    }
    
    @Override
    protected void a_(final com.facebook.ads.internal.view.n n) {
        n.getEventBus().a(this.d);
        super.a_(n);
    }
    
    private String a(final long n) {
        if (n <= 0L) {
            return "00:00";
        }
        final long minutes = TimeUnit.MILLISECONDS.toMinutes(n);
        final long seconds = TimeUnit.MILLISECONDS.toSeconds(n % 60000L);
        if (this.c.isEmpty()) {
            return String.format("%02d:%02d", minutes, seconds);
        }
        return this.c.replace("{{REMAINING_TIME}}", String.format("%02d:%02d", minutes, seconds));
    }
    
    public void setCountdownTextColor(final int textColor) {
        this.b.setTextColor(textColor);
    }
}
