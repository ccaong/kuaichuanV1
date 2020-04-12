package com.bestgo.adsplugin.ads.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.bestgo.adsplugin.ads.AdAppHelper;
import com.bestgo.adsplugin.ads.service.WorkerService;
import com.umeng.analytics.game.UMGameAgent;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MonitorReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
            if (isNetworkConnected(context)) {
            }
        } else {
            uploadAliveEvent(context);
        }

        Intent it = new Intent(context, WorkerService.class);
        context.startService(it);
    }

    private boolean isNetworkConnected(Context context) {
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

    private void uploadAliveEvent(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        int hour = sp.getInt("last_alive_hour", -1);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT-8"));
        calendar.setTime(new Date());
        if (hour != calendar.get(Calendar.HOUR_OF_DAY)) {
            String aliveTag = sp.getString("aliveTag", "");
            if (!TextUtils.isEmpty(aliveTag)) {
                AdAppHelper.getInstance(context).getFireBase().logEvent("存活记录", aliveTag);
                AdAppHelper.getInstance(context).getFacebook().logEvent("app_install_date", aliveTag);

                SharedPreferences.Editor editor = sp.edit();
                editor.putString("aliveTag", aliveTag);
                editor.putInt("last_alive_hour", calendar.get(Calendar.HOUR_OF_DAY));
                editor.commit();
            }
        }
    }
}
