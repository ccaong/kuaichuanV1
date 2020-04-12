package com.facebook.ads.internal.adapters;

import com.facebook.ads.AdNetwork;
import com.facebook.ads.internal.util.ai;
import com.facebook.ads.NativeAdViewAttributes;
import java.util.Iterator;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAdView;
import android.util.Log;
import android.view.ViewGroup;
import org.json.JSONArray;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.formats.NativeContentAd;
import java.util.List;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.AdLoader;
import android.text.TextUtils;
import org.json.JSONException;
import com.facebook.ads.AdError;
import org.json.JSONObject;
import com.facebook.ads.internal.util.g;
import java.util.Map;
import com.facebook.ads.internal.gpackage.f;
import android.content.Context;
import android.net.Uri;
import com.google.android.gms.ads.formats.NativeAdView;
import com.google.android.gms.ads.formats.NativeAd;
import android.view.View;

public class c extends v implements t
{
    private static final String a;
    private View b;
    private NativeAd c;
    private w d;
    private NativeAdView e;
    private View f;
    private boolean g;
    private Uri h;
    private Uri i;
    private String j;
    private String k;
    private String l;
    private String m;

    @Override
    public void a(final Context context, final w d, final f f, final Map<String, Object> map) {
        com.facebook.ads.internal.util.g.a(context, com.facebook.ads.internal.util.v.a(this.D()) + " Loading");
        final JSONObject jsonObject = (JSONObject)map.get("data");
        final String optString = jsonObject.optString("ad_unit_id");
        final JSONArray optJSONArray = jsonObject.optJSONArray("creative_types");
        boolean b = false;
        boolean b2 = false;
        if (optJSONArray != null) {
            for (int length = optJSONArray.length(), i = 0; i < length; ++i) {
                try {
                    final String string = optJSONArray.getString(i);
                    if (string != null) {
                        final String s = string;
                        switch (s) {
                            case "app_install": {
                                b = true;
                                break;
                            }
                            case "page_post": {
                                b2 = true;
                                break;
                            }
                        }
                    }
                }
                catch (JSONException ex) {
                    com.facebook.ads.internal.util.g.a(context, com.facebook.ads.internal.util.v.a(this.D()) + " AN server error");
                    d.a(this, AdError.SERVER_ERROR);
                    return;
                }
            }
        }
        if (TextUtils.isEmpty((CharSequence)optString) || (!b && !b2)) {
            com.facebook.ads.internal.util.g.a(context, com.facebook.ads.internal.util.v.a(this.D()) + " AN server error");
            d.a(this, AdError.SERVER_ERROR);
            return;
        }
        this.d = d;
        final AdLoader.Builder builder = new AdLoader.Builder(context, optString);
        if (b) {
            builder.forAppInstallAd((NativeAppInstallAd.OnAppInstallAdLoadedListener)new NativeAppInstallAd.OnAppInstallAdLoadedListener() {
                public void onAppInstallAdLoaded(final NativeAppInstallAd nativeAppInstallAd) {
                    com.facebook.ads.internal.adapters.c.this.c = (NativeAd)nativeAppInstallAd;
                    com.facebook.ads.internal.adapters.c.this.g = true;
                    com.facebook.ads.internal.adapters.c.this.j = ((nativeAppInstallAd.getHeadline() != null) ? nativeAppInstallAd.getHeadline().toString() : null);
                    com.facebook.ads.internal.adapters.c.this.k = ((nativeAppInstallAd.getBody() != null) ? nativeAppInstallAd.getBody().toString() : null);
                    com.facebook.ads.internal.adapters.c.this.m = ((nativeAppInstallAd.getStore() != null) ? nativeAppInstallAd.getStore().toString() : null);
                    com.facebook.ads.internal.adapters.c.this.l = ((nativeAppInstallAd.getCallToAction() != null) ? nativeAppInstallAd.getCallToAction().toString() : null);
                    final List<NativeAd.Image> images = nativeAppInstallAd.getImages();
                    com.facebook.ads.internal.adapters.c.this.h = ((images != null && images.size() > 0) ? images.get(0).getUri() : null);
                    com.facebook.ads.internal.adapters.c.this.i = ((nativeAppInstallAd.getIcon() != null) ? nativeAppInstallAd.getIcon().getUri() : null);
                    if (com.facebook.ads.internal.adapters.c.this.d != null) {
                        com.facebook.ads.internal.util.g.a(context, com.facebook.ads.internal.util.v.a(com.facebook.ads.internal.adapters.c.this.D()) + " Loaded");
                        com.facebook.ads.internal.adapters.c.this.d.a(com.facebook.ads.internal.adapters.c.this);
                    }
                }
            });
        }
        if (b2) {
            builder.forContentAd((NativeContentAd.OnContentAdLoadedListener)new NativeContentAd.OnContentAdLoadedListener() {
                public void onContentAdLoaded(final NativeContentAd nativeContentAd) {
                    com.facebook.ads.internal.adapters.c.this.c = (NativeAd)nativeContentAd;
                    com.facebook.ads.internal.adapters.c.this.g = true;
                    com.facebook.ads.internal.adapters.c.this.j = ((nativeContentAd.getHeadline() != null) ? nativeContentAd.getHeadline().toString() : null);
                    com.facebook.ads.internal.adapters.c.this.k = ((nativeContentAd.getBody() != null) ? nativeContentAd.getBody().toString() : null);
                    com.facebook.ads.internal.adapters.c.this.m = ((nativeContentAd.getAdvertiser() != null) ? nativeContentAd.getAdvertiser().toString() : null);
                    com.facebook.ads.internal.adapters.c.this.l = ((nativeContentAd.getCallToAction() != null) ? nativeContentAd.getCallToAction().toString() : null);
                    final List<NativeAd.Image> images = nativeContentAd.getImages();
                    com.facebook.ads.internal.adapters.c.this.h = ((images != null && images.size() > 0) ? images.get(0).getUri() : null);
                    com.facebook.ads.internal.adapters.c.this.i = ((nativeContentAd.getLogo() != null) ? nativeContentAd.getLogo().getUri() : null);
                    if (com.facebook.ads.internal.adapters.c.this.d != null) {
                        com.facebook.ads.internal.util.g.a(context, com.facebook.ads.internal.util.v.a(com.facebook.ads.internal.adapters.c.this.D()) + " Loaded");
                        com.facebook.ads.internal.adapters.c.this.d.a(com.facebook.ads.internal.adapters.c.this);
                    }
                }
            });
        }
        builder.withAdListener((AdListener)new AdListener() {
            public void onAdFailedToLoad(final int n) {
                com.facebook.ads.internal.util.g.a(context, com.facebook.ads.internal.util.v.a(com.facebook.ads.internal.adapters.c.this.D()) + " Failed with error code: " + n);
                if (com.facebook.ads.internal.adapters.c.this.d != null) {
                    com.facebook.ads.internal.adapters.c.this.d.a(com.facebook.ads.internal.adapters.c.this, new AdError(3001, "AdMob error code: " + n));
                }
            }

            public void onAdOpened() {
                if (com.facebook.ads.internal.adapters.c.this.d != null) {
                    com.facebook.ads.internal.adapters.c.this.d.c(com.facebook.ads.internal.adapters.c.this);
                }
            }
        }).withNativeAdOptions(new NativeAdOptions.Builder().setReturnUrlsForImageAssets(true).build()).build().loadAd(new AdRequest.Builder().build());
    }

