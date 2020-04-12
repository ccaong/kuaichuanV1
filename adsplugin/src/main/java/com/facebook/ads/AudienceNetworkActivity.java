// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads;

import android.util.Log;
import android.net.Uri;
import android.os.AsyncTask;
import com.facebook.ads.internal.util.y;
import java.util.Map;
import com.facebook.ads.internal.util.x;
import java.util.HashMap;
import android.text.TextUtils;
import com.facebook.ads.internal.adapters.k;
import android.content.res.Configuration;
import java.util.Iterator;
import android.support.v4.content.LocalBroadcastManager;

import java.io.Serializable;

import com.facebook.ads.internal.view.f;
import com.facebook.ads.internal.view.h;
import com.facebook.ads.internal.view.l;
import com.facebook.ads.internal.view.r;
import com.facebook.ads.internal.gpackage.q;
import android.view.ViewGroup;
import android.view.View;
import android.content.Context;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;
import com.facebook.ads.internal.view.d;
import com.facebook.ads.internal.g;
import android.content.Intent;
import android.widget.RelativeLayout;
import com.facebook.ads.internal.view.c;
import android.app.Activity;

public class AudienceNetworkActivity extends Activity
{
    private static final String a;
    public static final String AUDIENCE_NETWORK_UNIQUE_ID_EXTRA = "uniqueId";
    public static final String AD_TITLE = "adTitle";
    public static final String AD_SUBTITLE = "adSubtitle";
    public static final String AD_ICON_URL = "adIconUrl";
    public static final String CONTEXT_SWITCH_BEHAVIOR = "contextSwitchBehavior";
    public static final String END_CARD_MARKUP = "facebookRewardedVideoEndCardMarkup";
    public static final String END_CARD_ACTIVATION_COMMAND = "facebookRewardedVideoEndCardActivationCommand";
    public static final String REWARD_SERVER_URL = "rewardServerURL";
    public static final String WEBVIEW_MIME_TYPE = "text/html";
    public static final String WEBVIEW_ENCODING = "utf-8";
    public static final String PREDEFINED_ORIENTATION_KEY = "predefinedOrientationKey";
    public static final String SKIP_DELAY_SECONDS_KEY = "skipAfterSeconds";
    public static final String AUTOPLAY = "autoplay";
    public static final String BROWSER_URL = "browserURL";
    public static final String VIEW_TYPE = "viewType";
    public static final String VIDEO_URL = "videoURL";
    public static final String VIDEO_REPORT_URL = "videoReportURL";
    public static final String VIDEO_LOGGER = "videoLogger";
    public static final String VIDEO_MPD = "videoMPD";
    public static final String VIDEO_SEEK_TIME = "videoSeekTime";
    public static final String CLIENT_TOKEN = "clientToken";
    public static final String HANDLER_TIME = "handlerTime";
    private String b;
    private String c;
    private c d;
    private boolean e;
    private RelativeLayout f;
    private Intent g;
    private g h;
    private int i;
    private String j;
    private Type k;
    private long l;
    private long m;
    private int n;
    private d o;
    private List<BackButtonInterceptor> p;
    
