package com.facebook.ads.internal.adapters;

import com.facebook.ads.internal.gpackage.f;
import java.util.Map;
import android.content.Context;
import com.facebook.ads.internal.server.AdPlacementType;

public abstract class InterstitialAdapter implements AdAdapter
{
    @Override
    public final AdPlacementType getPlacementType() {
        return AdPlacementType.INTERSTITIAL;
    }
    
    public abstract void loadInterstitialAd(final Context p0, final InterstitialAdapterListener p1, final Map<String, Object> p2, final f p3);
    
    public abstract boolean show();
}
