// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.util;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.graphics.Bitmap;
import android.content.Context;

public class t
{
    public static Bitmap a(final Context context, final r r) {
        final byte[] decode = Base64.decode(r.a(context.getResources().getDisplayMetrics().density), 0);
        return BitmapFactory.decodeByteArray(decode, 0, decode.length);
    }
    
    public static Drawable b(final Context context, final r r) {
        return (Drawable)new BitmapDrawable(context.getResources(), a(context, r));
    }
}
