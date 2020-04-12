// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.util;

import android.util.Log;
import com.facebook.ads.internal.cpackage.c;
import com.facebook.ads.internal.view.e;
import android.widget.ImageView;
import android.content.Context;
import java.lang.ref.WeakReference;
import android.graphics.Bitmap;
import android.os.AsyncTask;

public class p extends AsyncTask<String, Void, Bitmap[]>
{
    private static final String a;
    private final WeakReference<Context> b;
    private final WeakReference<ImageView> c;
    private final WeakReference<e> d;
    private q e;
    
    public p(final ImageView imageView) {
        this.b = new WeakReference<Context>(imageView.getContext());
        this.d = null;
        this.c = new WeakReference<ImageView>(imageView);
    }
    
    public p(final e e) {
        this.b = new WeakReference<Context>(e.getContext());
        this.d = new WeakReference<e>(e);
        this.c = null;
    }
    
    public p a(final q e) {
        this.e = e;
        return this;
    }
    
    public void a(final String... array) {
        this.executeOnExecutor(p.THREAD_POOL_EXECUTOR, (String[])array);
    }
    
    protected Bitmap[] doInBackground(final String... array) {
        final String s = array[0];
        Bitmap a = null;
        Bitmap a2 = null;
        final Context context = this.b.get();
        if (context == null) {
            return new Bitmap[] { a, a2 };
        }
        try {
            a = com.facebook.ads.internal.cpackage.c.a(context).a(s);
            if (this.d != null && this.d.get() != null && a != null) {
                a2 = a;
                final ac ac = new ac(a);
                ac.a(Math.round(a.getWidth() / 40.0f));
                a2 = ac.a();
            }
        }
        catch (Throwable t) {
            Log.e(p.a, "Error downloading image: " + s, t);
            com.facebook.ads.internal.util.c.a(com.facebook.ads.internal.util.b.a(t, null));
        }
        return new Bitmap[] { a, a2 };
    }
    
    protected void onPostExecute(final Bitmap[] array) {
        if (this.c != null) {
            final ImageView imageView = this.c.get();
            if (imageView != null) {
                imageView.setImageBitmap(array[0]);
            }
        }
        if (this.d != null) {
            final e e = this.d.get();
            if (e != null) {
                e.a(array[0], array[1]);
            }
        }
        if (this.e != null) {
            this.e.a();
        }
    }
    
    static {
        a = p.class.getSimpleName();
    }
}
