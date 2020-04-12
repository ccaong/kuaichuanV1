// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads;

import com.facebook.ads.internal.view.a;
import android.view.View;
import android.content.Context;

public class NativeAdView
{
    public static View render(final Context context, final NativeAd nativeAd, final Type type) {
        return render(context, nativeAd, type, null);
    }
    
    public static View render(final Context context, final NativeAd nativeAd, final Type type, NativeAdViewAttributes adViewAttributes) {
        if (nativeAd.isNativeConfigEnabled()) {
            adViewAttributes = nativeAd.getAdViewAttributes();
        }
        else if (adViewAttributes == null) {
            adViewAttributes = new NativeAdViewAttributes();
        }
        nativeAd.a(type);
        return (View)new a(context, nativeAd, type, adViewAttributes);
    }
    
    public enum Type
    {
        HEIGHT_100(-1, 100), 
        HEIGHT_120(-1, 120), 
        HEIGHT_300(-1, 300), 
        HEIGHT_400(-1, 400);
        
        private final int a;
        private final int b;
        
        private Type(final int a, final int b) {
            this.a = a;
            this.b = b;
        }
        
        public int getWidth() {
            return this.a;
        }
        
        public int getHeight() {
            return this.b;
        }
        
        public int getValue() {
            switch (this.b) {
                case 100: {
                    return 1;
                }
                case 120: {
                    return 2;
                }
                case 300: {
                    return 3;
                }
                case 400: {
                    return 4;
                }
                default: {
                    return -1;
                }
            }
        }
    }
}
