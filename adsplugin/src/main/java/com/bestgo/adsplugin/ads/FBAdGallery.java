package com.bestgo.adsplugin.ads;

import android.content.Context;
import android.os.Build;
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
import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;

import java.util.ArrayList;
import java.util.List;

public class FBAdGallery {
    private Context mContext;
    private boolean enableNative;
    private String mNativeId = "";
    private boolean nativeLoaded;
    private boolean nativeRequest;
    private NativeAd mNativeAd;
    private MyFrameLayout mNativeAdView;
    private long lastRequestNativeTime;
    private AdStateListener mAdListener;
    private View mPB;
    private View mNext;

    public FBAdGallery(Context context) {
        mContext = context;
        mNativeAdView = new MyFrameLayout(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            initView();
        }
    }

    public void resetId() {
        AdConfig config = AdAppHelper.getInstance(mContext).getConfig();
        enableNative = config.fb_ad_gallery.exe == 1;
        mNativeId = config.fb_ad_gallery.fb;
    }

    public void setAdListener(AdStateListener listener) {
        this.mAdListener = listener;
    }

    public boolean isLoaded() {
        return nativeLoaded;
    }

    private void initView() {
        // Add the Ad view into the ad container.
        LayoutInflater inflater = LayoutInflater.from(mContext);
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        View adView = inflater.inflate(R.layout.adsplugin_native_gallery_layout, mNativeAdView, false);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        mNativeAdView.addView(adView, params);

        mPB = mNativeAdView.findViewById(R.id.ads_plugin_pb);
        mPB.setVisibility(View.GONE);

        mNext = mNativeAdView.findViewById(R.id.ads_plugin_native_next);
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNewNativeAd();
                mPB.setVisibility(View.VISIBLE);
                mNext.setVisibility(View.GONE);
            }
        });
    }

    private void resetButton() {
        mPB.setVisibility(View.GONE);
        mNext.setVisibility(View.VISIBLE);
    }

    public View getNativeView() {
        return mNativeAdView;
    }

    public void loadNewNativeAd() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return;
        }
        if (TextUtils.isEmpty(mNativeId)) return;
        if (nativeRequest && (System.currentTimeMillis() - lastRequestNativeTime) < AdAppHelper.MAX_REQEUST_TIME) return;
        if (!enableNative) return;

        nativeRequest = true;
        lastRequestNativeTime = System.currentTimeMillis();

        if (mNativeAd != null) {
            mNativeAd.destroy();
        }
        mNativeAd = new NativeAd(mContext, mNativeId);
        mNativeAd.setAdListener(new AdListener() {

            @Override
            public void onError(Ad ad, AdError error) {
                nativeRequest = false;
                nativeLoaded = false;
                resetButton();
                if (mAdListener != null) {
                    mAdListener.onAdLoadFailed(new AdType(AdType.FACEBOOK_GALLERY_NATIVE), 0, error.getErrorMessage());
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                resetButton();
                AdAppHelper.getInstance(mContext).getFireBase().logEvent(Const.CATEGORY_AD_POSISTION, mNativeId, Const.ACTION_LOAD);
                nativeLoaded = true;
                nativeRequest = false;
                if (mNativeAd != null) {
                    mNativeAd.unregisterView();
                }

                // Create native UI using the ad metadata.
                View root = mNativeAdView.findViewById(R.id.ads_plugin_native_ad_unit);
                ImageView nativeAdIcon = (ImageView) mNativeAdView.findViewById(R.id.ads_plugin_native_ad_icon);
                TextView nativeAdTitle = (TextView) mNativeAdView.findViewById(R.id.ads_plugin_native_ad_title);
                MediaView nativeAdMedia = (MediaView) mNativeAdView.findViewById(R.id.ads_plugin_native_ad_media);
                TextView nativeAdBody = (TextView) mNativeAdView.findViewById(R.id.ads_plugin_native_ad_body);
                Button nativeAdCallToAction = (Button) mNativeAdView.findViewById(R.id.ads_plugin_native_ad_call_to_action);
                // Register the Title and CTA button to listen for clicks.
                List<View> clickableViews = new ArrayList<>();
                clickableViews.add(nativeAdTitle);
                clickableViews.add(nativeAdMedia);
                clickableViews.add(nativeAdBody);
                clickableViews.add(nativeAdCallToAction);
                mNativeAd.registerViewForInteraction(mNativeAdView, clickableViews);

                // Set the Text.
                if (nativeAdTitle != null) {
                    nativeAdTitle.setText(mNativeAd.getAdTitle());
                }
                if (nativeAdBody != null) {
                    nativeAdBody.setText(mNativeAd.getAdBody());
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
//                    NativeAd.downloadAndDisplayImage(adCoverImage, nativeAdMedia);
                    nativeAdMedia.setNativeAd(mNativeAd);
                }

                // Add the AdChoices icon
                LinearLayout adChoicesContainer = (LinearLayout) mNativeAdView.findViewById(R.id.ads_plugin_ad_choices_container);
                if (adChoicesContainer != null) {
                    adChoicesContainer.removeAllViews();
                    AdChoicesView adChoicesView = new AdChoicesView(mContext, mNativeAd, true);
                    adChoicesContainer.addView(adChoicesView);
                }

                if (mAdListener != null) {
                    mAdListener.onAdLoaded(new AdType(AdType.FACEBOOK_GALLERY_NATIVE), 0);
                }
            }

            @Override
            public void onAdClicked(Ad ad) {
                AdAppHelper.getInstance(mContext).getFireBase().logEvent(Const.CATEGORY_AD_POSISTION, mNativeId, Const.ACTION_CLICK);
                if (mAdListener != null) {
                    mAdListener.onAdClick(new AdType(AdType.FACEBOOK_GALLERY_NATIVE), 0);
                }
            }

            public void onLoggingImpression(Ad ad) {
            }
        });

        mNativeAd.loadAd();
        AdAppHelper.getInstance(mContext).getFireBase().logEvent(Const.CATEGORY_AD_POSISTION, mNativeId, Const.ACTION_REQUEST);
    }
}
