// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.adapters;

import com.facebook.ads.internal.view.dpackage.a.s;
import android.content.res.Configuration;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.widget.LinearLayout;
import android.text.TextUtils;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.Rect;
import android.annotation.TargetApi;

import org.json.JSONException;
import org.json.JSONObject;
import android.widget.RelativeLayout;
import android.graphics.Typeface;
import com.facebook.ads.internal.view.dpackage.b.f;
import com.facebook.ads.internal.util.p;
import com.facebook.ads.internal.view.dpackage.b.m;
import android.view.MotionEvent;
import android.view.ViewGroup;
import com.facebook.ads.internal.view.dpackage.b.o;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.ads.internal.view.cpackage.a;
import com.facebook.ads.AudienceNetworkActivity;
import android.app.Activity;
import android.support.annotation.Nullable;
import com.facebook.ads.internal.view.d;
import android.view.View;

public class k extends i implements View.OnTouchListener, d
{
    final int e = 64;
    final int f = 64;
    final int g = 16;
    @Nullable
    private a i;
    @Nullable
    private Activity j;
    private AudienceNetworkActivity.BackButtonInterceptor k;
    private final View.OnTouchListener l;
    private j.a m;
    private com.facebook.ads.internal.view.cpackage.a n;
    private TextView o;
    private TextView p;
    private ImageView q;
    private com.facebook.ads.internal.view.dpackage.b.a.aClass r;
    private o s;
    private ViewGroup t;
    private com.facebook.ads.internal.view.dpackage.b.d u;
    private com.facebook.ads.internal.view.dpackage.b.i v;
    private int w;
    private int x;
    private int y;
    private boolean z;
    static final /* synthetic */ boolean h;
    
