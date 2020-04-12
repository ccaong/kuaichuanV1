package com.bestgo.adsplugin.ads;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bestgo.adsplugin.R;
import com.bestgo.adsplugin.utils.BitmapBlur;
import com.bestgo.adsplugin.utils.ColorArt;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;

import java.util.List;

public class AdMobSplashAd {
    private Context mContext;
    private boolean enableNative;
    private String mNativeId = "";
    private boolean nativeLoaded;
    private boolean nativeRequest;
    private NativeAd mNativeAd;
    private MyFrameLayout mNativeAdView;
    private long lastRequestNativeTime;
    private AdStateListener mAdListener;
    private boolean isWhite;
    private Handler mHandler;

    public AdMobSplashAd(Context context) {
        mContext = context;
        initView();

        mHandler = new Handler(Looper.getMainLooper()) {
        };
    }

    public void resetId() {
        AdConfig config = AdAppHelper.getInstance(mContext).getConfig();
        enableNative = config.splash_ctrl.exe == 1;
        mNativeId = config.splash_ctrl.admob;
    }

    public void setAdListener(AdStateListener listener) {
        this.mAdListener = listener;
    }

    public boolean isLoaded() {
        return nativeLoaded;
    }

    private void initView() {
        mNativeAdView = new MyFrameLayout(mContext);
    }

    public View getNativeView() {
        return mNativeAdView;
    }

    public void loadNewNativeAd() {
        if (TextUtils.isEmpty(mNativeId)) return;
        if (nativeLoaded) return;
        if (nativeRequest) return;
        if (!enableNative) return;

        nativeRequest = true;
        lastRequestNativeTime = System.currentTimeMillis();

        AdLoader.Builder builder = new AdLoader.Builder(mContext, mNativeId);

        builder.forContentAd(new NativeContentAd.OnContentAdLoadedListener() {
            @Override
            public void onContentAdLoaded(NativeContentAd ad) {
                NativeContentAdView adView  = (NativeContentAdView)LayoutInflater.from(mContext).inflate(R.layout.adsplugin_splash_native_layout_admob, mNativeAdView, false);
                nativeRequest = false;
                try {
                    populateContentAdView(ad, adView);
                    AdAppHelper.getInstance(mContext).getFireBase().logEvent(Const.CATEGORY_AD_POSISTION, mNativeId, Const.ACTION_LOAD);
                    if (mAdListener != null) {
                        mAdListener.onAdLoaded(new AdType(AdType.ADMOB_SPLASH_AD), 0);
                    }
                    int height = (int)(mContext.getResources().getDisplayMetrics().heightPixels * 0.75f);
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height, Gravity.CENTER);
                    mNativeAdView.removeAllViews();
                    mNativeAdView.addView(adView, params);
                } catch (Exception ex) {
                    AdAppHelper.getInstance(mContext).getFireBase().logEvent(Const.CATEGORY_AD, "错误", ex.getMessage());
                }
            }
        });

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(false)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setImageOrientation(NativeAdOptions.ORIENTATION_LANDSCAPE)
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdLeftApplication() {
                nativeLoaded = false;
                AdAppHelper.getInstance(mContext).getFireBase().logEvent(Const.CATEGORY_AD_POSISTION, mNativeId, Const.ACTION_CLICK);
                if (mAdListener != null) {
                    mAdListener.onAdClick(new AdType(AdType.ADMOB_SPLASH_AD), 0);
                }
            }

            @Override
            public void onAdOpened() {
                if (mAdListener != null) {
                    mAdListener.onAdOpen(new AdType(AdType.ADMOB_SPLASH_AD), 0);
                }
                AdAppHelper.getInstance(mContext).getFireBase().logEvent(Const.CATEGORY_AD_POSISTION, mNativeId, Const.ACTION_SHOW_NATIVE);
                AdAppHelper.getInstance(mContext).getFacebook().logEvent(Const.CATEGORY_FB_AD_POSISTION, mNativeId, Const.ACTION_SHOW_NATIVE);
            }

