// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal;

import android.content.ComponentName;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import android.os.Parcel;
import android.os.IBinder;
import android.os.IInterface;
import android.content.ServiceConnection;
import android.content.Intent;
import java.lang.reflect.Method;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;

import android.os.RemoteException;
import android.text.TextUtils;
import android.os.Looper;
import com.facebook.ads.internal.util.g;
import android.content.Context;

public class f
{
    public static final String a;
    private final String b;
    private final boolean cc;
    private final c d;
    
    private f(final String b, final boolean c, final c d) {
        this.b = b;
        this.cc = c;
        this.d = d;
    }
    
    public String a() {
        return this.b;
    }
    
    public boolean b() {
        return this.cc;
    }
    
    public c c() {
        return this.d;
    }
    
    public static f a(final Context context, final g.a a) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalStateException("Cannot get advertising info on main thread.");
        }
        if (a != null && !TextUtils.isEmpty((CharSequence)a.b)) {
            return new f(a.b, a.c, c.FB4A);
        }
        f f = a(context);
        if (f == null || TextUtils.isEmpty((CharSequence)f.a())) {
            f = b(context);
        }
        if (f == null || TextUtils.isEmpty((CharSequence)f.a())) {
            f = c(context);
        }
        return f;
    }
    
    private static f a(final Context context) {
        try {
            final AdvertisingIdClient.Info advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
            if (advertisingIdInfo != null) {
                return new f(advertisingIdInfo.getId(), advertisingIdInfo.isLimitAdTrackingEnabled(), c.DIRECT);
            }
        }
        catch (Throwable t) {}
        return null;
    }
    
    private static f b(final Context context) {
        final Method a = g.a("com.google.android.gms.common.GooglePlayServicesUtil", "isGooglePlayServicesAvailable", Context.class);
        if (a == null) {
            return null;
        }
        final Object a2 = g.a((Object)null, a, context);
        if (a2 == null || (int)a2 != 0) {
            return null;
        }
        final Method a3 = g.a("com.google.android.gms.ads.identifier.AdvertisingIdClient", "getAdvertisingIdInfo", Context.class);
        if (a3 == null) {
            return null;
        }
        final Object a4 = g.a((Object)null, a3, context);
        if (a4 == null) {
            return null;
        }
        final Method a5 = g.a(a4.getClass(), "getId", (Class<?>[])new Class[0]);
        final Method a6 = g.a(a4.getClass(), "isLimitAdTrackingEnabled", (Class<?>[])new Class[0]);
        if (a5 == null || a6 == null) {
            return null;
        }
        return new f((String)g.a(a4, a5, new Object[0]), (boolean)g.a(a4, a6, new Object[0]), c.REFLECTION);
    }
    
    private static f c(final Context context) {
        final b b = new b();
        final Intent intent = new Intent("com.google.android.gms.ads.identifier.service.START");
        intent.setPackage("com.google.android.gms");
        if (context.bindService(intent, (ServiceConnection)b, 1)) {
            try {
                final a a = new a(b.a());
                return new f(a.a(), a.b(), c.SERVICE);
            }
            catch (Exception ex) {}
            finally {
                context.unbindService((ServiceConnection)b);
            }
        }
        return null;
    }
    
    static {
        a = f.class.getSimpleName();
    }
    
    private static final class a implements IInterface
    {
        private IBinder a;
        
        a(final IBinder a) {
            this.a = a;
        }
        
        public IBinder asBinder() {
            return this.a;
        }
        
        public String a() throws RemoteException {
            final Parcel obtain = Parcel.obtain();
            final Parcel obtain2 = Parcel.obtain();
            String string;
            try {
                obtain.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                this.a.transact(1, obtain, obtain2, 0);
                obtain2.readException();
                string = obtain2.readString();
            }
            finally {
                obtain2.recycle();
                obtain.recycle();
            }
            return string;
        }
        
        public boolean b() throws RemoteException {
            final Parcel obtain = Parcel.obtain();
            final Parcel obtain2 = Parcel.obtain();
            boolean b;
            try {
                obtain.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                obtain.writeInt(1);
                this.a.transact(2, obtain, obtain2, 0);
                obtain2.readException();
                b = (0 != obtain2.readInt());
            }
            finally {
                obtain2.recycle();
                obtain.recycle();
            }
            return b;
        }
    }
    
    private static final class b implements ServiceConnection
    {
        private AtomicBoolean a;
        private final BlockingQueue<IBinder> b;
        
        private b() {
            this.a = new AtomicBoolean(false);
            this.b = new LinkedBlockingDeque<IBinder>();
        }
        
        public void onServiceConnected(final ComponentName componentName, final IBinder binder) {
            try {
                this.b.put(binder);
            }
            catch (InterruptedException ex) {}
        }
        
        public void onServiceDisconnected(final ComponentName componentName) {
        }
        
        public IBinder a() throws InterruptedException {
            if (this.a.compareAndSet(true, true)) {
                throw new IllegalStateException("Binder already consumed");
            }
            return this.b.take();
        }
    }
    
    public enum c
    {
        SHARED_PREFS, //a
        FB4A, //b
        DIRECT, //c
        REFLECTION, //d
        SERVICE; //e
    }
}
