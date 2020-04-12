package com.facebook.ads;

public interface Ad
{
    void loadAd();
    
    void destroy();
    
    String getPlacementId();

    String getRequestId();

    void setUseCache(boolean use);
}
