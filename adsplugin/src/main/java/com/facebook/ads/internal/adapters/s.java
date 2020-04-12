// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.adapters;

import android.support.v4.content.LocalBroadcastManager;
import android.content.IntentFilter;
import com.facebook.ads.AdError;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;

public class s extends BroadcastReceiver
{
    private String a;
    private Context b;
    private InterstitialAdapterListener c;
    private InterstitialAdapter d;
    
    public s(final Context b, final String a, final InterstitialAdapter d, final InterstitialAdapterListener c) {
        this.b = b;
        this.a = a;
        this.c = c;
        this.d = d;
    }
    
    public void onReceive(final Context context, final Intent intent) {
        final String s = intent.getAction().split(":")[0];
        if (this.c == null || s == null) {
            return;
        }
        if ("com.facebook.ads.interstitial.clicked".equals(s)) {
            this.c.onInterstitialAdClicked(this.d, null, true);
        }
        else if ("com.facebook.ads.interstitial.dismissed".equals(s)) {
            this.c.onInterstitialAdDismissed(this.d);
        }
        else if ("com.facebook.ads.interstitial.displayed".equals(s)) {
            this.c.onInterstitialAdDisplayed(this.d);
        }
        else if ("com.facebook.ads.interstitial.impression.logged".equals(s)) {
            this.c.onInterstitialLoggingImpression(this.d);
        }
        else if ("com.facebook.ads.interstitial.error".equals(s)) {
            this.c.onInterstitialError(this.d, AdError.INTERNAL_ERROR);
        }
    }
    
    public void a() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.facebook.ads.interstitial.impression.logged:" + this.a);
        intentFilter.addAction("com.facebook.ads.interstitial.displayed:" + this.a);
        intentFilter.addAction("com.facebook.ads.interstitial.dismissed:" + this.a);
        intentFilter.addAction("com.facebook.ads.interstitial.clicked:" + this.a);
        intentFilter.addAction("com.facebook.ads.interstitial.error:" + this.a);
        LocalBroadcastManager.getInstance(this.b).registerReceiver((BroadcastReceiver)this, intentFilter);
    }
    
    public void b() {
        try {
            LocalBroadcastManager.getInstance(this.b).unregisterReceiver((BroadcastReceiver)this);
        }
        catch (Exception ex) {}
    }
}
