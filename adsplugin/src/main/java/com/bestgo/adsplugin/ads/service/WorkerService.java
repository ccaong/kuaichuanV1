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
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.bestgo.adsplugin.R;
import com.bestgo.adsplugin.ads.AdAppHelper;
import com.bestgo.adsplugin.ads.AdConfig;
import com.bestgo.adsplugin.ads.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

public class WorkerService extends Service {
    private final static int GRAY_SERVICE_ID = 1001;
    private static final String AUTOSHOW_AD_ACTION = "com.bestgo.adplugin.ads.AUTOSHOW_AD_ACTION";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter filter = new IntentFilter();
        filter.addAction(AUTOSHOW_AD_ACTION);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                autoLoadAd();
            }
        }, filter);

        AdConfig config = AdAppHelper.getInstance(getApplicationContext()).getConfig();
        long interval = config != null ? config.auto_show_ctrl.interval * 1000 : 0;
        if (interval <= 0) {
            interval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
        }
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0x102, new Intent(AUTOSHOW_AD_ACTION), PendingIntent.FLAG_UPDATE_CURRENT);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000 * 60, interval, pendingIntent);

        new Thread(new UpdateThread(), "UpdateThread").start();

        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
                startForeground(GRAY_SERVICE_ID, new Notification());
            } else {
                Intent innerIntent = new Intent(this, C.class);
                startService(innerIntent);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.adsplugin_ic_empty)
                                .setContentTitle("")
                                .setContentText("");
                startForeground(GRAY_SERVICE_ID, new Notification());
            }
        } catch (Exception ex) {
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                BB.start(getApplicationContext());
                Intent it = new Intent(WorkerService.this, B.class);
                startService(it);
//                autoLoadAd();
//            }
        } catch (Exception ex) {
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void autoLoadAd() {
        try {
            AdConfig config = AdAppHelper.getInstance(getApplicationContext()).getConfig();
            if (config.auto_show_ctrl.exe == 1 && AdAppHelper.getInstance(getApplicationContext()).isNetworkConnected(getApplicationContext())) {
                int count = config.auto_show_ctrl.max_count;
                SharedPreferences sp = getSharedPreferences(AdAppHelper.SHARED_SP_NAME, 0);
                int ngsCount = sp.getInt("curr_ngs_auto_load_count", 0);
                int nativeCount = sp.getInt("curr_native_auto_load_count", 0);
                int day = sp.getInt("last_auto_load_day", 0);
                int currDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                if (day != currDay) {
                    ngsCount = 0;
                    nativeCount = 0;
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("last_auto_load_day", currDay);
                    editor.putInt("curr_ngs_auto_load_count", 0);
                    editor.putInt("curr_native_auto_load_count", 0);
                    editor.commit();
                }
                if (ngsCount < count) {
                    if (config.auto_show_ctrl.ngs == 1) {
                        AdAppHelper.getInstance(getApplicationContext()).loadNewFBNAutoShow();
                    }
                }
                if (nativeCount < count) {
                    if (config.auto_show_ctrl.banner == 1) {
                        AdAppHelper.getInstance(getApplicationContext()).loadNewBanner();
                    }
                    if (config.auto_show_ctrl.native_ == 1) {
                        AdAppHelper.getInstance(getApplicationContext()).loadNewNative(true);
                    }
                }
            }
        } catch (Exception ex) {
            AdAppHelper.getInstance(getApplicationContext()).getFireBase().logEvent(Const.CATEGORY_AD, "AutoShowError", ex.getMessage());
        }
    }

    private class UpdateThread implements Runnable {
        String DOMAIN_URL = "http://config.uugame.info:8080/online_config_server/config?method=get_config&app_id=%s";
        String IP_URL = "http://34.209.139.199:8080/online_config_server/config?method=get_config&app_id=%s";
        String METRICS_URL = "http://money.uugame.info/query?app_id=%s&country_code=%s";

        class UpdateResult {
            public boolean saved;
            public boolean changed;
            public boolean success;
        }

        @Override
        public void run() {
            while (true) {
                SharedPreferences sp = WorkerService.this.getSharedPreferences(AdAppHelper.SHARED_SP_NAME, 0);
                long lastUpdateTime = sp.getLong("last_update_time", 0);
                long lastUpdateMetricTime = sp.getLong("last_update_metric_time", 0);
                long now = System.currentTimeMillis();

                if (Math.abs(now - lastUpdateTime) > 1000 * 60 * 60) {
                    String packageName = getPackageName();
                    String url = String.format(Locale.US, DOMAIN_URL, packageName);
                    UpdateResult result = updateData(url, "域名");
                    if (result.saved) {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putLong("last_update_time", now);
                        editor.commit();
                        AdAppHelper.getInstance(WorkerService.this).getFireBase().logEvent("ADSDK_配置", "更新成功", "域名");
                    } else if (!result.success) {
                        url = String.format(Locale.US, IP_URL, packageName);
                        result = updateData(url, "IP");
                        if (result.saved) {
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putLong("last_update_time", now);
                            editor.commit();
                            AdAppHelper.getInstance(WorkerService.this).getFireBase().logEvent("ADSDK_配置", "更新成功", "IP");
                        }
                    }
                    if (result.changed) {
                        AdAppHelper.getInstance(WorkerService.this).reloadConfig();
                    }
                }

                if (Math.abs(now - lastUpdateMetricTime) > 1000 * 60 * 60) {
                    String packgeName = getPackageName();
                    String countryCode = getCountryCodeByIp();
                    if (countryCode == null) {
                        countryCode = Locale.getDefault().getCountry();
                    }
                    String url = String.format(Locale.US, METRICS_URL, packgeName, countryCode);
                    UpdateResult result = updateMetrics(url);
                    if (result.saved) {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putLong("last_update_metric_time", now);
                        editor.commit();

                        AdAppHelper.getInstance(getApplicationContext()).reloadAdMetrics();
                    }
                }
                try {
                    Thread.sleep(1000 * 60 * 10);
                } catch (InterruptedException e) {
                }
            }
        }

        private String getCountryCodeByIp() {
            try {
                HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection)new URL("http://freegeoip.net/json/").openConnection();
                    connection.setConnectTimeout(1000 * 20);
                    connection.setReadTimeout(1000 * 20);
                    int code = connection.getResponseCode();
                    if (code == 200) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuffer buffer = new StringBuffer();
                        String line = reader.readLine();
                        while (line != null) {
                            buffer.append(line);
                            line = reader.readLine();
                        }

                        try {
                            JSONObject json = new JSONObject(buffer.toString());
                            String countryCode = json.getString("country_code");
                            if (countryCode != null && countryCode.length() == 2) {
                                return countryCode;
                            }
                        } catch (Exception e) {
                        }
                    } else {
                    }
                } catch (Exception e) {
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            } catch (Exception ex) {
            }
            return null;
        }

        private UpdateResult updateMetrics(String url) {
            UpdateResult result = new UpdateResult();
            result.saved = false;
            result.changed = false;
            result.success = false;
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection)new URL(url).openConnection();
                connection.setConnectTimeout(1000 * 20);
                connection.setReadTimeout(1000 * 20);
                int code = connection.getResponseCode();
                if (code == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuffer buffer = new StringBuffer();
                    String line = reader.readLine();
                    while (line != null) {
                        buffer.append(line);
                        line = reader.readLine();
                    }

                    try {
                        JSONObject json = new JSONObject(buffer.toString());
                        if (json.getInt("ret") == 1) {
                            SharedPreferences sp = WorkerService.this.getSharedPreferences(AdAppHelper.SHARED_SP_NAME, 0);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("ad_unit_metrics", buffer.toString());
                            editor.commit();
                            result.saved = true;
                        }
                    } catch (JSONException e) {
                    }
                } else {
                }
            } catch (Exception e) {
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }

        private UpdateResult updateData(String url, String tag) {
            UpdateResult result = new UpdateResult();
            result.saved = false;
            result.changed = false;
            result.success = false;
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection)new URL(url).openConnection();
                connection.setConnectTimeout(1000 * 20);
                connection.setReadTimeout(1000 * 20);
                int code = connection.getResponseCode();
                if (code == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuffer buffer = new StringBuffer();
                    String line = reader.readLine();
                    while (line != null) {
                        buffer.append(line);
                        line = reader.readLine();
                    }

                    try {
                        JSONObject json = new JSONObject(buffer.toString());
                        Iterator<String> keys = json.keys();
                        SharedPreferences sp = getApplicationContext().getSharedPreferences("ourdefault_game_config", 0);
                        SharedPreferences.Editor editor = sp.edit();
                        boolean updated = false;
                        while (keys.hasNext()) {
                            String key = keys.next();
                            String value = json.getString(key);
                            String oldValue = sp.getString(key, "");
                            if (!oldValue.equals(value)) {
                                result.changed = true;
                            }
                            editor.putString(key, value);
                            updated = true;
                        }
                        if (updated) {
                            result.saved = editor.commit();
                        }
                        result.success = true;
                    } catch (JSONException e) {
                    }
                } else {
                    AdAppHelper.getInstance(WorkerService.this).getFireBase().logEvent("ADSDK_配置", "更新失败_" + tag, "错误码" + code);
                }
            } catch (Exception e) {
                AdAppHelper.getInstance(WorkerService.this).getFireBase().logEvent("ADSDK_配置", "更新失败_" + tag, e.getMessage());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }
    }
}
