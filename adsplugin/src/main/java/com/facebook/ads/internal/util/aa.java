// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.util;

import android.os.Handler;
import android.database.ContentObserver;

class aa extends ContentObserver
{
    private final ah a;
    
    public aa(final Handler handler, final ah a) {
        super(handler);
        this.a = a;
    }
    
    public boolean deliverSelfNotifications() {
        return false;
    }
    
    public void onChange(final boolean b) {
        this.a.e();
    }
}
