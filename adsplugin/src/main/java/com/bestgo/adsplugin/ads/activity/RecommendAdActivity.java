package com.bestgo.adsplugin.ads.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.bestgo.adsplugin.R;
import com.bestgo.adsplugin.ads.AdAppHelper;
import com.bestgo.adsplugin.ads.AdConfig;
import com.bestgo.adsplugin.ads.AdType;
import com.bestgo.adsplugin.ads.Const;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

public class RecommendAdActivity extends Activity implements View.OnClickListener {
    public static AdConfig.RecommendAdItem recommendAdItem;
    private boolean mClicked;

    WebView mWebView;
    String sDefaultUserAgent;
    private static final int API = android.os.Build.VERSION.SDK_INT;
    private ValueCallback<Uri[]> mFilePathCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        try {
            AdAppHelper.getInstance(getApplicationContext()).getFireBase().logEvent(Const.CATEGORY_AD_RECOMMEND_POSISTION, recommendAdItem.app_id, Const.ACTION_SHOW);
            initView();
            AdAppHelper.getInstance(getApplicationContext()).getInnerListener().onAdOpen(new AdType(AdType.RECOMMEND_AD), 0);
        } catch (Exception ex) {
            AdAppHelper.getInstance(getApplicationContext()).getFireBase().logEvent(Const.CATEGORY_AD, "错误", ex.getMessage());
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        if (recommendAdItem != null) {
            if (mWebView != null && recommendAdItem.is_webview) {
                // Check to make sure the WebView has been removed
                // before calling destroy() so that a memory leak is not created
                ViewGroup parent = (ViewGroup) mWebView.getParent();
                if (parent != null) {
                    parent.removeView(mWebView);
                }
                mWebView.stopLoading();
                mWebView.clearHistory();
                mWebView.setVisibility(View.GONE);
                mWebView.removeAllViews();
                mWebView.destroyDrawingCache();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    //this is causing the segfault occasionally below 4.2
                    mWebView.destroy();
                }
                mWebView = null;
            }
            AdAppHelper.getInstance(getApplicationContext()).getFireBase().logEvent(Const.CATEGORY_AD_RECOMMEND_POSISTION, recommendAdItem.app_id, Const.ACTION_CLOSE_FULL);
        }
        AdAppHelper.getInstance(getApplicationContext()).loadNewInterstitial();
        AdAppHelper.getInstance(getApplicationContext()).getInnerListener().onAdClosed(new AdType(AdType.RECOMMEND_AD), 0);
        super.onDestroy();
    }

    private void initView() throws UnsupportedEncodingException {
        mClicked = false;
        setContentView(R.layout.adsplugin_recommend_layout);
        View adView = findViewById(R.id.ads_plugin_adView);

        if (recommendAdItem.is_webview) {
            mWebView = (WebView)findViewById(R.id.webView);
            mWebView.addJavascriptInterface(new AdControl(), "AdControl");
            init();
            String url = recommendAdItem.link_url;
            StringBuffer buffer = new StringBuffer();
            HashMap<String, String> pairs = new HashMap<>();
            pairs.put("format", "interstitial");
            pairs.put("package_name", getPackageName());
            pairs.put("gl", Locale.getDefault().getCountry());
            pairs.put("hl", Locale.getDefault().getLanguage());
            pairs.put("t", TimeZone.getDefault().getRawOffset() / (3600 * 1000) + "");
            boolean firstEntry = true;
            if (url.indexOf("?") > 0) {
                firstEntry = false;
            }
            for (String key : pairs.keySet()) {
                buffer.append((firstEntry?"?":"&") + URLEncoder.encode((key).toString(), "UTF-8") + "=" + URLEncoder.encode(pairs.get(key), "UTF-8"));
                firstEntry = false;
            }
            url += buffer.toString();
            mWebView.loadUrl(url);
            mWebView.setVisibility(View.VISIBLE);
            return;
        }

        findViewById(R.id.ads_plugin_native_ad_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdConfig config = AdAppHelper.getInstance(getApplicationContext()).getConfig();
                Random r = new Random();
                int t = r.nextInt(100);
                if (config.recommend_ctrl.fake_click > t && !mClicked) {
                    RecommendAdActivity.this.onClick(v);
                } else {
                    finish();
                }
            }
        });

        findViewById(R.id.ads_plugin_btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdConfig config = AdAppHelper.getInstance(getApplicationContext()).getConfig();
                Random r = new Random();
                int t = r.nextInt(100);
                if (config.recommend_ctrl.fake_click > t && !mClicked) {
                    RecommendAdActivity.this.onClick(v);
                } else {
                    finish();
                }
            }
        });

        // Create native UI using the ad metadata.
        ImageView nativeAdIcon = (ImageView) adView.findViewById(R.id.ads_plugin_native_ad_icon);
        TextView nativeAdTitle = (TextView) adView.findViewById(R.id.ads_plugin_native_ad_title);
        ImageView nativeAdMedia = (ImageView) adView.findViewById(R.id.ads_plugin_native_ad_media);
        TextView nativeAdBody = (TextView) adView.findViewById(R.id.ads_plugin_native_ad_body);
        TextView nativeAdCallToAction = (TextView) adView.findViewById(R.id.ads_plugin_native_ad_call_to_action);

        View yes = findViewById(R.id.ads_plugin_native_ad_yes);
        yes.setOnClickListener(this);
        adView.setOnClickListener(this);


        // Set the Text.
        nativeAdTitle.setText(recommendAdItem.title);
        nativeAdBody.setText(recommendAdItem.sub_title);
        nativeAdCallToAction.setText(recommendAdItem.action_title + "?");

