package com.bestgo.adsplugin.ads.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.bestgo.adsplugin.R;

public class CC extends Service {
    private final static int GRAY_SERVICE_ID = 1002;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
                startForeground(GRAY_SERVICE_ID, new Notification());
                stopForeground(true);
                stopSelf();
            }
        } catch (Exception ex) {}
        return super.onStartCommand(intent, flags, startId);
    }
}
