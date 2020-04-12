package com.bestgo.adsplugin.ads;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bestgo.adsplugin.R;
import com.bestgo.adsplugin.ads.activity.RecommendAdActivity;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

public class RecommendAd {
    private Context mContext;
    private ArrayList<MyFrameLayout> mNativeLayouts;
    private float mDensity;
    private AdStateListener mListener;

    WebView mWebView;
    String sDefaultUserAgent;
    private static final int API = android.os.Build.VERSION.SDK_INT;
    private ValueCallback<Uri[]> mFilePathCallback;

    public void init(Context context) {
        this.mContext = context;
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .defaultDisplayImageOptions(new DisplayImageOptions.Builder()
//                        .showImageOnLoading(mContext.getResources().getDrawable(android.R.drawable.progress_horizontal))
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .build())
                .build();
        ImageLoader.getInstance().init(config);
        mNativeLayouts = new ArrayList<>();
        mDensity = mContext.getResources().getDisplayMetrics().density;
    }

    public void setAdListener(AdStateListener listener) {
        mListener = listener;
    }
    public void loadNewNativeAd() {
        AdConfig config = AdAppHelper.getInstance(mContext).getConfig();
        boolean ret = (config.recommend_ctrl.exe == 1 && config.recommend_ctrl.native_count > 0);

        if (!ret) return;
        if (config.recommend_ctrl.native_list == null || config.recommend_ctrl.native_list.length == 0) return;

        final ImageLoader imageLoader = ImageLoader.getInstance();
        for (int i = 0; i < config.recommend_ctrl.native_list.length; i++) {
            final AdConfig.RecommendNativeAdItem item = config.recommend_ctrl.native_list[i];
            if (item == null) continue;
            if (item.requested && (System.currentTimeMillis() - item.lastRequestTime) < AdAppHelper.MAX_REQEUST_TIME) continue;

            if (item.enabled && !item.loaded) {
                item.requested = true;
                item.lastRequestTime = System.currentTimeMillis();
                AdAppHelper.getInstance(mContext).getFireBase().logEvent(Const.CATEGORY_AD_RECOMMEND_NATIVE_POSISTION, item.app_id, Const.ACTION_REQUEST);

                item.requested = false;

                imageLoader.loadImage(item.image_url, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {
                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {
                        item.requested = false;
                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        item.requested = false;
                        item.loaded = true;
                        AdAppHelper.getInstance(mContext).getFireBase().logEvent(Const.CATEGORY_AD_RECOMMEND_NATIVE_POSISTION, item.app_id, Const.ACTION_LOAD);
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {
                        item.requested = false;
                    }
                });
            }
        }
    }

    public View getNative(int height) {
        return getNativeInternal(height);
    }

    private View getNativeInternal(int height) {
        AdConfig config = AdAppHelper.getInstance(mContext).getConfig();
        boolean ret = (config.recommend_ctrl.exe == 1 && config.recommend_ctrl.native_count > 0);

        if (!ret) return null;
        if (config.recommend_ctrl.native_list == null || config.recommend_ctrl.native_list.length == 0) return null;

        if (mNativeLayouts.size() < config.recommend_ctrl.native_list.length) {
            mNativeLayouts.add(new MyFrameLayout(mContext));
        }

        for (int i = 0; i < mNativeLayouts.size(); i++) {
            MyFrameLayout layout = mNativeLayouts.get(i);
            if (!layout.isShown()) {
                ViewGroup parent = (ViewGroup)layout.getParent();
                if (parent != null) {
                    parent.removeView(layout);
                }
                if (i >= config.recommend_ctrl.native_list.length) continue;
                int count = layout.getChildCount();
                final AdConfig.RecommendNativeAdItem item = config.recommend_ctrl.native_list[i];

                try {
                    mContext.getPackageManager().getApplicationInfo(item.app_id, 0);
                    AdAppHelper.getInstance(mContext).getFireBase().logEvent(Const.CATEGORY_AD_RECOMMEND_NATIVE_POSISTION, item.app_id, "已经安装");
                    continue;
                } catch (PackageManager.NameNotFoundException e) {
                }

                if (count == 0) {
                    LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                    View v = layoutInflater.inflate(R.layout.adsplugin_recommend_native_layout, layout, false);
                    layout.addView(v, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
                    AdAppHelper.getInstance(mContext).getFireBase().logEvent(Const.CATEGORY_AD_RECOMMEND_NATIVE_POSISTION, item.app_id, Const.ACTION_SHOW);
                }
                View nativeView = layout.findViewById(R.id.ads_plugin_native_ad_unit);
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)nativeView.getLayoutParams();
                params.width = item.width > 0 ? (int)(item.width * mDensity) : ViewGroup.LayoutParams.WRAP_CONTENT;
                if (height == -1) {
                    params.height = item.height > 0 ? (int) (item.height * mDensity) : ViewGroup.LayoutParams.WRAP_CONTENT;
                } else if (height > 0) {
                    params.height = (int) (height * mDensity);
                }
                nativeView.setLayoutParams(params);
                if (item.is_webview) {
                    mWebView = (WebView)nativeView.findViewById(R.id.webView);
                    if (mWebView.getVisibility() != View.VISIBLE) {
                        mWebView.addJavascriptInterface(new AdControl(item), "AdControl");
                        init();
                        String url = item.link_url;
                        StringBuffer buffer = new StringBuffer();
                        HashMap<String, String> pairs = new HashMap<>();
                        pairs.put("format", item.width + "x" + item.height);
                        pairs.put("package_name", mContext.getPackageName());
                        pairs.put("gl", Locale.getDefault().getCountry());
                        pairs.put("hl", Locale.getDefault().getLanguage());
                        pairs.put("t", TimeZone.getDefault().getRawOffset() / (3600 * 1000) + "");
                        boolean firstEntry = true;
                        if (url.indexOf("?") > 0) {
                            firstEntry = false;
                        }
                        for (String key : pairs.keySet()) {
                            try {
                                buffer.append((firstEntry ? "?" : "&") + URLEncoder.encode((key).toString(), "UTF-8") + "=" + URLEncoder.encode(pairs.get(key), "UTF-8"));
                            } catch (Exception ex) {
                            }
                            firstEntry = false;
                        }
                        url += buffer.toString();
                        mWebView.loadUrl(url);
                        mWebView.setVisibility(View.VISIBLE);
                    }
                } else {
                    ImageView imageView = (ImageView) layout.findViewById(R.id.ads_plugin_native_ad_media);
                    final View pb = layout.findViewById(R.id.ads_plugin_pb);
                    final String url = item.link_url;
                    final int index = i;
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AdAppHelper.getInstance(mContext).getFireBase().logEvent(Const.CATEGORY_AD_RECOMMEND_NATIVE_POSISTION, item.app_id, Const.ACTION_CLICK);
                            openGooglePlay(url, item.not_play_link);

                            if (mListener != null)
                                mListener.onAdClick(new AdType(AdType.RECOMMEND_AD_NATIVE), index);
                        }
                    });
                    final ImageLoader imageLoader = ImageLoader.getInstance();
                    imageLoader.displayImage(item.image_url, imageView, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {
                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                            pb.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {
                        }
                    });
                }
                layout.setUseTime();
                return layout;
            }
        }
        return null;
    }

    private void init() {
        mWebView.setDrawingCacheBackgroundColor(Color.WHITE);
        mWebView.setFocusableInTouchMode(true);
        mWebView.setFocusable(true);
        mWebView.setDrawingCacheEnabled(false);
        mWebView.setWillNotCacheDrawing(true);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            //noinspection deprecation
            mWebView.setAnimationCacheEnabled(false);
            //noinspection deprecation
            mWebView.setAlwaysDrawnWithCacheEnabled(false);
        }
        mWebView.setBackgroundColor(Color.WHITE);

        mWebView.setScrollbarFadingEnabled(true);
        mWebView.setSaveEnabled(true);
        mWebView.setNetworkAvailable(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
//        mWebView.setDownloadListener(new LightningDownloadListener(activity));
        sDefaultUserAgent = mWebView.getSettings().getUserAgentString();

        initializeSettings();

    }

    private void initializeSettings() {
        if (mWebView == null) {
            return;
        }
        final WebSettings settings = mWebView.getSettings();
        if (API < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            //noinspection deprecation
            settings.setAppCacheMaxSize(Long.MAX_VALUE);
        }
        if (API < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            settings.setEnableSmoothTransition(true);
        }
        if (API > Build.VERSION_CODES.JELLY_BEAN) {
            settings.setMediaPlaybackRequiresUserGesture(true);
        }
        if (API >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        settings.setDomStorageEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDatabaseEnabled(true);

        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccess(true);

        settings.setGeolocationEnabled(true);

        if (API >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowFileAccessFromFileURLs(false);
            settings.setAllowUniversalAccessFromFileURLs(false);
        }

        if (API < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            settings.setSavePassword(true);
        }
        settings.setSaveFormData(true);
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportMultipleWindows(false);
    }

    public void loadNewFullAd() {
        AdConfig config = AdAppHelper.getInstance(mContext).getConfig();
        boolean ret = (config.recommend_ctrl.exe == 1 && config.recommend_ctrl.count > 0);

        if (!ret) return;
        if (config.recommend_ctrl.list == null || config.recommend_ctrl.list.length == 0) return;

        final ImageLoader imageLoader = ImageLoader.getInstance();
        for (int i = 0; i < config.recommend_ctrl.list.length; i++) {
            final AdConfig.RecommendAdItem item = config.recommend_ctrl.list[i];
            if (item == null) continue;
            if (item.requested && (System.currentTimeMillis() - item.lastRequestTime) < AdAppHelper.MAX_REQEUST_TIME) continue;

            if (item.enabled && !item.loaded) {
                item.requested = true;
                item.lastRequestTime = System.currentTimeMillis();
                AdAppHelper.getInstance(mContext).getFireBase().logEvent(Const.CATEGORY_AD_RECOMMEND_POSISTION, item.app_id, Const.ACTION_REQUEST);

                item.requested = false;
                reportAdLoaed(item);

//                imageLoader.loadImage(item.image_url, new ImageLoadingListener() {
//                    @Override
//                    public void onLoadingStarted(String s, View view) {
//                    }
//
//                    @Override
//                    public void onLoadingFailed(String s, View view, FailReason failReason) {
//                        item.requested = false;
//                    }
//
//                    @Override
//                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//                        item.requested = false;
//                        item.image = bitmap;
//                        reportAdLoaed(item);
//                    }
//
//                    @Override
//                    public void onLoadingCancelled(String s, View view) {
//                        item.requested = false;
//                    }
//                });
//                imageLoader.loadImage(item.icon_url, new ImageLoadingListener() {
//                    @Override
//                    public void onLoadingStarted(String s, View view) {
//                    }
//
//                    @Override
//                    public void onLoadingFailed(String s, View view, FailReason failReason) {
//                    }
//
//                    @Override
//                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//                        item.icon = bitmap;
//                        reportAdLoaed(item);
//                    }
//
//                    @Override
//                    public void onLoadingCancelled(String s, View view) {
//                    }
//                });
            }
        }
    }

    private synchronized void reportAdLoaed(AdConfig.RecommendAdItem item) {
//        if (item.image != null) {
            item.loaded = true;
            AdAppHelper.getInstance(mContext).getFireBase().logEvent(Const.CATEGORY_AD_RECOMMEND_POSISTION, item.app_id, Const.ACTION_LOAD);
//        }
    }

    public boolean isFullAdLoaded() {
        if (!AdAppHelper.getInstance(mContext).isNetworkConnected(mContext)) {
//            if (report) {
//                Analytics.getInstance(mContext)._sendEvent(Const.CATEGORY_AD_RECOMMEND_POSISTION, "当前没有网络");
//                AdAppHelper.getInstance(mContext).getFireBase().logEvent(Const.CATEGORY_AD_RECOMMEND_POSISTION, "当前没有网络");
//            }
            return false;
        }
        AdConfig config = AdAppHelper.getInstance(mContext).getConfig();
        boolean ret = (config.recommend_ctrl.exe == 1 && config.recommend_ctrl.count > 0);

        if (!ret) {
            AdAppHelper.getInstance(mContext).getFireBase().logEvent(Const.CATEGORY_AD_RECOMMEND, "没有启用");
            return ret;
        }

        if (System.currentTimeMillis() - AdAppHelper.INIT_TIME < config.ad_ctrl.home_delay_time) {
            AdAppHelper.getInstance(mContext).getFireBase().logEvent(Const.CATEGORY_AD_RECOMMEND, "启动时间小于主页延迟时间");
            return false;
        }
        if (config.recommend_ctrl.list == null || config.recommend_ctrl.list.length == 0) {
            AdAppHelper.getInstance(mContext).getFireBase().logEvent(Const.CATEGORY_AD_RECOMMEND, "广告列表不存在");
            return false;
        }

        AdAppHelper.getInstance(mContext).getFireBase().logEvent(Const.CATEGORY_AD_RECOMMEND, "判断广告是否可用");

        boolean allInstalled = true;
        for (int i = 0; i < config.recommend_ctrl.list.length; i++) {
            AdConfig.RecommendAdItem item = config.recommend_ctrl.list[i];
            if (item == null) continue;
            if (item.enabled) {
                try {
                    mContext.getPackageManager().getApplicationInfo(item.app_id, 0);
                    AdAppHelper.getInstance(mContext).getFireBase().logEvent(Const.CATEGORY_AD_RECOMMEND_POSISTION, item.app_id, "已经安装");
                } catch (PackageManager.NameNotFoundException e) {
                    allInstalled = false;
                    if (item.loaded) {
                        return true;
                    }
                }
            }
        }
        if (allInstalled) {
            AdAppHelper.getInstance(mContext).getFireBase().logEvent(Const.CATEGORY_AD_RECOMMEND, "都已经安装了");
        } else {
            AdAppHelper.getInstance(mContext).getFireBase().logEvent(Const.CATEGORY_AD_RECOMMEND, "广告没有准备好");
        }
        return false;
    }

    public void showFullAd() {
        AdConfig config = AdAppHelper.getInstance(mContext).getConfig();
        boolean ret = (config.recommend_ctrl.exe == 1 && config.recommend_ctrl.count > 0);

        if (!ret) return;
        if (config.recommend_ctrl.list == null || config.recommend_ctrl.list.length == 0) return;

        AdAppHelper.getInstance(mContext).getFireBase().logEvent(Const.CATEGORY_AD_RECOMMEND, "显示推荐位广告");

        if (config.recommend_ctrl.show_random == 1) {
            int index = new Random().nextInt(config.recommend_ctrl.list.length);
            AdConfig.RecommendAdItem item = config.recommend_ctrl.list[index];
            if (item != null && item.enabled) {
                try {
                    mContext.getPackageManager().getApplicationInfo(item.app_id, 0);
                } catch (PackageManager.NameNotFoundException e) {
                    if (item.loaded) {
                        AdAppHelper.getInstance(mContext).getFireBase().logEvent(Const.CATEGORY_AD_RECOMMEND_POSISTION, item.app_id, Const.ACTION_OPEN);
                        Intent intent = new Intent(mContext, RecommendAdActivity.class);
                        RecommendAdActivity.recommendAdItem = item;
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                        return;
                    }
                }
            }
        }
        for (int i = 0; i < config.recommend_ctrl.list.length; i++) {
            AdConfig.RecommendAdItem item = config.recommend_ctrl.list[i];
            if (item == null) continue;
            if (item.enabled) {
                try {
                    mContext.getPackageManager().getApplicationInfo(item.app_id, 0);
                } catch (PackageManager.NameNotFoundException e) {
                    if (item.loaded) {
                        AdAppHelper.getInstance(mContext).getFireBase().logEvent(Const.CATEGORY_AD_RECOMMEND_POSISTION, item.app_id, Const.ACTION_OPEN);
                        Intent intent = new Intent(mContext, RecommendAdActivity.class);
                        RecommendAdActivity.recommendAdItem = item;
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                        return;
                    }
                }
            }
        }
    }

    public void onResume() {
//        if (mNativeLayouts != null) {
//            for (int i = 0; i < mNativeLayouts.size(); i++) {
//                mNativeLayouts.get(i).setVisibility(View.VISIBLE);
//            }
//        }
    }

    public void onPause() {
//        if (mNativeLayouts != null) {
//            for (int i = 0; i < mNativeLayouts.size(); i++) {
//                mNativeLayouts.get(i).setVisibility(View.INVISIBLE);
//            }
//        }
    }

    private void openGooglePlay(String url, boolean notPlayLink) {
        boolean hasPlay = false;
        try {
            mContext.getPackageManager().getApplicationInfo("com.android.vending", 0);
            hasPlay = true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }
        try {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (hasPlay && !notPlayLink) {
                intent.setPackage("com.android.vending");
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class AdControl {
        private AdConfig.RecommendNativeAdItem item;
        public AdControl(AdConfig.RecommendNativeAdItem item) {
            this.item = item;
        }
        @JavascriptInterface
        public void onPageLoaded() {

        }

        @JavascriptInterface
        public boolean checkPackage(String packageId) {
            try {
                mContext.getPackageManager().getApplicationInfo(packageId, 0);
                return true;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            return false;
        }

        @JavascriptInterface
        public void openLink(String appId, String url) {
            try {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                if (checkPackage(appId)) {
                    intent.setPackage(appId);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @JavascriptInterface
        public void close() {
        }
    }
}
