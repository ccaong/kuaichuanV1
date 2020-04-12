// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.adapters;

import java.util.Map;
import android.content.Context;
import com.facebook.ads.internal.server.AdPlacementType;
import com.facebook.ads.RewardData;

public abstract class x implements AdAdapter
{
    protected RewardData a;
    
    @Override
    public AdPlacementType getPlacementType() {
        return AdPlacementType.REWARDED_VIDEO;
    }
    
    public void a(final RewardData a) {
        this.a = a;
    }
    
    public abstract void a(final Context p0, final y p1, final Map<String, Object> p2);
    
    public abstract boolean b();
}
