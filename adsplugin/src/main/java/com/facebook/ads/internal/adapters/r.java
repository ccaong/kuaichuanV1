// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.adapters;

import com.facebook.ads.internal.gpackage.f;
import java.util.Map;
import com.facebook.ads.a.a;
import android.content.Context;
import com.facebook.ads.internal.server.AdPlacementType;
import android.os.Bundle;
import com.facebook.ads.internal.util.ad;

public abstract class r implements AdAdapter, ad<Bundle>
{
    @Override
    public AdPlacementType getPlacementType() {
        return AdPlacementType.INSTREAM;
    }
    
    public abstract boolean d();
    
    public abstract void a(final Context p0, final a p1, final Map<String, Object> p2, final f p3);
}
