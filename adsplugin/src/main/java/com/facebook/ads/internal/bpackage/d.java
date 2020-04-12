// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.bpackage;

import java.io.Serializable;
import android.os.Bundle;
import com.facebook.ads.internal.util.ad;

public class d implements ad<Bundle>
{
    private c a;
    private final c b;
    private final b c;
    private boolean d;
    private boolean e;
    private boolean f;
    
    public d(final b c) {
        this.d = false;
        this.e = false;
        this.f = false;
        this.c = c;
        this.b = new c(c.a);
        this.a = new c(c.a);
    }
    
    public d(final b c, final Bundle bundle) {
        this.d = false;
        this.e = false;
        this.f = false;
        this.c = c;
        this.b = (c)bundle.getSerializable("testStats");
        this.a = (c)bundle.getSerializable("viewableStats");
        this.d = bundle.getBoolean("ended");
        this.e = bundle.getBoolean("passed");
        this.f = bundle.getBoolean("complete");
    }
    
    public void a(final double n, final double n2) {
        if (this.d) {
            return;
        }
        this.b.a(n, n2);
        this.a.a(n, n2);
        final double f = this.a.b().f();
        if (this.c.d && n2 < this.c.a) {
            this.a = new c(this.c.a);
        }
        if (this.c.b >= 0.0 && this.b.b().e() > this.c.b && f == 0.0) {
            this.b();
            return;
        }
        if (f >= this.c.c) {
            this.a();
        }
    }
    
    private void a() {
        this.e = true;
        this.b();
    }
    
    private void b() {
        this.f = true;
        this.c();
    }
    
    private void c() {
        this.d = true;
        this.c.a(this.f, this.e, this.e ? this.a : this.b);
    }
    
    @Override
    public Bundle getSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putSerializable("viewableStats", (Serializable)this.a);
        bundle.putSerializable("testStats", (Serializable)this.b);
        bundle.putBoolean("ended", this.d);
        bundle.putBoolean("passed", this.e);
        bundle.putBoolean("complete", this.f);
        return bundle;
    }
}