    @Override
    public void a(final Map<String, String> map) {
        if (this.b() && this.d != null) {
            this.d.b(this);
        }
    }

    @Override
    public void b(final Map<String, String> map) {
    }

    @Override
    public void a(final int n) {
    }

    @Override
    public void a(final View b, final List<View> list) {
        this.b = b;
        if (!this.b() || b == null) {
            return;
        }
        ViewGroup viewGroup = null;
        int indexOfChild = -1;
        do {
            final ViewGroup viewGroup2 = (ViewGroup)b.getParent();
            if (viewGroup2 == null) {
                Log.e(com.facebook.ads.internal.adapters.c.a, "View must have valid parent for AdMob registration, skipping registration. Impressions and clicks will not be logged.");
                return;
            }
            if (viewGroup2 instanceof NativeAdView) {
                final ViewGroup viewGroup3 = (ViewGroup)viewGroup2.getParent();
                if (viewGroup3 == null) {
                    Log.e(com.facebook.ads.internal.adapters.c.a, "View must have valid parent for AdMob registration, skipping registration. Impressions and clicks will not be logged.");
                    return;
                }
                final int indexOfChild2 = viewGroup3.indexOfChild((View)viewGroup2);
                viewGroup2.removeView(b);
                viewGroup3.removeView((View)viewGroup2);
                viewGroup3.addView(b, indexOfChild2);
            }
            else {
                viewGroup = viewGroup2;
                indexOfChild = viewGroup.indexOfChild(b);
            }
        } while (viewGroup == null);
        final Object e = (this.c instanceof NativeContentAd) ? new NativeContentAdView(b.getContext()) : new NativeAppInstallAdView(b.getContext());
        if (b instanceof ViewGroup) {
            ((NativeAdView)e).setLayoutParams(b.getLayoutParams());
        }
        this.a(b);
        ((NativeAdView)e).addView(b);
        viewGroup.removeView((View)e);
        viewGroup.addView((View)e, indexOfChild);
        (this.e = (NativeAdView)e).setNativeAd(this.c);
        this.f = new View(b.getContext());
        this.e.addView(this.f);
        this.f.setVisibility(View.GONE);
        if (this.e instanceof NativeContentAdView) {
            ((NativeContentAdView)this.e).setCallToActionView(this.f);
        }
        else if (this.e instanceof NativeAppInstallAdView) {
            ((NativeAppInstallAdView)this.e).setCallToActionView(this.f);
        }
        final View.OnClickListener onClickListener = (View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
                com.facebook.ads.internal.adapters.c.this.f.performClick();
            }
        };
        final Iterator<View> iterator = list.iterator();
        while (iterator.hasNext()) {
            iterator.next().setOnClickListener((View.OnClickListener)onClickListener);
        }
    }

    private void a(final View view) {
        if (view == null) {
            return;
        }
        final ViewGroup viewGroup = (ViewGroup)view.getParent();
        if (viewGroup == null) {
            return;
        }
        viewGroup.removeView(view);
    }

    @Override
    public void a() {
        this.a(this.f);
        this.f = null;
        if (this.b != null) {
            final ViewGroup viewGroup = (ViewGroup)this.b.getParent();
            if (viewGroup instanceof NativeContentAdView || viewGroup instanceof NativeAppInstallAdView) {
                final ViewGroup viewGroup2 = (ViewGroup)viewGroup.getParent();
                if (viewGroup2 != null) {
                    final int indexOfChild = viewGroup2.indexOfChild((View)viewGroup);
                    this.a(this.b);
                    this.a((View)viewGroup);
                    viewGroup2.addView(this.b, indexOfChild);
                }
            }
            this.b = null;
        }
        this.e = null;
    }

    @Override
    public boolean b() {
        return this.g && this.c != null;
    }

    @Override
    public boolean c() {
        return false;
    }

    @Override
    public boolean d() {
        return false;
    }

    @Override
    public boolean e() {
        return false;
    }

    @Override
    public boolean f() {
        return false;
    }

    @Override
    public boolean g() {
        return false;
    }

    @Override
    public void a(final w d) {
        this.d = d;
    }

    @Override
    public int h() {
        return 0;
    }

    @Override
    public int i() {
        return 0;
    }

    @Override
    public int j() {
        return 0;
    }

    @Override
    public com.facebook.ads.NativeAd.Image k() {
        if (this.b() && this.i != null) {
            return new com.facebook.ads.NativeAd.Image(this.i.toString(), 50, 50);
        }
        return null;
    }

    @Override
    public com.facebook.ads.NativeAd.Image l() {
        if (this.b() && this.h != null) {
            return new com.facebook.ads.NativeAd.Image(this.h.toString(), 1200, 600);
        }
        return null;
    }

    @Override
    public NativeAdViewAttributes m() {
        return null;
    }

    @Override
    public String n() {
        return this.j;
    }

    @Override
    public String o() {
        return null;
    }

    @Override
    public String p() {
        return this.k;
    }

    @Override
    public String q() {
        return this.l;
    }

    @Override
    public String r() {
        return this.m;
    }

    @Override
    public com.facebook.ads.NativeAd.Rating s() {
        return null;
    }

    @Override
    public com.facebook.ads.NativeAd.Image t() {
        return null;
    }

    @Override
    public String u() {
        return null;
    }

    @Override
    public String v() {
        return null;
    }

    @Override
    public String w() {
        return null;
    }

    @Override
    public String x() {
        return null;
    }

    @Override
    public ai y() {
        return ai.UNKNOWN;
    }

    @Override
    public String z() {
        return null;
    }

    @Override
    public List<com.facebook.ads.NativeAd> A() {
        return null;
    }

    @Override
    public String B() {
        return null;
    }

    @Override
    public AdNetwork C() {
        return AdNetwork.ADMOB;
    }

    @Override
    public void onDestroy() {
        this.a();
        this.d = null;
        this.c = null;
        this.g = false;
        this.h = null;
        this.i = null;
        this.j = null;
        this.k = null;
        this.l = null;
        this.m = null;
    }

    @Override
    public e D() {
        return com.facebook.ads.internal.adapters.e.ADMOB;
    }

    static {
        a = c.class.getSimpleName();
    }
}
