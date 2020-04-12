// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.rewarded_video;

public enum c
{
    UNKNOWN(0),//a
    IS_VIEWABLE(1),//b
    AD_IS_NULL(2),//c
    INVALID_PARENT(3),//d
    INVALID_WINDOW(4),//e
    AD_IS_NOT_VISIBLE(5),//f
    INVALID_DIMENSIONS(6),//g
    AD_IS_TRANSPARENT(7),//h
    AD_IS_OBSTRUCTED(8),//i
    AD_OFFSCREEN_HORIZONTALLY(9),//j
    AD_OFFSCREEN_TOP(10),//k
    AD_OFFSCREEN_BOTTOM(11),//l
    SCREEN_NOT_INTERACTIVE(12),//m
    AD_INSUFFICIENT_VISIBLE_AREA(13),//n
    AD_VIEWABILITY_TICK_DURATION(14),//o
    AD_IS_OBSTRUCTED_BY_KEYGUARD(15),//p
    AD_IN_LOCKSCREEN(16);//q
    
    private final int r;
    
    private c(final int r) {
        this.r = r;
    }
    
    public int a() {
        return this.r;
    }
}
