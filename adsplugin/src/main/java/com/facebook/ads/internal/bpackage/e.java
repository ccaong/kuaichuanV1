// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.bpackage;

import android.view.Window;
import android.app.KeyguardManager;
import android.app.Activity;
import android.os.PowerManager;
import java.util.Arrays;
import android.os.Build;
import java.util.Collection;
import android.view.ViewGroup;
import java.util.Vector;
import android.graphics.Rect;
import android.content.Context;
import android.view.View;

public class e
{
    public static double a(final View view, final Context context) {
        if (!a(context)) {
            return 0.0;
        }
        if (!d(view)) {
            return 0.0;
        }
        final Rect rect = new Rect();
        if (!view.getGlobalVisibleRect(rect)) {
            return 0.0;
        }
        final Vector<Rect> a = a(view);
        final int a2 = a(a);
        a.add(rect);
        return (a(a) - a2) * 1.0 / (view.getMeasuredHeight() * view.getMeasuredWidth());
    }
    
    private static Vector<Rect> a(final View view) {
        final Vector<Rect> vector = new Vector<Rect>();
        if (!(view.getParent() instanceof ViewGroup)) {
            return vector;
        }
        final ViewGroup viewGroup = (ViewGroup)view.getParent();
        for (int i = viewGroup.indexOfChild(view) + 1; i < viewGroup.getChildCount(); ++i) {
            vector.addAll(b(viewGroup.getChildAt(i)));
        }
        vector.addAll(a((View)viewGroup));
        return vector;
    }
    
    private static Vector<Rect> b(final View view) {
        final Vector<Rect> vector = new Vector<Rect>();
        if (!view.isShown() || (Build.VERSION.SDK_INT >= 11 && view.getAlpha() <= 0.0f)) {
            return vector;
        }
        if (view instanceof ViewGroup && c(view)) {
            final ViewGroup viewGroup = (ViewGroup)view;
            for (int i = 0; i < viewGroup.getChildCount(); ++i) {
                vector.addAll(b(viewGroup.getChildAt(i)));
            }
            return vector;
        }
        final Rect rect = new Rect();
        if (view.getGlobalVisibleRect(rect)) {
            vector.add(rect);
        }
        return vector;
    }
    
    private static boolean c(final View view) {
        return view.getBackground() == null || (Build.VERSION.SDK_INT >= 19 && view.getBackground().getAlpha() <= 0);
    }
    
    private static int a(final Vector<Rect> vector) {
        final int size = vector.size();
        final int[] array = new int[size * 2];
        final int[] array2 = new int[size * 2];
        final boolean[][] array3 = new boolean[2 * size][2 * size];
        int n = 0;
        int n2 = 0;
        for (int i = 0; i < size; ++i) {
            final Rect rect = vector.elementAt(i);
            array[n++] = rect.left;
            array2[n2++] = rect.bottom;
            array[n++] = rect.right;
            array2[n2++] = rect.top;
        }
        Arrays.sort(array);
        Arrays.sort(array2);
        for (int j = 0; j < size; ++j) {
            final Rect rect2 = vector.elementAt(j);
            final int a = a(array, rect2.left);
            final int a2 = a(array, rect2.right);
            final int a3 = a(array2, rect2.top);
            final int a4 = a(array2, rect2.bottom);
            for (int k = a + 1; k <= a2; ++k) {
                for (int l = a3 + 1; l <= a4; ++l) {
                    array3[k][l] = true;
                }
            }
        }
        int n3 = 0;
        for (int n4 = 0; n4 < 2 * size; ++n4) {
            for (int n5 = 0; n5 < 2 * size; ++n5) {
                n3 += (array3[n4][n5] ? ((array[n4] - array[n4 - 1]) * (array2[n5] - array2[n5 - 1])) : 0);
            }
        }
        return n3;
    }
    
    private static int a(final int[] array, final int n) {
        int i = 0;
        int length = array.length;
        while (i < length) {
            final int n2 = i + (length - i) / 2;
            if (array[n2] == n) {
                return n2;
            }
            if (array[n2] > n) {
                length = n2;
            }
            else {
                i = n2 + 1;
            }
        }
        return -1;
    }
    
    private static boolean d(final View view) {
        return view != null && view.isShown() && view.getWindowVisibility() == View.VISIBLE && view.getVisibility() == 0 && view.getMeasuredWidth() > 0 && view.getMeasuredHeight() > 0 && (Build.VERSION.SDK_INT < 11 || view.getAlpha() >= 0.9f);
    }
    
    private static boolean a(final Context context) {
        if (context == null) {
            return true;
        }
        try {
            if (!((PowerManager)context.getSystemService(Context.POWER_SERVICE)).isScreenOn()) {
                return false;
            }
            boolean b = false;
            if (context instanceof Activity) {
                final Window window = ((Activity)context).getWindow();
                if (window != null) {
                    final int flags = window.getAttributes().flags;
                    b = ((flags & 0x400000) != 0x0 || (flags & 0x80000) != 0x0);
                }
            }
            if (((KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE)).inKeyguardRestrictedInputMode() && !b) {
                return false;
            }
        }
        catch (Exception ex) {
            return false;
        }
        return true;
    }
}