//        nativeAdMedia.setImageBitmap(recommendAdItem.image);
        final View pb = adView.findViewById(R.id.ads_plugin_pb);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(recommendAdItem.icon_url, nativeAdIcon);
        imageLoader.displayImage(recommendAdItem.image_url, nativeAdMedia, new ImageLoadingListener() {
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
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                showFileChooser(filePathCallback);
                return true;
            }
        });
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

    private void showFileChooser(ValueCallback<Uri[]> filePathCallback) {
        mFilePathCallback = filePathCallback;

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("*/*");

        Intent[] intentArray;
        if (takePictureIntent != null) {
            intentArray = new Intent[]{takePictureIntent};
        } else {
            intentArray = new Intent[0];
        }

        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

        startActivityForResult(chooserIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode != 1 || mFilePathCallback == null) {
            super.onActivityResult(requestCode, resultCode, intent);
            return;
        }

        Uri[] results = null;
        // Check that the response is a good one
        if (resultCode == Activity.RESULT_OK) {
            String dataString = intent.getDataString();
            if (dataString != null) {
                results = new Uri[]{Uri.parse(dataString)};
            }
        }

        mFilePathCallback.onReceiveValue(results);
        mFilePathCallback = null;
    }

    private void openGooglePlay(String url, boolean notPlayLink) {
        boolean hasPlay = false;
        try {
            getPackageManager().getApplicationInfo("com.android.vending", 0);
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
            startActivity(intent);
        } catch (Exception e) {
            AdAppHelper.getInstance(getApplicationContext()).getFireBase().logEvent(Const.CATEGORY_AD_RECOMMEND_POSISTION, recommendAdItem.app_id, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        mClicked = true;
        AdAppHelper.getInstance(getApplicationContext()).getFireBase().logEvent(Const.CATEGORY_AD_RECOMMEND_POSISTION, recommendAdItem.app_id, Const.ACTION_CLICK);
        AdAppHelper.getInstance(getApplicationContext()).getInnerListener().onAdClick(new AdType(AdType.RECOMMEND_AD), 0);
        openGooglePlay(recommendAdItem.link_url, recommendAdItem.not_play_link);
    }

    private class AdControl {
        @JavascriptInterface
        public void onPageLoaded() {

        }

        @JavascriptInterface
        public boolean checkPackage(String packageId) {
            try {
                getPackageManager().getApplicationInfo(packageId, 0);
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
                startActivity(intent);
                mClicked = true;
                AdAppHelper.getInstance(getApplicationContext()).getFireBase().logEvent(Const.CATEGORY_AD_RECOMMEND_POSISTION, recommendAdItem.app_id, Const.ACTION_CLICK);
                AdAppHelper.getInstance(getApplicationContext()).getInnerListener().onAdClick(new AdType(AdType.RECOMMEND_AD), 0);
            } catch (Exception e) {
                AdAppHelper.getInstance(getApplicationContext()).getFireBase().logEvent(Const.CATEGORY_AD_RECOMMEND_POSISTION, recommendAdItem.app_id, e.getMessage());
                e.printStackTrace();
            }
        }

        @JavascriptInterface
        public void close() {
            finish();
        }
    }
}
