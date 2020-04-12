package com.facebook.ads.internal.adapters;

import java.util.Map;
import com.facebook.ads.AdSize;
import android.content.Context;
import com.facebook.ads.internal.server.AdPlacementType;

public abstract class BannerAdapter implements AdAdapter
{
    @Override
    public final AdPlacementType getPlacementType() {
        return AdPlacementType.BANNER;
    }
    
    public abstract void loadBannerAd(final Context p0, final AdSize p1, final BannerAdapterListener p2, final Map<String, Object> p3);
}
