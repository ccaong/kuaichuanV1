package com.facebook.ads.internal.action;

import com.facebook.ads.internal.util.g;
import android.util.Log;
import com.facebook.ads.internal.util.b;
import android.net.Uri;
import android.content.Context;

public class f extends a
{
    private static final String a;
    private final Context b;
    private final String c;
    private final Uri d;
    
    public f(final Context b, final String c, final Uri d) {
        this.b = b;
        this.c = c;
        this.d = d;
    }
    
    @Override
    public b.a a() {
        return com.facebook.ads.internal.util.b.a.OPEN_LINK;
    }
    
    @Override
    public void b() {
        try {
            Log.w("REDIRECTACTION: ", this.d.toString());
            g.a(this.b, this.d, this.c);
        }
        catch (Exception ex) {
            Log.d(f.a, "Failed to open link url: " + this.d.toString(), (Throwable)ex);
        }
    }
    
    static {
        a = f.class.getSimpleName();
    }
}
