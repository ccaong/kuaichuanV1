// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.gpackage;

import java.util.Iterator;
import java.util.Collection;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Queue;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

public class r<T extends s, E extends q>
{
    private final Map<Class<E>, List<WeakReference<T>>> a;
    private final Queue<E> b;
    
    public r() {
        this.a = new HashMap<Class<E>, List<WeakReference<T>>>();
        this.b = new ArrayDeque<E>();
    }
    
    public synchronized boolean a(final T t) {
        if (t == null) {
            return false;
        }
        final Class<E> a = t.a();
        if (this.a.get(a) == null) {
            this.a.put(a, new ArrayList<WeakReference<T>>());
        }
        final List<WeakReference<T>> list = this.a.get(a);
        this.a(list);
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i).get() == t) {
                return false;
            }
        }
        return list.add(new WeakReference<T>(t));
    }
    
    public synchronized boolean b(@Nullable final T t) {
        if (t == null) {
            return false;
        }
        final List<WeakReference<T>> list = this.a.get(t.a());
        if (list == null) {
            return false;
        }
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i).get() == t) {
                list.get(i).clear();
                return true;
            }
        }
        return false;
    }
    
    public synchronized void a(final E e) {
        if (this.b.isEmpty()) {
            this.b.add(e);
            while (!this.b.isEmpty()) {
                this.b(this.b.peek());
                this.b.remove();
            }
        }
        else {
            this.b.add(e);
        }
    }
    
    private void b(final E e) {
        if (this.a == null) {
            return;
        }
        final List<WeakReference<T>> list = this.a.get(e.getClass());
        if (list == null) {
            return;
        }
        this.a(list);
        if (list.isEmpty()) {
            return;
        }
        final Iterator<WeakReference<T>> iterator = (list).iterator();
        while (iterator.hasNext()) {
            final s<E> s = iterator.next().get();
            if (s != null && s.b(e)) {
                s.a(e);
            }
        }
    }
    
    private void a(final List<WeakReference<T>> list) {
        if (list != null) {
            int n = 0;
            for (int i = 0; i < list.size(); ++i) {
                final WeakReference<T> weakReference = list.get(i);
                if (weakReference.get() != null) {
                    list.set(n++, weakReference);
                }
            }
            for (int j = list.size() - 1; j >= n; --j) {
                list.remove(j);
            }
        }
    }
}
