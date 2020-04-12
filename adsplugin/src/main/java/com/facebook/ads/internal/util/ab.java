//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.facebook.ads.internal.util;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.facebook.ads.internal.gpackage.f;
import com.facebook.ads.internal.gpackage.s;
import com.facebook.ads.internal.util.ah;
import com.facebook.ads.internal.view.dpackage.a.b;
import com.facebook.ads.internal.view.dpackage.a.h;
import com.facebook.ads.internal.view.dpackage.a.j;
import com.facebook.ads.internal.view.dpackage.a.l;
import com.facebook.ads.internal.view.dpackage.a.m;
import com.facebook.ads.internal.view.dpackage.a.n;
import com.facebook.ads.internal.view.dpackage.a.p;
import com.facebook.ads.internal.view.dpackage.a.q;
import com.facebook.ads.internal.view.dpackage.a.r;
import com.facebook.ads.internal.view.dpackage.a.t;
import com.facebook.ads.internal.view.dpackage.a.u;
import com.facebook.ads.internal.view.dpackage.a.v;
import com.facebook.ads.internal.view.dpackage.a.w;

public class ab extends ah {
    private final u b = new u() {
        public void a(t var1) {
            assert ab.this != null;

            if(ab.this != null) {
                ab.this.e();
            }
        }
    };
    private final s<q> c = new s<q>() {
        public void a(q var1) {
            assert ab.this != null;

            if(ab.this != null) {
                ab.this.h();
            }
        }

        public Class<q> a() {
            return q.class;
        }
    };
    private final s<h> d = new s<h>() {
        public void a(h var1) {
            assert ab.this != null;

            if(ab.this != null) {
                ab.this.i();
            }
        }

        public Class<h> a() {
            return h.class;
        }
    };
    private final s<j> e = new s<j>() {
        public void a(j var1) {
            assert ab.this != null;

            if(ab.this != null) {
                if(!ab.this.n) {
                    ab.this.n = true;
                } else {
                    ab.this.j();
                }
            }
        }

        public Class<j> a() {
            return j.class;
        }
    };
    private final s<n> f = new s<n>() {
        public void a(n var1) {
            if(ab.this.a <= 0 || ab.this.m.getCurrentPosition() != ab.this.m.getDuration() || ab.this.m.getDuration() <= ab.this.a) {
                ab.this.a(ab.this.m.getCurrentPosition());
            }
        }

        public Class<n> a() {
            return n.class;
        }
    };
    private final s<com.facebook.ads.internal.view.dpackage.a.b> g = new s<com.facebook.ads.internal.view.dpackage.a.b>() {
        public void a(com.facebook.ads.internal.view.dpackage.a.b var1) {
            int var2 = ab.this.m.getCurrentPosition();
            if(ab.this.a <= 0 || var2 != ab.this.m.getDuration() || ab.this.m.getDuration() <= ab.this.a) {
                if((var2 != 0 || !ab.this.m.b()) && ab.this.m.getDuration() >= var2 + 500) {
                    ab.this.b(var2);
                } else {
                    ab.this.b(ab.this.m.getDuration());
                }

            }
        }

        public Class<com.facebook.ads.internal.view.dpackage.a.b> a() {
            return com.facebook.ads.internal.view.dpackage.a.b.class;
        }
    };
    private final s<p> h = new s<p>() {
        public void a(p var1) {
            ab.this.a(var1.a(), var1.b());
        }

        public Class<p> a() {
            return p.class;
        }
    };
    private final s<v> i = new s<v>() {
        public void a(v var1) {
            ab.this.b();
        }

        public Class<v> a() {
            return v.class;
        }
    };
    private final s<w> j = new s<w>() {
        public void a(w var1) {
            ab.this.c();
        }

        public Class<w> a() {
            return w.class;
        }
    };
    private final s<r> k = new s<r>() {
        public void a(r var1) {
            ab.this.a(ab.this.k(), ab.this.k());
        }

        public Class<r> a() {
            return r.class;
        }
    };
    private final m l = new m() {
        public void a(l var1) {
            ab.this.a = ab.this.m.getDuration();
        }
    };
    private final com.facebook.ads.internal.view.n m;
    public int a;
    private boolean n = false;

    public ab(Context var1, f var2, com.facebook.ads.internal.view.n var3, String var4) {
        super(var1, var2, var3, var4);
        this.m = var3;
        var3.getEventBus().a(this.b);
        var3.getEventBus().a(this.f);
        var3.getEventBus().a(this.c);
        var3.getEventBus().a(this.e);
        var3.getEventBus().a(this.d);
        var3.getEventBus().a(this.g);
        var3.getEventBus().a(this.h);
        var3.getEventBus().a(this.i);
        var3.getEventBus().a(this.j);
        var3.getEventBus().a(this.l);
        var3.getEventBus().a(this.k);
    }

    public ab(Context var1, f var2, com.facebook.ads.internal.view.n var3, String var4, @Nullable Bundle var5) {
        super(var1, var2, var3, var4, var5);
        this.m = var3;
        var3.getEventBus().a(this.b);
        var3.getEventBus().a(this.f);
        var3.getEventBus().a(this.c);
        var3.getEventBus().a(this.e);
        var3.getEventBus().a(this.d);
        var3.getEventBus().a(this.g);
        var3.getEventBus().a(this.i);
        var3.getEventBus().a(this.j);
        var3.getEventBus().a(this.k);
    }

    public void a() {
        this.m.getEventBus().b(this.b);
        this.m.getEventBus().b(this.f);
        this.m.getEventBus().b(this.c);
        this.m.getEventBus().b(this.e);
        this.m.getEventBus().b(this.d);
        this.m.getEventBus().b(this.g);
        this.m.getEventBus().b(this.i);
        this.m.getEventBus().b(this.j);
        this.m.getEventBus().b(this.l);
        this.m.getEventBus().b(this.k);
    }
}
