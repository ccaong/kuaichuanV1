package com.bestgo.adsplugin.ads;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bestgo.adsplugin.R;
import com.bestgo.adsplugin.ads.entity.NewsEntity;
import com.bestgo.adsplugin.utils.FeedParser;
import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static android.content.Context.ALARM_SERVICE;

public class NewsAd {
    private long mLastRequestTime;
    private Context mContext;
    private List<NewsEntity> mNews;
    private MyFrameLayout mRoot;
    private MyFrameLayout mFBView;
    private NativeExpressAdView mAdmob;
    private boolean admobLoaded;
    private boolean fbLoaded;
    private NativeAd mFBAd;

    private NewsListener mNewsListener;
    private static final String REFRESH_NEWS_ACTION = "com.bestgo.adplugin.ads.REFRESH_NEWS_ACTION";

    private Intent mClickIntent;

    public void init(Context context) {
        mContext = context;

        IntentFilter filter = new IntentFilter();
        filter.addAction(REFRESH_NEWS_ACTION);
        mContext.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (mClickIntent != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            loadNewsInternal(true);
                        }
                    }).start();
                }
            }
        }, filter);

        AlarmManager am = (AlarmManager)mContext.getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext.getApplicationContext(), 0x103, new Intent(REFRESH_NEWS_ACTION), PendingIntent.FLAG_UPDATE_CURRENT);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000 * 60, AlarmManager.INTERVAL_HOUR, pendingIntent);
    }

    public boolean isReady() {
        if (mNews != null && mNews.size() > 0) {
            return true;
        }
        return false;
    }

    public View getNewsView() {
        if (mRoot == null) {
            mRoot = new MyFrameLayout(mContext);
        }
        mRoot.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.adsplugin_native_news_layout, mRoot, false);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mRoot.addView(view, params);

        ListView listView = (ListView)mRoot.findViewById(R.id.ads_plugin_news_list);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsEntity entity = mNews.get(position);
                if (mNewsListener != null) mNewsListener.onNewsClicked();
                if (!entity.isAdmob && !entity.isFacebook) {
                    openLink(entity.link);
                }
            }
        });
        if (mRoot.getParent() != null) {
            ViewGroup parent = (ViewGroup)mRoot.getParent();
            parent.removeView(mRoot);
        }
        return mRoot;
    }

    public void setNewsListener(NewsListener listener) {
        mNewsListener = listener;
    }

    private BaseAdapter mAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return mNews.size();
        }

        @Override
        public Object getItem(int position) {
            return mNews.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(R.layout.adsplugin_native_news_item, parent, false);
            }
            TextView title = (TextView)convertView.findViewById(R.id.news_title);
            TextView details = (TextView)convertView.findViewById(R.id.news_details);
            title.setTextColor(AdAppHelper.NEWS_TITLE_COLOR);
            details.setTextColor(AdAppHelper.NEWS_DETAIL_COLOR);
            ImageView pic = (ImageView)convertView.findViewById(R.id.ads_plugin_news_pic);
            RelativeLayout adItem = (RelativeLayout)convertView.findViewById(R.id.adItem);
            View newsItem = convertView.findViewById(R.id.ads_plugin_newsItem);
            if (position >= mNews.size()) {
                return convertView;
            }
            NewsEntity entity = mNews.get(position);
            if (entity.isFacebook) {
                adItem.setVisibility(View.VISIBLE);
                newsItem.setVisibility(View.GONE);
                if (mFBView.getParent() != adItem) {
                    ViewGroup p = (ViewGroup) mFBView.getParent();
                    if (p != null) {
                        p.removeView(mFBView);
                    }
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    adItem.addView(mFBView, params);
                    mAdapter.notifyDataSetChanged();
                }
            } else if (entity.isAdmob) {
                adItem.setVisibility(View.VISIBLE);
                newsItem.setVisibility(View.GONE);
                if (mAdmob.getParent() != adItem) {
                    ViewGroup p = (ViewGroup) mAdmob.getParent();
                    if (p != null) {
                        p.removeView(mAdmob);
                    }
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    adItem.addView(mAdmob, params);
                    mAdapter.notifyDataSetChanged();
                }
            } else {
                adItem.setVisibility(View.GONE);
                newsItem.setVisibility(View.VISIBLE);
                title.setText(entity.title);
                details.setText(entity.description);
                if (TextUtils.isEmpty(entity.imgUrl)) {
                    pic.setVisibility(View.GONE);
                } else {
                    pic.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(entity.imgUrl, pic);
                }
            }
            return convertView;
        }
    };

    private void openLink(String url) {
        try {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setClickIntent(Intent intent) {
        mClickIntent = intent;
    }

    public void loadNews() {
        if (!isReady() && System.currentTimeMillis() - mLastRequestTime > 1000 * 3600) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    loadNewsInternal(false);
                }
            }).start();
        } else if (isReady()) {
            loadAdmob();
            loadFacebook();
        }
    }

    private void loadNewsInternal(boolean notify) {
        Locale locale = Locale.getDefault();
        String url = String.format(Locale.US, "https://news.google.com/news?cf=all&hl=%s&pz=1&ned=%s&output=rss", locale.getLanguage(), locale.getCountry().toLowerCase());

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(10000);
            if (connection.getResponseCode() == 200) {
                InputStream is = connection.getInputStream();
                FeedParser parser = new FeedParser();
                List<NewsEntity> news = parser.parse(is);
                if (news.size() > 0) {
                    if (notify && mClickIntent != null) {
                        if (mNews == null || !mNews.get(0).title.equals(news.get(0).title)) {
                            sendNotification(news.get(0).title, news.get(0).description);
                        }
                    }
                    if (mNews == null) {
                        mNews = news;
                    } else {
                        int j = 0;
                        for (int i = 0; i < mNews.size(); i++) {
                            NewsEntity one = mNews.get(i);
                            if (one.isAdmob || one.isFacebook) continue;

                            mNews.set(i, news.get(j++));
                        }
                    }
                    mLastRequestTime = System.currentTimeMillis();
                    mHandler.sendEmptyMessage(1000);
                    if (mNewsListener != null) mNewsListener.onNewsReady();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1000) {
                loadAdmob();
                loadFacebook();
            }
        }
    };

    private void sendNotification(String title, String detail) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        long last_news_notification_time = sp.getLong("last_news_notification_time", -1);
        long now = System.currentTimeMillis();

        if (Math.abs(now - last_news_notification_time) < 1000 * 3600) {
            return;
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("last_news_notification_time", now).commit();

        Intent intent = mClickIntent;
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 100, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.adsplugin_ic_news_s)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.adsplugin_ic_news))
                .setContentTitle(title)
                .setContentText(detail)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0x1217, notificationBuilder.build());

        AdAppHelper.getInstance(mContext).getFireBase().logEvent(Const.CATEGORY_AD, "新闻通知", "显示");
    }

    private void loadAdmob() {
        AdConfig config = AdAppHelper.getInstance(mContext).getConfig();
        if (config.news_ctrl.exe == 0) return;
        if (config.news_ctrl.admob == -1) return;
        if (TextUtils.isEmpty(config.news_ids.admob)) return;
        if (admobLoaded) return;

        final String admobId = config.news_ids.admob;
        if (mAdmob == null) {
            mAdmob = new NativeExpressAdView(mContext);
            mAdmob.setAdUnitId(config.news_ids.admob);
            mAdmob.setAdSize(new AdSize(AdAppHelper.NEWS_ADMOB_WIDTH, 150));

            mAdmob.setAdListener(new AdListener() {
                @Override
                public void onAdOpened() {
                    if (mNewsListener != null) mNewsListener.onNewsClicked();
                    AdAppHelper.getInstance(mContext).getFireBase().logEvent(Const.CATEGORY_AD_NEWS_POSISTION, admobId, Const.ACTION_CLICK);
                }

                @Override
                public void onAdFailedToLoad(int i) {
                }

                @Override
                public void onAdLoaded() {
                    admobLoaded = true;
                    AdAppHelper.getInstance(mContext).getFireBase().logEvent(Const.CATEGORY_AD_NEWS_POSISTION, admobId, Const.ACTION_LOAD);

                    if (isReady()) {
                        boolean found = false;
                        for (int i = 0; i < mNews.size(); i++) {
                            if (mNews.get(i).isAdmob) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            int count = Math.min(mNews.size(), 4);
                            int index = new Random().nextInt(count);
                            NewsEntity entity = new NewsEntity("", "", "", "", "", "");
                            entity.isAdmob = true;
                            mNews.add(index, entity);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }

        AdRequest adRequest = new AdRequest.Builder().addTestDevice("919D974C26F3EC724E027BE77DF536ED").build();
        mAdmob.loadAd(adRequest);
        AdAppHelper.getInstance(mContext).getFireBase().logEvent(Const.CATEGORY_AD_NEWS_POSISTION, admobId, Const.ACTION_REQUEST);
    }

    private void loadFacebook() {
        AdConfig config = AdAppHelper.getInstance(mContext).getConfig();
        if (config.news_ctrl.exe == 0) return;
        if (config.news_ctrl.facebook == -1) return;
        if (TextUtils.isEmpty(config.news_ids.fb)) return;
        if (fbLoaded) return;

        if (mFBAd != null) {
            mFBAd.destroy();
        }
        final String fbId = config.news_ids.fb;
        mFBAd = new NativeAd(mContext, config.news_ids.fb);
        mFBAd.setAdListener(new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError error) {
                if (error.getErrorCode() == 1002) {
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                AdAppHelper.getInstance(mContext).getFireBase().logEvent(Const.CATEGORY_AD_NEWS_POSISTION, fbId, Const.ACTION_LOAD);
                fbLoaded = true;
                if (mFBAd != null) {
                    mFBAd.unregisterView();
                }

                if (mFBView == null) {
                    mFBView = new MyFrameLayout(mContext);
                }
                mFBView.removeAllViews();
                // Add the Ad view into the ad container.
                LayoutInflater inflater = LayoutInflater.from(mContext);
                // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
                View adView = inflater.inflate(R.layout.adsplugin_native_news_fb_ad_layout, mFBView, false);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
                mFBView.addView(adView, params);

                // Create native UI using the ad metadata.
                ImageView nativeAdIcon = (ImageView) adView.findViewById(R.id.ads_plugin_native_ad_icon);
                TextView nativeAdTitle = (TextView) adView.findViewById(R.id.ads_plugin_native_ad_title);
                MediaView nativeAdMedia = (MediaView) adView.findViewById(R.id.ads_plugin_native_ad_media);
                TextView nativeAdBody = (TextView) adView.findViewById(R.id.ads_plugin_native_ad_body);
                Button nativeAdCallToAction = (Button) adView.findViewById(R.id.ads_plugin_native_ad_call_to_action);

                // Register the Title and CTA button to listen for clicks.
                mFBAd.registerViewForInteraction(mFBView);

                // Set the Text.
                if (nativeAdTitle != null) {
                    nativeAdTitle.setText(mFBAd.getAdTitle());
                    nativeAdTitle.setTextColor(AdAppHelper.NEWS_TITLE_COLOR);
                }
                if (nativeAdBody != null) {
                    nativeAdBody.setText(mFBAd.getAdBody());
                    nativeAdBody.setTextColor(AdAppHelper.NEWS_DETAIL_COLOR);
                }
                if (nativeAdCallToAction != null) {
                    nativeAdCallToAction.setText(mFBAd.getAdCallToAction());
                }

                // Download and display the ad icon.
                NativeAd.Image adIcon = mFBAd.getAdIcon();
                if (nativeAdIcon != null && adIcon != null) {
                    NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);
                }

                // Download and display the cover image.
                if (nativeAdMedia != null) {
//                    NativeAd.downloadAndDisplayImage(adCoverImage, nativeAdMedia);
                    nativeAdMedia.setNativeAd(mFBAd);
                }

                if (isReady()) {
                    boolean found = false;
                    for (int i = 0; i < mNews.size(); i++) {
                        if (mNews.get(i).isFacebook) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        int count = Math.min(mNews.size(), 4);
                        int index = new Random().nextInt(count);
                        NewsEntity entity = new NewsEntity("", "", "", "", "", "");
                        entity.isFacebook = true;
                        mNews.add(index, entity);
                        mAdapter.notifyDataSetChanged();
                    }
                }

                // Add the AdChoices icon
                LinearLayout adChoicesContainer = (LinearLayout) adView.findViewById(R.id.ads_plugin_ad_choices_container);
                if (adChoicesContainer != null) {
                    AdChoicesView adChoicesView = new AdChoicesView(mContext, mFBAd, true);
                    adChoicesContainer.addView(adChoicesView);
                }
            }

            @Override
            public void onAdClicked(Ad ad) {
                if (mNewsListener != null) mNewsListener.onNewsClicked();
                AdAppHelper.getInstance(mContext).getFireBase().logEvent(Const.CATEGORY_AD_NEWS_POSISTION, fbId, Const.ACTION_CLICK);
            }

            public void onLoggingImpression(Ad ad) {
            }
        });

        mFBAd.loadAd();
        AdAppHelper.getInstance(mContext).getFireBase().logEvent(Const.CATEGORY_AD_NEWS_POSISTION, fbId, Const.ACTION_REQUEST);
    }
}
