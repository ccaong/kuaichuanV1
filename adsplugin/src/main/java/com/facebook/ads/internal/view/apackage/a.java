// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view.apackage;

import android.view.MotionEvent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Bitmap;
import java.util.List;
import android.content.pm.ResolveInfo;
import android.content.Intent;
import android.text.TextUtils;
import android.view.ViewGroup;
import com.facebook.ads.internal.util.t;
import com.facebook.ads.internal.util.r;
import android.content.Context;
import android.widget.ImageView;
import android.view.View;
import android.net.Uri;
import android.annotation.TargetApi;
import android.widget.LinearLayout;

@TargetApi(19)
public class a extends LinearLayout
{
    private static final int a;
    private static final Uri b;
    private static final View.OnTouchListener c;
    private static final int d;
    private ImageView e;
    private c f;
    private ImageView g;
    private aListner h;
    private String i;
    
    public a(final Context context) {
        super(context);
        this.a(context);
    }
    
    private void a(final Context context) {
        final float density = this.getResources().getDisplayMetrics().density;
        final int n = (int)(50.0f * density);
        final int n2 = (int)(4.0f * density);
        this.setBackgroundColor(-1);
        this.setGravity(16);
        this.e = new ImageView(context);
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(n, n);
        this.e.setScaleType(ImageView.ScaleType.CENTER);
        this.e.setImageBitmap(t.a(context, r.BROWSER_CLOSE));
        this.e.setOnTouchListener(com.facebook.ads.internal.view.apackage.a.c);
        this.e.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
                if (com.facebook.ads.internal.view.apackage.a.this.h != null) {
                    com.facebook.ads.internal.view.apackage.a.this.h.a();
                }
            }
        });
        this.addView((View)this.e, (ViewGroup.LayoutParams)layoutParams);
        this.f = new c(context);
        final LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(0, -2);
        layoutParams2.weight = 1.0f;
        this.f.setPadding(0, n2, 0, n2);
        this.addView((View)this.f, (ViewGroup.LayoutParams)layoutParams2);
        this.g = new ImageView(context);
        final LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(n, n);
        this.g.setScaleType(ImageView.ScaleType.CENTER);
        this.g.setOnTouchListener(com.facebook.ads.internal.view.apackage.a.c);
        this.g.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
                if (TextUtils.isEmpty((CharSequence)com.facebook.ads.internal.view.apackage.a.this.i) || "about:blank".equals(com.facebook.ads.internal.view.apackage.a.this.i)) {
                    return;
                }
                final Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(com.facebook.ads.internal.view.apackage.a.this.i));
                intent.addFlags(268435456);
                try {
                    com.facebook.ads.internal.view.apackage.a.this.getContext().startActivity(intent);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        this.addView((View)this.g, (ViewGroup.LayoutParams)layoutParams3);
        this.setupDefaultNativeBrowser(context);
    }
    
    private void setupDefaultNativeBrowser(final Context context) {
        final List<ResolveInfo> queryIntentActivities = context.getPackageManager().queryIntentActivities(new Intent("android.intent.action.VIEW", com.facebook.ads.internal.view.apackage.a.b), 65536);
        Bitmap imageBitmap;
        if (queryIntentActivities.size() == 0) {
            this.g.setVisibility(8);
            imageBitmap = null;
        }
        else if (queryIntentActivities.size() == 1 && "com.android.chrome".equals(queryIntentActivities.get(0).activityInfo.packageName)) {
            imageBitmap = t.a(context, r.BROWSER_LAUNCH_CHROME);
        }
        else {
            imageBitmap = t.a(context, r.BROWSER_LAUNCH_NATIVE);
        }
        this.g.setImageBitmap(imageBitmap);
    }
    
    public void setListener(final aListner h) {
        this.h = h;
    }
    
    public void setTitle(final String title) {
        this.f.setTitle(title);
    }
    
    public void setUrl(final String s) {
        this.i = s;
        if (TextUtils.isEmpty((CharSequence)s) || "about:blank".equals(s)) {
            this.f.setSubtitle(null);
            this.g.setEnabled(false);
            this.g.setColorFilter((ColorFilter)new PorterDuffColorFilter(com.facebook.ads.internal.view.apackage.a.a, PorterDuff.Mode.SRC_IN));
        }
        else {
            this.f.setSubtitle(s);
            this.g.setEnabled(true);
            this.g.setColorFilter((ColorFilter)null);
        }
    }
    
    static {
        a = Color.rgb(224, 224, 224);
        b = Uri.parse("http://www.facebook.com");
        c = (View.OnTouchListener)new View.OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case 0: {
                        view.setBackgroundColor(com.facebook.ads.internal.view.apackage.a.d);
                        break;
                    }
                    case 1: {
                        view.setBackgroundColor(0);
                        break;
                    }
                }
                return false;
            }
        };
        d = Color.argb(34, 0, 0, 0);
    }
    
    public interface aListner
    {
        void a();
    }
}
