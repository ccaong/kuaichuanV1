package com.bestgo.adsplugin.ads;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.umeng.analytics.game.UMGameAgent;

import java.util.ArrayList;
import java.util.HashMap;

public class AdConfig {
    public static class BannerIds {
        public String admob = "";
        public String fb = "";
    }

    public static class VideoIds {
        public String admob = "";
    }

    public static class AdUnitItem {
        public String id = "";
        public String name = "";
        public int width;
        public int height;
    }

    public static class IdCollection {
        public int count;
        public AdUnitItem[] ids;
    }

    public static class VideoCtrl {
        public int exe;
        public int admob;
    }

    public static class NgsCtrl {
        public int exe;
        public int admob;
        public int facebook;
        public int fbn;
    }

    public static class ResumeCtrl {
        public int exe;
    }

    public static class NewsCtrl {
        public int exe;
        public int admob;
        public int facebook;
    }

    public static class NewsIds {
        public String fb;
        public String admob;
    }

    public static class AdCtrl {
        public static int OPT_TYPE_LINK = 1;
        public static int OPT_TYPE_APP = 2;
        public static int OPT_TYPE_VIDEO = 4;

        public int ad_silent;
        public int ad_silent_native;
        public int ad_delay;
        public int fake_click;
        public int only_cta;
        public static class NgsOrder {
            public int before;
            public int adt;
            public int adt_type;
        }
        public int banner_click;
        public int ngs_click;
        public int native_click;
        public NgsOrder ngsorder = new NgsOrder();
        public NgsOrder ngsorder_admob = new NgsOrder();
        public int home_delay_time;
        public int enable_index_ngs;
        public int admob_fail_reload_count;
        public int fb_fail_reload_count;
        public int screen_off;
        public int screen_off_count;
        public int full_ad_count;
        public int immediate_show;
        public int reuse_cache;
        public int fb_cache;
        public int screen_off_ngs;
        public int delay_close;
        public int cache_first;
        public int max_delay_show;
        public int opt_type;
        public int watch_oncreate;
        public int risk;
        public int risk_rate;
        public int risk_n;
        public int auto_risk;
        public int auto_risk_n;
        public int auto_load;
        public int auto_ctrl;
        public int target_ctr;
    }

    public static class AdSeqCtrl {
        public static final int TYPE_ADMOB = 1;
        public static final int TYPE_FACEBOOK = 2;
        public static final int TYPE_FBN = 3;
        public static final int TYPE_ADMOB_EN = 4;
        public static final int TYPE_ADMOB_AN = 5;
        public static class AdSeqItem {
            public int type;
            public int index;
        }
        public int exe;
        public ArrayList<AdSeqItem> list;
    }

    public static class AutoShowCtrl {
        public int exe;
        public int interval;
        public int max_count;
        public int banner;
        public int native_;
        public int ngs;
    }

    public static class CacheLoadCtrl {
        public int exe;
        public int interval;
        public int max_cache_count;
        public int native_;
        public int ngs;
    }

    public static class BannerCtrl {
        public int exe;
        public int admob;
        public int facebook;
        public int fbn;
    }

    public static class NativeCtrl {
        public int exe;
        public int admob;
        public int facebook;
        public int admob_en;
        public int admob_an;
    }

    public static class FBAdGallery {
        public int exe;
        public String fb = "";
    }

    public static class SplashCtrl {
        public int exe;
        public String fb = "";
        public String admob = "";
    }

    public static class AdCountCtrl {
        public int last_day;
        public int last_screen_off_count;
        public int last_risk_count;
        public int last_risk_native_count;
        public int last_failed_count;
        public int last_full_show_count;
    }

    public static class RecommendCtrl {
        public int exe;
        public int count;
        public int native_count;
        public int fake_click;
        public int show_random;
        RecommendAdItem list[];
        RecommendNativeAdItem native_list[];
    }

