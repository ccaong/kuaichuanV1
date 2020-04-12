package com.facebook.ads.internal.adapters;

import android.content.Context;
import com.facebook.ads.internal.adapters.b;
import com.facebook.ads.internal.util.g;
import java.util.HashMap;
import java.util.Map;

public abstract class a {
    private final Context c;
    protected final b a;
    protected final com.facebook.ads.internal.rewarded_video.a b;
    private boolean d;

    public a(Context var1, b var2, com.facebook.ads.internal.rewarded_video.a var3) {
        this.c = var1;
        this.a = var2;
        this.b = var3;
    }

    public final void a() {
        if(!this.d) {
            if(this.a != null) {
                this.a.d();
            }

            HashMap var1 = new HashMap();
            if(this.b != null) {
                this.b.a(var1);
            }

            this.a(var1);
            this.d = true;
            g.a(this.c, "Impression logged");
            if(this.a != null) {
                this.a.e();
            }

        }
    }

    protected abstract void a(Map<String, String> var1);
}
