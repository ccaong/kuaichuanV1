// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.adapters;

import android.content.IntentFilter;
import com.facebook.ads.AdError;
import com.facebook.ads.internal.j;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;

public class z extends BroadcastReceiver
{
    private String a;
    private y b;
    private x c;
    
    public z(final String a, final x c, final y b) {
        this.c = c;
        this.b = b;
        this.a = a;
    }
    
    public void onReceive(final Context context, final Intent intent) {
        final String action = intent.getAction();
        if (j.REWARDED_VIDEO_COMPLETE.a(this.a).equals(action)) {
            this.b.d(this.c);
        }
        else if (j.REWARDED_VIDEO_ERROR.a(this.a).equals(action)) {
            this.b.a(this.c, AdError.INTERNAL_ERROR);
        }
        else if (j.REWARDED_VIDEO_AD_CLICK.a(this.a).equals(action)) {
            this.b.b(this.c);
        }
        else if (j.REWARDED_VIDEO_IMPRESSION.a(this.a).equals(action)) {
            this.b.c(this.c);
        }
        else if (j.REWARDED_VIDEO_CLOSED.a(this.a).equals(action)) {
            this.b.a();
        }
        else if (j.REWARD_SERVER_FAILED.a(this.a).equals(action)) {
            this.b.e(this.c);
        }
        else if (j.REWARD_SERVER_SUCCESS.a(this.a).equals(action)) {
            this.b.f(this.c);
        }
    }
    
    public IntentFilter a() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(j.REWARDED_VIDEO_COMPLETE.a(this.a));
        intentFilter.addAction(j.REWARDED_VIDEO_ERROR.a(this.a));
        intentFilter.addAction(j.REWARDED_VIDEO_AD_CLICK.a(this.a));
        intentFilter.addAction(j.REWARDED_VIDEO_IMPRESSION.a(this.a));
        intentFilter.addAction(j.REWARDED_VIDEO_CLOSED.a(this.a));
        intentFilter.addAction(j.REWARD_SERVER_SUCCESS.a(this.a));
        intentFilter.addAction(j.REWARD_SERVER_FAILED.a(this.a));
        return intentFilter;
    }
}
