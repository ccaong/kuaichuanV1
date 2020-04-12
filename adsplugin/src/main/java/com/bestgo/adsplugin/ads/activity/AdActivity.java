package com.bestgo.adsplugin.ads.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bestgo.adsplugin.R;
import com.bestgo.adsplugin.ads.AdAppHelper;
import com.bestgo.adsplugin.ads.AdConfig;
import com.bestgo.adsplugin.ads.AdType;
import com.bestgo.adsplugin.ads.Const;
import com.bestgo.adsplugin.ads.cache.FBCache;
import com.bestgo.adsplugin.ads.entity.AppAdUnitMetrics;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.internal.server.AdPlacementType;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AdActivity extends Activity {
    public static NativeAd mNativeAd;
    public static String AdId;
    public static int AdIndex;
    public View mCancelButton;
    public View mCloseButton;

    private long enterTime;
    private boolean isRisk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        enterTime = SystemClock.elapsedRealtime();
        try {
            initView();
        } catch (Exception ex) {
            AdAppHelper.getInstance(getApplicationContext()).getFireBase().logEvent(Const.CATEGORY_AD, "错误", ex.getMessage());
            AdAppHelper.getInstance(getApplicationContext()).showFullAd();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        if (mNativeAd != null) {
            String requestId = mNativeAd.getRequestId();
            if (requestId != null) {
                FBCache.releaseObtain(requestId);
            }
            if (mNativeAd.isUseCache()) {
                AdAppHelper.getInstance(getApplicationContext()).getFireBase().logEvent(Const.CATEGORY_AD_POSISTION, AdId, Const.ACTION_CACHE_CLOSE_FULL);
            } else {
                AdAppHelper.getInstance(getApplicationContext()).getFireBase().logEvent(Const.CATEGORY_AD_POSISTION, AdId, Const.ACTION_CLOSE_FULL);
            }
        }
        AdAppHelper.getInstance(getApplicationContext()).loadNewInterstitial(AdIndex);
        AdAppHelper.getInstance(getApplicationContext()).getInnerListener().onAdClosed(new AdType(AdType.FACEBOOK_FBN), AdIndex);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        AdConfig config = AdAppHelper.getInstance(getApplicationContext()).getConfig();
        if (config.ad_ctrl.delay_close == 1) {
            if (SystemClock.elapsedRealtime() - enterTime > 2000) {
                super.onBackPressed();
            } else {
                long t = 2000 - (SystemClock.elapsedRealtime() - enterTime);
                if (t <= 0 || t >= 2000) {
                    t = 1000;
                }
                mCloseButton.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, t);
            }
        } else {
            super.onBackPressed();
        }
    }

    private View.OnClickListener closeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private void initView() {
        setContentView(R.layout.adsplugin_native_full_ad_layout);
        View adView = findViewById(R.id.ads_plugin_adView);

        if (AdAppHelper.getInstance(getApplicationContext()).allowRisk()) {
//            mNativeAd.setIsRisk(true);
            isRisk = true;
            AdAppHelper.getInstance(getApplicationContext()).increaseRiskCount();
        }

        mNativeAd.setAdListener(new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {}
            @Override
            public void onAdLoaded(Ad ad) {}
            @Override
            public void onAdClicked(Ad ad) {
                String requestId = ad.getRequestId();
                if (requestId != null) {
                    FBCache.deleteFromCache(requestId, AdPlacementType.NATIVE.toString(), AdId);
                }
                if (mNativeAd.isUseCache()) {
                    AdAppHelper.getInstance(getApplicationContext()).getFireBase().logEvent(Const.CATEGORY_AD_POSISTION, AdId, Const.ACTION_CACHE_CLICK);
                } else {
                    AdAppHelper.getInstance(getApplicationContext()).getFireBase().logEvent(Const.CATEGORY_AD_POSISTION, AdId, Const.ACTION_CLICK);
                }
                try {
                    mCancelButton.setOnClickListener(closeListener);
                    mCloseButton.setOnClickListener(closeListener);
                } catch (Exception ex) {
                }
                AdAppHelper.getInstance(getApplicationContext()).getInnerListener().onAdClick(new AdType(AdType.FACEBOOK_FBN), AdIndex);
                if (isRisk) {
                    finish();
                }
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                String requestId = ad.getRequestId();
                AdConfig config = AdAppHelper.getInstance(getApplicationContext()).getConfig();
                if (requestId != null && config.ad_ctrl.reuse_cache != 1) {
                    FBCache.deleteFromCache(requestId, AdPlacementType.NATIVE.toString(), AdId);
                }
                if (mNativeAd.isUseCache()) {
                    AdAppHelper.getInstance(getApplicationContext()).getFireBase().logEvent(Const.CATEGORY_AD_POSISTION, AdId, Const.ACTION_CACHE_SHOW);
                    AdAppHelper.getInstance(getApplicationContext()).getFireBase().logEvent(Const.CATEGORY_AD_POSISTION, AdId, Const.ACTION_CACHE_SHOW_FULL);
                } else {
                    AdAppHelper.getInstance(getApplicationContext()).getFireBase().logEvent(Const.CATEGORY_AD_POSISTION, AdId, Const.ACTION_SHOW);
                    AdAppHelper.getInstance(getApplicationContext()).getFireBase().logEvent(Const.CATEGORY_AD_POSISTION, AdId, Const.ACTION_SHOW_FULL);
                }
                AdAppHelper.getInstance(getApplicationContext()).getInnerListener().onAdOpen(new AdType(AdType.FACEBOOK_FBN), AdIndex);
            }
        });
        mCancelButton = findViewById(R.id.ads_plugin_native_ad_cancel);
        mCancelButton.setOnClickListener(closeListener);
        mCloseButton = findViewById(R.id.ads_plugin_btn_close);
        mCloseButton.setOnClickListener(closeListener);

        // Create native UI using the ad metadata.
        ImageView nativeAdIcon = (ImageView) adView.findViewById(R.id.ads_plugin_native_ad_icon);
        TextView nativeAdTitle = (TextView) adView.findViewById(R.id.ads_plugin_native_ad_title);
        MediaView nativeAdMedia = (MediaView) adView.findViewById(R.id.ads_plugin_native_ad_media);
        TextView nativeAdBody = (TextView) adView.findViewById(R.id.ads_plugin_native_ad_body);
        TextView nativeAdCallToAction = (TextView) adView.findViewById(R.id.ads_plugin_native_ad_call_to_action);

        Random random = new Random();
        View yes = findViewById(R.id.ads_plugin_native_ad_yes);
        // Register the Title and CTA button to listen for clicks.
        List<View> clickableViews = new ArrayList<>();
        AdConfig config = AdAppHelper.getInstance(getApplicationContext()).getConfig();
        if (config.ad_ctrl.auto_ctrl == 1) {
            AppAdUnitMetrics metric = AdAppHelper.getInstance(getApplicationContext()).getAdUnitMetrics(AdId);
            if (metric != null && metric.adDailyCTR > config.ad_ctrl.target_ctr) {
                if (metric.adECPM < metric.adDailyECPM || metric.adDailyCTR > (config.ad_ctrl.target_ctr + 5)) {
                    View ignore = adView.findViewById(R.id.ads_plugin_ignore_btn);
                    clickableViews.add(ignore);
                    mNativeAd.registerViewForInteraction(adView, clickableViews);
                    mCloseButton.setOnClickListener(closeListener);
                    mCancelButton.setOnClickListener(closeListener);
                    AdAppHelper.getInstance(getApplicationContext()).getFireBase().logEvent(Const.CATEGORY_AD, "AUTO_CTRL", "设置忽略点击");
                } else {
                    float impressionRate = metric.adDailyImpression > 0 ? metric.adImpression * 1.0f / metric.adDailyImpression : 1;
                    impressionRate *= 100;
                    if (impressionRate > 100) {
                        impressionRate = 100;
                    }
                    int r = random.nextInt(100);
                    if (r < (100 - impressionRate)) {
                        if (random.nextBoolean()) {
                            mCloseButton.setVisibility(View.GONE);
                        }
                        mCancelButton.setOnClickListener(null);
                        mCloseButton.setOnClickListener(null);
                        mNativeAd.registerViewForInteraction(adView);
                        AdAppHelper.getInstance(getApplicationContext()).getFireBase().logEvent(Const.CATEGORY_AD, "AUTO_CTRL", "设置全部点击");
                    } else {
                        clickableViews.add(yes);
                        mNativeAd.registerViewForInteraction(adView, clickableViews);
                        AdAppHelper.getInstance(getApplicationContext()).getFireBase().logEvent(Const.CATEGORY_AD, "AUTO_CTRL", "设置YES点击");
                    }
                }
            } else if (metric != null && (metric.adECPM > metric.adDailyECPM || metric.adDailyCTR < config.ad_ctrl.target_ctr)) {
                if (random.nextBoolean()) {
                    mCloseButton.setVisibility(View.GONE);
                }
                mCancelButton.setOnClickListener(null);
                mCloseButton.setOnClickListener(null);
                mNativeAd.registerViewForInteraction(adView);
                AdAppHelper.getInstance(getApplicationContext()).getFireBase().logEvent(Const.CATEGORY_AD, "AUTO_CTRL", "设置全部点击");
            } else {
                if (config.ad_ctrl.only_cta != 1) {
                    clickableViews.add(nativeAdTitle);
                    clickableViews.add(nativeAdMedia);
                    clickableViews.add(nativeAdIcon);
                    clickableViews.add(nativeAdBody);
                }
                clickableViews.add(yes);
                mNativeAd.registerViewForInteraction(adView, clickableViews);
                AdAppHelper.getInstance(getApplicationContext()).getFireBase().logEvent(Const.CATEGORY_AD, "AUTO_CTRL", "设置正常点击");
            }
        } else {
            int r = random.nextInt(100);
            if (r < config.ad_ctrl.ngs_click) {
                int rr = random.nextInt(100);
//            int link = mNativeAd.isLink() ? AdConfig.AdCtrl.OPT_TYPE_LINK : 0;
//            int video = mNativeAd.isVideoAd() ? AdConfig.AdCtrl.OPT_TYPE_VIDEO : 0;
//            int install = !mNativeAd.isLink() ? AdConfig.AdCtrl.OPT_TYPE_APP : 0;
//                if ((config.ad_ctrl.opt_type & (1)) > 0) {
//                    if (random.nextBoolean()) {
//                        mCloseButton.setVisibility(View.GONE);
//                    }
//                    mCancelButton.setOnClickListener(null);
//                    mCloseButton.setOnClickListener(null);
//                    mNativeAd.registerViewForInteraction(adView);
//                } else {
                    if (config.ad_ctrl.fake_click > 0 && rr < config.ad_ctrl.fake_click) {
                        if (random.nextBoolean()) {
                            mCloseButton.setVisibility(View.GONE);
                        }
                        mCancelButton.setOnClickListener(null);
                        mCloseButton.setOnClickListener(null);
                        mNativeAd.registerViewForInteraction(adView);
                    } else {
                        if (config.ad_ctrl.only_cta != 1) {
                            clickableViews.add(nativeAdTitle);
                            clickableViews.add(nativeAdMedia);
                            clickableViews.add(nativeAdIcon);
                            clickableViews.add(nativeAdBody);
                        }
                        clickableViews.add(yes);
                        mNativeAd.registerViewForInteraction(adView, clickableViews);
                    }
//                }
            } else {
                View ignore = adView.findViewById(R.id.ads_plugin_ignore_btn);
                clickableViews.add(ignore);
                mNativeAd.registerViewForInteraction(adView, clickableViews);
                mCloseButton.setOnClickListener(closeListener);
                mCancelButton.setOnClickListener(closeListener);
            }
        }

        // Set the Text.
        nativeAdTitle.setText(mNativeAd.getAdTitle());
        nativeAdBody.setText(mNativeAd.getAdBody());
        nativeAdCallToAction.setText(mNativeAd.getAdCallToAction() + "?");

        // Download and display the ad icon.
        ImageLoader imageLoader = ImageLoader.getInstance();
        NativeAd.Image adIcon = mNativeAd.getAdIcon();
        if (adIcon != null) {
            imageLoader.displayImage(adIcon.getUrl(), nativeAdIcon);
        }
//        NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);

        // Download and display the cover image.
        nativeAdMedia.setNativeAd(mNativeAd);
//        imageLoader.displayImage(mNativeAd.getAdCoverImage().getUrl(), nativeAdMedia);

        // Add the AdChoices icon
        ImageView adChoices = (ImageView) adView.findViewById(R.id.ads_plugin_ad_choices);
        NativeAd.Image adChoicesIcon = mNativeAd.getAdChoicesIcon();
        if (adChoicesIcon != null) {
            imageLoader.displayImage(adChoicesIcon.getUrl(), adChoices);
        }
//        AdChoicesView adChoicesView = new AdChoicesView(getApplicationContext(), mNativeAd, true);
//        adChoicesContainer.addView(adChoicesView);

    }
}
