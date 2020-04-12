// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view.dpackage.c;

import android.view.View;
import android.net.Uri;

public interface c
{
    void setup(final Uri p0);
    
    int getCurrentPosition();
    
    void start();
    
    void pause();
    
    void b();
    
    void seekTo(final int p0);
    
    void setVideoStateChangeListener(final e p0);
    
    void a();
    
    long getInitialBufferTime();
    
    int getDuration();
    
    d getTargetState();
    
    d getState();
    
    void setRequestedVolume(final float p0);
    
    void setVideoMPD(final String p0);
    
    void setFullScreen(final boolean p0);
    
    void setControlsAnchorView(final View p0);
    
    View getView();
    
    float getVolume();
    
    void a(final boolean p0);
    
    int getVideoHeight();
    
    int getVideoWidth();
}
