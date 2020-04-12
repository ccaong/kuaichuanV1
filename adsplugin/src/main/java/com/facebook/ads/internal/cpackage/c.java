// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.cpackage;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import com.facebook.ads.internal.ipackage.a.p;
import com.facebook.ads.internal.util.w;
import java.io.IOException;
import android.util.Log;
import android.graphics.Rect;
import java.io.InputStream;
import java.io.FileInputStream;
import android.support.annotation.Nullable;
import android.graphics.BitmapFactory;
import java.io.File;
import android.graphics.Bitmap;
import android.content.Context;

public class c
{
    private static final String TAG;
    private static c b;
    private final Context context;
    
    public static c a(final Context context) {
        if (c.b == null) {
            final Context applicationContext = context.getApplicationContext();
            synchronized (applicationContext) {
                if (c.b == null) {
                    c.b = new c(applicationContext);
                }
            }
        }
        return c.b;
    }
    
    private c(final Context c) {
        this.context = c;
    }
    
    @Nullable
    public Bitmap a(final String s) {
        final File file = new File(this.context.getCacheDir(), s.hashCode() + ".png");
        if (!file.exists()) {
            return s.startsWith("file://") ? this.b(s) : this.c(s);
        }
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }
    
    @Nullable
    private Bitmap b(final String s) {
        try {
            final Bitmap decodeStream = BitmapFactory.decodeStream((InputStream)new FileInputStream(s.substring("file://".length())), (Rect)null, (BitmapFactory.Options)null);
            this.a(s, decodeStream);
            return decodeStream;
        }
        catch (IOException ex) {
            Log.e(c.TAG, "Failed to copy local image into cache (url=" + s + ").", (Throwable)ex);
            return null;
        }
    }
    
    @Nullable
    private Bitmap c(final String s) {
        Bitmap bitmap;
        if (s.startsWith("asset:///")) {
            InputStream open = null;
            try {
                open = this.context.getAssets().open(s.substring(9, s.length()));
                bitmap = BitmapFactory.decodeStream(open);
            }
            catch (IOException ex2) {
                return null;
            }
            finally {
                if (open != null) {
                    try {
                        open.close();
                    }
                    catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        else {
            final byte[] d = w.a(this.context).a(s, (p)null).d();
            bitmap = BitmapFactory.decodeByteArray(d, 0, d.length);
        }
        this.a(s, bitmap);
        return bitmap;
    }
    
    public void a(final String s, final Bitmap bitmap) {
        final File file = new File(this.context.getCacheDir(), s.hashCode() + ".png");
        ByteArrayOutputStream byteArrayOutputStream = null;
        OutputStream outputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, (OutputStream)byteArrayOutputStream);
            if (byteArrayOutputStream.size() >= 3145728) {
                Log.d(c.TAG, "Bitmap size exceeds max size for storage");
                return;
            }
            outputStream = new FileOutputStream(file);
            byteArrayOutputStream.writeTo(outputStream);
            outputStream.flush();
        }
        catch (FileNotFoundException ex) {
            Log.e(c.TAG, "Bad output destination (file=" + file.getAbsolutePath() + ").", (Throwable)ex);
        }
        catch (IOException ex2) {
            Log.e(c.TAG, "Unable to write bitmap to file (url=" + s + ").", (Throwable)ex2);
        }
        catch (OutOfMemoryError outOfMemoryError) {
            Log.e(c.TAG, "Unable to write bitmap to output stream", (Throwable)outOfMemoryError);
        }
        finally {
            a(byteArrayOutputStream);
            a(outputStream);
        }
    }
    
    private static void a(@Nullable final Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        }
        catch (IOException ex) {}
    }
    
    static {
        TAG = c.class.getSimpleName();
    }
}