    public k() {
        this.k = new AudienceNetworkActivity.BackButtonInterceptor() {
            @Override
            public boolean interceptBackButton() {
                if (k.this.v == null) {
                    return false;
                }
                if (!k.this.v.a()) {
                    return true;
                }
                if (k.this.v.getSkipSeconds() != 0 && k.this.a != null) {
                    k.this.a.f();
                }
                if (k.this.a != null) {
                    k.this.a.g();
                }
                k.this.j.finish();
                return false;
            }
        };
        this.l = (View.OnTouchListener)new View.OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                if (motionEvent.getAction() != 1) {
                    return true;
                }
                if (com.facebook.ads.internal.adapters.k.this.v == null) {
                    com.facebook.ads.internal.adapters.k.this.j.finish();
                    return true;
                }
                if (!com.facebook.ads.internal.adapters.k.this.v.a()) {
                    return true;
                }
                if (com.facebook.ads.internal.adapters.k.this.v.getSkipSeconds() != 0 && com.facebook.ads.internal.adapters.k.this.a != null) {
                    com.facebook.ads.internal.adapters.k.this.a.f();
                }
                if (com.facebook.ads.internal.adapters.k.this.a != null) {
                    com.facebook.ads.internal.adapters.k.this.a.g();
                }
                com.facebook.ads.internal.adapters.k.this.j.finish();
                return true;
            }
        };
        this.m = com.facebook.ads.internal.adapters.j.a.UNSPECIFIED;
        this.w = -1;
        this.x = -10525069;
        this.y = -12286980;
        this.z = false;
    }
    
    @TargetApi(17)
    @Override
    protected void a() throws JSONException {
        final String optString = this.b.getJSONObject("context").optString("orientation");
        if (!optString.isEmpty()) {
            this.m = com.facebook.ads.internal.adapters.j.a.a(Integer.parseInt(optString));
        }
        if (this.b.has("layout") && !this.b.isNull("layout")) {
            final JSONObject jsonObject = this.b.getJSONObject("layout");
            this.w = (int)jsonObject.optLong("bgColor", (long)this.w);
            this.x = (int)jsonObject.optLong("textColor", (long)this.x);
            this.y = (int)jsonObject.optLong("accentColor", (long)this.y);
            this.z = jsonObject.optBoolean("persistentAdDetails", this.z);
        }
        final JSONObject jsonObject2 = this.b.getJSONObject("text");
        this.a.setId(View.generateViewId());
        final int c = this.c();
        (this.v = new com.facebook.ads.internal.view.dpackage.b.i(this.c, (c < 0) ? 0 : c, this.y)).setOnTouchListener(this.l);
        this.a.a(this.v);
        if (this.b.has("cta") && !this.b.isNull("cta")) {
            final JSONObject jsonObject3 = this.b.getJSONObject("cta");
            this.n = new com.facebook.ads.internal.view.cpackage.a(this.c, jsonObject3.getString("url"), jsonObject3.getString("text"), this.y, this.a);
        }
        if (this.b.has("icon") && !this.b.isNull("icon")) {
            final JSONObject jsonObject4 = this.b.getJSONObject("icon");
            this.q = new ImageView(this.c);
            new p(this.q).a(jsonObject4.getString("url"));
        }
        if (this.b.has("image") && !this.b.isNull("image")) {
            final JSONObject jsonObject5 = this.b.getJSONObject("image");
            final f f = new f(this.c);
            this.a.a(f);
            f.setImage(jsonObject5.getString("url"));
        }
        final String optString2 = jsonObject2.optString("title");
        if (!optString2.isEmpty()) {
            (this.o = new TextView(this.c)).setText((CharSequence)optString2);
            this.o.setTypeface(Typeface.defaultFromStyle(1));
        }
        final String optString3 = jsonObject2.optString("subtitle");
        if (!optString3.isEmpty()) {
            (this.p = new TextView(this.c)).setText((CharSequence)optString3);
            this.p.setTextSize(16.0f);
        }
        this.s = new o(this.c);
        this.a.a(this.s);
        this.r = new com.facebook.ads.internal.view.dpackage.b.a.aClass(this.c, "AdChoices", "http://m.facebook.com/ads/ad_choices", new float[] { 0.0f, 0.0f, 8.0f, 0.0f }, this.b.getString("ct"));
        final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(10);
        layoutParams.addRule(9);
        this.r.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        this.a.a(new com.facebook.ads.internal.view.dpackage.b.j(this.c));
        final com.facebook.ads.internal.view.dpackage.b.k k = new com.facebook.ads.internal.view.dpackage.b.k(this.c);
        this.a.a(k);
        final com.facebook.ads.internal.view.dpackage.b.d.aType a = this.f() ? com.facebook.ads.internal.view.dpackage.b.d.aType.FADE_OUT_ON_PLAY : com.facebook.ads.internal.view.dpackage.b.d.aType.VISIBLE;
        this.a.a(new com.facebook.ads.internal.view.dpackage.b.d((View)k, a));
        this.u = new com.facebook.ads.internal.view.dpackage.b.d((View)new RelativeLayout(this.c), a);
        this.a.a(this.u);
    }
    
    private boolean k() {
        return ((this.a.getVideoHeight() > 0) ? (this.a.getVideoWidth() / this.a.getVideoHeight()) : -1.0f) <= 0.9;
    }
    
    private boolean l() {
        if (this.a.getVideoHeight() <= 0) {
            return false;
        }
        final Rect rect = new Rect();
        final float density = this.c.getResources().getDisplayMetrics().density;
        this.j.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        if (rect.width() > rect.height()) {
            return rect.width() - rect.height() * this.a.getVideoWidth() / this.a.getVideoHeight() - 192.0f * density < 0.0f;
        }
        return rect.height() - rect.width() * this.a.getVideoHeight() / this.a.getVideoWidth() - 64.0f * density - 64.0f * density - 40.0f * density < 0.0f;
    }
    
    private boolean m() {
        final float n = (this.a.getVideoHeight() > 0) ? (this.a.getVideoWidth() / this.a.getVideoHeight()) : -1.0f;
        return n > 0.9 && n < 1.1;
    }
    
    private void a(final int n) {
        final float density = this.c.getResources().getDisplayMetrics().density;
        final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int)(56.0f * density), (int)(56.0f * density));
        layoutParams.addRule(10);
        layoutParams.addRule(11);
        final int n2 = (int)(16.0f * density);
        this.v.setPadding(n2, n2, n2, n2);
        this.v.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        final com.facebook.ads.internal.view.dpackage.b.d.aType a = this.f() ? com.facebook.ads.internal.view.dpackage.b.d.aType.FADE_OUT_ON_PLAY : com.facebook.ads.internal.view.dpackage.b.d.aType.VISIBLE;
        final int id = this.a.getId();
        if (n == 1 && (this.k() || this.l())) {
            final GradientDrawable background = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] { 0, -15658735 });
            background.setCornerRadius(0.0f);
            final RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-1, -2);
            layoutParams2.addRule(10);
            this.a.setLayoutParams((ViewGroup.LayoutParams)layoutParams2);
            this.a((View)this.a);
            this.a(this.v);
            this.a((View)this.r);
            final RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(-1, (int)((((this.n != null) ? 64 : 0) + 60 + 16 + 16 + 16) * density));
            layoutParams3.addRule(12);
            final RelativeLayout t = new RelativeLayout(this.c);
            t.setBackground((Drawable)background);
            t.setLayoutParams((ViewGroup.LayoutParams)layoutParams3);
            t.setPadding(n2, 0, n2, (int)((((this.n != null) ? 64 : 0) + 16 + 16) * density));
            this.t = (ViewGroup)t;
            if (!this.z) {
                this.u.a((View)t, a);
            }
            this.a((View)t);
            if (this.s != null) {
                final RelativeLayout.LayoutParams layoutParams4 = new RelativeLayout.LayoutParams(-1, (int)(6.0f * density));
                layoutParams4.addRule(12);
                layoutParams4.topMargin = (int)(-6.0f * density);
                this.s.setLayoutParams((ViewGroup.LayoutParams)layoutParams4);
                this.a(this.s);
            }
            if (this.n != null) {
                final RelativeLayout.LayoutParams layoutParams5 = new RelativeLayout.LayoutParams(-1, (int)(64.0f * density));
                layoutParams5.bottomMargin = (int)(16.0f * density);
                layoutParams5.leftMargin = (int)(16.0f * density);
                layoutParams5.rightMargin = (int)(16.0f * density);
                layoutParams5.addRule(14);
                layoutParams5.addRule(12);
                this.n.setLayoutParams((ViewGroup.LayoutParams)layoutParams5);
                this.a((View)this.n);
            }
            if (this.q != null) {
                final RelativeLayout.LayoutParams layoutParams6 = new RelativeLayout.LayoutParams((int)(60.0f * density), (int)(60.0f * density));
                layoutParams6.addRule(12);
                layoutParams6.addRule(9);
                this.q.setLayoutParams((ViewGroup.LayoutParams)layoutParams6);
                this.a((ViewGroup)t, (View)this.q);
            }
            if (this.o != null) {
                final RelativeLayout.LayoutParams layoutParams7 = new RelativeLayout.LayoutParams(-1, -2);
                layoutParams7.bottomMargin = (int)(36.0f * density);
                layoutParams7.addRule(12);
                layoutParams7.addRule(9);
                this.o.setEllipsize(TextUtils.TruncateAt.END);
                this.o.setGravity(8388611);
                this.o.setLayoutParams((ViewGroup.LayoutParams)layoutParams7);
                this.o.setMaxLines(1);
                this.o.setPadding((int)(72.0f * density), 0, 0, 0);
                this.o.setTextColor(-1);
                this.o.setTextSize(18.0f);
                this.a((ViewGroup)t, (View)this.o);
            }
            if (this.p != null) {
                final RelativeLayout.LayoutParams layoutParams8 = new RelativeLayout.LayoutParams(-1, -2);
                layoutParams8.addRule(12);
                layoutParams8.addRule(9);
                layoutParams8.bottomMargin = (int)(4.0f * density);
                this.p.setEllipsize(TextUtils.TruncateAt.END);
                this.p.setGravity(8388611);
                this.p.setLayoutParams((ViewGroup.LayoutParams)layoutParams8);
                this.p.setMaxLines(1);
                this.p.setPadding((int)(72.0f * density), 0, 0, 0);
                this.p.setTextColor(-1);
                this.a((ViewGroup)t, (View)this.p);
            }
            ((ViewGroup)this.a.getParent()).setBackgroundColor(-16777216);
        }
        else if (n == 1) {
            final RelativeLayout.LayoutParams layoutParams9 = new RelativeLayout.LayoutParams(-1, -2);
            layoutParams9.addRule(10);
            this.a.setLayoutParams((ViewGroup.LayoutParams)layoutParams9);
            this.a((View)this.a);
            this.a(this.v);
            this.a((View)this.r);
            final LinearLayout t2 = new LinearLayout(this.c);
            ((LinearLayout)(this.t = (ViewGroup)t2)).setGravity(112);
            t2.setOrientation(1);
            final RelativeLayout.LayoutParams layoutParams10 = new RelativeLayout.LayoutParams(-1, -1);
            layoutParams10.leftMargin = (int)(33.0f * density);
            layoutParams10.rightMargin = (int)(33.0f * density);
            layoutParams10.topMargin = (int)(8.0f * density);
            if (this.n == null) {
                layoutParams10.bottomMargin = (int)(16.0f * density);
            }
            else {
                layoutParams10.bottomMargin = (int)(80.0f * density);
            }
            layoutParams10.addRule(3, id);
            t2.setLayoutParams((ViewGroup.LayoutParams)layoutParams10);
            this.a((View)t2);
            if (this.n != null) {
                final RelativeLayout.LayoutParams layoutParams11 = new RelativeLayout.LayoutParams(-1, (int)(64.0f * density));
                layoutParams11.bottomMargin = (int)(16.0f * density);
                layoutParams11.leftMargin = (int)(33.0f * density);
                layoutParams11.rightMargin = (int)(33.0f * density);
                layoutParams11.addRule(14);
                layoutParams11.addRule(12);
                this.n.setLayoutParams((ViewGroup.LayoutParams)layoutParams11);
                this.a((View)this.n);
            }
            if (this.o != null) {
                final LinearLayout.LayoutParams layoutParams12 = new LinearLayout.LayoutParams(-2, -2);
                layoutParams12.weight = 2.0f;
                layoutParams12.gravity = 17;
                this.o.setEllipsize(TextUtils.TruncateAt.END);
                this.o.setGravity(17);
                this.o.setLayoutParams((ViewGroup.LayoutParams)layoutParams12);
                this.o.setMaxLines(2);
                this.o.setPadding(0, 0, 0, 0);
                this.o.setTextColor(this.x);
                this.o.setTextSize(24.0f);
                this.a((ViewGroup)t2, (View)this.o);
            }
            if (this.q != null) {
                final LinearLayout.LayoutParams layoutParams13 = new LinearLayout.LayoutParams((int)(64.0f * density), (int)(64.0f * density));
                layoutParams13.weight = 0.0f;
                layoutParams13.gravity = 17;
                this.q.setLayoutParams((ViewGroup.LayoutParams)layoutParams13);
                this.a((ViewGroup)t2, (View)this.q);
            }
            if (this.p != null) {
                final LinearLayout.LayoutParams layoutParams14 = new LinearLayout.LayoutParams(-1, -2);
                layoutParams14.weight = 2.0f;
                layoutParams14.gravity = 16;
                this.p.setEllipsize(TextUtils.TruncateAt.END);
                this.p.setGravity(16);
                this.p.setLayoutParams((ViewGroup.LayoutParams)layoutParams14);
                this.p.setMaxLines(2);
                this.p.setPadding(0, 0, 0, 0);
                this.p.setTextColor(this.x);
                this.a((ViewGroup)t2, (View)this.p);
            }
            if (this.s != null) {
                final RelativeLayout.LayoutParams layoutParams15 = new RelativeLayout.LayoutParams(-1, (int)(6.0f * density));
                layoutParams15.addRule(3, id);
                this.s.setLayoutParams((ViewGroup.LayoutParams)layoutParams15);
                this.a(this.s);
            }
            ((ViewGroup)this.a.getParent()).setBackgroundColor(this.w);
        }
        else if (this.m() && !this.l()) {
            final RelativeLayout.LayoutParams layoutParams16 = new RelativeLayout.LayoutParams(-2, -1);
            layoutParams16.addRule(9);
            this.a.setLayoutParams((ViewGroup.LayoutParams)layoutParams16);
            this.a((View)this.a);
            this.a(this.v);
            this.a((View)this.r);
            final LinearLayout t3 = new LinearLayout(this.c);
            ((LinearLayout)(this.t = (ViewGroup)t3)).setGravity(112);
            t3.setOrientation(1);
            final RelativeLayout.LayoutParams layoutParams17 = new RelativeLayout.LayoutParams(-1, -1);
            layoutParams17.leftMargin = (int)(16.0f * density);
            layoutParams17.rightMargin = (int)(16.0f * density);
            layoutParams17.topMargin = (int)(8.0f * density);
            layoutParams17.bottomMargin = (int)(80.0f * density);
            layoutParams17.addRule(1, id);
            t3.setLayoutParams((ViewGroup.LayoutParams)layoutParams17);
            this.a((View)t3);
            if (this.s != null) {
                final RelativeLayout.LayoutParams layoutParams18 = new RelativeLayout.LayoutParams(-1, (int)(6.0f * density));
                layoutParams18.addRule(5, id);
                layoutParams18.addRule(7, id);
                layoutParams18.addRule(3, id);
                layoutParams18.topMargin = (int)(-6.0f * density);
                this.s.setLayoutParams((ViewGroup.LayoutParams)layoutParams18);
                this.a(this.s);
            }
            if (this.o != null) {
                final LinearLayout.LayoutParams layoutParams19 = new LinearLayout.LayoutParams(-2, -2);
                layoutParams19.weight = 2.0f;
                layoutParams19.gravity = 17;
                this.o.setEllipsize(TextUtils.TruncateAt.END);
                this.o.setGravity(17);
                this.o.setLayoutParams((ViewGroup.LayoutParams)layoutParams19);
                this.o.setMaxLines(10);
                this.o.setPadding(0, 0, 0, 0);
                this.o.setTextColor(this.x);
                this.o.setTextSize(24.0f);
                this.a((ViewGroup)t3, (View)this.o);
            }
            if (this.q != null) {
                final LinearLayout.LayoutParams layoutParams20 = new LinearLayout.LayoutParams((int)(64.0f * density), (int)(64.0f * density));
                layoutParams20.weight = 0.0f;
                layoutParams20.gravity = 17;
                this.q.setLayoutParams((ViewGroup.LayoutParams)layoutParams20);
                this.a((ViewGroup)t3, (View)this.q);
            }
            if (this.p != null) {
                final LinearLayout.LayoutParams layoutParams21 = new LinearLayout.LayoutParams(-1, -2);
                layoutParams21.weight = 2.0f;
                layoutParams21.gravity = 16;
                this.p.setEllipsize(TextUtils.TruncateAt.END);
                this.p.setGravity(17);
                this.p.setLayoutParams((ViewGroup.LayoutParams)layoutParams21);
                this.p.setMaxLines(10);
                this.p.setPadding(0, 0, 0, 0);
                this.p.setTextColor(this.x);
                this.a((ViewGroup)t3, (View)this.p);
            }
            if (this.n != null) {
                final RelativeLayout.LayoutParams layoutParams22 = new RelativeLayout.LayoutParams(-1, (int)(64.0f * density));
                layoutParams22.bottomMargin = (int)(16.0f * density);
                layoutParams22.leftMargin = (int)(16.0f * density);
                layoutParams22.rightMargin = (int)(16.0f * density);
                layoutParams22.addRule(14);
                layoutParams22.addRule(12);
                layoutParams22.addRule(1, id);
                this.n.setLayoutParams((ViewGroup.LayoutParams)layoutParams22);
                this.a((View)this.n);
            }
            ((ViewGroup)this.a.getParent()).setBackgroundColor(this.w);
        }
        else {
            final GradientDrawable background2 = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] { 0, -15658735 });
            background2.setCornerRadius(0.0f);
            this.a.setLayoutParams((ViewGroup.LayoutParams)new RelativeLayout.LayoutParams(-1, -1));
            this.a((View)this.a);
            this.a(this.v);
            this.a((View)this.r);
            final RelativeLayout.LayoutParams layoutParams23 = new RelativeLayout.LayoutParams(-1, (int)(124.0f * density));
            layoutParams23.addRule(12);
            final RelativeLayout t4 = new RelativeLayout(this.c);
            t4.setBackground((Drawable)background2);
            t4.setLayoutParams((ViewGroup.LayoutParams)layoutParams23);
            t4.setPadding(n2, 0, n2, n2);
            this.t = (ViewGroup)t4;
            if (!this.z) {
                this.u.a((View)t4, a);
            }
            this.a((View)t4);
            if (this.n != null) {
                final RelativeLayout.LayoutParams layoutParams24 = new RelativeLayout.LayoutParams((int)(110.0f * density), (int)(56.0f * density));
                layoutParams24.rightMargin = (int)(16.0f * density);
                layoutParams24.bottomMargin = (int)(16.0f * density);
                layoutParams24.addRule(12);
                layoutParams24.addRule(11);
                this.n.setLayoutParams((ViewGroup.LayoutParams)layoutParams24);
                this.a((View)this.n);
            }
            if (this.q != null) {
                final RelativeLayout.LayoutParams layoutParams25 = new RelativeLayout.LayoutParams((int)(64.0f * density), (int)(64.0f * density));
                layoutParams25.addRule(12);
                layoutParams25.addRule(9);
                layoutParams25.bottomMargin = (int)(8.0f * density);
                this.q.setLayoutParams((ViewGroup.LayoutParams)layoutParams25);
                this.a((ViewGroup)t4, (View)this.q);
            }
            if (this.o != null) {
                final RelativeLayout.LayoutParams layoutParams26 = new RelativeLayout.LayoutParams(-1, -2);
                layoutParams26.bottomMargin = (int)(48.0f * density);
                layoutParams26.addRule(12);
                layoutParams26.addRule(9);
                this.o.setEllipsize(TextUtils.TruncateAt.END);
                this.o.setGravity(8388611);
                this.o.setLayoutParams((ViewGroup.LayoutParams)layoutParams26);
                this.o.setMaxLines(1);
                this.o.setPadding((int)(80.0f * density), 0, (this.n != null) ? ((int)(126.0f * density)) : 0, 0);
                this.o.setTextColor(-1);
                this.o.setTextSize(24.0f);
                this.a((ViewGroup)t4, (View)this.o);
            }
            if (this.p != null) {
                final RelativeLayout.LayoutParams layoutParams27 = new RelativeLayout.LayoutParams(-1, -2);
                layoutParams27.addRule(12);
                layoutParams27.addRule(9);
                this.p.setEllipsize(TextUtils.TruncateAt.END);
                this.p.setGravity(8388611);
                this.p.setLayoutParams((ViewGroup.LayoutParams)layoutParams27);
                this.p.setMaxLines(2);
                this.p.setTextColor(-1);
                this.p.setPadding((int)(80.0f * density), 0, (this.n != null) ? ((int)(126.0f * density)) : 0, 0);
                this.a((ViewGroup)t4, (View)this.p);
            }
            if (this.s != null) {
                final RelativeLayout.LayoutParams layoutParams28 = new RelativeLayout.LayoutParams(-1, (int)(6.0f * density));
                layoutParams28.addRule(12);
                this.s.setLayoutParams((ViewGroup.LayoutParams)layoutParams28);
                this.a(this.s);
            }
            ((ViewGroup)this.a.getParent()).setBackgroundColor(-16777216);
        }
        final View rootView = this.a.getRootView();
        if (rootView != null) {
            rootView.setOnTouchListener((View.OnTouchListener)this);
        }
    }
    
    private void a(final View view) {
        if (this.i == null) {
            return;
        }
        this.i.a(view);
    }
    
    private void a(@Nullable final ViewGroup viewGroup, @Nullable final View view) {
        if (viewGroup != null) {
            viewGroup.addView(view);
        }
    }
    
    private void b(final View view) {
        if (view == null) {
            return;
        }
        final ViewGroup viewGroup = (ViewGroup)view.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(view);
        }
    }
    
    private void n() {
        this.b((View)this.a);
        this.b((View)this.n);
        this.b((View)this.o);
        this.b((View)this.p);
        this.b((View)this.q);
        this.b((View)this.r);
        this.b(this.s);
        this.b((View)this.t);
        this.b(this.v);
    }
    
    protected boolean f() {
        if (!com.facebook.ads.internal.adapters.k.h && this.b == null) {
            throw new AssertionError();
        }
        try {
            return this.b.getJSONObject("video").getBoolean("autoplay");
        }
        catch (Exception ex) {
            Log.w(String.valueOf(k.class), "Invalid JSON", (Throwable)ex);
            return true;
        }
    }
    
    @TargetApi(17)
    public void a(final Intent intent, final Bundle bundle, final AudienceNetworkActivity j) {
        this.j = j;
        if (!com.facebook.ads.internal.adapters.k.h && this.i == null) {
            throw new AssertionError();
        }
        j.addBackButtonInterceptor(this.k);
        this.n();
        this.a(this.j.getResources().getConfiguration().orientation);
        if (this.f()) {
            this.d();
        }
        else {
            this.e();
        }
    }
    
    public void a(final Bundle bundle) {
    }
    
    public void g() {
    }
    
    public void h() {
    }
    
    @Override
    public void onDestroy() {
        if (this.a != null) {
            this.a.g();
        }
        com.facebook.ads.internal.adapters.j.a(this);
    }
    
    public void a(final a i) {
        this.i = i;
    }
    
    public j.a i() {
        return this.m;
    }
    
    public void a(final Configuration configuration) {
        this.n();
        this.a(configuration.orientation);
    }
    
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        if (this.a != null) {
            this.a.getEventBus().a(new s(view, motionEvent));
        }
        return true;
    }
    
    public void j() {
        if (this.j != null) {
            this.j.finish();
        }
    }
    
    static {
        h = !k.class.desiredAssertionStatus();
    }
}