    public AudienceNetworkActivity() {
        this.e = false;
        this.i = -1;
        this.p = new ArrayList<BackButtonInterceptor>();
    }
    
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.requestWindowFeature(1);
        this.getWindow().setFlags(1024, 1024);
        (this.f = new RelativeLayout((Context)this)).setBackgroundColor(-16777216);
        this.setContentView((View)this.f, (ViewGroup.LayoutParams)new RelativeLayout.LayoutParams(-1, -1));
        this.g = this.getIntent();
        if (this.g.getBooleanExtra("useNativeCloseButton", false)) {
            (this.h = new g((Context)this)).setId(100002);
            this.h.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
                public void onClick(final View view) {
                    AudienceNetworkActivity.this.finish();
                }
            });
        }
        this.b = this.g.getStringExtra("clientToken");
        this.a(this.g, bundle);
        if (this.k == Type.VIDEO) {
            final r o = new r(this, new d.a() {
                @Override
                public void a(final String s, final q q) {
                    AudienceNetworkActivity.this.a(s, q);
                }
                
                @Override
                public void a(final String s) {
                    AudienceNetworkActivity.this.a(s);
                }
                
                @Override
                public void a(final View view) {
                    AudienceNetworkActivity.this.f.addView(view);
                    if (AudienceNetworkActivity.this.h != null) {
                        AudienceNetworkActivity.this.f.addView((View)AudienceNetworkActivity.this.h);
                    }
                }
            });
            o.a((View)this.f);
            this.o = o;
        }
        else if (this.k == Type.REWARDED_VIDEO) {
            this.o = new l((Context)this, new d.a() {
                @Override
                public void a(final String s, final q q) {
                    AudienceNetworkActivity.this.a(s);
                    if (s.startsWith(com.facebook.ads.internal.j.REWARDED_VIDEO_COMPLETE.a())) {
                        if (!s.equals(com.facebook.ads.internal.j.REWARDED_VIDEO_COMPLETE_WITHOUT_REWARD.a())) {
                            AudienceNetworkActivity.this.b();
                        }
                        AudienceNetworkActivity.this.e = true;
                        AudienceNetworkActivity.this.c();
                        AudienceNetworkActivity.this.d();
                    }
                }
                
                @Override
                public void a(final String s) {
                    AudienceNetworkActivity.this.a(s);
                    if (s.equals(com.facebook.ads.internal.j.REWARDED_VIDEO_END_ACTIVITY.a())) {
                        AudienceNetworkActivity.this.finish();
                    }
                }
                
                @Override
                public void a(final View view) {
                    AudienceNetworkActivity.this.f.addView(view);
                }
            });
            this.addBackButtonInterceptor(new BackButtonInterceptor() {
                @Override
                public boolean interceptBackButton() {
                    return !AudienceNetworkActivity.this.e;
                }
            });
        }
        else if (this.k == Type.DISPLAY) {
            this.o = new h(this, new d.a() {
                @Override
                public void a(final String s, final q q) {
                    AudienceNetworkActivity.this.a(s, q);
                }
                
                @Override
                public void a(final String s) {
                    AudienceNetworkActivity.this.a(s);
                }
                
                @Override
                public void a(final View view) {
                    AudienceNetworkActivity.this.f.addView(view);
                    if (AudienceNetworkActivity.this.h != null) {
                        AudienceNetworkActivity.this.f.addView((View)AudienceNetworkActivity.this.h);
                    }
                }
            });
        }
        else if (this.k == Type.BROWSER) {
            this.o = new f(this, new d.a() {
                @Override
                public void a(final String s, final q q) {
                    AudienceNetworkActivity.this.a(s, q);
                }
                
                @Override
                public void a(final String s) {
                    AudienceNetworkActivity.this.a(s);
                }
                
                @Override
                public void a(final View view) {
                    AudienceNetworkActivity.this.f.addView(view);
                    if (AudienceNetworkActivity.this.h != null) {
                        AudienceNetworkActivity.this.f.addView((View)AudienceNetworkActivity.this.h);
                    }
                }
            });
        }
        else {
            if (this.k != Type.NATIVE) {
                com.facebook.ads.internal.util.c.a(com.facebook.ads.internal.util.b.a(null, "Unable to infer viewType from intent or savedInstanceState"));
                this.a("com.facebook.ads.interstitial.error");
                this.finish();
                return;
            }
            this.o = com.facebook.ads.internal.adapters.j.a(this.g.getStringExtra("uniqueId"));
            if (this.o == null) {
                com.facebook.ads.internal.util.c.a(com.facebook.ads.internal.util.b.a(null, "Unable to find view"));
                this.a("com.facebook.ads.interstitial.error");
                this.finish();
                return;
            }
            this.o.a(new d.a() {
                @Override
                public void a(final String s, final q q) {
                    AudienceNetworkActivity.this.a(s, q);
                }
                
                @Override
                public void a(final String s) {
                    AudienceNetworkActivity.this.a(s);
                }
                
                @Override
                public void a(final View view) {
                    AudienceNetworkActivity.this.f.addView(view);
                }
            });
        }
        this.o.a(this.g, bundle, this);
        this.a("com.facebook.ads.interstitial.displayed");
        this.l = System.currentTimeMillis();
    }
    
    public void onStart() {
        super.onStart();
        if (this.i != -1) {
            this.setRequestedOrientation(this.i);
        }
    }
    
    public void onPause() {
        this.m += System.currentTimeMillis() - this.l;
        if (this.o != null && !this.e) {
            this.o.g();
        }
        super.onPause();
    }
    
    public void onResume() {
        super.onResume();
        this.l = System.currentTimeMillis();
        if (this.o != null) {
            this.o.h();
        }
    }
    
    public void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (this.o != null) {
            this.o.a(bundle);
        }
        bundle.putInt("predefinedOrientationKey", this.i);
        bundle.putString("uniqueId", this.j);
        bundle.putSerializable("viewType", (Serializable)this.k);
    }
    
    private void a(final Intent intent, final Bundle bundle) {
        if (bundle != null) {
            this.i = bundle.getInt("predefinedOrientationKey", -1);
            this.j = bundle.getString("uniqueId");
            this.k = (Type)bundle.getSerializable("viewType");
            return;
        }
        this.i = intent.getIntExtra("predefinedOrientationKey", -1);
        this.j = intent.getStringExtra("uniqueId");
        this.k = (Type)intent.getSerializableExtra("viewType");
        this.n = intent.getIntExtra("skipAfterSeconds", 0) * 1000;
    }
    
    protected void onDestroy() {
        this.f.removeAllViews();
        if (this.o != null) {
            com.facebook.ads.internal.adapters.j.a(this.o);
            this.o.onDestroy();
            this.o = null;
        }
        if (this.d != null) {
            com.facebook.ads.internal.util.h.a(this.d);
            this.d.destroy();
            this.d = null;
            this.c = null;
        }
        if (this.k == Type.REWARDED_VIDEO) {
            this.a(com.facebook.ads.internal.j.REWARDED_VIDEO_CLOSED.a());
        }
        else {
            this.a("com.facebook.ads.interstitial.dismissed");
        }
        super.onDestroy();
    }
    
    private void a(final String s, final q q) {
        final Intent intent = new Intent(s + ":" + this.j);
        intent.putExtra("event", (Serializable)q);
        LocalBroadcastManager.getInstance((Context)this).sendBroadcast(intent);
    }
    
    private void a(final String s) {
        LocalBroadcastManager.getInstance((Context)this).sendBroadcast(new Intent(s + ":" + this.j));
    }
    
    public void addBackButtonInterceptor(final BackButtonInterceptor backButtonInterceptor) {
        this.p.add(backButtonInterceptor);
    }
    
    public void removeBackButtonInterceptor(final BackButtonInterceptor backButtonInterceptor) {
        this.p.remove(backButtonInterceptor);
    }
    
    public void onBackPressed() {
        final long currentTimeMillis = System.currentTimeMillis();
        this.m += currentTimeMillis - this.l;
        this.l = currentTimeMillis;
        if (this.m > this.n) {
            boolean b = false;
            final Iterator<BackButtonInterceptor> iterator = this.p.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().interceptBackButton()) {
                    b = true;
                }
            }
            if (!b) {
                super.onBackPressed();
            }
        }
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
        if (this.o instanceof k) {
            ((k)this.o).a(configuration);
        }
        super.onConfigurationChanged(configuration);
    }
    
    private void b() {
        final String stringExtra = this.g.getStringExtra("rewardServerURL");
        if (!TextUtils.isEmpty((CharSequence)stringExtra)) {
            final x x = new x(new HashMap<String, String>());
            x.a(new x.a() {
                @Override
                public void a(final y y) {
                    if (y != null && y.a()) {
                        AudienceNetworkActivity.this.a(com.facebook.ads.internal.j.REWARD_SERVER_SUCCESS.a());
                    }
                    else {
                        AudienceNetworkActivity.this.a(com.facebook.ads.internal.j.REWARD_SERVER_FAILED.a());
                    }
                }
                
                @Override
                public void a() {
                    AudienceNetworkActivity.this.a(com.facebook.ads.internal.j.REWARD_SERVER_FAILED.a());
                }
            });
            x.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[] { stringExtra });
        }
    }
    
    private void c() {
        final String a = com.facebook.ads.internal.util.g.a(this.g.getByteArrayExtra("facebookRewardedVideoEndCardMarkup"));
        if (TextUtils.isEmpty((CharSequence)a)) {
            return;
        }
        (this.d = new c((Context)this, new c.b() {
            @Override
            public void a(final String s, final Map<String, String> map) {
                final Uri parse = Uri.parse(s);
                if ("fbad".equals(parse.getScheme()) && parse.getAuthority().equals("close")) {
                    AudienceNetworkActivity.this.finish();
                    return;
                }
                if ("fbad".equals(parse.getScheme()) && com.facebook.ads.internal.action.b.a(parse.getAuthority())) {
                    AudienceNetworkActivity.this.a(com.facebook.ads.internal.j.REWARDED_VIDEO_AD_CLICK.a());
                }
                final com.facebook.ads.internal.action.a a = com.facebook.ads.internal.action.b.a((Context)AudienceNetworkActivity.this, AudienceNetworkActivity.this.b, parse, map);
                if (a != null) {
                    try {
                        a.b();
                    }
                    catch (Exception ex) {
                        Log.e(AudienceNetworkActivity.a, "Error executing action", (Throwable)ex);
                    }
                }
            }
            
            @Override
            public void a() {
                if (AudienceNetworkActivity.this.d != null && !TextUtils.isEmpty((CharSequence)AudienceNetworkActivity.this.c)) {
                    AudienceNetworkActivity.this.d.post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            if (AudienceNetworkActivity.this.d.c()) {
                                Log.w(AudienceNetworkActivity.a, "Webview already destroyed, cannot activate");
                            }
                            else {
                                AudienceNetworkActivity.this.d.loadUrl("javascript:" + AudienceNetworkActivity.this.c);
                            }
                        }
                    });
                }
            }
            
            @Override
            public void b() {
            }
            
            @Override
            public void a(final int n) {
            }
        }, 1)).setLayoutParams((ViewGroup.LayoutParams)new RelativeLayout.LayoutParams(-1, -1));
        this.c = this.g.getStringExtra("facebookRewardedVideoEndCardActivationCommand");
        this.d.loadDataWithBaseURL(com.facebook.ads.internal.util.h.a(), a, "text/html", "utf-8", (String)null);
    }
    
    private void d() {
        if (this.d == null) {
            this.finish();
            return;
        }
        this.f.removeAllViews();
        this.o.onDestroy();
        this.o = null;
        this.f.addView((View)this.d);
    }
    
    static {
        a = AudienceNetworkActivity.class.getSimpleName();
    }
    
    public enum Type
    {
        DISPLAY, 
        VIDEO, 
        REWARDED_VIDEO, 
        NATIVE, 
        BROWSER;
    }
    
    public interface BackButtonInterceptor
    {
        boolean interceptBackButton();
    }
}
