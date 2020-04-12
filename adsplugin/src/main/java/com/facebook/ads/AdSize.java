// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads;

import java.io.Serializable;

public class AdSize implements Serializable
{
    @Deprecated
    public static final AdSize BANNER_320_50;
    public static final AdSize INTERSTITIAL;
    public static final AdSize BANNER_HEIGHT_50;
    public static final AdSize BANNER_HEIGHT_90;
    public static final AdSize RECTANGLE_HEIGHT_250;
    private final int a;
    private final int b;
    
    public AdSize(final int a, final int b) {
        this.a = a;
        this.b = b;
    }
    
    public int getWidth() {
        return this.a;
    }
    
    public int getHeight() {
        return this.b;
    }
    
    public static AdSize fromWidthAndHeight(final int n, final int n2) {
        if (AdSize.INTERSTITIAL.b == n2 && AdSize.INTERSTITIAL.a == n) {
            return AdSize.INTERSTITIAL;
        }
        if (AdSize.BANNER_320_50.b == n2 && AdSize.BANNER_320_50.a == n) {
            return AdSize.BANNER_320_50;
        }
        if (AdSize.BANNER_HEIGHT_50.b == n2 && AdSize.BANNER_HEIGHT_50.a == n) {
            return AdSize.BANNER_HEIGHT_50;
        }
        if (AdSize.BANNER_HEIGHT_90.b == n2 && AdSize.BANNER_HEIGHT_90.a == n) {
            return AdSize.BANNER_HEIGHT_90;
        }
        if (AdSize.RECTANGLE_HEIGHT_250.b == n2 && AdSize.RECTANGLE_HEIGHT_250.a == n) {
            return AdSize.RECTANGLE_HEIGHT_250;
        }
        return null;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final AdSize adSize = (AdSize)o;
        return this.a == adSize.a && this.b == adSize.b;
    }
    
    @Override
    public int hashCode() {
        return 31 * this.a + this.b;
    }
    
    static {
        BANNER_320_50 = new AdSize(320, 50);
        INTERSTITIAL = new AdSize(0, 0);
        BANNER_HEIGHT_50 = new AdSize(-1, 50);
        BANNER_HEIGHT_90 = new AdSize(-1, 90);
        RECTANGLE_HEIGHT_250 = new AdSize(-1, 250);
    }
}
