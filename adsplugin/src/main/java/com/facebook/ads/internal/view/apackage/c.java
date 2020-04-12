// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view.apackage;

import com.facebook.ads.internal.util.t;
import com.facebook.ads.internal.util.r;
import android.net.Uri;
import android.view.ViewGroup;
import android.view.View;
import android.text.TextUtils;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;
import android.annotation.TargetApi;
import android.widget.LinearLayout;

@TargetApi(19)
public class c extends LinearLayout
{
    private TextView a;
    private TextView b;
    private Drawable c;
    
    public c(final Context context) {
        super(context);
        this.a();
    }
    
    private void a() {
        final float density = this.getResources().getDisplayMetrics().density;
        this.setOrientation(1);
        this.a = new TextView(this.getContext());
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        this.a.setTextColor(-16777216);
        this.a.setTextSize(2, 20.0f);
        this.a.setEllipsize(TextUtils.TruncateAt.END);
        this.a.setSingleLine(true);
        this.a.setVisibility(8);
        this.addView((View)this.a, (ViewGroup.LayoutParams)layoutParams);
        this.b = new TextView(this.getContext());
        final LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
        this.b.setAlpha(0.5f);
        this.b.setTextColor(-16777216);
        this.b.setTextSize(2, 15.0f);
        this.b.setCompoundDrawablePadding((int)(5.0f * density));
        this.b.setEllipsize(TextUtils.TruncateAt.END);
        this.b.setSingleLine(true);
        this.b.setVisibility(8);
        this.addView((View)this.b, (ViewGroup.LayoutParams)layoutParams2);
    }
    
    public void setTitle(final String text) {
        if (TextUtils.isEmpty((CharSequence)text)) {
            this.a.setText((CharSequence)null);
            this.a.setVisibility(8);
        }
        else {
            this.a.setText((CharSequence)text);
            this.a.setVisibility(0);
        }
    }
    
    public void setSubtitle(final String s) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            this.b.setText((CharSequence)null);
            this.b.setVisibility(8);
        }
        else {
            final Uri parse = Uri.parse(s);
            this.b.setText((CharSequence)parse.getHost());
            this.b.setCompoundDrawablesRelativeWithIntrinsicBounds("https".equals(parse.getScheme()) ? this.getPadlockDrawable() : null, (Drawable)null, (Drawable)null, (Drawable)null);
            this.b.setVisibility(0);
        }
    }
    
    private Drawable getPadlockDrawable() {
        if (this.c == null) {
            this.c = t.b(this.getContext(), r.BROWSER_PADLOCK);
        }
        return this.c;
    }
}
