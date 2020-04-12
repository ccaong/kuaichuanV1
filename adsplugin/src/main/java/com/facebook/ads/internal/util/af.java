// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.util;

import java.util.HashMap;
import java.util.Map;
import android.graphics.Rect;
import android.view.InputDevice;
import android.view.View;
import android.view.MotionEvent;

public class af
{
    private static final String a;
    private boolean b;
    private int c;
    private int d;
    private int e;
    private int f;
    private long g;
    private int h;
    private long i;
    private long j;
    private int k;
    private int l;
    private int m;
    private int n;
    private float o;
    private float p;
    private float q;
    
    public af() {
        this.c = -1;
        this.d = -1;
        this.e = -1;
        this.f = -1;
        this.g = -1L;
        this.h = -1;
        this.i = -1L;
        this.j = -1L;
        this.k = -1;
        this.l = -1;
        this.m = -1;
        this.n = -1;
        this.o = -1.0f;
        this.p = -1.0f;
        this.q = -1.0f;
    }
    
    public void a(final MotionEvent motionEvent, final View view, final View view2) {
        if (!this.b) {
            this.b = true;
            final InputDevice device = motionEvent.getDevice();
            if (device != null) {
                final InputDevice.MotionRange motionRange = device.getMotionRange(0);
                final InputDevice.MotionRange motionRange2 = device.getMotionRange(1);
                if (motionRange != null && motionRange2 != null) {
                    this.q = Math.min(motionRange.getRange(), motionRange2.getRange());
                }
            }
            if (this.q <= 0.0f) {
                this.q = Math.min(view.getMeasuredWidth(), view.getMeasuredHeight());
            }
        }
        final int[] array = new int[2];
        view.getLocationInWindow(array);
        final int[] array2 = new int[2];
        view2.getLocationInWindow(array2);
        switch (motionEvent.getAction()) {
            case 0: {
                this.c = array[0];
                this.d = array[1];
                this.e = view.getWidth();
                this.f = view.getHeight();
                this.h = 1;
                this.i = System.currentTimeMillis();
                this.k = (int)(motionEvent.getX() + 0.5f) + array2[0] - array[0];
                this.l = (int)(motionEvent.getY() + 0.5f) + array2[1] - array[1];
                this.o = motionEvent.getPressure();
                this.p = motionEvent.getSize();
                break;
            }
            case 2: {
                this.o -= this.o / this.h;
                this.o += motionEvent.getPressure() / this.h;
                this.p -= this.p / this.h;
                this.p += motionEvent.getSize() / this.h;
                ++this.h;
                break;
            }
            case 1:
            case 3: {
                this.j = System.currentTimeMillis();
                this.m = (int)(motionEvent.getX() + 0.5f) + array2[0] - array[0];
                this.n = (int)(motionEvent.getY() + 0.5f) + array2[1] - array[1];
                break;
            }
        }
    }
    
    public void a() {
        this.g = System.currentTimeMillis();
    }
    
    public boolean b() {
        return this.g != -1L;
    }
    
    public long c() {
        if (this.b()) {
            return System.currentTimeMillis() - this.g;
        }
        return -1L;
    }
    
    public boolean a(final int n) {
        if (!this.d() || this.m == -1 || this.n == -1 || this.e == -1 || this.f == -1) {
            return false;
        }
        final int n2 = this.f * n / 100;
        final int n3 = this.e * n / 100;
        return !new Rect(n3, n2, this.e - n3, this.f - n2).contains(this.m, this.n);
    }
    
    public boolean d() {
        return this.b;
    }
    
    public Map<String, String> e() {
        if (!this.b) {
            return null;
        }
        final String value = String.valueOf(this.p * this.q / 2.0f);
        final long n = (this.g > 0L && this.j > this.g) ? (this.j - this.g) : -1L;
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("adPositionX", String.valueOf(this.c));
        hashMap.put("adPositionY", String.valueOf(this.d));
        hashMap.put("width", String.valueOf(this.e));
        hashMap.put("height", String.valueOf(this.f));
        hashMap.put("clickDelayTime", String.valueOf(n));
        hashMap.put("startTime", String.valueOf(this.i));
        hashMap.put("endTime", String.valueOf(this.j));
        hashMap.put("startX", String.valueOf(this.k));
        hashMap.put("startY", String.valueOf(this.l));
        hashMap.put("clickX", String.valueOf(this.m));
        hashMap.put("clickY", String.valueOf(this.n));
        hashMap.put("endX", String.valueOf(this.m));
        hashMap.put("endY", String.valueOf(this.n));
        hashMap.put("force", String.valueOf(this.o));
        hashMap.put("radiusX", value);
        hashMap.put("radiusY", value);
        return hashMap;
    }
    
    static {
        a = af.class.getSimpleName();
    }
}
