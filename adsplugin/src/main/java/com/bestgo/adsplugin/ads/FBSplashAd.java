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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bestgo.adsplugin.R;
import com.bestgo.adsplugin.ads.cache.FBCache;
import com.bestgo.adsplugin.utils.BitmapBlur;
import com.bestgo.adsplugin.utils.ColorArt;
import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.internal.server.AdPlacementType;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

public class FBSplashAd {
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

    public FBSplashAd(Context context) {
        mContext = context;
        initView();

        mHandler = new Handler(Looper.getMainLooper()) {
        };
    }

    public void resetId() {
        AdConfig config = AdAppHelper.getInstance(mContext).getConfig();
        enableNative = config.splash_ctrl.exe == 1;
        mNativeId = config.splash_ctrl.fb;
    }

    public void setAdListener(AdStateListener listener) {
        this.mAdListener = listener;
    }

    public boolean isLoaded() {
        return nativeLoaded;
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View adView = inflater.inflate(R.layout.adsplugin_splash_native_layout, mNativeAdView, false);
        int height = (int)(mContext.getResources().getDisplayMetrics().heightPixels * 0.75f);
        mNativeAdView = new MyFrameLayout(mContext);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height, Gravity.CENTER);
        mNativeAdView.addView(adView, params);
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

        if (mNativeAd != null) {
            mNativeAd.destroy();
        }
        mNativeAd = new NativeAd(mContext, mNativeId);
        FBCache.CacheItem item = FBCache.getAdFromCache(AdPlacementType.NATIVE.toString(), mNativeId);
        if (item != null) {
            FBCache.releaseObtain(item.requestId);
            mNativeAd.setUseCache(true);
        }

        mNativeAd.setAdListener(new AdListener() {
            private String requestId = null;
            @Override
            public void onError(Ad ad, AdError error) {
                nativeRequest = false;
                nativeLoaded = false;
                if (mAdListener != null) {
                    mAdListener.onAdLoadFailed(new AdType(AdType.FACEBOOK_SPLASH_AD), 0, error.getErrorMessage());
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                requestId = ad.getRequestId();
                AdAppHelper.getInstance(mContext).getFireBase().logEvent(Const.CATEGORY_AD_POSISTION, mNativeId, Const.ACTION_LOAD);
                nativeRequest = false;
                if (mNativeAd != null) {
                    mNativeAd.unregisterView();
                }

                // Create native UI using the ad metadata.
                final View root = mNativeAdView.findViewById(R.id.ads_plugin_native_ad_unit);
                ImageView nativeAdIcon = (ImageView) mNativeAdView.findViewById(R.id.ads_plugin_native_ad_icon);
                final TextView nativeAdTitle = (TextView) mNativeAdView.findViewById(R.id.ads_plugin_native_ad_title);
                ImageView nativeAdMedia = (ImageView) mNativeAdView.findViewById(R.id.ads_plugin_native_ad_media);
                final TextView nativeAdCallToAction = (TextView) mNativeAdView.findViewById(R.id.ads_plugin_native_ad_call_to_action);
                // Register the Title and CTA button to listen for clicks.
                List<View> clickableViews = new ArrayList<>();
                clickableViews.add(nativeAdTitle);
                clickableViews.add(nativeAdMedia);
                clickableViews.add(nativeAdCallToAction);
                mNativeAd.registerViewForInteraction(mNativeAdView, clickableViews);

                // Set the Text.
                if (nativeAdTitle != null) {
                    nativeAdTitle.setText(mNativeAd.getAdTitle());
                }
                if (nativeAdCallToAction != null) {
                    nativeAdCallToAction.setText(mNativeAd.getAdCallToAction());
                }

                // Download and display the ad icon.
                NativeAd.Image adIcon = mNativeAd.getAdIcon();
                if (nativeAdIcon != null && adIcon != null) {
                    NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);
                }

                // Download and display the cover image.
                if (nativeAdMedia != null) {
                    ImageLoader imageLoader = ImageLoader.getInstance();
                    imageLoader.displayImage(mNativeAd.getAdCoverImage().getUrl(), nativeAdMedia, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {
                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {
                        }

                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                            disposePicture(bitmap, nativeAdCallToAction, nativeAdTitle, root);
                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {
                        }
                    });
                }

                // Add the AdChoices icon
                LinearLayout adChoicesContainer = (LinearLayout) mNativeAdView.findViewById(R.id.ads_plugin_ad_choices_container);
                if (adChoicesContainer != null) {
                    adChoicesContainer.removeAllViews();
                    AdChoicesView adChoicesView = new AdChoicesView(mContext, mNativeAd, true);
                    adChoicesContainer.addView(adChoicesView);
                }
            }

            @Override
            public void onAdClicked(Ad ad) {
                AdAppHelper.getInstance(mContext).getFireBase().logEvent(Const.CATEGORY_AD_POSISTION, mNativeId, Const.ACTION_CLICK);
                nativeLoaded = false;
                if (mAdListener != null) {
                    mAdListener.onAdClick(new AdType(AdType.FACEBOOK_SPLASH_AD), 0);
                }
                if (requestId != null) {
                    FBCache.deleteFromCache(requestId, AdPlacementType.NATIVE.toString(), mNativeId);
                }
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                String requestId = ad.getRequestId();
                FBCache.releaseObtain(requestId);
            }
        });

        mNativeAd.loadAd();
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
}
