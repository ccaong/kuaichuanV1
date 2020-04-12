package com.bestgo.adsplugin.ads;

public class AdType {
    public static final int ADMOB_BANNER = 1;
    public static final int ADMOB_NATIVE = 2;
    public static final int ADMOB_NATIVE_EN = 3;
    public static final int ADMOB_FULL = 4;
    public static final int FACEBOOK_BANNER = 5;
    public static final int FACEBOOK_NATIVE = 6;
    public static final int FACEBOOK_FULL = 7;
    public static final int FACEBOOK_FBN = 8;
    public static final int FACEBOOK_FBN_BANNER = 9;
    public static final int FACEBOOK_GALLERY_NATIVE = 10;
    public static final int NEWS_AD = 11;
    public static final int RECOMMEND_AD_NATIVE = 12;
    public static final int RECOMMEND_AD = 13;
    public static final int FACEBOOK_SPLASH_AD = 14;
    public static final int ADMOB_NATIVE_AN = 15;
    public static final int ADMOB_VIDEO_AD = 16;
    public static final int ADMOB_SPLASH_AD = 17;


    private int type;
    public AdType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
