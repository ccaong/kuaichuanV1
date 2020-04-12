package com.bestgo.adsplugin.ads.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bestgo.adsplugin.ads.AdAppHelper;
import com.bestgo.adsplugin.ads.AdConfig;
import com.bestgo.adsplugin.ads.service.D;

public class PackageMonitorReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AdConfig config = AdAppHelper.getInstance(context).getConfig();
        if (config.ad_ctrl.full_ad_count > 0 &&
                config.ad_ctrl.full_ad_count > config.ad_count_ctrl.last_full_show_count) {
            String action = intent.getAction();
            Bundle bundle = intent.getExtras();
            boolean isReplace = false;
            if (bundle != null) {
                isReplace = bundle.getBoolean(Intent.EXTRA_REPLACING);
            }
            if ((!isReplace && Intent.ACTION_PACKAGE_REMOVED.equals(action)) ||
                    Intent.ACTION_PACKAGE_ADDED.equals(action)) {
                AdAppHelper.getInstance(context).showFBNPakcageAd();
            }
        }
        Intent it = new Intent(context, D.class);
        context.startService(it);
    }
}
