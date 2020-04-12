package com.facebook.ads.internal.adapters;

import android.support.v4.content.LocalBroadcastManager;
import android.content.IntentFilter;
import java.io.Serializable;
import com.facebook.ads.internal.view.dpackage.a.h;
import com.facebook.ads.internal.view.dpackage.a.j;
import com.facebook.ads.internal.view.dpackage.a.b;
import com.facebook.ads.internal.view.dpackage.a.g;
import com.facebook.ads.internal.view.dpackage.a.f;
import com.facebook.ads.internal.view.dpackage.a.p;
import android.content.Intent;
import com.facebook.ads.internal.view.i;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.view.View;

public class aa extends BroadcastReceiver
{
    private Context a;
    private i b;
    private boolean c;
    
    public aa(final i b, final Context a) {
        this.c = false;
        this.b = b;
        this.a = a;
    }
    
    public void onReceive(final Context context, final Intent intent) {
        final String[] split = intent.getAction().split(":");
        if (split.length != 2) {
            return;
        }
        if (!split[1].equals(this.b.getUniqueId())) {
            return;
        }
        if (split[0].equals("com.facebook.ads.interstitial.displayed")) {
            if (this.b.getListener() != null) {
                this.b.getListener().g();
                this.b.getListener().a();
            }
        }
        else if (split[0].equals("videoInterstitalEvent")) {
            final Serializable serializableExtra = intent.getSerializableExtra("event");
            if (serializableExtra instanceof p) {
                if (this.b.getListener() != null) {
                    this.b.getListener().f();
                    this.b.getListener().a();
                }
                if (this.c) {
                    this.b.a(1);
                }
                else {
                    this.b.a(((p)serializableExtra).b());
                }
                this.b.setVisibility(View.VISIBLE);
                this.b.d();
            }
            else if (serializableExtra instanceof f) {
                if (this.b.getListener() != null) {
                    this.b.getListener().d();
                }
            }
            else if (serializableExtra instanceof g) {
                if (this.b.getListener() != null) {
                    this.b.getListener().e();
                }
            }
            else if (serializableExtra instanceof b) {
                if (this.b.getListener() != null) {
                    this.b.getListener().h();
                }
                this.c = true;
            }
            else if (serializableExtra instanceof j) {
                if (this.b.getListener() != null) {
                    this.b.getListener().c();
                }
                this.c = false;
            }
            else if (serializableExtra instanceof h && this.b.getListener() != null) {
                this.b.getListener().b();
            }
        }
    }
    
    public void a() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.facebook.ads.interstitial.displayed:" + this.b.getUniqueId());
        intentFilter.addAction("videoInterstitalEvent:" + this.b.getUniqueId());
        LocalBroadcastManager.getInstance(this.a).registerReceiver((BroadcastReceiver)this, intentFilter);
    }
    
    public void b() {
        try {
            LocalBroadcastManager.getInstance(this.a).unregisterReceiver((BroadcastReceiver)this);
        }
        catch (Exception ex) {}
    }
}
