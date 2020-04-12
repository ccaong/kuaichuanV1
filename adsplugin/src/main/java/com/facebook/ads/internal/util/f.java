// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.util;

import java.util.HashSet;
import org.json.JSONArray;
import android.content.pm.PackageManager;
import java.util.Iterator;
import java.util.Collection;
import java.util.Map;
import com.facebook.ads.internal.gpackage.g;
import android.text.TextUtils;
import android.content.Context;

public class f
{
    public static boolean a(final Context context, final a a) {
        final e d = a.D();
        if (d == null || d == e.NONE) {
            return false;
        }
        boolean b = false;
        final Collection<String> e = a.E();
        if (e == null || e.isEmpty()) {
            return false;
        }
        final Iterator<String> iterator = e.iterator();
        while (iterator.hasNext()) {
            if (a(context, iterator.next())) {
                b = true;
                break;
            }
        }
        if (b != (d == com.facebook.ads.internal.util.e.INSTALLED)) {
            return false;
        }
        final String b2 = a.B();
        if (!TextUtils.isEmpty((CharSequence)b2)) {
            g.a(context).f(b2, null);
            return true;
        }
        return true;
    }
    
    public static boolean a(final Context context, final String s) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            return false;
        }
        final PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getPackageInfo(s, 1);
            return true;
        }
        catch (PackageManager.NameNotFoundException ex) {
            return false;
        }
        catch (RuntimeException ex2) {
            return false;
        }
    }
    
    public static Collection<String> a(final JSONArray jsonArray) {
        if (jsonArray == null || jsonArray.length() == 0) {
            return null;
        }
        final HashSet<String> set = new HashSet<String>();
        for (int i = 0; i < jsonArray.length(); ++i) {
            set.add(jsonArray.optString(i));
        }
        return set;
    }
    
    public interface a
    {
        e D();
        
        Collection<String> E();
        
        String B();
    }
}
