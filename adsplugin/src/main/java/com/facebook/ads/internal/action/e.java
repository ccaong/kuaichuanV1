package com.facebook.ads.internal.action;

import android.text.TextUtils;
import com.facebook.ads.internal.gpackage.h;
import com.facebook.ads.internal.gpackage.g;
import com.facebook.ads.internal.util.b;
import java.util.Map;
import android.net.Uri;
import android.content.Context;

public class e extends a
{
    private static final String a;
    private final Context b;
    private final String c;
    private final Uri d;
    private final Map<String, String> e;
    
    public e(final Context b, final String c, final Uri d, final Map<String, String> e) {
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
    }
    
    @Override
    public b.a a() {
        return null;
    }
    
    @Override
    public void b() {
        final g a = g.a(this.b);
        h a2 = h.IMMEDIATE;
        final String queryParameter = this.d.getQueryParameter("priority");
        if (!TextUtils.isEmpty((CharSequence)queryParameter)) {
            try {
                a2 = h.values()[Integer.valueOf(queryParameter)];
            }
            catch (Exception ex) {}
        }
        a.a(this.c, this.e, this.d.getQueryParameter("type"), a2);
    }
    
    static {
        a = e.class.getSimpleName();
    }
}
