// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.adapters;

import com.facebook.ads.AdNetwork;
import com.facebook.ads.internal.util.ai;
import com.facebook.ads.NativeAdViewAttributes;
import com.facebook.ads.NativeAd;
import java.util.List;
import android.view.View;
import java.util.Map;
import com.facebook.ads.internal.gpackage.f;
import android.content.Context;
import com.facebook.ads.internal.server.AdPlacementType;

public abstract class v implements AdAdapter
{
    @Override
    public final AdPlacementType getPlacementType() {
        return AdPlacementType.NATIVE;
    }
    
    public abstract void a(final Context p0, final w p1, final f p2, final Map<String, Object> p3);
    
    public abstract void a(final Map<String, String> p0);
    
    public abstract void b(final Map<String, String> p0);
    
    public abstract void a(final int p0);
    
    public abstract void a(final View p0, final List<View> p1);
    
    public abstract void a();
    
    public abstract boolean b();
    
    public abstract boolean c();
    
    public abstract boolean d();
    
    public abstract boolean e();
    
    public abstract boolean f();
    
    public abstract boolean g();
    
    public abstract int h();
    
    public abstract int i();
    
    public abstract int j();
    
    public abstract NativeAd.Image k();
    
    public abstract NativeAd.Image l();
    
    public abstract NativeAdViewAttributes m();
    
    public abstract String n();
    
    public abstract String o();
    
    public abstract String p();
    
    public abstract String q();
    
    public abstract String r();
    
    public abstract NativeAd.Rating s();
    
    public abstract NativeAd.Image t();
    
    public abstract String u();
    
    public abstract String v();
    
    public abstract String w();
    
    public abstract String x();
    
    public abstract ai y();
    
    public abstract List<NativeAd> A();
    
    public abstract String B();
    
    public abstract AdNetwork C();
    
    public abstract String z();
    
    public abstract void a(final w p0);
}
