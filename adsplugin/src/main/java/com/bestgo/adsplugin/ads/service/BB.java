package com.bestgo.adsplugin.ads.service;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

@TargetApi(21)
public class BB extends JobService {
    private static JobScheduler scheduler;

    public static void start(Context context) {
        if (scheduler == null) {
            scheduler = (JobScheduler)context.getSystemService(JOB_SCHEDULER_SERVICE);
        }
        scheduler.cancel(110);
        scheduler.schedule(new JobInfo.Builder(110, new ComponentName(context, BB.class))
                .setPeriodic(1000 * 600).build());
    }

    @Override
    public boolean onStartJob(final JobParameters params) {
        try {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    Intent intent = new Intent(BB.this, B.class);
//                    startService(intent);

                    Intent intent = new Intent(BB.this, WorkerService.class);
                    startService(intent);

                    jobFinished(params, true);
                }
            }, 1000);
        } catch (Exception ex) {
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
