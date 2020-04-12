package com.bestgo.adsplugin.ads.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.bestgo.adsplugin.R;
import com.bestgo.adsplugin.ads.AdAppHelper;
import com.bestgo.adsplugin.ads.AdConfig;
import com.bestgo.adsplugin.ads.cache.FBCache;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.NativeAd;
import com.facebook.ads.internal.server.AdPlacementType;

public class B extends Service {
    private final static int GRAY_SERVICE_ID = 1002;
    private final static String LOAD_AD_CACHE_CACTION = "com.bestgo.adplugin.ads.LOAD_AD_CACHE_CACTION";
    private long mInterval;
    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter filter = new IntentFilter();
        filter.addAction(LOAD_AD_CACHE_CACTION);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                loadCacheAd();
            }
        }, filter);

        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
                startForeground(GRAY_SERVICE_ID, new Notification());
            } else {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
                    Intent innerIntent = new Intent(this, CC.class);
                    startService(innerIntent);
                    startForeground(GRAY_SERVICE_ID, new Notification());
                }
            }
        } catch (Exception ex) {
        }

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                Process.killProcess(Process.myPid());
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                BB.start(getApplicationContext());
            }
            schedule();
        } catch (Exception ex) {
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void schedule() {
        AdAppHelper.getInstance(getApplicationContext())._loadConfig();
        AdConfig config = AdAppHelper.getInstance(getApplicationContext()).getConfig();
        long interval = config.cache_load_ctrl.interval * 1000;
        if (interval <= 0) {
            interval = 1000 * 60;
        }
        if (interval != mInterval) {
            mInterval = interval;
            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0x101, new Intent(LOAD_AD_CACHE_CACTION), PendingIntent.FLAG_UPDATE_CURRENT);
            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000 * 5, interval, pendingIntent);
        }
    }

    private void loadCacheAd() {
        try {
            AdConfig config = AdAppHelper.getInstance(getApplicationContext()).getConfig();
            if (config.cache_load_ctrl.exe == 1) {
                if (config.cache_load_ctrl.native_ == 1) {
                    loadFBNative();
                }
                if (config.cache_load_ctrl.ngs == 1) {
                    loadFBInterstitial();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadFBNative() {
        AdConfig config = AdAppHelper.getInstance(getApplicationContext()).getConfig();
        if (config.native_ctrl.exe == 1 && config.native_ctrl.facebook >= 0) {
            if (config.fb_native_ids.count > 0 && config.fb_native_ids.ids != null) {
                for (int i = 0; i < config.fb_native_ids.ids.length; i++) {
                    AdConfig.AdUnitItem item = config.fb_native_ids.ids[i];
                    if (!TextUtils.isEmpty(item.id)) {
                        int count = FBCache.getCacheCount(AdPlacementType.NATIVE.toString(), item.id);
                        if (count < config.cache_load_ctrl.max_cache_count) {
                            NativeAd ad = new NativeAd(getApplicationContext(), item.id);
                            ad.loadAd();
                        }
                    }
                }
            }
        }
    }

    private void loadFBInterstitial() {
        AdConfig config = AdAppHelper.getInstance(getApplicationContext()).getConfig();
        if (config.ngs_ctrl.exe == 1 && config.ngs_ctrl.facebook >= 0) {
            if (config.fb_full_ids.count > 0 && config.fb_full_ids.ids != null) {
                for (int i = 0; i < config.fb_full_ids.ids.length; i++) {
                    AdConfig.AdUnitItem item = config.fb_full_ids.ids[i];
                    if (!TextUtils.isEmpty(item.id)) {
                        int count = FBCache.getCacheCount(AdPlacementType.INTERSTITIAL.toString(), item.id);
                        if (count < config.cache_load_ctrl.max_cache_count) {
                            InterstitialAd ad = new InterstitialAd(getApplicationContext(), item.id);
                            ad.loadAd();
                        }
                    }
                }
            }
        }
        if (config.ngs_ctrl.exe == 1 && config.ngs_ctrl.fbn >= 0) {
            if (config.fbn_full_ids.count > 0 && config.fbn_full_ids.ids != null) {
                for (int i = 0; i < config.fbn_full_ids.ids.length; i++) {
                    AdConfig.AdUnitItem item = config.fbn_full_ids.ids[i];
                    if (!TextUtils.isEmpty(item.id)) {
                        int count = FBCache.getCacheCount(AdPlacementType.NATIVE.toString(), item.id);
                        if (count < config.cache_load_ctrl.max_cache_count) {
                            NativeAd ad = new NativeAd(getApplicationContext(), item.id);
                            ad.loadAd();
                        }
                    }
                }
            }
        }
    }
}
