package com.bestgo.adsplugin.ads.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.bestgo.adsplugin.ads.AdAppHelper;

import java.util.ArrayList;
import java.util.List;

public class D extends IntentService {
    private ArrayList<String> mSoltaireList;

    public D() {
        super("LoadPackages");
    }

    private void initData() {
        mSoltaireList = new ArrayList<>();
        mSoltaireList.add("solitaire");
        mSoltaireList.add("spider");
        mSoltaireList.add("freecell");
        mSoltaireList.add("klondike");
        mSoltaireList.add("solitär");
        mSoltaireList.add("пасьянс");
        mSoltaireList.add("solitário");
        mSoltaireList.add("пасьянс");
        mSoltaireList.add("solitario");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            initData();

            PackageManager pm = getPackageManager();
            List<ApplicationInfo> list = pm.getInstalledApplications(0);
            for (ApplicationInfo info : list) {
                String label = pm.getApplicationLabel(info).toString();
                label = label.toLowerCase();
                for (int i = 0; i < mSoltaireList.size(); i++) {
                    if (label.contains(mSoltaireList.get(i))) {
                        AdAppHelper.getInstance(D.this).getFacebook().logEvent("SolitaireUser", 1);
                        AdAppHelper.getInstance(D.this).getFireBase().logEvent("SolitaireUser", 1);
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
