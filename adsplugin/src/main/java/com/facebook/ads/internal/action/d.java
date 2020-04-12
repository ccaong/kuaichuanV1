package com.facebook.ads.internal.action;

import android.util.Log;
import com.facebook.ads.internal.util.g;
import com.facebook.ads.internal.util.b;
import java.util.Map;
import android.net.Uri;
import android.content.Context;

public class d extends a
{
    private static final String a;
    private final Context b;
    private final String c;
    private final Uri d;
    private final Map<String, String> e;
    
    public d(final Context b, final String c, final Uri d, final Map<String, String> e) {
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
    }
    
    @Override
    public b.a a() {
        return com.facebook.ads.internal.util.b.a.OPEN_LINK;
    }
    
    @Override
    public void b() {
        this.a(this.b, this.c, this.e);
        try {
            g.a(this.b, Uri.parse(this.d.getQueryParameter("link")), this.c);
        }
        catch (Exception ex) {
            Log.d(com.facebook.ads.internal.action.d.a, "Failed to open link url: " + this.d.toString(), (Throwable)ex);
        }
    }
    
    static {
        a = d.class.getSimpleName();
    }
}
