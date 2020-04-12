package com.facebook.ads.internal.action;//a

import com.facebook.ads.internal.gpackage.g;
import android.text.TextUtils;
import java.util.Map;
import android.content.Context;
import com.facebook.ads.internal.util.b;

public abstract class a
{
    public abstract b.a a();

    public abstract void b();

    protected void a(final Context context, final String s, final Map<String, String> map) {
        if (!TextUtils.isEmpty((CharSequence)s)) {
            final g a = g.a(context);
            if (this instanceof com.facebook.ads.internal.action.c) {
                a.g(s, map);
            }
            else {
                a.b(s, map);
            }
        }
        com.facebook.ads.internal.util.g.a(context, "Click logged");
    }
}
