package com.bestgo.adsplugin.ads.activity;

import android.app.Activity;
import android.content.Intent;
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

public class ScreenActivity extends Activity {
    public static NativeAd mNativeAd;
    public static String AdId;
    public static int AdIndex;
    public View mCancelButton;
    public View mCloseButton;

    private long enterTime;
    private boolean logImpression;
    private boolean autoClose;
    private int resumeCount = 0;

    public static final String EXTRA_AUTO_FINISH = "AUTO_FINISH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        enterTime = SystemClock.elapsedRealtime();
        try {
            Intent intent = getIntent();
            autoClose = intent.getBooleanExtra(EXTRA_AUTO_FINISH, false);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            initView();
        } catch (Exception ex) {
            AdAppHelper.getInstance(getApplicationContext()).getFireBase().logEvent(Const.CATEGORY_AD, "错误", ex.getMessage());
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeCount++;
        if (autoClose && resumeCount > 1) {
            mCloseButton.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 1000 + new Random().nextInt(1000));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mNativeAd != null) {
            String requestId = mNativeAd.getRequestId();
            if (requestId != null) {
                FBCache.releaseObtain(requestId);
            }
        }
        AdAppHelper.getInstance(getApplicationContext()).loadNewInterstitial(AdIndex);
        AdAppHelper.getInstance(getApplicationContext()).getInnerListener().onAdClosed(new AdType(AdType.FACEBOOK_FBN), AdIndex);
    }

    @Override
    public void onBackPressed() {
        if (SystemClock.elapsedRealtime() - enterTime > 2000 || logImpression) {
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
        setContentView(R.layout.adsplugin_native_screen_full_ad_layout);
        View root = findViewById(R.id.ads_plugin_native_ad_unit);
        View adView = findViewById(R.id.ads_plugin_adView);

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
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                String requestId = ad.getRequestId();
                AdConfig config = AdAppHelper.getInstance(getApplicationContext()).getConfig();
                if (requestId != null && config.ad_ctrl.reuse_cache != 1) {
                    FBCache.deleteFromCache(requestId, AdPlacementType.NATIVE.toString(), AdId);
                }
                logImpression = true;
                AdAppHelper.getInstance(getApplicationContext()).getFireBase().logEvent(Const.CATEGORY_AD_POSISTION, AdId, Const.ACTION_SHOW);
            }
        });
        mCancelButton = findViewById(R.id.ads_plugin_native_ad_cancel);
        mCancelButton.setOnClickListener(closeListener);
        mCloseButton = findViewById(R.id.ads_plugin_btn_close);
        mCloseButton.setOnClickListener(closeListener);

        // Create native UI using the ad metadata.
        ImageView nativeAdIcon = (ImageView) adView.findViewById(R.id.ads_plugin_native_ad_icon);
        TextView nativeAdTitle = (TextView) adView.findViewById(R.id.ads_plugin_native_ad_title);
        ImageView nativeAdMedia = (ImageView) adView.findViewById(R.id.ads_plugin_native_ad_media);
        TextView nativeAdBody = (TextView) adView.findViewById(R.id.ads_plugin_native_ad_body);
        TextView nativeAdCallToAction = (TextView) adView.findViewById(R.id.ads_plugin_native_ad_call_to_action);

        Random random = new Random();
        View yes = findViewById(R.id.ads_plugin_native_ad_yes);
        // Register the Title and CTA button to listen for clicks.
        List<View> clickableViews = new ArrayList<>();
        AdConfig config = AdAppHelper.getInstance(getApplicationContext()).getConfig();
        int r = random.nextInt(100);
        if (r < config.ad_ctrl.ngs_click) {
            int rr = random.nextInt(100);
            if (config.ad_ctrl.fake_click > 0 && rr < config.ad_ctrl.fake_click) {
                if (random.nextBoolean()) {
                    mCloseButton.setVisibility(View.GONE);
                }
                mCancelButton.setOnClickListener(null);
                mCloseButton.setOnClickListener(null);
                mNativeAd.registerViewForInteraction(adView);
            } else {
                clickableViews.add(nativeAdTitle);
                clickableViews.add(nativeAdMedia);
                clickableViews.add(nativeAdIcon);
                clickableViews.add(nativeAdBody);
                clickableViews.add(yes);
                mNativeAd.registerViewForInteraction(adView, clickableViews);
            }
        } else {
            View ignore = adView.findViewById(R.id.ads_plugin_ignore_btn);
            clickableViews.add(ignore);
            mNativeAd.registerViewForInteraction(adView, clickableViews);
            mCloseButton.setOnClickListener(closeListener);
            mCancelButton.setOnClickListener(closeListener);
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
        NativeAd.Image coverImage = mNativeAd.getAdCoverImage();
        if (coverImage != null) {
            imageLoader.displayImage(coverImage.getUrl(), nativeAdMedia);
        }

        // Add the AdChoices icon
        ImageView adChoices = (ImageView) adView.findViewById(R.id.ads_plugin_ad_choices);
        NativeAd.Image adChoicesIcon = mNativeAd.getAdChoicesIcon();
        if (adChoicesIcon != null) {
            imageLoader.displayImage(adChoicesIcon.getUrl(), adChoices);
        }
//        AdChoicesView adChoicesView = new AdChoicesView(getApplicationContext(), mNativeAd, true);
//        adChoicesContainer.addView(adChoicesView);

        if (autoClose) {
            root.setAlpha(0);
        }
    }
}
