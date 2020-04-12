package com.bestgo.adsplugin.ads.cache;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class FBCache {
    private static Set<String> usedCacheFiles = new HashSet<>();
    public static class CacheItem {
        public String requestId;
        public String data;
    }

    public static String PACKAGE_NAME = "";

    public static void deleteFromCache(String requestId, String type, String adId) {
        try {
            File cacheDir = new File("/data/data/" + PACKAGE_NAME + "/adCache/facebook/" + type + "/" + adId);
            File data = new File(cacheDir.getAbsolutePath() + "/" + requestId);
            if (data.exists()) {
                data.delete();
            }
        } catch (Exception ex) {
        }
    }

    public static void saveToCache(String requestId, String type, String adId, String data) {
        try {
            File cacheDir = new File("/data/data/" + PACKAGE_NAME + "/adCache/facebook/" + type + "/" + adId);
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            File file = new File(cacheDir.getAbsolutePath() + "/" + requestId);
            if (file.exists()) {
                return;
            }
            FileOutputStream os = new FileOutputStream(file);
            os.write(data.getBytes());
            os.flush();
            os.close();
        } catch (Exception ex) {
        }
    }

    public static void releaseObtain(String requestid) {
        usedCacheFiles.remove(requestid);
    }

    public synchronized static CacheItem getAdFromCache(String type, String adId) {
        File cacheDir = new File("/data/data/" + PACKAGE_NAME + "/adCache/facebook/" + type + "/" + adId);
        CacheItem item = null;
        File[] files = cacheDir.listFiles();
        long t = 0;
        long now = System.currentTimeMillis();
        File adFile = null;
        if (files != null) {
            for (File one : files) {
                if (usedCacheFiles.contains(one.getName())) {
                    continue;
                }
                if (Math.abs(now - one.lastModified()) > 1000 * 3600) {
                    one.delete();
                    continue;
                }
                if (t < one.lastModified()) {
                    t = one.lastModified();
                    adFile = one;
                }
            }
        }
        if (adFile != null) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(adFile));
                StringBuffer buffer = new StringBuffer();
                String line = reader.readLine();
                while (line != null) {
                    buffer.append(line);
                    line = reader.readLine();
                }
                item = new CacheItem();
                item.requestId = adFile.getName();
                item.data = buffer.toString();
                usedCacheFiles.add(adFile.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return item;
    }

    public synchronized static int getCacheCount(String type, String adId) {
        int count = 0;
        File cacheDir = new File("/data/data/" + PACKAGE_NAME + "/adCache/facebook/" + type + "/" + adId);
        File[] files = cacheDir.listFiles();
        long now = System.currentTimeMillis();
        if (files != null) {
            for (File one : files) {
                if (Math.abs(now - one.lastModified()) > 1000 * 3600) {
                    continue;
                }
                count++;
            }
        }
        return count;
    }
}
