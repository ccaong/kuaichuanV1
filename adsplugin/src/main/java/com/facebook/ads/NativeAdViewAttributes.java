// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads;

import com.facebook.ads.internal.util.c;
import com.facebook.ads.internal.util.b;
import android.graphics.Color;
import org.json.JSONObject;
import android.graphics.Typeface;

public class NativeAdViewAttributes
{
    private Typeface a;
    private int b;
    private int c;
    private int d;
    private int e;
    private int f;
    private int g;
    private boolean h;
    private boolean i;
    
    public NativeAdViewAttributes() {
        this.a = Typeface.DEFAULT;
        this.b = -1;
        this.c = -16777216;
        this.d = -11643291;
        this.e = 0;
        this.f = -12420889;
        this.g = -12420889;
        this.h = AdSettings.isVideoAutoplay();
        this.i = AdSettings.isVideoAutoplayOnMobile();
    }
    
    public NativeAdViewAttributes(final JSONObject jsonObject) {
        this.a = Typeface.DEFAULT;
        this.b = -1;
        this.c = -16777216;
        this.d = -11643291;
        this.e = 0;
        this.f = -12420889;
        this.g = -12420889;
        this.h = AdSettings.isVideoAutoplay();
        this.i = AdSettings.isVideoAutoplayOnMobile();
        try {
            final int b = jsonObject.getBoolean("background_transparent") ? 0 : Color.parseColor(jsonObject.getString("background_color"));
            final int color = Color.parseColor(jsonObject.getString("title_text_color"));
            final int color2 = Color.parseColor(jsonObject.getString("description_text_color"));
            final int e = jsonObject.getBoolean("button_transparent") ? 0 : Color.parseColor(jsonObject.getString("button_color"));
            final int g = jsonObject.getBoolean("button_border_transparent") ? 0 : Color.parseColor(jsonObject.getString("button_border_color"));
            final int color3 = Color.parseColor(jsonObject.getString("button_text_color"));
            final Typeface create = Typeface.create(jsonObject.getString("android_typeface"), 0);
            this.b = b;
            this.c = color;
            this.d = color2;
            this.e = e;
            this.g = g;
            this.f = color3;
            this.a = create;
        }
        catch (Exception ex) {
            com.facebook.ads.internal.util.c.a(com.facebook.ads.internal.util.b.a(ex, "Error retrieving native ui configuration data"));
        }
    }
    
    public NativeAdViewAttributes setTypeface(final Typeface a) {
        this.a = a;
        return this;
    }
    
    public Typeface getTypeface() {
        return this.a;
    }
    
    public NativeAdViewAttributes setBackgroundColor(final int b) {
        this.b = b;
        return this;
    }
    
    public int getBackgroundColor() {
        return this.b;
    }
    
    public NativeAdViewAttributes setTitleTextColor(final int c) {
        this.c = c;
        return this;
    }
    
    public int getTitleTextColor() {
        return this.c;
    }
    
    public NativeAdViewAttributes setDescriptionTextColor(final int d) {
        this.d = d;
        return this;
    }
    
    public int getDescriptionTextColor() {
        return this.d;
    }
    
    public NativeAdViewAttributes setButtonColor(final int e) {
        this.e = e;
        return this;
    }
    
    public int getButtonColor() {
        return this.e;
    }
    
    public NativeAdViewAttributes setButtonTextColor(final int f) {
        this.f = f;
        return this;
    }
    
    public int getButtonTextColor() {
        return this.f;
    }
    
    public NativeAdViewAttributes setButtonBorderColor(final int g) {
        this.g = g;
        return this;
    }
    
    public int getButtonBorderColor() {
        return this.g;
    }
    
    public int getTitleTextSize() {
        return 16;
    }
    
    public int getDescriptionTextSize() {
        return 10;
    }
    
    public boolean getAutoplay() {
        return this.h;
    }
    
    public boolean getAutoplayOnMobile() {
        return AdSettings.isVideoAutoplayOnMobile();
    }
    
    public NativeAdViewAttributes setAutoplayOnMobile(final boolean i) {
        this.i = i;
        return this;
    }
    
    public NativeAdViewAttributes setAutoplay(final boolean h) {
        this.h = h;
        return this;
    }
}
