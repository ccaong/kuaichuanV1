package com.bestgo.adsplugin.ads;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bestgo.adsplugin.BuildConfig;
import com.bestgo.adsplugin.ads.activity.ShowAdFilter;
import com.bestgo.adsplugin.ads.analytics.AbstractFacebook;
import com.bestgo.adsplugin.ads.analytics.AbstractFirebase;
import com.bestgo.adsplugin.ads.analytics.DebugFacebook;
import com.bestgo.adsplugin.ads.analytics.DebugFireBase;
import com.bestgo.adsplugin.ads.cache.FBCache;
import com.bestgo.adsplugin.ads.entity.AppAdUnitMetrics;
import com.bestgo.adsplugin.ads.service.D;
import com.bestgo.adsplugin.ads.service.WorkerService;
import com.bestgo.adsplugin.animation.AbstractAnimator;
import com.bestgo.adsplugin.daemon.Daemon;
import com.facebook.ads.AudienceNetworkActivity;
import com.facebook.ads.InterstitialAdActivity;
import com.google.android.gms.ads.AdActivity;
import com.umeng.analytics.game.UMGameAgent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Queue;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdAppHelper {
    private static AdAppHelper _this;
    private Context context;
    private String NAME = "AdAppHelper";
    private SharedPreferences mSP;
    protected boolean allowResumeAd = false;
    private boolean enableResumeAd = true;
    private boolean firstOnResume = false;

    private AdMobAd mAdMobAd;
    private FacebookAd mFacebookAd;
    private RecommendAd mRecommendAd;
    private FBAdGallery mFBGallery;
    private FBSplashAd mFBSplashAd;
    private AdMobSplashAd mAdMobSplashAd;
    private NewsAd mNewsAd;

    private MyFrameLayout mBannerLayout;
    private MyFrameLayout mNativeLayout;
    private MyFrameLayout[] mNativeLayouts;

    private AdConfig mAdConfig;
    private Queue<Long> mPendingShowQueue = new ArrayDeque<>();
    private boolean mFullAdShowing;
    private boolean mFullAdShowPending;
    private long mLastShowFullTime;

    public static AdAppHelper getInstance(Context ctx) {
        if (_this == null) {
            _this = new AdAppHelper();
            _this.context = ctx;
        }
        return _this;
    }

    public static long INIT_TIME = System.currentTimeMillis();
    public static long FIRST_ENTER_TIME;
    public static int GA_RESOURCE_ID = 1;
    public static boolean ENABLE_UPLOAD_AD_EVENT = true;
    public static int NATIVE_REFRESH_TIME = 30000;
    public static int MAX_REQEUST_TIME = 20000;
    public static int MAX_AD_ALIVE_TIME = 3600 * 1000;
    public static AbstractFirebase FIREBASE;
    public static AbstractFacebook FACEBOOK;
    public static int[] NATIVE_ADMOB_WIDTH_LIST;
    public static int[] NATIVE_BG_COLOR_LIST;
    public static int[] NATIVE_TEXT_COLOR_LIST;

    public static int NEWS_ADMOB_WIDTH = -1;
    public static int NEWS_TITLE_COLOR = Color.BLACK;
    public static int NEWS_DETAIL_COLOR = Color.GRAY;

    public static final String SHARED_SP_NAME = "ad_app_helper";

    private int mAliveActivityCount = 0;

    private ConcurrentHashMap<String, AppAdUnitMetrics> mMetricsMap = new ConcurrentHashMap<>();

    private void writeAssetsToSP() throws IOException {
        InputStream is = context.getAssets().open("config.xml");
        String packageName = context.getPackageName();
        String sharedSpFile = "/data/data/" + packageName + "/shared_prefs/ourdefault_game_config.xml";
        File dir = new File(sharedSpFile);
        if (dir.exists()) {
            return;
        }
        if (!dir.getParentFile().exists()) {
            dir.getParentFile().mkdirs();
        }
        OutputStream os = new FileOutputStream(sharedSpFile);
        byte[] bytes = new byte[1024];
        int len = is.read(bytes);
        while (len > 0) {
            os.write(bytes, 0, len);
            len = is.read(bytes);
        }
        is.close();
        os.flush();
        os.close();
    }

    public static String getVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    public void init() {
        int pid = android.os.Process.myPid();
        String processName = "";
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                processName = appProcess.processName;
                break;
            }
        }
        String packageName = context.getPackageName();
        if (processName.equals(packageName)) {
            mAdConfig = new AdConfig();
            mSP = context.getSharedPreferences(SHARED_SP_NAME, 0);
            FBCache.PACKAGE_NAME = packageName;
            initUmeng();
            initAd();
            initAliveTag(context);

            Intent it = new Intent(context, WorkerService.class);
            context.startService(it);

            Intent it1 = new Intent(context, D.class);
            context.startService(it1);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                Daemon.run(context, WorkerService.class, Daemon.INTERVAL_ONE_MINUTE);
            }
            registerScreenOff();
            registerActivityLifeCycle();

            reloadAdMetrics();
        }
    }

    private void registerActivityLifeCycle() {
        Application app = (Application) context;
        if (app != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            app.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                    if (mAdConfig.ad_ctrl.watch_oncreate == 1) {
                        if (!(activity instanceof AdActivity)
                                && !(activity instanceof AudienceNetworkActivity)
                                && !(activity instanceof InterstitialAdActivity)
                                && !(activity instanceof com.bestgo.adsplugin.ads.activity.PackageActivity)
                                && !(activity instanceof com.bestgo.adsplugin.ads.activity.AdActivity)
                                && !(activity instanceof com.bestgo.adsplugin.ads.activity.RecommendAdActivity)) {
                            boolean allowShow = true;
                            if (activity instanceof ShowAdFilter) {
                                allowShow = ((ShowAdFilter)activity).allowShowAd();
                            }
                            if (allowShow && isFullAdLoaded() && mAdConfig.ad_count_ctrl.last_failed_count > 0 && !mFullAdShowPending) {
                                mAdConfig.ad_count_ctrl.last_failed_count--;
                                mSP.edit().putInt("last_failed_count", mAdConfig.ad_count_ctrl.last_failed_count).apply();
                                showFullAd();
                            }
                        }
                    }
                    mAliveActivityCount++;
                }

                @Override
                public void onActivityStarted(Activity activity) {
                }

                @Override
                public void onActivityResumed(Activity activity) {
                }

                @Override
                public void onActivityPaused(Activity activity) {
                }

                @Override
                public void onActivityStopped(Activity activity) {
                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                    mAliveActivityCount--;
                }
            });
        }
    }

    private void initAliveTag(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String aliveTag = sp.getString("aliveTag", "");
        FIRST_ENTER_TIME = sp.getLong("FIRST_ENTER_TIME", -1);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT-8"));
        if (FIRST_ENTER_TIME == -1) {
            FIRST_ENTER_TIME = System.currentTimeMillis();
            SharedPreferences.Editor editor = sp.edit();
            editor.putLong("FIRST_ENTER_TIME", System.currentTimeMillis());
            editor.commit();
        }
        if (TextUtils.isEmpty(aliveTag)) {
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                calendar.setTimeInMillis(packageInfo.firstInstallTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            aliveTag = String.format(Locale.US, "%04d%02d%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("aliveTag", aliveTag);
            editor.putInt("last_alive_hour", calendar.get(Calendar.HOUR_OF_DAY));
            editor.commit();
            getFireBase().logEvent("存活记录", aliveTag);
            getFacebook().logEvent("app_install_date", aliveTag);
        } else {
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                calendar.setTimeInMillis(packageInfo.firstInstallTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            aliveTag = String.format(Locale.US, "%04d%02d%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("aliveTag", aliveTag);
            editor.commit();
        }
    }

    public void onResume() {
        UMGameAgent.onResume(context);
        mAdMobAd.onResume();
        mRecommendAd.onResume();

        if (!firstOnResume) {
            firstOnResume = true;
            return;
        }
        if (mAdConfig.resume_ctrl.exe == 1 && enableResumeAd) {
            if (isFullAdLoaded() && allowResumeAd) {
                showFullAd();
            } else {
                allowResumeAd = true;
            }
        }
    }

    public void onPause() {
        UMGameAgent.onPause(context);
        mAdMobAd.onPause();
        mRecommendAd.onPause();
    }

    public AdConfig getConfig() {
        return mAdConfig;
    }

    public AbstractFirebase getFireBase() {
        if (FIREBASE == null) {
            FIREBASE = new DebugFireBase();
        }
        return FIREBASE;
    }

    public AbstractFacebook getFacebook() {
        if (FACEBOOK == null) {
            FACEBOOK = new DebugFacebook();
        }
        return FACEBOOK;
    }

    private final static int INIT_AD = 1000;
    private final static int SHOW_FULL_AD = 1001;
    private final static int SHOW_FULL_AD_INDEX = 1002;
    private final static int LOAD_FULL_AD = 1003;
    private final static int LOAD_FULL_AD_INDEX = 1004;
    private final static int SHOW_FBN_AUTO_CLOSE = 1005;
    private final static int AUTO_LOAD_AD = 1007;
    private final static int SHOW_PACKAGE_AD = 1008;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == INIT_AD) {
                try {
                    initAd();
                } catch (Exception ex) {
                }
            } else if (msg.what == SHOW_FULL_AD) {
                showFullAdDelayed(-1);
            } else if (msg.what == SHOW_FULL_AD_INDEX) {
                int index = (int)msg.obj;
                showFullAdDelayed(index);
            } else if (msg.what == LOAD_FULL_AD) {
                loadNewFullAdInternal();
            } else if (msg.what == LOAD_FULL_AD_INDEX) {
                int index = (int)msg.obj;
                loadNewFullAdInternal(index);
            } else if (msg.what == SHOW_FBN_AUTO_CLOSE) {
                mAdConfig.ad_count_ctrl.last_screen_off_count++;
                SharedPreferences.Editor editor = mSP.edit();
                editor.putInt("last_screen_off_count", mAdConfig.ad_count_ctrl.last_screen_off_count);
                editor.commit();
                if (mAdConfig.ad_count_ctrl.last_screen_off_count <= mAdConfig.ad_ctrl.screen_off_count) {
                    mFacebookAd.showFBNAdAutoClose();
                }
            } else if (msg.what == AUTO_LOAD_AD) {
                if (mAliveActivityCount > 0) {
                    loadNewFullAdInternal();
                    loadNewBanner();
                    loadNewNative();
                }
                handler.sendEmptyMessageDelayed(AUTO_LOAD_AD, 1000*mAdConfig.ad_ctrl.auto_load);
            } else if (msg.what == SHOW_PACKAGE_AD) {
                mFacebookAd.showFBNAdPackage();
            }
        }
    };

    private void registerScreenOff() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (mAdConfig.ad_ctrl.screen_off == 1) {
                    mAdConfig.ad_count_ctrl.last_day = mSP.getInt("last_day", 0);
                    mAdConfig.ad_count_ctrl.last_screen_off_count = mSP.getInt("last_screen_off_count", 0);
                    if (mAdConfig.ad_count_ctrl.last_day != Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
                        SharedPreferences.Editor editor = mSP.edit();
                        editor.putInt("last_day", Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                        mAdConfig.ad_count_ctrl.last_screen_off_count = 0;
                        editor.putInt("last_screen_off_count", 0);
                        editor.commit();
                    }
                    if (mFacebookAd.isFBNLoaded()) {
                        handler.sendEmptyMessageDelayed(SHOW_FBN_AUTO_CLOSE, 1000);
                    } else if (mAdConfig.ad_count_ctrl.last_screen_off_count <= mAdConfig.ad_ctrl.screen_off_count) {
                        mFacebookAd.loadNewFBNAd();
                        mFacebookAd.loadNewInterstitial();
                    }
                }
            }
        }, intentFilter);
    }

    private void initUmeng() {
        UMGameAgent.init(context);
        UMGameAgent.setTraceSleepTime(false);
        UMGameAgent.setSessionContinueMillis(60000L);

        try {
            writeAssetsToSP();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        String adIds = UMGameAgent.getConfigParams(context, "admob_full_ids");
//        if (TextUtils.isEmpty(adIds)) {
//            try {
//                writeAssetsToSP();
//                SharedPreferences sp = context.getSharedPreferences("mobclick_agent_online_setting_" + context.getPackageName(), 0);
//                SharedPreferences.Editor editor = sp.edit();
//
//                adIds = context.getSharedPreferences("ourdefault_game_config", 0).getString("admob_full_ids", "");
//                editor.putString("admob_full_ids", adIds).apply();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        try {
            readConfig();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//        UMGameAgent.updateOnlineConfig(context);
//        UMGameAgent.setOnlineConfigureListener(new UmengOnlineConfigureListener() {
//            public void onDataReceived(JSONObject data) {
//                try {
//                    readConfig();
//                    handler.sendEmptyMessage(INIT_AD);
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            }
//        });
    }

    public AppAdUnitMetrics getAdUnitMetrics(String adUnitId) {
        return mMetricsMap.get(adUnitId);
    }

    public void reloadConfig() {
        try {
            readConfig();
            handler.sendEmptyMessage(INIT_AD);

            if (mAdConfig.ad_ctrl.auto_load > 1) {
                handler.removeMessages(AUTO_LOAD_AD);
                handler.sendEmptyMessageDelayed(AUTO_LOAD_AD, 1000 * mAdConfig.ad_ctrl.auto_load);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void reloadAdMetrics() {
        try {
            SharedPreferences sp = context.getSharedPreferences(AdAppHelper.SHARED_SP_NAME, 0);
            String metrics = sp.getString("ad_unit_metrics", "");
            JSONObject json = new JSONObject(metrics);
            JSONArray daily = json.optJSONArray("daily");
            JSONArray country = json.optJSONArray("country");
            if (daily != null) {
                for (int i = 0; i < daily.length(); i++) {
                    JSONObject one = daily.getJSONObject(i);
                    AppAdUnitMetrics metric = new AppAdUnitMetrics();
                    metric.adNetwork = one.getString("adNetwork");
                    metric.adUnitId = one.getString("adUnitId");
                    metric.adRequest = one.getLong("adRequest");
                    metric.adFilled = one.getLong("adFilled");
                    metric.adImpression = one.getLong("adImpression");
                    metric.adClicked = one.getLong("adClicked");
                    metric.adRevenue = one.getDouble("adRevenue");
                    metric.adECPM = metric.adImpression > 0 ? metric.adRevenue / metric.adImpression * 1000 : 0;
                    metric.adCTR = metric.adImpression > 0 ? metric.adClicked * 1.0f / metric.adImpression * 100 : 0;

                    metric.adDailyRequest = metric.adRequest;
                    metric.adDailyFilled = metric.adFilled;
                    metric.adDailyImpression = metric.adImpression;
                    metric.adDailyClicked = metric.adClicked;
                    metric.adDailyRevenue = metric.adRevenue;
                    metric.adDailyECPM = metric.adECPM;
                    metric.adDailyCTR = metric.adCTR;

                    mMetricsMap.put(metric.adUnitId, metric);
                }
            }
            if (country != null) {
                for (int i = 0; i < country.length(); i++) {
                    JSONObject one = country.getJSONObject(i);
                    AppAdUnitMetrics metric = mMetricsMap.get(one.getString("adUnitId"));
                    metric.adNetwork = one.getString("adNetwork");
                    metric.adUnitId = one.getString("adUnitId");
                    metric.adRequest = one.getLong("adRequest");
                    metric.adFilled = one.getLong("adFilled");
                    metric.adImpression = one.getLong("adImpression");
                    metric.adClicked = one.getLong("adClicked");
                    metric.adRevenue = one.getDouble("adRevenue");
                    metric.adECPM = metric.adImpression > 0 ? metric.adRevenue / metric.adImpression * 1000 : 0;
                    if (metric.adECPM == 0) {
                        metric.adECPM = metric.adDailyECPM;
                    }
                    metric.adCTR = metric.adImpression > 0 ? metric.adClicked * 1.0f / metric.adImpression * 100 : 0;

                    mMetricsMap.put(metric.adUnitId, metric);
                }
            }

            if (mAdConfig.ad_ctrl.auto_ctrl == 1) {
                ArrayList<AppAdUnitMetrics> list = new ArrayList<>(mMetricsMap.values());
                Collections.sort(list, new Comparator<AppAdUnitMetrics>() {
                    @Override
                    public int compare(AppAdUnitMetrics o1, AppAdUnitMetrics o2) {
                        if (o1.adECPM > o2.adECPM) {
                            return -1;
                        } else if (o1.adECPM < o2.adECPM) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                });
                try {
                    AdConfig.AdSeqCtrl seqCtrl = new AdConfig.AdSeqCtrl();
                    seqCtrl.list = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        if (mAdConfig.admob_full_ids.count > 0 && mAdConfig.admob_full_ids.ids != null) {
                            for (int ii = 0; ii < mAdConfig.admob_full_ids.count; ii++) {
                                AdConfig.AdUnitItem item = mAdConfig.admob_full_ids.ids[ii];
                                if (list.get(i).adUnitId.equals(item.id)) {
                                    AdConfig.AdSeqCtrl.AdSeqItem seqItem = new AdConfig.AdSeqCtrl.AdSeqItem();
                                    seqItem.type = AdConfig.AdSeqCtrl.TYPE_ADMOB;
                                    seqItem.index = ii;
                                    seqCtrl.list.add(seqItem);
                                    break;
                                }
                            }
                        }
                        if (mAdConfig.fbn_full_ids.count > 0 && mAdConfig.fbn_full_ids.ids != null) {
                            for (int ii = 0; ii < mAdConfig.fbn_full_ids.count; ii++) {
                                AdConfig.AdUnitItem item = mAdConfig.fbn_full_ids.ids[ii];
                                if (list.get(i).adUnitId.equals(item.id)) {
                                    AdConfig.AdSeqCtrl.AdSeqItem seqItem = new AdConfig.AdSeqCtrl.AdSeqItem();
                                    seqItem.type = AdConfig.AdSeqCtrl.TYPE_FBN;
                                    seqItem.index = ii;
                                    seqCtrl.list.add(seqItem);
                                    break;
                                }
                            }
                        }
                        if (mAdConfig.fb_full_ids.count > 0 && mAdConfig.fb_full_ids.ids != null) {
                            for (int ii = 0; ii < mAdConfig.fb_full_ids.count; ii++) {
                                AdConfig.AdUnitItem item = mAdConfig.fb_full_ids.ids[ii];
                                if (list.get(i).adUnitId.equals(item.id)) {
                                    AdConfig.AdSeqCtrl.AdSeqItem seqItem = new AdConfig.AdSeqCtrl.AdSeqItem();
                                    seqItem.type = AdConfig.AdSeqCtrl.TYPE_FACEBOOK;
                                    seqItem.index = ii;
                                    seqCtrl.list.add(seqItem);
                                    break;
                                }
                            }
                        }
                    }
                    if (seqCtrl.list.size() > 0) {
                        seqCtrl.exe = 1;
                        mAdConfig.ad_seq_ctrl = seqCtrl;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                try {
                    AdConfig.AdSeqCtrl seqCtrl = new AdConfig.AdSeqCtrl();
                    seqCtrl.list = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        if (mAdConfig.admob_banner_ids.count > 0 && mAdConfig.admob_banner_ids.ids != null) {
                            for (int ii = 0; ii < mAdConfig.admob_banner_ids.count; ii++) {
                                AdConfig.AdUnitItem item = mAdConfig.admob_banner_ids.ids[ii];
                                if (list.get(i).adUnitId.equals(item.id)) {
                                    AdConfig.AdSeqCtrl.AdSeqItem seqItem = new AdConfig.AdSeqCtrl.AdSeqItem();
                                    seqItem.type = AdConfig.AdSeqCtrl.TYPE_ADMOB;
                                    seqItem.index = ii;
                                    seqCtrl.list.add(seqItem);
                                    break;
                                }
                            }
                        }
                        if (mAdConfig.fbn_banner_ids.count > 0 && mAdConfig.fbn_banner_ids.ids != null) {
                            for (int ii = 0; ii < mAdConfig.fbn_banner_ids.count; ii++) {
                                AdConfig.AdUnitItem item = mAdConfig.fbn_banner_ids.ids[ii];
                                if (list.get(i).adUnitId.equals(item.id)) {
                                    AdConfig.AdSeqCtrl.AdSeqItem seqItem = new AdConfig.AdSeqCtrl.AdSeqItem();
                                    seqItem.type = AdConfig.AdSeqCtrl.TYPE_FBN;
                                    seqItem.index = ii;
                                    seqCtrl.list.add(seqItem);
                                    break;
                                }
                            }
                        }
                        if (list.get(i).adUnitId.equals(mAdConfig.banner_ids.fb)) {
                            AdConfig.AdSeqCtrl.AdSeqItem seqItem = new AdConfig.AdSeqCtrl.AdSeqItem();
                            seqItem.type = AdConfig.AdSeqCtrl.TYPE_FACEBOOK;
                            seqItem.index = 0;
                            seqCtrl.list.add(seqItem);
                            break;
                        }
                    }
                    if (seqCtrl.list.size() > 0) {
                        seqCtrl.exe = 1;
                        mAdConfig.ad_banner_seq_ctrl = seqCtrl;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                try {
                    ArrayList<AdConfig.AdSeqCtrl> seqCtrls = new ArrayList<>();
                    int maxLen = Math.max(mAdConfig.admob_native_ids.count, mAdConfig.fb_native_ids.count);
                    for (int j = 0;  j < maxLen; j++) {
                        AdConfig.AdSeqCtrl seqCtrl = new AdConfig.AdSeqCtrl();
                        seqCtrl.list = new ArrayList<>();
                        for (int i = 0; i < list.size(); i++) {
                            if (mAdConfig.admob_an_ids.count > 0 && mAdConfig.admob_an_ids.ids != null) {
                                for (int ii = 0; ii < mAdConfig.admob_an_ids.count; ii++) {
                                    AdConfig.AdUnitItem item = mAdConfig.admob_an_ids.ids[ii];
                                    if (list.get(i).adUnitId.equals(item.id)) {
                                        AdConfig.AdSeqCtrl.AdSeqItem seqItem = new AdConfig.AdSeqCtrl.AdSeqItem();
                                        seqItem.type = AdConfig.AdSeqCtrl.TYPE_ADMOB_AN;
                                        seqItem.index = ii;
                                        seqCtrl.list.add(seqItem);
                                        break;
                                    }
                                }
                            }
                            if (mAdConfig.admob_native_ids.count > 0 && mAdConfig.admob_native_ids.ids != null) {
                                for (int ii = 0; ii < mAdConfig.admob_native_ids.count; ii++) {
                                    AdConfig.AdUnitItem item = mAdConfig.admob_native_ids.ids[ii];
                                    if (list.get(i).adUnitId.equals(item.id)) {
                                        AdConfig.AdSeqCtrl.AdSeqItem seqItem = new AdConfig.AdSeqCtrl.AdSeqItem();
                                        seqItem.type = AdConfig.AdSeqCtrl.TYPE_ADMOB_EN;
                                        seqItem.index = ii;
                                        seqCtrl.list.add(seqItem);
                                        break;
                                    }
                                }
                            }
                            if (mAdConfig.fb_native_ids.count > 0 && mAdConfig.fb_native_ids.ids != null) {
                                for (int ii = 0; ii < mAdConfig.fb_native_ids.count; ii++) {
                                    AdConfig.AdUnitItem item = mAdConfig.fb_native_ids.ids[ii];
                                    if (list.get(i).adUnitId.equals(item.id)) {
                                        AdConfig.AdSeqCtrl.AdSeqItem seqItem = new AdConfig.AdSeqCtrl.AdSeqItem();
                                        seqItem.type = AdConfig.AdSeqCtrl.TYPE_FACEBOOK;
                                        seqItem.index = ii;
                                        seqCtrl.list.add(seqItem);
                                        break;
                                    }
                                }
                            }
                            if (mAdConfig.admob_banner_ids.count > 0 && mAdConfig.admob_banner_ids.ids != null) {
                                for (int ii = 0; ii < mAdConfig.admob_banner_ids.count; ii++) {
                                    AdConfig.AdUnitItem item = mAdConfig.admob_banner_ids.ids[ii];
                                    if (list.get(i).adUnitId.equals(item.id)) {
                                        AdConfig.AdSeqCtrl.AdSeqItem seqItem = new AdConfig.AdSeqCtrl.AdSeqItem();
                                        seqItem.type = AdConfig.AdSeqCtrl.TYPE_ADMOB;
                                        seqItem.index = ii;
                                        seqCtrl.list.add(seqItem);
                                        break;
                                    }
                                }
                            }
                        }
                        if (seqCtrl.list.size() > 0) {
                            seqCtrl.exe = 1;
                            seqCtrls.add(seqCtrl);
                        }
                    }
                    mAdConfig.ad_native_seq_ctrl = seqCtrls;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void _loadConfig() {
        try {
            mAdConfig = new AdConfig();
            mSP = context.getSharedPreferences(SHARED_SP_NAME, 0);
            FBCache.PACKAGE_NAME = context.getPackageName();
            readConfig();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void checkConfig() {
        try {
            readConfig();
        } catch (Exception ex) {
            throw ex;
        }
    }

    private synchronized void readConfig() {
        HashMap<String, String> kv = AdConfig.getValues(context, "banner_ids");
        mAdConfig.banner_ids.admob = AdConfig.getString(kv, "admob", "");
        mAdConfig.banner_ids.fb = AdConfig.getString(kv, "fb", "");

        kv = AdConfig.getValues(context, "admob_banner_ids");
        mAdConfig.admob_banner_ids.count = AdConfig.getInt(kv, "count", 0);
        if (mAdConfig.admob_banner_ids.count > 0) {
            mAdConfig.admob_banner_ids.ids = new AdConfig.AdUnitItem[mAdConfig.admob_banner_ids.count];
            for (int i = 0; i < mAdConfig.admob_banner_ids.count; i++) {
                String s = AdConfig.getString(kv, "id" + (i+1), "");
                try {
                    mAdConfig.admob_banner_ids.ids[i] = new AdConfig.AdUnitItem();
                    mAdConfig.admob_banner_ids.ids[i].id = s.split(",")[0];
                    mAdConfig.admob_banner_ids.ids[i].name = s.split(",")[1];
                } catch (Exception ex) {
                    ex.printStackTrace();
                    throw ex;
                }
            }
        } else {
            mAdConfig.admob_banner_ids.count = 1;
            mAdConfig.admob_banner_ids.ids = new AdConfig.AdUnitItem[mAdConfig.admob_banner_ids.count];
            mAdConfig.admob_banner_ids.ids[0] = new AdConfig.AdUnitItem();
            mAdConfig.admob_banner_ids.ids[0].id = mAdConfig.banner_ids.admob;
            mAdConfig.admob_banner_ids.ids[0].name = "";
        }

        kv = AdConfig.getValues(context, "video_ids");
        mAdConfig.video_ids.admob = AdConfig.getString(kv, "admob", "");

        kv = AdConfig.getValues(context, "resume_ctrl");
        mAdConfig.resume_ctrl.exe = AdConfig.getInt(kv, "exe", 0);

        kv = AdConfig.getValues(context, "admob_full_ids");
        mAdConfig.admob_full_ids.count = AdConfig.getInt(kv, "count", 0);
        if (mAdConfig.admob_full_ids.count > 0) {
            mAdConfig.admob_full_ids.ids = new AdConfig.AdUnitItem[mAdConfig.admob_full_ids.count];
        }
        for (int i = 0; i < mAdConfig.admob_full_ids.count; i++) {
            String s = AdConfig.getString(kv, "id" + (i+1), "");
            try {
                mAdConfig.admob_full_ids.ids[i] = new AdConfig.AdUnitItem();
                mAdConfig.admob_full_ids.ids[i].id = s.split(",")[0];
                mAdConfig.admob_full_ids.ids[i].name = s.split(",")[1];
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;
            }
        }

        kv = AdConfig.getValues(context, "fb_full_ids");
        mAdConfig.fb_full_ids.count = AdConfig.getInt(kv, "count", 0);
        if (mAdConfig.fb_full_ids.count > 0) {
            mAdConfig.fb_full_ids.ids = new AdConfig.AdUnitItem[mAdConfig.fb_full_ids.count];
        }
        for (int i = 0; i < mAdConfig.fb_full_ids.count; i++) {
            String s = AdConfig.getString(kv, "id" + (i+1), "");
            try {
                mAdConfig.fb_full_ids.ids[i] = new AdConfig.AdUnitItem();
                mAdConfig.fb_full_ids.ids[i].id = s.split(",")[0];
                mAdConfig.fb_full_ids.ids[i].name = s.split(",")[1];
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;
            }
        }

        kv = AdConfig.getValues(context, "fbn_full_ids");
        mAdConfig.fbn_full_ids.count = AdConfig.getInt(kv, "count", 0);
        if (mAdConfig.fbn_full_ids.count > 0) {
            mAdConfig.fbn_full_ids.ids = new AdConfig.AdUnitItem[mAdConfig.fbn_full_ids.count];
        }
        for (int i = 0; i < mAdConfig.fbn_full_ids.count; i++) {
            String s = AdConfig.getString(kv, "id" + (i+1), "");
            try {
                mAdConfig.fbn_full_ids.ids[i] = new AdConfig.AdUnitItem();
                mAdConfig.fbn_full_ids.ids[i].id = s.split(",")[0];
                mAdConfig.fbn_full_ids.ids[i].name = s.split(",")[1];
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;
            }
        }

        kv = AdConfig.getValues(context, "fbn_banner_ids");
        mAdConfig.fbn_banner_ids.count = AdConfig.getInt(kv, "count", 0);
        if (mAdConfig.fbn_banner_ids.count > 0) {
            mAdConfig.fbn_banner_ids.ids = new AdConfig.AdUnitItem[mAdConfig.fbn_banner_ids.count];
        }
        for (int i = 0; i < mAdConfig.fbn_banner_ids.count; i++) {
            String s = AdConfig.getString(kv, "id" + (i+1), "");
            try {
                mAdConfig.fbn_banner_ids.ids[i] = new AdConfig.AdUnitItem();
                mAdConfig.fbn_banner_ids.ids[i].id = s.split(",")[0];
                mAdConfig.fbn_banner_ids.ids[i].name = s.split(",")[1];
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;
            }
        }

        kv = AdConfig.getValues(context, "admob_an_ids");
        mAdConfig.admob_an_ids.count = AdConfig.getInt(kv, "count", 0);
        if (mAdConfig.admob_an_ids.count > 0) {
            mAdConfig.admob_an_ids.ids = new AdConfig.AdUnitItem[mAdConfig.admob_an_ids.count];
        }
        for (int i = 0; i < mAdConfig.admob_an_ids.count; i++) {
            String s = AdConfig.getString(kv, "id" + (i+1), "");
            try {
                mAdConfig.admob_an_ids.ids[i] = new AdConfig.AdUnitItem();
                mAdConfig.admob_an_ids.ids[i].id = s.split(",")[0];
                mAdConfig.admob_an_ids.ids[i].name = s.split(",")[1];
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;
            }
        }

        kv = AdConfig.getValues(context, "admob_native_ids");
        if (kv.isEmpty()) {
            mAdConfig.admob_native_ids.count = mAdConfig.admob_an_ids.count;
        } else {
            mAdConfig.admob_native_ids.count = AdConfig.getInt(kv, "count", 0);
        }
        if (mAdConfig.admob_native_ids.count > 0) {
            mAdConfig.admob_native_ids.ids = new AdConfig.AdUnitItem[mAdConfig.admob_native_ids.count];
        }
        for (int i = 0; i < mAdConfig.admob_native_ids.count; i++) {
            String s = AdConfig.getString(kv, "id" + (i+1), "");
            try {
                mAdConfig.admob_native_ids.ids[i] = new AdConfig.AdUnitItem();
                mAdConfig.admob_native_ids.ids[i].id = s.split(",")[0];
                mAdConfig.admob_native_ids.ids[i].name = s.split(",")[1];
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        kv = AdConfig.getValues(context, "admob_banner_native_ids");
        mAdConfig.admob_banner_native_ids.count = AdConfig.getInt(kv, "count", 0);
        if (mAdConfig.admob_banner_native_ids.count > 0) {
            mAdConfig.admob_banner_native_ids.ids = new AdConfig.AdUnitItem[mAdConfig.admob_banner_native_ids.count];
        }
        for (int i = 0; i < mAdConfig.admob_banner_native_ids.count; i++) {
            String s = AdConfig.getString(kv, "id" + (i+1), "");
            try {
                mAdConfig.admob_banner_native_ids.ids[i] = new AdConfig.AdUnitItem();
                mAdConfig.admob_banner_native_ids.ids[i].id = s.split(",")[0];
                mAdConfig.admob_banner_native_ids.ids[i].name = s.split(",")[1];
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;
            }
        }

        kv = AdConfig.getValues(context, "fb_native_ids");
        mAdConfig.fb_native_ids.count = AdConfig.getInt(kv, "count", 0);
        if (mAdConfig.fb_native_ids.count > 0) {
            mAdConfig.fb_native_ids.ids = new AdConfig.AdUnitItem[mAdConfig.fb_native_ids.count];
        }
        for (int i = 0; i < mAdConfig.fb_native_ids.count; i++) {
            String s = AdConfig.getString(kv, "id" + (i+1), "");
            try {
                mAdConfig.fb_native_ids.ids[i] = new AdConfig.AdUnitItem();
                mAdConfig.fb_native_ids.ids[i].id = s.split(",")[0];
                mAdConfig.fb_native_ids.ids[i].name = s.split(",")[1];
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;
            }
        }

        kv = AdConfig.getValues(context, "native_size");
        int size = AdConfig.getInt(kv, "count", 0);
        for (int i = 0; i < size; i++) {
            String s = AdConfig.getString(kv, "id" + (i + 1), "");
            String[] ss = s.split(",");
            int width = Integer.parseInt(ss[0]);
            int height = Integer.parseInt(ss[1]);
            if (i < mAdConfig.admob_native_ids.count) {
                if (NATIVE_ADMOB_WIDTH_LIST != null && i < NATIVE_ADMOB_WIDTH_LIST.length) {
                    mAdConfig.admob_native_ids.ids[i].width = NATIVE_ADMOB_WIDTH_LIST[i];
                } else {
                    mAdConfig.admob_native_ids.ids[i].width = width;
                }
                mAdConfig.admob_native_ids.ids[i].height = height;
            }
            if (i < mAdConfig.fb_native_ids.count) {
                mAdConfig.fb_native_ids.ids[i].width = width;
                mAdConfig.fb_native_ids.ids[i].height = height;
            }
            if (i < mAdConfig.admob_an_ids.count) {
                mAdConfig.admob_an_ids.ids[i].width = width;
                mAdConfig.admob_an_ids.ids[i].height = height;
            }
        }

        kv = AdConfig.getValues(context, "video_ctrl");
        mAdConfig.video_ctrl.exe = AdConfig.getInt(kv, "exe", 0);
        mAdConfig.video_ctrl.admob = AdConfig.getInt(kv, "admob", 100);

        kv = AdConfig.getValues(context, "news_ctrl");
        mAdConfig.news_ctrl.exe = AdConfig.getInt(kv, "exe", 1);
        mAdConfig.news_ctrl.admob = AdConfig.getInt(kv, "admob", 100);
        mAdConfig.news_ctrl.facebook = AdConfig.getInt(kv, "facebook", 100);

        kv = AdConfig.getValues(context, "news_ids");
        mAdConfig.news_ids.admob = AdConfig.getString(kv, "admob", "");
        mAdConfig.news_ids.fb = AdConfig.getString(kv, "fb", "");

        kv = AdConfig.getValues(context, "ngs_ctrl");
        mAdConfig.ngs_ctrl.exe = AdConfig.getInt(kv, "exe", 1);
        mAdConfig.ngs_ctrl.admob = AdConfig.getInt(kv, "admob", 100);
        mAdConfig.ngs_ctrl.facebook = AdConfig.getInt(kv, "facebook", 100);
        mAdConfig.ngs_ctrl.fbn = AdConfig.getInt(kv, "fbn", 100);

        kv= AdConfig.getValues(context, "banner_ctrl");
        mAdConfig.banner_ctrl.exe = AdConfig.getInt(kv, "exe", 1);
        mAdConfig.banner_ctrl.admob = AdConfig.getInt(kv, "admob", 100);
        mAdConfig.banner_ctrl.facebook = AdConfig.getInt(kv, "facebook", 100);
        mAdConfig.banner_ctrl.fbn = AdConfig.getInt(kv, "fbn", 100);

        kv = AdConfig.getValues(context, "native_ctrl");
        mAdConfig.native_ctrl.exe = AdConfig.getInt(kv, "exe", 1);
        mAdConfig.native_ctrl.admob = AdConfig.getInt(kv, "admob", 100);
        mAdConfig.native_ctrl.facebook = AdConfig.getInt(kv, "facebook", 100);
        mAdConfig.native_ctrl.admob_en = AdConfig.getInt(kv, "admob_en", 100);
        mAdConfig.native_ctrl.admob_an = AdConfig.getInt(kv, "admob_an", 100);

        kv = AdConfig.getValues(context, "ad_count_ctrl");
        //不控制显示次数了

        kv = AdConfig.getValues(context, "ad_ctrl");
        mAdConfig.ad_ctrl.ad_delay = AdConfig.getInt(kv, "ad_delay", 0);
        mAdConfig.ad_ctrl.ad_silent = AdConfig.getInt(kv, "ad_silent", 0);
        mAdConfig.ad_ctrl.ad_silent_native = AdConfig.getInt(kv, "ad_silent_native", 0);
        mAdConfig.ad_ctrl.fake_click = AdConfig.getInt(kv, "fake_click", 0);
        mAdConfig.ad_ctrl.only_cta = AdConfig.getInt(kv, "only_cta", 0);
        String value = AdConfig.getString(kv, "ngsorder", "");
        parseNgsOrder(mAdConfig.ad_ctrl.ngsorder, value);
        value = AdConfig.getString(kv, "ngsorder:admob", "");
        parseNgsOrder(mAdConfig.ad_ctrl.ngsorder_admob, value);
        mAdConfig.ad_ctrl.banner_click = AdConfig.getInt(kv, "banner_click", 100);
        mAdConfig.ad_ctrl.ngs_click = AdConfig.getInt(kv, "ngs_click", 100);
        mAdConfig.ad_ctrl.native_click = AdConfig.getInt(kv, "native_click", 100);
        mAdConfig.ad_ctrl.home_delay_time = AdConfig.getInt(kv, "home_delay_time", 5000);
        mAdConfig.ad_ctrl.enable_index_ngs = AdConfig.getInt(kv, "enable_index_ngs", 1);
        mAdConfig.ad_ctrl.admob_fail_reload_count = AdConfig.getInt(kv, "admob_fail_reload_count", 1);
        mAdConfig.ad_ctrl.fb_fail_reload_count = AdConfig.getInt(kv, "fb_fail_reload_count", 1);
        mAdConfig.ad_ctrl.screen_off = AdConfig.getInt(kv, "screen_off", 0);
        mAdConfig.ad_ctrl.screen_off_count = AdConfig.getInt(kv, "screen_off_count", 0);
        mAdConfig.ad_ctrl.screen_off_ngs = AdConfig.getInt(kv, "screen_off_ngs", 0);
        mAdConfig.ad_ctrl.full_ad_count = AdConfig.getInt(kv, "full_ad_count", 0);
        mAdConfig.ad_ctrl.immediate_show = AdConfig.getInt(kv, "immediate_show", 0);
        mAdConfig.ad_ctrl.reuse_cache = AdConfig.getInt(kv, "reuse_cache", 0);
        mAdConfig.ad_ctrl.fb_cache = AdConfig.getInt(kv, "fb_cache", 0);
        mAdConfig.ad_ctrl.delay_close = AdConfig.getInt(kv, "delay_close", 0);
        mAdConfig.ad_ctrl.cache_first = AdConfig.getInt(kv, "cache_first", 0);
        mAdConfig.ad_ctrl.max_delay_show = AdConfig.getInt(kv, "max_delay_show", 2000);
        mAdConfig.ad_ctrl.opt_type = AdConfig.getInt(kv, "opt_type", 7);
        mAdConfig.ad_ctrl.watch_oncreate = AdConfig.getInt(kv, "watch_oncreate", 1);
        mAdConfig.ad_ctrl.risk = AdConfig.getInt(kv, "risk", 0);
        mAdConfig.ad_ctrl.risk_rate = AdConfig.getInt(kv, "risk_rate", 100);
        mAdConfig.ad_ctrl.risk_n = AdConfig.getInt(kv, "risk_n", 0);
        mAdConfig.ad_ctrl.auto_risk = AdConfig.getInt(kv, "auto_risk", 0);
        mAdConfig.ad_ctrl.auto_risk_n = AdConfig.getInt(kv, "auto_risk_n", 0);
        mAdConfig.ad_ctrl.auto_load = AdConfig.getInt(kv, "auto_load", 0);
        mAdConfig.ad_ctrl.auto_ctrl = AdConfig.getInt(kv, "auto_ctrl", 1);
        mAdConfig.ad_ctrl.target_ctr = AdConfig.getInt(kv, "target_ctr", 10);

        kv = AdConfig.getValues(context, "auto_show_ctrl");
        mAdConfig.auto_show_ctrl.exe = AdConfig.getInt(kv, "exe", 0);
        mAdConfig.auto_show_ctrl.interval = AdConfig.getInt(kv, "interval", 900);
        mAdConfig.auto_show_ctrl.max_count = AdConfig.getInt(kv, "max_count", 0);
        mAdConfig.auto_show_ctrl.banner = AdConfig.getInt(kv, "banner", 0);
        mAdConfig.auto_show_ctrl.native_ = AdConfig.getInt(kv, "native", 0);
        mAdConfig.auto_show_ctrl.ngs = AdConfig.getInt(kv, "ngs", 0);

        kv = AdConfig.getValues(context, "cache_load_ctrl");
        mAdConfig.cache_load_ctrl.exe = AdConfig.getInt(kv, "exe", 0);
        mAdConfig.cache_load_ctrl.interval = AdConfig.getInt(kv, "interval", 900);
        mAdConfig.cache_load_ctrl.max_cache_count = AdConfig.getInt(kv, "max_cache_count", 0);
        mAdConfig.cache_load_ctrl.native_ = AdConfig.getInt(kv, "native", 0);
        mAdConfig.cache_load_ctrl.ngs = AdConfig.getInt(kv, "ngs", 0);

        if (mAdConfig.ad_ctrl.auto_ctrl != 1) {
            kv = AdConfig.getValues(context, "ad_seq_ctrl");
            mAdConfig.ad_seq_ctrl.exe = AdConfig.getInt(kv, "exe", 0);
            mAdConfig.ad_seq_ctrl.list = new ArrayList<>();
            value = AdConfig.getString(kv, "seq", "");
            try {
                Pattern pattern = Pattern.compile("([a-z]+)(\\d+)");
                String[] seqs = value.split(",");
                for (int i = 0; i < seqs.length; i++) {
                    String one = seqs[i];
                    Matcher matcher = pattern.matcher(one);
                    if (matcher.find() && matcher.groupCount() == 2) {
                        String type = matcher.group(1);
                        String index = matcher.group(2);
                        if (type.equalsIgnoreCase("admob")) {
                            AdConfig.AdSeqCtrl.AdSeqItem item = new AdConfig.AdSeqCtrl.AdSeqItem();
                            item.type = AdConfig.AdSeqCtrl.TYPE_ADMOB;
                            item.index = Integer.parseInt(index) - 1;
                            mAdConfig.ad_seq_ctrl.list.add(item);
                        } else if (type.equalsIgnoreCase("fb")) {
                            AdConfig.AdSeqCtrl.AdSeqItem item = new AdConfig.AdSeqCtrl.AdSeqItem();
                            item.type = AdConfig.AdSeqCtrl.TYPE_FACEBOOK;
                            item.index = Integer.parseInt(index) - 1;
                            mAdConfig.ad_seq_ctrl.list.add(item);
                        } else if (type.equalsIgnoreCase("fbn")) {
                            AdConfig.AdSeqCtrl.AdSeqItem item = new AdConfig.AdSeqCtrl.AdSeqItem();
                            item.type = AdConfig.AdSeqCtrl.TYPE_FBN;
                            item.index = Integer.parseInt(index) - 1;
                            mAdConfig.ad_seq_ctrl.list.add(item);
                        }
                    }
                }
            } catch (Exception ex) {
            }

            kv = AdConfig.getValues(context, "ad_banner_seq_ctrl");
            mAdConfig.ad_banner_seq_ctrl.exe = AdConfig.getInt(kv, "exe", 0);
            mAdConfig.ad_banner_seq_ctrl.list = new ArrayList<>();
            value = AdConfig.getString(kv, "seq", "");
            try {
                Pattern pattern = Pattern.compile("([a-z]+)(\\d+)");
                String[] seqs = value.split(",");
                for (int i = 0; i < seqs.length; i++) {
                    String one = seqs[i];
                    Matcher matcher = pattern.matcher(one);
                    if (matcher.find() && matcher.groupCount() == 2) {
                        String type = matcher.group(1);
                        String index = matcher.group(2);
                        if (type.equalsIgnoreCase("admob")) {
                            AdConfig.AdSeqCtrl.AdSeqItem item = new AdConfig.AdSeqCtrl.AdSeqItem();
                            item.type = AdConfig.AdSeqCtrl.TYPE_ADMOB;
                            item.index = Integer.parseInt(index) - 1;
                            mAdConfig.ad_banner_seq_ctrl.list.add(item);
                        } else if (type.equalsIgnoreCase("fb")) {
                            AdConfig.AdSeqCtrl.AdSeqItem item = new AdConfig.AdSeqCtrl.AdSeqItem();
                            item.type = AdConfig.AdSeqCtrl.TYPE_FACEBOOK;
                            item.index = Integer.parseInt(index) - 1;
                            mAdConfig.ad_banner_seq_ctrl.list.add(item);
                        } else if (type.equalsIgnoreCase("fbn")) {
                            AdConfig.AdSeqCtrl.AdSeqItem item = new AdConfig.AdSeqCtrl.AdSeqItem();
                            item.type = AdConfig.AdSeqCtrl.TYPE_FBN;
                            item.index = Integer.parseInt(index) - 1;
                            mAdConfig.ad_banner_seq_ctrl.list.add(item);
                        }
                    }
                }
            } catch (Exception ex) {
            }
        }
        kv = AdConfig.getValues(context, "custom_ctrl");
        for (String key : kv.keySet()) {
            mAdConfig.custom_ctrl.params.put(key, kv.get(key));
        }

        kv = AdConfig.getValues(context, "fb_ad_gallery");
        mAdConfig.fb_ad_gallery.exe = AdConfig.getInt(kv, "exe", 1);
        mAdConfig.fb_ad_gallery.fb = AdConfig.getString(kv, "fb", "");

        kv = AdConfig.getValues(context, "splash_ctrl");
        mAdConfig.splash_ctrl.exe = AdConfig.getInt(kv, "exe", 1);
        mAdConfig.splash_ctrl.fb = AdConfig.getString(kv, "fb", "");
        mAdConfig.splash_ctrl.admob = AdConfig.getString(kv, "admob", "");

        kv = AdConfig.getValues(context, "recommend_ctrl");
        mAdConfig.recommend_ctrl.exe = AdConfig.getInt(kv, "exe", 0);
        mAdConfig.recommend_ctrl.count = AdConfig.getInt(kv, "count", 0);
        mAdConfig.recommend_ctrl.show_random = AdConfig.getInt(kv, "show_random", 0);
        mAdConfig.recommend_ctrl.fake_click = AdConfig.getInt(kv, "fake_click", 0);
        if (mAdConfig.recommend_ctrl.count > 0) {
            if (mAdConfig.recommend_ctrl.list == null || mAdConfig.recommend_ctrl.count != mAdConfig.recommend_ctrl.list.length) {
                mAdConfig.recommend_ctrl.list = new AdConfig.RecommendAdItem[mAdConfig.recommend_ctrl.count];
                for (int i = 0; i < mAdConfig.recommend_ctrl.count; i++) {
                    mAdConfig.recommend_ctrl.list[i] = new AdConfig.RecommendAdItem();
                }
            }
            for (int i = 0; i < mAdConfig.recommend_ctrl.count; i++) {
                String data = AdConfig.getString(kv, "ad" + (i+1), "");
                if (!TextUtils.isEmpty(data)) {
                    String[] pairs = data.split("\\|");
                    if (pairs.length == 8 || pairs.length == 9) {
                        AdConfig.RecommendAdItem item = mAdConfig.recommend_ctrl.list[i];
                        item.enabled = pairs[0].equals("1") ? true : false;
                        item.app_id = pairs[1];
                        item.title = pairs[2];
                        item.sub_title = pairs[3];
                        item.action_title = pairs[4];
                        item.icon_url = pairs[5];
                        item.image_url = pairs[6];
                        item.link_url = pairs[7];
                        if (pairs.length == 9) {
                            item.not_play_link = pairs[8].equals("1") ? true : false;
                        } else {
                            item.not_play_link = false;
                        }
                        continue;
                    } else if (pairs.length == 3) {
                        AdConfig.RecommendAdItem item = mAdConfig.recommend_ctrl.list[i];
                        item.enabled = pairs[0].equals("1") ? true : false;
                        String format = pairs[1];
                        if ("webview".equals(format)) {
                            item.is_webview = true;
                        }
                        item.link_url = pairs[2];
                        continue;
                    }
                }
                mAdConfig.recommend_ctrl.list[i].enabled = false;
            }
        }
        mAdConfig.recommend_ctrl.native_count = AdConfig.getInt(kv, "native_count", 0);
        if (mAdConfig.recommend_ctrl.native_count > 0) {
            if (mAdConfig.recommend_ctrl.native_list == null || mAdConfig.recommend_ctrl.native_count != mAdConfig.recommend_ctrl.native_list.length) {
                mAdConfig.recommend_ctrl.native_list = new AdConfig.RecommendNativeAdItem[mAdConfig.recommend_ctrl.native_count];
                for (int i = 0; i < mAdConfig.recommend_ctrl.native_count; i++) {
                    mAdConfig.recommend_ctrl.native_list[i] = new AdConfig.RecommendNativeAdItem();
                }
            }
            for (int i = 0; i < mAdConfig.recommend_ctrl.native_count; i++) {
                String data = AdConfig.getString(kv, "native_ad" + (i+1), "");
                if (!TextUtils.isEmpty(data)) {
                    String[] pairs = data.split("\\|");
                    if (pairs.length == 9 || pairs.length == 10) {
                        AdConfig.RecommendNativeAdItem item = mAdConfig.recommend_ctrl.native_list[i];
                        item.enabled = pairs[0].equals("1") ? true : false;
                        item.app_id = pairs[1];
                        item.title = pairs[2];
                        item.sub_title = pairs[3];
                        item.action_title = pairs[4];
                        item.icon_url = pairs[5];
                        item.image_url = pairs[6];
                        item.link_url = pairs[7];
                        String s = pairs[8];
                        try {
                            String[] ss = s.split(",");
                            item.width = Integer.parseInt(ss[0]);
                            item.height = Integer.parseInt(ss[1]);
                        } catch (NumberFormatException e) {
                        }
                        if (pairs.length == 10) {
                            item.not_play_link = pairs[9].equals("1") ? true : false;
                        } else {
                            item.not_play_link = false;
                        }
                        continue;
                    } else if (pairs.length == 4) {
                        AdConfig.RecommendNativeAdItem item = mAdConfig.recommend_ctrl.native_list[i];
                        item.enabled = pairs[0].equals("1") ? true : false;
                        String format = pairs[1];
                        if ("webview".equals(format)) {
                            item.is_webview = true;
                        }
                        item.link_url = pairs[2];
                        String s = pairs[3];
                        try {
                            String[] ss = s.split(",");
                            item.width = Integer.parseInt(ss[0]);
                            item.height = Integer.parseInt(ss[1]);
                        } catch (NumberFormatException e) {
                        }
                        continue;
                    }
                }
                mAdConfig.recommend_ctrl.native_list[i].enabled = false;
            }
        }

        mAdConfig.ad_count_ctrl.last_day = mSP.getInt("last_day", 0);
        mAdConfig.ad_count_ctrl.last_screen_off_count = mSP.getInt("last_screen_off_count", 0);
        mAdConfig.ad_count_ctrl.last_risk_count = mSP.getInt("last_risk_count", 0);
        mAdConfig.ad_count_ctrl.last_risk_native_count = mSP.getInt("last_risk_native_count", 0);
        mAdConfig.ad_count_ctrl.last_failed_count = mSP.getInt("last_failed_count", 0);
        mAdConfig.ad_count_ctrl.last_full_show_count = mSP.getInt("last_full_show_count", 0);
        if (mAdConfig.ad_count_ctrl.last_day != Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
            SharedPreferences.Editor editor = mSP.edit();
            editor.putInt("last_day", Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            mAdConfig.ad_count_ctrl.last_screen_off_count = 0;
            mAdConfig.ad_count_ctrl.last_risk_count = 0;
            mAdConfig.ad_count_ctrl.last_risk_native_count = 0;
            mAdConfig.ad_count_ctrl.last_failed_count = 0;
            mAdConfig.ad_count_ctrl.last_full_show_count = 0;
            editor.putInt("last_screen_off_count", 0);
            editor.putInt("last_risk_count", 0);
            editor.putInt("last_risk_native_count", 0);
            editor.putInt("last_failed_count", 0);
            editor.putInt("last_full_show_count", 0);
            editor.commit();
        }
    }

    public boolean allowRisk() {
        int r = new Random().nextInt(100);
        return mAdConfig.ad_count_ctrl.last_risk_count < mAdConfig.ad_ctrl.risk && r <= mAdConfig.ad_ctrl.risk_rate;
    }
    public boolean allowRiskNative() {
        int r = new Random().nextInt(100);
        return mAdConfig.ad_count_ctrl.last_risk_native_count < mAdConfig.ad_ctrl.risk_n && r <= mAdConfig.ad_ctrl.risk_rate;
    }
    public boolean allowAutoRisk() {
        int r = new Random().nextInt(100);
        return mAdConfig.ad_count_ctrl.last_risk_count < mAdConfig.ad_ctrl.auto_risk && r <= mAdConfig.ad_ctrl.risk_rate;
    }
    public boolean allowAutoRiskNative() {
        int r = new Random().nextInt(100);
        return mAdConfig.ad_count_ctrl.last_risk_native_count < mAdConfig.ad_ctrl.auto_risk_n && r <= mAdConfig.ad_ctrl.risk_rate;
    }

    public void increaseRiskCount() {
        SharedPreferences.Editor editor = mSP.edit();
        mAdConfig.ad_count_ctrl.last_risk_count++;
        editor.putInt("last_risk_count", 0);
        editor.commit();
    }
    public void increaseRiskNativeCount() {
        SharedPreferences.Editor editor = mSP.edit();
        mAdConfig.ad_count_ctrl.last_risk_native_count++;
        editor.putInt("last_risk_native_count", 0);
        editor.commit();
    }

    private void parseNgsOrder(AdConfig.AdCtrl.NgsOrder ngsOrder, String value) {
        String[] ngsorder = value.split("\\|");
        for (int i = 0; i < ngsorder.length; i++) {
            String one = ngsorder[i];
            if (one.contains("before:")) {
                String vv = one.replace("before:", "");
                try {
                    ngsOrder.before = Integer.parseInt(vv);
                } catch (Exception ex) {
                }
            } else if (one.contains("adt:")) {
                String vv = one.replace("adt:", "");
                try {
                    ngsOrder.adt = Integer.parseInt(vv);
                } catch (Exception ex) {
                }
            } else if (one.contains("adtype=")) {
                String vv = one.replace("adtype=", "");
                try {
                    ngsOrder.adt_type = Integer.parseInt(vv);
                } catch (Exception ex) {
                }
            }
        }
    }

    private void initAd() {
        if (mBannerLayout == null) {
            mBannerLayout = new MyFrameLayout(context);
        }
        if (mNativeLayout == null) {
            mNativeLayout = new MyFrameLayout(context);
        }
        if (mNativeLayouts == null) {
            int len = Math.max(mAdConfig.admob_native_ids.count, mAdConfig.fb_native_ids.count);
            mNativeLayouts = new MyFrameLayout[len];
            for (int i = 0; i < len; i++) {
                mNativeLayouts[i] = new MyFrameLayout(context);
            }
        }
        if (mAdMobAd == null) {
            mAdMobAd = new AdMobAd(context);
            mAdMobAd.setBannerEnabled(mAdConfig.banner_ctrl.exe == 1 && mAdConfig.banner_ctrl.admob > 0);
            mAdMobAd.setNativeEnabled(mAdConfig.native_ctrl.exe == 1 && mAdConfig.native_ctrl.admob > 0);
            mAdMobAd.setNativeENEnabled(mAdConfig.native_ctrl.exe == 1 && mAdConfig.native_ctrl.admob_en > 0);
            mAdMobAd.setNativeANEnabled(mAdConfig.native_ctrl.exe == 1 && mAdConfig.native_ctrl.admob_an > 0);
            mAdMobAd.setInterstitialEnabled(mAdConfig.ngs_ctrl.exe == 1 && mAdConfig.ngs_ctrl.admob > 0);
            mAdMobAd.setVideoEnabled(mAdConfig.video_ctrl.exe == 1 && mAdConfig.video_ctrl.admob > 0);
        } else {
            mAdMobAd.setBannerEnabled(mAdConfig.banner_ctrl.exe == 1 && mAdConfig.banner_ctrl.admob > 0);
            mAdMobAd.setNativeEnabled(mAdConfig.native_ctrl.exe == 1 && mAdConfig.native_ctrl.admob > 0);
            mAdMobAd.setNativeENEnabled(mAdConfig.native_ctrl.exe == 1 && mAdConfig.native_ctrl.admob_en > 0);
            mAdMobAd.setNativeANEnabled(mAdConfig.native_ctrl.exe == 1 && mAdConfig.native_ctrl.admob_an > 0);
            mAdMobAd.setInterstitialEnabled(mAdConfig.ngs_ctrl.exe == 1 && mAdConfig.ngs_ctrl.admob > 0);
            mAdMobAd.setVideoEnabled(mAdConfig.video_ctrl.exe == 1 && mAdConfig.video_ctrl.admob > 0);
            mAdMobAd.resetId();
        }

        if (mFacebookAd == null) {
            mFacebookAd = new FacebookAd(context);
            mFacebookAd.setBannerEnabled(mAdConfig.banner_ctrl.exe == 1 && mAdConfig.banner_ctrl.facebook > 0);
            mFacebookAd.setNativeEnabled(mAdConfig.native_ctrl.exe == 1 && mAdConfig.native_ctrl.facebook > 0);
            mFacebookAd.setInterstitialEnabled(mAdConfig.ngs_ctrl.exe == 1 && mAdConfig.ngs_ctrl.facebook > 0);
            mFacebookAd.setFBNEnabled(mAdConfig.ngs_ctrl.exe == 1 && mAdConfig.ngs_ctrl.fbn > 0);
            mFacebookAd.setFBNBannerEnabled(mAdConfig.banner_ctrl.exe == 1 && mAdConfig.banner_ctrl.fbn > 0);
        } else {
            mFacebookAd.setBannerEnabled(mAdConfig.banner_ctrl.exe == 1 && mAdConfig.banner_ctrl.facebook > 0);
            mFacebookAd.setNativeEnabled(mAdConfig.native_ctrl.exe == 1 && mAdConfig.native_ctrl.facebook > 0);
            mFacebookAd.setInterstitialEnabled(mAdConfig.ngs_ctrl.exe == 1 && mAdConfig.ngs_ctrl.facebook > 0);
            mFacebookAd.setFBNEnabled(mAdConfig.ngs_ctrl.exe == 1 && mAdConfig.ngs_ctrl.fbn > 0);
            mFacebookAd.setFBNBannerEnabled(mAdConfig.banner_ctrl.exe == 1 && mAdConfig.banner_ctrl.fbn > 0);
            mFacebookAd.resetId();
        }
        if (mFBGallery == null) {
            mFBGallery = new FBAdGallery(context);
        }
        if (mFBSplashAd == null) {
            mFBSplashAd = new FBSplashAd(context);
        }
        if (mAdMobSplashAd == null) {
            mAdMobSplashAd = new AdMobSplashAd(context);
        }
        mAdMobSplashAd.resetId();
        mFBGallery.resetId();
        mFBSplashAd.resetId();
        mFBGallery.setAdListener(listener);
        mAdMobAd.setAdListener(listener);
        mFacebookAd.setAdListener(listener);

        if (mNewsAd == null) {
            mNewsAd = new NewsAd();
        }
        mNewsAd.init(context);

        if (mRecommendAd == null) {
            mRecommendAd = new RecommendAd();
            mRecommendAd.init(context);
            mRecommendAd.setAdListener(listener);
        }
    }

    public AdStateListener getInnerListener() {
        return listener;
    }
    public void setAdStateListener(AdStateListener listener) {
        outerListener = listener;
    }
    public void setNewsListener(NewsListener listener) {
        mNewsAd.setNewsListener(listener);
    }
    public void setRewardedListener(RewardedListener listener) {
        mAdMobAd.setRewardedListener(listener);
    }
    private AdStateListener outerListener;
    private AdStateListener listener = new AdStateListener() {
        @Override
        public void onAdLoaded(AdType adType, int index) {
            switch (adType.getType()) {
                case AdType.ADMOB_BANNER:
                case AdType.FACEBOOK_BANNER:
                case AdType.FACEBOOK_FBN_BANNER:
                    addBanner();
                    break;
                case AdType.ADMOB_NATIVE:
                case AdType.ADMOB_NATIVE_EN:
                case AdType.FACEBOOK_NATIVE:
                case AdType.ADMOB_NATIVE_AN:
                    addNative(index);
                    break;
                case AdType.ADMOB_FULL:
                case AdType.FACEBOOK_FULL:
                case AdType.FACEBOOK_FBN:
                case AdType.ADMOB_VIDEO_AD:
                    if (popupPendingQueue() && !mFullAdShowPending) {
                        handler.sendEmptyMessageDelayed(SHOW_FULL_AD, 0);
                    }
                    break;
            }
            if (outerListener != null) {
                outerListener.onAdLoaded(adType, index);
            }
        }

        @Override
        public void onAdOpen(AdType adType, int index) {
            switch (adType.getType()) {
                case AdType.ADMOB_BANNER:
                    break;
                case AdType.ADMOB_NATIVE:
                    break;
                case AdType.FACEBOOK_BANNER:
                    break;
                case AdType.FACEBOOK_NATIVE:
                    break;
                case AdType.ADMOB_FULL:
                case AdType.FACEBOOK_FULL:
                case AdType.FACEBOOK_FBN:
                case AdType.RECOMMEND_AD:
                case AdType.ADMOB_VIDEO_AD:
                    mFullAdShowing = true;
                    mLastShowFullTime = SystemClock.elapsedRealtime();
                    mAdConfig.ad_count_ctrl.last_full_show_count++;
                    mSP.edit().putInt("last_full_show_count", mAdConfig.ad_count_ctrl.last_full_show_count).apply();
                    break;
                case AdType.FACEBOOK_FBN_BANNER:
                    break;
            }
            if (outerListener != null) {
                outerListener.onAdOpen(adType, index);
            }
        }

        @Override
        public void onAdClosed(AdType adType, int index) {
            switch (adType.getType()) {
                case AdType.ADMOB_FULL:
                case AdType.FACEBOOK_FULL:
                case AdType.FACEBOOK_FBN:
                case AdType.RECOMMEND_AD:
                case AdType.ADMOB_VIDEO_AD:
                    mFullAdShowing = false;
                    mFullAdShowPending = false;
//                    allowResumeAd = false;
                    break;
            }
            if (outerListener != null) {
                outerListener.onAdClosed(adType, index);
            }
        }

        @Override
        public void onAdLoadFailed(AdType adType, int index, String reason) {
            if (outerListener != null) {
                outerListener.onAdLoadFailed(adType, index, reason);
            }
        }

        @Override
        public void onAdClick(AdType adType, int index) {
            if (outerListener != null) {
                outerListener.onAdClick(adType, index);
            }
        }
    };

    private void addNative(int index) {
        try {
            ViewGroup parent = (ViewGroup) mNativeLayout.getParent();
            ViewGroup.LayoutParams params = mNativeLayout.getLayoutParams();
            if (parent != null) {
                getNative(parent, params);
                AbstractAnimator animator = mNativeLayout.getAnimator();
                if (animator != null) {
                    animator.run(parent);
                    mNativeLayout.setAnimator(null);
                }
            }

            parent = (ViewGroup) mNativeLayouts[index].getParent();
            params = mNativeLayouts[index].getLayoutParams();
            if (parent != null) {
                getNative(index, parent, params);
                AbstractAnimator animator = mNativeLayouts[index].getAnimator();
                if (animator != null) {
                    animator.run(parent);
                    mNativeLayouts[index].setAnimator(null);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addBanner() {
        try {
            ViewGroup parent = (ViewGroup) mBannerLayout.getParent();
            ViewGroup.LayoutParams params = mBannerLayout.getLayoutParams();
            if (parent != null) {
                View banner = getBanner();
                if (banner != null) {
                    parent.addView(banner, params);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setEnableResumeAd(boolean flag) {
        enableResumeAd = flag;
    }

    public void setNewsClickIntent(Intent intent) {
        if (mNewsAd != null) {
            mNewsAd.setClickIntent(intent);
        }
    }

    public boolean isNetworkConnected(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            for (int i = 0; i < networkInfo.length; i++) {
                if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED){
                    return true;
                }
            }
        } catch (Exception ex) {
        }
        return false;
    }

    public View getBanner() {
        View adView = null;

        if (mAdConfig.ad_banner_seq_ctrl.exe == 1 && mAdConfig.ad_banner_seq_ctrl.list.size() > 0) {
            for (int i = 0; i < mAdConfig.ad_banner_seq_ctrl.list.size(); i++) {
                AdConfig.AdSeqCtrl.AdSeqItem item = mAdConfig.ad_banner_seq_ctrl.list.get(i);
                switch (item.type) {
                    case AdConfig.AdSeqCtrl.TYPE_ADMOB:
                        if (mAdMobAd.isBannerLoaded(item.index)) {
                            adView = mAdMobAd.getBanner(item.index);
                        }
                        break;
                    case AdConfig.AdSeqCtrl.TYPE_FACEBOOK:
                        if (mFacebookAd.isBannerLoaded()) {
                            adView = mFacebookAd.getBanner();
                        }
                        break;
                    case AdConfig.AdSeqCtrl.TYPE_FBN:
                        if (mFacebookAd.isFBNBannerLoaded(item.index)) {
                            adView = mFacebookAd.getBannerFBN(item.index);
                        }
                        break;
                }
                if (adView != null) {
                    break;
                }
            }
        }

        if (adView == null) {
            int max = mAdConfig.banner_ctrl.facebook + mAdConfig.banner_ctrl.admob + mAdConfig.banner_ctrl.fbn;
            if (max < 1) {
                max = 1;
            }
            Random random = new Random();
            int r = random.nextInt(max);
            if (r >= (mAdConfig.banner_ctrl.facebook + mAdConfig.banner_ctrl.admob) && mFacebookAd.isFBNBannerLoaded()) {
                adView = mFacebookAd.getBannerFBN();
            } else if (r > mAdConfig.banner_ctrl.admob && r <= (max - mAdConfig.banner_ctrl.admob - mAdConfig.banner_ctrl.fbn)
                    && mFacebookAd.isBannerLoaded()) {
                adView = mFacebookAd.getBanner();
            } else if (mFacebookAd.isBannerLoaded()) {
                adView = mFacebookAd.getBanner();
            } else {
                adView = mAdMobAd.getBanner();
            }
        }

        ViewGroup parent = (ViewGroup)mBannerLayout.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }
        mBannerLayout.removeAllViews();
        if (adView != null) {
            ViewGroup adParent = (ViewGroup)adView.getParent();
            if (adParent != null) {
                adParent.removeView(adView);
            }
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
            mBannerLayout.addView(adView, params);
        }
        return mBannerLayout;
    }

    public View getNative() {
        View adView = null;
        if (mAdConfig.ad_native_seq_ctrl.size() > 0) {
            AdConfig.AdSeqCtrl seqCtrl = mAdConfig.ad_native_seq_ctrl.get(0);
            if (seqCtrl.exe == 1 && seqCtrl.list.size() > 0) {
                for (int i = 0; i < seqCtrl.list.size(); i++) {
                    AdConfig.AdSeqCtrl.AdSeqItem item = seqCtrl.list.get(i);
                    switch (item.type) {
                        case AdConfig.AdSeqCtrl.TYPE_ADMOB:
                            if (mAdMobAd.isNativeLoaded()) {
                                adView = mAdMobAd.getNative();
                            }
                            break;
                        case AdConfig.AdSeqCtrl.TYPE_FACEBOOK:
                            if (mFacebookAd.isNativeLoaded()) {
                                adView = mFacebookAd.getNative();
                            }
                            break;
                        case AdConfig.AdSeqCtrl.TYPE_ADMOB_EN:
                            if (mAdMobAd.isNativeENLoaded()) {
                                adView = mAdMobAd.getNativeEN();
                            }
                            break;
                        case AdConfig.AdSeqCtrl.TYPE_ADMOB_AN:
                            if (mAdMobAd.isNativeANLoaded()) {
                                adView = mAdMobAd.getNativeAN();
                            }
                            break;
                    }
                    if (adView != null) {
                        break;
                    }
                }
            }
        }

        if (adView == null) {
            int max = 0;
            int index = -1;
            ArrayList<Integer> list = new ArrayList<>();
            ArrayList<Integer> list1 = new ArrayList<>();
            if (index == -1 ? mFacebookAd.isNativeLoaded() : mFacebookAd.isNativeLoaded(index)) {
                list.add(AdType.FACEBOOK_NATIVE);
                list1.add(mAdConfig.native_ctrl.facebook);
                max += mAdConfig.native_ctrl.facebook;
            }
            if (index == -1 ? mAdMobAd.isNativeLoaded() : mAdMobAd.isNativeLoaded(index)) {
                list.add(AdType.ADMOB_NATIVE);
                list1.add(mAdConfig.native_ctrl.admob);
                max += mAdConfig.native_ctrl.admob;
            }
            if (index == -1 ? mAdMobAd.isNativeENLoaded() : mAdMobAd.isNativeENLoaded(index)) {
                list.add(AdType.ADMOB_NATIVE_EN);
                list1.add(mAdConfig.native_ctrl.admob_en);
                max += mAdConfig.native_ctrl.admob_en;
            }
            if (index == -1 ? mAdMobAd.isNativeANLoaded() : mAdMobAd.isNativeANLoaded(index)) {
                list.add(AdType.ADMOB_NATIVE_AN);
                list1.add(mAdConfig.native_ctrl.admob_an);
                max += mAdConfig.native_ctrl.admob_an;
            }
            if (max < 1) {
                max = 1;
            }

            Random random = new Random();
            int r = random.nextInt(max);
            int offset = 0;
            for (int i = 0; i < list.size(); i++) {
                offset += list1.get(i);
                if (r <= offset) {
                    switch (list.get(i)) {
                        case AdType.FACEBOOK_NATIVE: {
                            if (mFacebookAd.isNativeLoaded()) {
                                adView = mFacebookAd.getNative();
                            }
                        }
                        break;
                        case AdType.ADMOB_NATIVE: {
                            if (mAdMobAd.isNativeLoaded()) {
                                adView = mAdMobAd.getNative();
                            }
                        }
                        break;
                        case AdType.ADMOB_NATIVE_AN: {
                            if (mAdMobAd.isNativeANLoaded()) {
                                adView = mAdMobAd.getNativeAN();
                            }
                        }
                        break;
                        case AdType.ADMOB_NATIVE_EN: {
                            if (mAdMobAd.isNativeENLoaded()) {
                                adView = mAdMobAd.getNativeEN();
                            }
                        }
                        break;
                    }
                    break;
                }
            }
        }
        ViewGroup parent = (ViewGroup)mNativeLayout.getParent();
        if (parent != null) {
            parent.removeView(mNativeLayout);
        }
        mNativeLayout.removeAllViews();
        if (adView != null) {
            ViewGroup adParent = (ViewGroup)adView.getParent();
            if (adParent != null) {
                adParent.removeView(adView);
            }
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
            mNativeLayout.addView(adView, params);
        }
        return mNativeLayout;
    }

    public boolean getNative(ViewGroup viewGroup, ViewGroup.LayoutParams params) {
        if (viewGroup == null || params == null) {
            return false;
        }

        boolean loaded = false;
        viewGroup.removeAllViews();
        View view = getNative();
        View recommend = getRecommendNativeView();

        if (view != null) {
            viewGroup.removeAllViews();
            viewGroup.addView(view, params);
            loaded = true;
        } else {
            int height = 0;
            if (mAdConfig.admob_native_ids.ids != null && 0 < mAdConfig.admob_native_ids.ids.length) {
                height = mAdConfig.admob_native_ids.ids[0].height;
            }
            if (recommend != null && height >= NativeAdSize.SIZE_150) {
                viewGroup.addView(recommend, params);
                loaded = true;
            }
            if (recommend != null) {
                viewGroup.addView(recommend, params);
                loaded = true;
            }
            ViewGroup parent = (ViewGroup) mNativeLayout.getParent();
            if (parent != null) {
                parent.removeView(mNativeLayout);
            }
            viewGroup.addView(mNativeLayout, params);
        }
        return loaded;
    }

    public void getNative(ViewGroup viewGroup, ViewGroup.LayoutParams params, AbstractAnimator animator) {
        boolean loaded = getNative(viewGroup, params);
        if (loaded && animator != null) {
            animator.run(viewGroup);
        } else {
            mNativeLayout.setAnimator(animator);
        }
    }

    public View getNative(int index) {
        View adView = null;
        if (index < mAdConfig.ad_native_seq_ctrl.size()) {
            AdConfig.AdSeqCtrl seqCtrl = mAdConfig.ad_native_seq_ctrl.get(index);
            if (seqCtrl.exe == 1 && seqCtrl.list.size() > 0) {
                for (int i = 0; i < seqCtrl.list.size(); i++) {
                    AdConfig.AdSeqCtrl.AdSeqItem item = seqCtrl.list.get(i);
                    switch (item.type) {
                        case AdConfig.AdSeqCtrl.TYPE_ADMOB:
                            if (mAdMobAd.isNativeLoaded(index)) {
                                adView = mAdMobAd.getNative(index);
                            }
                            break;
                        case AdConfig.AdSeqCtrl.TYPE_FACEBOOK:
                            if (mFacebookAd.isNativeLoaded(index)) {
                                adView = mFacebookAd.getNative(index);
                            }
                            break;
                        case AdConfig.AdSeqCtrl.TYPE_ADMOB_EN:
                            if (mAdMobAd.isNativeENLoaded(index)) {
                                adView = mAdMobAd.getNativeEN(index);
                            }
                            break;
                        case AdConfig.AdSeqCtrl.TYPE_ADMOB_AN:
                            if (mAdMobAd.isNativeANLoaded(index)) {
                                adView = mAdMobAd.getNativeAN(index);
                            }
                            break;
                    }
                    if (adView != null) {
                        break;
                    }
                }
            }
        }

        if (adView == null) {
            int max = 0;
            ArrayList<Integer> list = new ArrayList<>();
            ArrayList<Integer> list1 = new ArrayList<>();
            if (index == -1 ? mFacebookAd.isNativeLoaded() : mFacebookAd.isNativeLoaded(index)) {
                list.add(AdType.FACEBOOK_NATIVE);
                list1.add(mAdConfig.native_ctrl.facebook);
                max += mAdConfig.native_ctrl.facebook;
            }
            if (index == -1 ? mAdMobAd.isNativeLoaded() : mAdMobAd.isNativeLoaded(index)) {
                list.add(AdType.ADMOB_NATIVE);
                list1.add(mAdConfig.native_ctrl.admob);
                max += mAdConfig.native_ctrl.admob;
            }
            if (index == -1 ? mAdMobAd.isNativeENLoaded() : mAdMobAd.isNativeENLoaded(index)) {
                list.add(AdType.ADMOB_NATIVE_EN);
                list1.add(mAdConfig.native_ctrl.admob_en);
                max += mAdConfig.native_ctrl.admob_en;
            }
            if (index == -1 ? mAdMobAd.isNativeANLoaded() : mAdMobAd.isNativeANLoaded(index)) {
                list.add(AdType.ADMOB_NATIVE_AN);
                list1.add(mAdConfig.native_ctrl.admob_an);
                max += mAdConfig.native_ctrl.admob_an;
            }
            if (max < 1) {
                max = 1;
            }

            Random random = new Random();
            int r = random.nextInt(max);
            int offset = 0;
            for (int i = 0; i < list.size(); i++) {
                offset += list1.get(i);
                if (r <= offset) {
                    switch (list.get(i)) {
                        case AdType.FACEBOOK_NATIVE: {
                            if (mFacebookAd.isNativeLoaded(index)) {
                                adView = mFacebookAd.getNative(index);
                            }
                        }
                        break;
                        case AdType.ADMOB_NATIVE: {
                            if (mAdMobAd.isNativeLoaded(index)) {
                                adView = mAdMobAd.getNative(index);
                            }
                        }
                        break;
                        case AdType.ADMOB_NATIVE_AN: {
                            if (mAdMobAd.isNativeANLoaded(index)) {
                                adView = mAdMobAd.getNativeAN(index);
                            }
                        }
                        break;
                        case AdType.ADMOB_NATIVE_EN: {
                            if (mAdMobAd.isNativeENLoaded(index)) {
                                adView = mAdMobAd.getNativeEN(index);
                            }
                        }
                        break;
                    }
                    break;
                }
            }
        }
        if (adView != null && (mNativeLayouts != null && index < mNativeLayouts.length)) {
            ViewGroup parent = (ViewGroup) mNativeLayouts[index].getParent();
            if (parent != null) {
                parent.removeView(mNativeLayouts[index]);
            }
            mNativeLayouts[index].removeAllViews();

            ViewGroup adParent = (ViewGroup)adView.getParent();
            if (adParent != null) {
                adParent.removeView(adView);
            }
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
            mNativeLayouts[index].addView(adView, params);
            return mNativeLayouts[index];
        }
        return null;
    }

    public boolean getNative(int index, ViewGroup viewGroup, ViewGroup.LayoutParams params) {
        if (viewGroup == null || params == null) {
            return false;
        }

        boolean loaded = false;
        viewGroup.removeAllViews();
        View view = getNative(index);
        View recommend = getRecommendNativeView();

        if (view != null) {
            viewGroup.removeAllViews();
            viewGroup.addView(view, params);
            loaded = true;
        } else {
            int height = 0;
            if (mAdConfig.admob_native_ids.ids != null && index < mAdConfig.admob_native_ids.ids.length) {
                height = mAdConfig.admob_native_ids.ids[index].height;
            }
            if (recommend != null && height >= NativeAdSize.SIZE_150) {
                viewGroup.addView(recommend, params);
                loaded = true;
            }
            if (index < mNativeLayouts.length) {
                ViewGroup parent = (ViewGroup) mNativeLayouts[index].getParent();
                if (parent != null) {
                    parent.removeView(mNativeLayouts[index]);
                }
                viewGroup.addView(mNativeLayouts[index], params);
            }
        }

        return loaded;
    }

    public void getNative(int index, ViewGroup viewGroup, ViewGroup.LayoutParams params, AbstractAnimator animator) {
        boolean loaded = getNative(index, viewGroup, params);
        if (loaded && animator != null) {
            animator.run(viewGroup);
        } else {
            if (index < mNativeLayouts.length) {
                mNativeLayouts[index].setAnimator(animator);
            }
        }
    }

    public boolean isNativeLoaded() {
        return mAdMobAd.isNativeANLoaded() || mAdMobAd.isNativeENLoaded() || mAdMobAd.isNativeLoaded() || mFacebookAd.isNativeLoaded();
    }

    public boolean isNativeLoaded(int index) {
        return  mAdMobAd.isNativeANLoaded(index) || mAdMobAd.isNativeENLoaded(index) || mAdMobAd.isNativeLoaded(index) || mFacebookAd.isNativeLoaded(index);
    }

    public boolean isFullAdLoaded() {
        return mFacebookAd.isInterstitialLoaded() || mAdMobAd.isInterstitialLoaded() || mFacebookAd.isFBNLoaded();
    }

    public boolean isFullAdLoaded(int index) {
        if (mAdConfig.ad_ctrl.enable_index_ngs == 1) {
            return mFacebookAd.isInterstitialLoaded(index) || mAdMobAd.isInterstitialLoaded(index) || mFacebookAd.isFBNLoaded(index);
        } else {
            return isFullAdLoaded();
        }
    }

    public void showFBNPakcageAd() {
        if (mFacebookAd.isFBNLoaded()) {
            handler.sendEmptyMessageDelayed(SHOW_PACKAGE_AD, 0);
        } else {
            mFacebookAd.loadNewFBNAd();
        }
    }

    public void showFullAd() {
        allowResumeAd = false;
        Random random = new Random();
        if (mAdConfig.ad_ctrl.ngsorder.adt_type == 1 && (mFacebookAd.isFBNLoaded() || mFacebookAd.isInterstitialLoaded())) {
            int fbn_show_count = mSP.getInt("fbn_show_count", 0);
            int facebook_show_count = mSP.getInt("facebook_show_count", 0);
            if (mAdConfig.ad_ctrl.ngsorder.before >= (fbn_show_count + facebook_show_count)) {
                handler.sendEmptyMessageDelayed(SHOW_FULL_AD, 0);
            } else {
                int r = mAdConfig.ad_ctrl.ngsorder.adt > 0 ? random.nextInt(mAdConfig.ad_ctrl.ngsorder.adt) : 0;
                handler.sendEmptyMessageDelayed(SHOW_FULL_AD, r);
            }
        } else if (mAdConfig.ad_ctrl.ngsorder_admob.adt_type == 1 && mAdMobAd.isInterstitialLoaded()) {
            int admob_show_count = mSP.getInt("admob_show_count", 0);
            if (mAdConfig.ad_ctrl.ngsorder_admob.before >= admob_show_count) {
                handler.sendEmptyMessageDelayed(SHOW_FULL_AD, 0);
            } else {
                int r = mAdConfig.ad_ctrl.ngsorder_admob.adt > 0 ? random.nextInt(mAdConfig.ad_ctrl.ngsorder_admob.adt) : 0;
                handler.sendEmptyMessageDelayed(SHOW_FULL_AD, r);
            }
        } else {
            int r = mAdConfig.ad_ctrl.ad_delay > 0 ? random.nextInt(mAdConfig.ad_ctrl.ad_delay) : 0;
            handler.sendEmptyMessageDelayed(SHOW_FULL_AD, r);
        }
    }

    public void showFullAd(int index) {
        if (mAdConfig.ad_ctrl.enable_index_ngs != 1) {
            showFullAd();
            return;
        }
        allowResumeAd = false;
        Random random = new Random();
        if (mAdConfig.ad_ctrl.ngsorder.adt_type == 1 && (mFacebookAd.isFBNLoaded(index) || mFacebookAd.isInterstitialLoaded(index))) {
            int fbn_show_count = mSP.getInt("fbn_show_count", 0);
            int facebook_show_count = mSP.getInt("facebook_show_count", 0);
            if (mAdConfig.ad_ctrl.ngsorder.before >= (fbn_show_count + facebook_show_count)) {
                handler.sendMessageDelayed(handler.obtainMessage(SHOW_FULL_AD_INDEX, index), 0);
            } else {
                int r = mAdConfig.ad_ctrl.ngsorder.adt > 0 ? random.nextInt(mAdConfig.ad_ctrl.ngsorder.adt) : 0;
                handler.sendMessageDelayed(handler.obtainMessage(SHOW_FULL_AD_INDEX, index), r);
            }
        } else if (mAdConfig.ad_ctrl.ngsorder_admob.adt_type == 1 && mAdMobAd.isInterstitialLoaded(index)) {
            int admob_show_count = mSP.getInt("admob_show_count", 0);
            if (mAdConfig.ad_ctrl.ngsorder_admob.before >= admob_show_count) {
                handler.sendMessageDelayed(handler.obtainMessage(SHOW_FULL_AD_INDEX, index), 0);
            } else {
                int r = mAdConfig.ad_ctrl.ngsorder_admob.adt > 0 ? random.nextInt(mAdConfig.ad_ctrl.ngsorder_admob.adt) : 0;
                handler.sendMessageDelayed(handler.obtainMessage(SHOW_FULL_AD_INDEX, index), r);
            }
        } else {
            int r = mAdConfig.ad_ctrl.ad_delay > 0 ? random.nextInt(mAdConfig.ad_ctrl.ad_delay) : 0;
            handler.sendMessageDelayed(handler.obtainMessage(SHOW_FULL_AD_INDEX, index), r);
        }
    }

    private void showFullAdDelayed(int index) {
        if (mFullAdShowing && (SystemClock.elapsedRealtime() - mLastShowFullTime) < 1000 * 30) {
            getFireBase().logEvent(Const.CATEGORY_AD, "全屏广告正在显示");
            return;
        }

        if (mAdConfig.ad_seq_ctrl.exe == 1 && mAdConfig.ad_seq_ctrl.list.size() > 0) {
            for (int i = 0; i < mAdConfig.ad_seq_ctrl.list.size(); i++) {
                AdConfig.AdSeqCtrl.AdSeqItem item = mAdConfig.ad_seq_ctrl.list.get(i);
                switch (item.type) {
                    case AdConfig.AdSeqCtrl.TYPE_ADMOB:
                        if (mAdMobAd.isInterstitialLoaded(item.index)) {
                            mFullAdShowPending = true;
                            mAdMobAd.showInterstitial(item.index);
                            if (mAdConfig.ad_ctrl.auto_ctrl == 1) {
                                AdAppHelper.getInstance(context).getFireBase().logEvent(Const.CATEGORY_AD, "AUTO_CTRL", "全屏AdMob" + item.index);
                            }
                            return;
                        }
                        break;
                    case AdConfig.AdSeqCtrl.TYPE_FACEBOOK:
                        if (mFacebookAd.isInterstitialLoaded(item.index)) {
                            mFullAdShowPending = true;
                            mFacebookAd.showInterstitial(item.index);
                            if (mAdConfig.ad_ctrl.auto_ctrl == 1) {
                                AdAppHelper.getInstance(context).getFireBase().logEvent(Const.CATEGORY_AD, "AUTO_CTRL", "全屏Facebook" + item.index);
                            }
                            return;
                        }
                        break;
                    case AdConfig.AdSeqCtrl.TYPE_FBN:
                        if (mFacebookAd.isFBNLoaded(item.index)) {
                            mFullAdShowPending = true;
                            mFacebookAd.showFBNAd(item.index);
                            if (mAdConfig.ad_ctrl.auto_ctrl == 1) {
                                AdAppHelper.getInstance(context).getFireBase().logEvent(Const.CATEGORY_AD, "AUTO_CTRL", "全屏FBN" + item.index);
                            }
                            return;
                        }
                        break;
                }
            }
        }

        int max = 0;
        ArrayList<Integer> list = new ArrayList<>();
        ArrayList<Integer> list1 = new ArrayList<>();
        if (index == -1 ? mFacebookAd.isFBNLoaded() : mFacebookAd.isFBNLoaded(index)) {
            list.add(AdType.FACEBOOK_FBN);
            list1.add(mAdConfig.ngs_ctrl.fbn);
            max += mAdConfig.ngs_ctrl.fbn;
        }
        if (index == -1 ? mFacebookAd.isInterstitialLoaded() : mFacebookAd.isInterstitialLoaded(index)) {
            list.add(AdType.FACEBOOK_FULL);
            list1.add(mAdConfig.ngs_ctrl.facebook);
            max += mAdConfig.ngs_ctrl.facebook;
        }
        if (index == -1 ? mAdMobAd.isInterstitialLoaded() : mAdMobAd.isInterstitialLoaded(index)) {
            list.add(AdType.ADMOB_FULL);
            list1.add(mAdConfig.ngs_ctrl.admob);
            max += mAdConfig.ngs_ctrl.admob;
        }
        if (max < 1) {
            max = 1;
        }

        Random random = new Random();
        int r = random.nextInt(max);
        int offset = 0;
        for (int i = 0; i < list.size(); i++) {
            offset += list1.get(i);
            if (r <= offset) {
                switch (list.get(i)) {
                    case AdType.FACEBOOK_FBN: {
                        if (index == -1) {
                            mFacebookAd.showFBNAd();
                        } else {
                            mFacebookAd.showFBNAd(index);
                        }
                        mFullAdShowPending = true;
                        int fbn_show_count = mSP.getInt("fbn_show_count", 0);
                        SharedPreferences.Editor editor = mSP.edit();
                        editor.putInt("fbn_show_count", ++fbn_show_count).apply();
                    }
                    break;
                    case AdType.FACEBOOK_FULL: {
                        if (index == -1) {
                            mFacebookAd.showInterstitial();
                        } else {
                            mFacebookAd.showInterstitial(index);
                        }
                        mFullAdShowPending = true;
                        int facebook_show_count = mSP.getInt("facebook_show_count", 0);
                        SharedPreferences.Editor editor = mSP.edit();
                        editor.putInt("facebook_show_count", ++facebook_show_count).apply();
                    }
                    break;
                    case AdType.ADMOB_FULL: {
                        if (index == -1) {
                            mAdMobAd.showInterstitial();
                        } else {
                            mAdMobAd.showInterstitial(index);
                        }
                        mFullAdShowPending = true;
                        int admob_show_count = mSP.getInt("admob_show_count", 0);
                        SharedPreferences.Editor editor = mSP.edit();
                        editor.putInt("admob_show_count", ++admob_show_count).apply();
                    }
                    break;
                }
                break;
            }
        }
        if (list.isEmpty()) {
            if (mRecommendAd.isFullAdLoaded()) {
                mFullAdShowPending = true;
                mRecommendAd.showFullAd();
            } else {
                addPendingQueue();
            }
            mAdConfig.ad_count_ctrl.last_failed_count++;
            mSP.edit().putInt("last_failed_count", mAdConfig.ad_count_ctrl.last_failed_count).apply();
            if (index == -1) {
                loadNewInterstitial();
            } else {
                loadNewInterstitial(index);
            }
        }
    }

    public void showVideoAd() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (mAdMobAd.isVideoLoaded()) {
                    allowResumeAd = false;
                    mAdMobAd.showRewardVideo();
                }
            }
        });
    }

    private void addPendingQueue() {
        mPendingShowQueue.add(SystemClock.elapsedRealtime());
    }

    private boolean popupPendingQueue() {
        Long time = mPendingShowQueue.poll();
        while (time != null) {
            if (time != null && (SystemClock.elapsedRealtime() - time) <= mAdConfig.ad_ctrl.max_delay_show) {
                return true;
            }
            time = mPendingShowQueue.poll();
        }
        return false;
    }

    //请求广告
    public void loadNewBanner() {
        if (mAdConfig.ad_ctrl.ad_silent_native > 0) {
            if ((System.currentTimeMillis() - FIRST_ENTER_TIME) / 1000 < mAdConfig.ad_ctrl.ad_silent_native) {
                return;
            }
        }
        mAdMobAd.loadNewBanner();
        mFacebookAd.loadNewBanner();
        mFacebookAd.loadNewFBNBanner();
    }

    public void loadNewNative() {
        if (mAdConfig.ad_ctrl.ad_silent_native > 0) {
            if ((System.currentTimeMillis() - FIRST_ENTER_TIME) / 1000 < mAdConfig.ad_ctrl.ad_silent_native) {
                return;
            }
        }
        mAdMobAd.loadNewNativeAd();
        mAdMobAd.loadNewNativeENAd();
        mAdMobAd.loadNewNativeANAd();
        mFacebookAd.loadNewNativeAd();

        mRecommendAd.loadNewNativeAd();
    }

    public void loadNewNative(boolean autoShow) {
        if (mAdConfig.ad_ctrl.ad_silent_native > 0) {
            if ((System.currentTimeMillis() - FIRST_ENTER_TIME) / 1000 < mAdConfig.ad_ctrl.ad_silent_native) {
                return;
            }
        }
        mFacebookAd.loadNewNativeAd(autoShow);
    }

    public void loadNewInterstitial() {
        if (mAdConfig.ad_ctrl.ad_silent > 0) {
            if ((System.currentTimeMillis() - FIRST_ENTER_TIME) / 1000 < mAdConfig.ad_ctrl.ad_silent) {
                return;
            }
        }
        handler.sendEmptyMessage(LOAD_FULL_AD);
    }

    public void loadNewFBNAutoShow() {
        if (mAdConfig.ad_ctrl.ad_silent > 0) {
            if ((System.currentTimeMillis() - FIRST_ENTER_TIME) / 1000 < mAdConfig.ad_ctrl.ad_silent) {
                return;
            }
        }
        mFacebookAd.loadNewFBNAd(true);
    }

    private void loadNewFullAdInternal() {
        mAdMobAd.loadNewInterstitial();
        mFacebookAd.loadNewInterstitial();
        mFacebookAd.loadNewFBNAd();
        mRecommendAd.loadNewFullAd();
    }

    public void loadNewNative(int index) {
        if (mAdConfig.ad_ctrl.ad_silent_native > 0) {
            if ((System.currentTimeMillis() - FIRST_ENTER_TIME) / 1000 < mAdConfig.ad_ctrl.ad_silent_native) {
                return;
            }
        }
        mAdMobAd.loadNewNativeAd(index);
        mAdMobAd.loadNewNativeENAd(index);
        mAdMobAd.loadNewNativeANAd(index);
        mFacebookAd.loadNewNativeAd(index, false);

        mRecommendAd.loadNewNativeAd();
    }

    public void loadNewInterstitial(int index) {
        if (mAdConfig.ad_ctrl.ad_silent > 0) {
            if ((System.currentTimeMillis() - FIRST_ENTER_TIME) / 1000 < mAdConfig.ad_ctrl.ad_silent) {
                return;
            }
        }
        handler.sendMessage(handler.obtainMessage(LOAD_FULL_AD_INDEX, index));
    }

    private void loadNewFullAdInternal(int index) {
        mAdMobAd.loadNewInterstitial(index);
        mFacebookAd.loadNewInterstitial(index);
        mFacebookAd.loadNewFBNAd(index, false);
        mRecommendAd.loadNewFullAd();
    }

    public boolean isSplashReady() {
        return mFBSplashAd.isLoaded() || mAdMobSplashAd.isLoaded();
    }

    public void loadNewSplashAd() {
        mFBSplashAd.loadNewNativeAd();
        mAdMobSplashAd.loadNewNativeAd();
    }

    public void loadNewRewardedVideoAd() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                mAdMobAd.loadNewVideo();
            }
        });
    }
    public boolean isVideoReady() {
        return mAdMobAd.isVideoLoaded();
    }

    public View getSplashAd() {
        AppAdUnitMetrics fbMetric = mMetricsMap.get(mAdConfig.splash_ctrl.fb);
        AppAdUnitMetrics admobMetric = mMetricsMap.get(mAdConfig.splash_ctrl.admob);
        ArrayList<Integer> orders = new ArrayList<>();
        boolean b = false;
        if (fbMetric != null && admobMetric != null) {
            if (fbMetric.adECPM > admobMetric.adECPM) {
                b = true;
            }
        } else {
            Random random = new Random();
            b = random.nextBoolean();
        }
        if (b) {
            orders.add(AdConfig.AdSeqCtrl.TYPE_FACEBOOK);
            orders.add(AdConfig.AdSeqCtrl.TYPE_ADMOB);
        } else {
            orders.add(AdConfig.AdSeqCtrl.TYPE_ADMOB);
            orders.add(AdConfig.AdSeqCtrl.TYPE_FACEBOOK);
        }
        for (int i = 0; i < orders.size(); i++) {
            switch (orders.get(i)) {
                case AdConfig.AdSeqCtrl.TYPE_FACEBOOK:
                    if (mFBSplashAd.isLoaded()) {
                        View view = mFBSplashAd.getNativeView();
                        ViewGroup parent = (ViewGroup) view.getParent();
                        if (parent != null) {
                            parent.removeView(view);
                        }
                        return view;
                    }
                    break;
                case AdConfig.AdSeqCtrl.TYPE_ADMOB:
                    if (mAdMobSplashAd.isLoaded()) {
                        View view = mAdMobSplashAd.getNativeView();
                        ViewGroup parent = (ViewGroup) view.getParent();
                        if (parent != null) {
                            parent.removeView(view);
                        }
                        return view;
                    }
                    break;
            }
        }
        if (b) {
            View view = mFBSplashAd.getNativeView();
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        } else {
            View view = mAdMobSplashAd.getNativeView();
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }
    }

    public boolean isFBGalleryReady() {
        return mFBGallery.isLoaded();
    }

    public void loadNewFBGallery() {
        mFBGallery.loadNewNativeAd();
    }

    public View getFBGalleryView() {
        View view = mFBGallery.getNativeView();
        ViewGroup parent = (ViewGroup)view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    public boolean isNewsReady() {
        return mNewsAd.isReady();
    }

    public void loadNews() {
        mNewsAd.loadNews();
    }

    public View getNewsView() {
        return mNewsAd.getNewsView();
    }

    public boolean isAdSilent() {
        if (mAdConfig.ad_ctrl.ad_silent > 0) {
            if ((System.currentTimeMillis() - FIRST_ENTER_TIME) / 1000 < mAdConfig.ad_ctrl.ad_silent) {
                return true;
            }
        }
        return false;
    }

    public boolean isNativeAdSilent() {
        if (mAdConfig.ad_ctrl.ad_silent_native > 0) {
            if ((System.currentTimeMillis() - FIRST_ENTER_TIME) / 1000 < mAdConfig.ad_ctrl.ad_silent_native) {
                return true;
            }
        }
        return false;
    }

    public View getRecommendNativeView() {
        return mRecommendAd.getNative(-1);
    }

    public View getRecommendNativeView(int height) {
        return mRecommendAd.getNative(height);
    }

    public String getCustomCtrlValue(String key, String defaultValue) {
        String value = mAdConfig.custom_ctrl.params.get(key);
        if (TextUtils.isEmpty(value)) {
            return defaultValue;
        } else {
            return value;
        }
    }
}
