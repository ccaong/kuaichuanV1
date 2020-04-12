// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.util;

import java.util.HashSet;
import java.net.URLEncoder;
import com.facebook.ads.internal.ipackage.a.a;
import android.util.Log;
import com.facebook.ads.internal.ipackage.a.p;
import java.util.Iterator;
import android.text.TextUtils;
import java.util.HashMap;
import com.facebook.ads.internal.ipackage.a.n;
import java.util.Map;
import java.util.Set;
import android.os.AsyncTask;

public class x extends AsyncTask<String, Void, y>
{
    private static final String a;
    private static final Set<String> b;
    private Map<String, String> c;
    private Map<String, String> d;
    private n e;
    private a f;
    
    public x() {
        this(null, null);
    }
    
    public x(final Map<String, String> map) {
        this(map, null);
    }
    
    public x(final Map<String, String> map, final Map<String, String> map2) {
        this.c = ((map != null) ? new HashMap<String, String>(map) : null);
        this.d = ((map2 != null) ? new HashMap<String, String>(map2) : null);
    }
    
    public void a(final a f) {
        this.f = f;
    }
    
    protected y doInBackground(final String... array) {
        final String s = array[0];
        if (TextUtils.isEmpty((CharSequence)s) || x.b.contains(s)) {
            return null;
        }
        String s2 = this.b(s);
        if (this.c != null && !this.c.isEmpty()) {
            for (final Map.Entry<String, String> entry : this.c.entrySet()) {
                s2 = this.a(s2, entry.getKey(), entry.getValue());
            }
        }
        int n = 1;
        while (n++ <= 2) {
            if (this.a(s2)) {
                return new y(this.e);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(final y y) {
        if (this.f != null) {
            this.f.a(y);
        }
    }
    
    protected void onCancelled() {
        if (this.f != null) {
            this.f.a();
        }
    }
    
    private boolean a(final String s) {
        final com.facebook.ads.internal.ipackage.a.a b = w.b();
        try {
            if (this.d == null || this.d.size() == 0) {
                this.e = b.a(s, (p)null);
            }
            else {
                final p p = new p();
                p.a(this.d);
                this.e = b.b(s, p);
            }
            return this.e != null && this.e.a() == 200;
        }
        catch (Exception ex) {
            Log.e(x.a, "Error opening url: " + s, (Throwable)ex);
            return false;
        }
    }
    
    private String b(final String s) {
        try {
            return this.a(s, "analog", g.a(com.facebook.ads.internal.util.a.a()));
        }
        catch (Exception ex) {
            return s;
        }
    }
    
    private String a(final String s, final String s2, final String s3) {
        if (TextUtils.isEmpty((CharSequence)s) || TextUtils.isEmpty((CharSequence)s2) || TextUtils.isEmpty((CharSequence)s3)) {
            return s;
        }
        return s + (s.contains("?") ? "&" : "?") + s2 + "=" + URLEncoder.encode(s3);
    }
    
    static {
        a = x.class.getSimpleName();
        (b = new HashSet<String>()).add("#");
        x.b.add("null");
    }
    
    public interface a
    {
        void a(final y p0);
        
        void a();
    }
}
