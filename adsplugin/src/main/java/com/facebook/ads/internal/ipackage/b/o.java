// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.ipackage.b;

import android.util.Log;
import android.os.Environment;
import java.io.File;
import android.content.Context;

final class o
{
    public static File a(final Context context) {
        return new File(a(context, true), "video-cache");
    }
    
    private static File a(final Context context, final boolean b) {
        File file = null;
        String externalStorageState;
        try {
            externalStorageState = Environment.getExternalStorageState();
        }
        catch (NullPointerException ex) {
            externalStorageState = "";
        }
        if (b && "mounted".equals(externalStorageState)) {
            file = b(context);
        }
        if (file == null) {
            file = context.getCacheDir();
        }
        if (file == null) {
            final String string = "/data/data/" + context.getPackageName() + "/cache/";
            Log.w("ProxyCache", "Can't define system cache directory! '" + string + "%s' will be used.");
            file = new File(string);
        }
        return file;
    }
    
    private static File b(final Context context) {
        final File file = new File(new File(new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data"), context.getPackageName()), "cache");
        if (!file.exists() && !file.mkdirs()) {
            Log.w("ProxyCache", "Unable to create external cache directory");
            return null;
        }
        return file;
    }
}
