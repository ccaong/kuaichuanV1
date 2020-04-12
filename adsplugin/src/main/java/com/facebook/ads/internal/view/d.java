// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view;

import android.view.View;
import com.facebook.ads.internal.gpackage.q;
import com.facebook.ads.AudienceNetworkActivity;
import android.os.Bundle;
import android.content.Intent;

public interface d
{
    void a(final Intent p0, final Bundle p1, final AudienceNetworkActivity p2);
    
    void a(final Bundle p0);
    
    void g();
    
    void h();
    
    void onDestroy();
    
    void a(final a p0);
    
    public interface a
    {
        void a(final String p0, final q p1);
        
        void a(final String p0);
        
        void a(final View p0);
    }
}