            @Override
            public void onAdFailedToLoad(int i) {
                nativeLoaded = false;
                nativeRequest = false;
                if (mAdListener != null) {
                    mAdListener.onAdLoadFailed(new AdType(AdType.ADMOB_SPLASH_AD), 0, getAdLoadError(i));
                }
            }

            @Override
            public void onAdLoaded() {

            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());
        AdAppHelper.getInstance(mContext).getFireBase().logEvent(Const.CATEGORY_AD_POSISTION, mNativeId, Const.ACTION_REQUEST);
    }

    private void disposePicture(final Bitmap bitmap, final TextView nativeAdCallToAction, final TextView nativeAdTitle, final View root) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap1 = BitmapBlur.fastBlur(bitmap, 10);
                ColorArt colorArt = new ColorArt(bitmap1);
                final int bgColor = colorArt.getBackgroundColor();

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        int r = (bgColor & 0x00FF0000) >> 16;
                        int g = (bgColor & 0x0000FF00) >> 8;
                        int b = (bgColor & 0x000000FF);
                        if (r >= 200 && g >= 200 && b >= 200) {
                            nativeAdTitle.setTextColor(Color.BLACK);
                            nativeAdCallToAction.setTextColor(Color.BLACK);
                        } else {
                            nativeAdTitle.setTextColor(Color.WHITE);
                            nativeAdCallToAction.setTextColor(Color.WHITE);
                        }

                        root.setBackgroundDrawable(new BitmapDrawable(bitmap1));
                        nativeLoaded = true;

                        if (mAdListener != null) {
                            mAdListener.onAdLoaded(new AdType(AdType.FACEBOOK_SPLASH_AD), 0);
                        }
                    }
                });
            }
        }).start();
    }

    private void populateContentAdView(NativeContentAd nativeContentAd,
                                       final NativeContentAdView adView) {

        adView.setHeadlineView(adView.findViewById(R.id.ads_plugin_native_ad_title));
        ImageView mainImageView = (ImageView)adView.findViewById(R.id.ads_plugin_native_main_image);
        adView.setImageView(mainImageView);
        adView.setCallToActionView(adView.findViewById(R.id.ads_plugin_native_ad_call_to_action));

        // Some assets are guaranteed to be in every NativeContentAd.
        ((TextView) adView.getHeadlineView()).setText(nativeContentAd.getHeadline());
        ((TextView) adView.getCallToActionView()).setText(nativeContentAd.getCallToAction());

        List<com.google.android.gms.ads.formats.NativeAd.Image> images = nativeContentAd.getImages();

        if (images.size() > 0 && mainImageView != null) {
            mainImageView.setImageDrawable(images.get(0).getDrawable());
            if (images.get(0).getDrawable() instanceof BitmapDrawable) {
                BitmapDrawable drawable = (BitmapDrawable)images.get(0).getDrawable();
                disposePicture(drawable.getBitmap(), (TextView) adView.getCallToActionView(), (TextView) adView.getHeadlineView(), adView);
            }
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeContentAd);
    }

    private String getAdLoadError(int code) {
        String reason = "ERROR_CODE_INTERNAL_ERROR";
        switch (code) {
            case AdRequest.ERROR_CODE_INTERNAL_ERROR:
                reason = "ERROR_CODE_INTERNAL_ERROR";
                break;
            case AdRequest.ERROR_CODE_INVALID_REQUEST:
                reason = "ERROR_CODE_INVALID_REQUEST";
                break;
            case AdRequest.ERROR_CODE_NETWORK_ERROR:
                reason = "ERROR_CODE_NETWORK_ERROR";
                break;
            case AdRequest.ERROR_CODE_NO_FILL:
                reason = "ERROR_CODE_NO_FILL";
                break;
        }
        return reason;
    }
}
