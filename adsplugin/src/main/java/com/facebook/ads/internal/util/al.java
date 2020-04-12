// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.util;

import android.app.KeyguardManager;
import android.view.Window;
import android.util.Log;
import android.app.Activity;
import java.util.HashMap;
import java.util.Map;
import android.content.Context;

public class al
{
    private static final String a;
    
    public static Map<String, String> a(final Context context) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        try {
            hashMap.put("kgr", String.valueOf(b(context)));
            if (context != null && context instanceof Activity) {
                final Window window = ((Activity)context).getWindow();
                if (window != null) {
                    final int flags = window.getAttributes().flags;
                    hashMap.put("wt", Integer.toString(window.getAttributes().type));
                    hashMap.put("wfdkg", ((flags & 0x400000) > 0) ? "1" : "0");
                    hashMap.put("wfswl", ((flags & 0x80000) > 0) ? "1" : "0");
                }
                else {
                    Log.v(al.a, "Invalid window in window interactive check, assuming interactive.");
                }
            }
            else {
                Log.v(al.a, "Invalid Activity context in window interactive check, assuming interactive.");
            }
        }
        catch (Exception ex) {
            Log.e(al.a, "Exception in window info check", (Throwable)ex);
            n.a(ex, context);
        }
        return hashMap;
    }
    
    public static boolean b(final Context context) {
        final KeyguardManager keyguardManager = (KeyguardManager)context.getSystemService("keyguard");
        return keyguardManager != null && keyguardManager.inKeyguardRestrictedInputMode();
    }
    
    public static boolean a(final Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            Log.v(al.a, "Invalid Window info in window interactive check, assuming not obstructed by Keyguard.");
            return false;
        }
        final String s = map.get("wfdkg");
        final String s2 = map.get("wfswl");
        if ((s != null && s.equals("1")) || (s2 != null && s2.equals("1"))) {
            return false;
        }
        final String s3 = map.get("kgr");
        return s3 != null && s3.equals("true");
    }
    
    public static boolean b(final Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            Log.v(al.a, "Invalid Window info in window interactive check, assuming is not a Lockscreen.");
            return false;
        }
        final String s = map.get("wfdkg");
        final String s2 = map.get("wfswl");
        final String s3 = map.get("kgr");
        return s != null && s.equals("1") && s2 != null && s2.equals("1") && s3 != null && s3.equals("true");
    }
    
    public static boolean c(final Context context) {
        return !a(a(context));
    }
    
    static {
        a = al.class.getSimpleName();
    }
}