    public static class RecommendAdItem {
        public boolean enabled;
        public boolean loaded;
        public boolean requested;
        public long lastRequestTime;
//        public Bitmap image;
        public String app_id;
        public String icon_url;
        public String image_url;
        public String link_url;
        public String title;
        public String sub_title;
        public String action_title;
        public boolean not_play_link;
        public boolean is_webview;
    }

    public static class RecommendNativeAdItem {
        public boolean enabled;
        public boolean loaded;
        public boolean requested;
        public long lastRequestTime;
        public String app_id;
        public String icon_url;
        public String image_url;
        public String link_url;
        public String title;
        public String sub_title;
        public String action_title;
        public boolean not_play_link;
        public int width;
        public int height;
        public boolean is_webview;
    }

    public static class CustomCtrl {
        public CustomCtrl() {
            params = new HashMap<>();
        }
        public HashMap<String, String> params;
    }

    public BannerIds banner_ids = new BannerIds();
    public IdCollection admob_banner_ids = new IdCollection();
    public IdCollection admob_full_ids = new IdCollection();
    public IdCollection fb_full_ids = new IdCollection();
    public IdCollection fbn_full_ids = new IdCollection();
    public IdCollection fbn_banner_ids = new IdCollection();
    public IdCollection admob_banner_native_ids = new IdCollection();
    public IdCollection admob_native_ids = new IdCollection();
    public IdCollection admob_an_ids = new IdCollection();
    public IdCollection fb_native_ids = new IdCollection();
    public NgsCtrl ngs_ctrl = new NgsCtrl();
    public BannerCtrl banner_ctrl = new BannerCtrl();
    public NativeCtrl native_ctrl = new NativeCtrl();
    public AdCountCtrl ad_count_ctrl = new AdCountCtrl();
    public AdCtrl ad_ctrl = new AdCtrl();
    public ResumeCtrl resume_ctrl = new ResumeCtrl();
    public RecommendCtrl recommend_ctrl = new RecommendCtrl();
    public FBAdGallery fb_ad_gallery = new FBAdGallery();
    public SplashCtrl splash_ctrl = new SplashCtrl();
    public AutoShowCtrl auto_show_ctrl = new AutoShowCtrl();
    public CacheLoadCtrl cache_load_ctrl = new CacheLoadCtrl();
    public CustomCtrl custom_ctrl = new CustomCtrl();
    public AdSeqCtrl ad_seq_ctrl = new AdSeqCtrl();
    public AdSeqCtrl ad_banner_seq_ctrl = new AdSeqCtrl();
    public ArrayList<AdSeqCtrl> ad_native_seq_ctrl = new ArrayList<>();
    public VideoCtrl video_ctrl = new VideoCtrl();
    public VideoIds video_ids = new VideoIds();

    public NewsCtrl news_ctrl = new NewsCtrl();
    public NewsIds news_ids = new NewsIds();

    public static int getInt(HashMap map, String key, int defaultValue) {
        try {
            Integer i = Integer.parseInt((String)map.get(key));
            return i;
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    public static String getString(HashMap map, String key, String defaultValue) {
        try {
            String value = (String)map.get(key);
            if (value == null) {
                return defaultValue;
            } else {
                return value;
            }
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    public static HashMap<String, String> getValues(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("ourdefault_game_config", 0);
        return convertToMap(sp.getString(key, ""));
    }


    private static HashMap convertToMap(String value) {
        HashMap ret = new HashMap();
        if(value != null && !"".equals(value.trim())) {
            value = value.trim();

            int start;
            int end;
            for(boolean b = true; (start = value.indexOf('{')) >= 0 && (end = value.indexOf('}')) > 0; value = value.substring(end + 1)) {
                String kv = value.substring(start + 1, end);
                if(kv != null && kv.indexOf("=") > 0) {
                    int equal = kv.indexOf("=");
                    ret.put(kv.substring(0, equal).trim(), kv.substring(equal + 1).trim());
                }
            }
        }
        return ret;
    }
}
