// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.util;

import android.hardware.SensorEvent;
import java.util.concurrent.ConcurrentHashMap;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.StatFs;
import android.os.Environment;
import android.app.ActivityManager;
import java.util.HashMap;
import android.hardware.SensorEventListener;
import android.content.Context;
import java.util.Map;
import android.hardware.Sensor;
import android.hardware.SensorManager;

public class a
{
    private static SensorManager sensorManager;
    private static Sensor b;
    private static Sensor c;
    private static volatile float[] d;
    private static volatile float[] e;
    private static Map<String, String> f;
    private static String[] g;
    
    public static synchronized void a(final Context context) {
        b(context);
        c(context);
        d(context);
        if (com.facebook.ads.internal.util.a.sensorManager == null) {
            com.facebook.ads.internal.util.a.sensorManager = (SensorManager)context.getSystemService("sensor");
            if (com.facebook.ads.internal.util.a.sensorManager == null) {
                return;
            }
        }
        if (com.facebook.ads.internal.util.a.b == null) {
            com.facebook.ads.internal.util.a.b = com.facebook.ads.internal.util.a.sensorManager.getDefaultSensor(1);
        }
        if (com.facebook.ads.internal.util.a.c == null) {
            com.facebook.ads.internal.util.a.c = com.facebook.ads.internal.util.a.sensorManager.getDefaultSensor(4);
        }
        if (com.facebook.ads.internal.util.a.b != null) {
            com.facebook.ads.internal.util.a.sensorManager.registerListener(new aListener(), com.facebook.ads.internal.util.a.b, 3);
        }
        if (com.facebook.ads.internal.util.a.c != null) {
            com.facebook.ads.internal.util.a.sensorManager.registerListener(new aListener(), com.facebook.ads.internal.util.a.c, 3);
        }
    }
    
    public static synchronized void a(final aListener listener) {
        if (a.sensorManager == null) {
            return;
        }
        a.sensorManager.unregisterListener((SensorEventListener)listener);
    }
    
    public static Map<String, String> a() {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.putAll(com.facebook.ads.internal.util.a.f);
        a((Map<String, String>)hashMap);
        return (Map<String, String>)hashMap;
    }
    
    private static void b(final Context context) {
        final ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ((ActivityManager)context.getSystemService("activity")).getMemoryInfo(memoryInfo);
        com.facebook.ads.internal.util.a.f.put("available_memory", String.valueOf(memoryInfo.availMem));
    }
    
    private static void c(final Context context) {
        final StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        com.facebook.ads.internal.util.a.f.put("free_space", String.valueOf(statFs.getAvailableBlocks() * statFs.getBlockSize()));
    }
    
    private static void d(final Context context) {
        final Intent registerReceiver = context.registerReceiver((BroadcastReceiver)null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        if (registerReceiver == null) {
            return;
        }
        final int intExtra = registerReceiver.getIntExtra("level", -1);
        final int intExtra2 = registerReceiver.getIntExtra("scale", -1);
        final int intExtra3 = registerReceiver.getIntExtra("status", -1);
        final boolean b = intExtra3 == 2 || intExtra3 == 5;
        float n = 0.0f;
        if (intExtra2 > 0) {
            n = intExtra / intExtra2 * 100.0f;
        }
        com.facebook.ads.internal.util.a.f.put("battery", String.valueOf(n));
        com.facebook.ads.internal.util.a.f.put("charging", b ? "1" : "0");
    }
    
    private static void a(final Map<String, String> map) {
        final float[] d = com.facebook.ads.internal.util.a.d;
        final float[] e = com.facebook.ads.internal.util.a.e;
        if (d != null) {
            for (int min = Math.min(com.facebook.ads.internal.util.a.g.length, d.length), i = 0; i < min; ++i) {
                map.put("accelerometer_" + com.facebook.ads.internal.util.a.g[i], String.valueOf(d[i]));
            }
        }
        if (e != null) {
            for (int min2 = Math.min(com.facebook.ads.internal.util.a.g.length, e.length), j = 0; j < min2; ++j) {
                map.put("rotation_" + com.facebook.ads.internal.util.a.g[j], String.valueOf(e[j]));
            }
        }
    }
    
    static {
        a.sensorManager = null;
        com.facebook.ads.internal.util.a.b = null;
        com.facebook.ads.internal.util.a.c = null;
        com.facebook.ads.internal.util.a.f = new ConcurrentHashMap<String, String>();
        com.facebook.ads.internal.util.a.g = new String[] { "x", "y", "z" };
    }
    
    private static class aListener implements SensorEventListener
    {
        public void onSensorChanged(final SensorEvent sensorEvent) {
            if (sensorEvent.sensor == com.facebook.ads.internal.util.a.b) {
                com.facebook.ads.internal.util.a.d = sensorEvent.values;
            }
            else if (sensorEvent.sensor == com.facebook.ads.internal.util.a.c) {
                com.facebook.ads.internal.util.a.e = sensorEvent.values;
            }
            com.facebook.ads.internal.util.a.a(this);
        }
        
        public void onAccuracyChanged(final Sensor sensor, final int n) {
        }
    }
}
