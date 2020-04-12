// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.util;

import java.io.FileInputStream;
import java.util.Map;
import java.util.UUID;
import com.facebook.ads.internal.fpackage.i;
import com.facebook.ads.internal.fpackage.g;
import android.support.annotation.WorkerThread;
import java.io.Reader;
import java.io.InputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import org.json.JSONArray;
import java.io.FileOutputStream;

import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import android.content.Context;
import com.facebook.ads.internal.gpackage.p;

public class n
{
    private static final String a;
    private static final Object b;
    
    public static void a(final p p2, final Context context) {
        if (p2 != null && context != null) {
            synchronized (n.b) {
                try {
                    final JSONObject a = a(p2);
                    final FileOutputStream openFileOutput = context.openFileOutput("crasheslog", 32768);
                    openFileOutput.write((a.toString() + "\n").getBytes());
                    openFileOutput.close();
                }
                catch (Exception ex) {
                    Log.e(n.a, "Failed to store crash", (Throwable)ex);
                }
            }
        }
    }
    
    @WorkerThread
    public static JSONArray a(final Context context) {
        final JSONArray jsonArray = new JSONArray();
        synchronized (n.b) {
            InputStream openFileInput = null;
            Reader reader = null;
            BufferedReader bufferedReader = null;
            try {
                if (new File(context.getFilesDir(), "crasheslog").exists()) {
                    openFileInput = context.openFileInput("crasheslog");
                    reader = new InputStreamReader(openFileInput);
                    bufferedReader = new BufferedReader(reader);
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        jsonArray.put((Object)new JSONObject(line));
                    }
                }
            }
            catch (Exception ex) {
                Log.e(n.a, "Failed to read crashes", (Throwable)ex);
                try {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                    if (reader != null) {
                        ((InputStreamReader)reader).close();
                    }
                    if (openFileInput != null) {
                        ((FileInputStream)openFileInput).close();
                    }
                }
                catch (IOException ex2) {
                    Log.e(n.a, "Failed to close buffers", (Throwable)ex2);
                }
            }
            finally {
                try {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                    if (reader != null) {
                        ((InputStreamReader)reader).close();
                    }
                    if (openFileInput != null) {
                        ((FileInputStream)openFileInput).close();
                    }
                }
                catch (IOException ex3) {
                    Log.e(n.a, "Failed to close buffers", (Throwable)ex3);
                }
            }
        }
        return jsonArray;
    }
    
    @WorkerThread
    public static void b(final Context context) {
        synchronized (n.b) {
            final File file = new File(context.getFilesDir(), "crasheslog");
            if (file.exists()) {
                file.delete();
            }
        }
    }
    
    public static p a(final Exception ex, final Context context) {
        final p p2 = new p(g.b(), g.c(), new m(ae.a(ex), i.f, true));
        a(p2, context);
        return p2;
    }
    
    private static JSONObject a(final p p) throws JSONException {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", (Object)UUID.randomUUID().toString());
        jsonObject.put("type", (Object)p.b());
        jsonObject.put("time", (Object)com.facebook.ads.internal.util.g.a(p.e()));
        jsonObject.put("session_time", (Object)com.facebook.ads.internal.util.g.a(p.f()));
        jsonObject.put("session_id", (Object)p.g());
        jsonObject.put("data", (Object)((p.h() != null) ? new JSONObject((Map)p.h()) : new JSONObject()));
        return jsonObject;
    }
    
    static {
        a = n.class.getName();
        b = new Object();
    }
}
