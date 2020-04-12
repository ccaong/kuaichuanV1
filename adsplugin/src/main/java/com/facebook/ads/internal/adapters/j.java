// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.adapters;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Iterator;
import android.view.WindowManager;
import android.content.ActivityNotFoundException;
import com.facebook.ads.InterstitialAdActivity;
import java.io.Serializable;
import android.content.Intent;
import com.facebook.ads.AudienceNetworkActivity;
import android.view.View;
import com.facebook.ads.a.a;
import com.facebook.ads.AdError;
import org.json.JSONObject;
import com.facebook.ads.internal.gpackage.f;
import java.util.Map;
import java.util.UUID;
import android.content.Context;
import com.facebook.ads.internal.view.d;
import java.util.concurrent.ConcurrentMap;

public class j extends InterstitialAdapter
{
    private static final ConcurrentMap<String, d> concurrentMap;
    private final String b;
    private Context c;
    private s d;
    private InterstitialAdapterListener e;
    private boolean f;
    private o g;
    private a h;
    
    public j() {
        this.b = UUID.randomUUID().toString();
        this.f = false;
    }
    
    @Override
    public void loadInterstitialAd(final Context c, final InterstitialAdapterListener e, final Map<String, Object> map, final f f) {
        this.c = c;
        this.e = e;
        final JSONObject jsonObject = (JSONObject)map.get("data");
        if (jsonObject.has("markup")) {
            this.g = o.a(jsonObject);
            if (com.facebook.ads.internal.util.f.a(c, this.g)) {
                e.onInterstitialError(this, AdError.NO_FILL);
                return;
            }
            (this.d = new s(c, this.b, this, this.e)).a();
            final Map<String, String> c2 = this.g.c();
            if (c2.containsKey("orientation")) {
                this.h = j.a.a(Integer.parseInt(c2.get("orientation")));
            }
            this.f = true;
            if (this.e != null) {
                this.e.onInterstitialAdLoaded(this);
            }
        }
        else {
            (this.d = new s(c, this.b, this, this.e)).a();
            final k k = new k();
            k.a(c, new com.facebook.ads.a.a() {
                @Override
                public void a(final r r) {
                    j.this.f = true;
                    if (j.this.e == null) {
                        return;
                    }
                    j.this.e.onInterstitialAdLoaded(j.this);
                }
                
                @Override
                public void a(final r r, final View view) {
                    j.this.h = k.i();
                    j.concurrentMap.put(j.this.b, k);
                }
                
                @Override
                public void a(final r r, final AdError adError) {
                    k.j();
                    j.this.e.onInterstitialError(j.this, adError);
                }
                
                @Override
                public void b(final r r) {
                    j.this.e.onInterstitialAdClicked(j.this, "", true);
                }
                
                @Override
                public void c(final r r) {
                    j.this.e.onInterstitialLoggingImpression(j.this);
                }
                
                @Override
                public void d(final r r) {
                }
            }, map, f);
        }
    }
    
    @Override
    public void onDestroy() {
        if (this.d != null) {
            this.d.b();
        }
    }
    
    @Override
    public boolean show() {
        if (!this.f) {
            if (this.e != null) {
                this.e.onInterstitialError(this, AdError.INTERNAL_ERROR);
            }
            return false;
        }
        final Intent intent = new Intent(this.c, (Class)AudienceNetworkActivity.class);
        intent.putExtra("predefinedOrientationKey", this.b());
        intent.putExtra("uniqueId", this.b);
        if (j.concurrentMap.containsKey(this.b)) {
            intent.putExtra("viewType", (Serializable)AudienceNetworkActivity.Type.NATIVE);
        }
        else {
            intent.putExtra("viewType", (Serializable)AudienceNetworkActivity.Type.DISPLAY);
            this.g.a(intent);
        }
        intent.addFlags(268435456);
        try {
            this.c.startActivity(intent);
        }
        catch (ActivityNotFoundException ex) {
            intent.setClass(this.c, (Class)InterstitialAdActivity.class);
            this.c.startActivity(intent);
        }
        return true;
    }
    
    private int b() {
        final int rotation = ((WindowManager)this.c.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        if (this.h == j.a.UNSPECIFIED) {
            return -1;
        }
        if (this.h == a.HORIZONTAL) {
            switch (rotation) {
                case 2:
                case 3: {
                    return 8;
                }
                default: {
                    return 0;
                }
            }
        }
        else {
            switch (rotation) {
                case 2: {
                    return 9;
                }
                default: {
                    return 1;
                }
            }
        }
    }
    
    public static d a(final String s) {
        return j.concurrentMap.get(s);
    }
    
    public static void a(final d d) {
        for (final Map.Entry<String, d> entry : j.concurrentMap.entrySet()) {
            if (entry.getValue() == d) {
                j.concurrentMap.remove(entry.getKey());
            }
        }
    }
    
    static {
        concurrentMap = new ConcurrentHashMap<String, d>();
    }
    
    public enum a
    {
        UNSPECIFIED,
        VERTICAL,
        HORIZONTAL;
        
        public static a a(final int n) {
            if (n == 0) {
                return a.UNSPECIFIED;
            }
            if (n == 2) {
                return a.HORIZONTAL;
            }
            return a.VERTICAL;
        }
    }
}
