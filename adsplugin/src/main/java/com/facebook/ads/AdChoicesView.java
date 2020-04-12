// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads;

import android.os.Handler;
import android.view.animation.Transformation;
import android.view.animation.Animation;
import android.graphics.Paint;
import android.widget.ImageView;
import android.view.ViewGroup;
import com.facebook.ads.internal.util.g;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.text.TextUtils;
import android.widget.TextView;
import android.util.DisplayMetrics;
import android.content.Context;
import android.widget.RelativeLayout;

public class AdChoicesView extends RelativeLayout
{
    private final Context a;
    private final NativeAd b;
    private final DisplayMetrics c;
    private boolean d;
    private TextView e;
    private String f;
    
    public AdChoicesView(final Context context, final NativeAd nativeAd) {
        this(context, nativeAd, false);
    }
    
    public AdChoicesView(final Context a, final NativeAd b, final boolean b2) {
        super(a);
        this.d = false;
        this.a = a;
        this.b = b;
        this.c = this.a.getResources().getDisplayMetrics();
        if (this.b.isAdLoaded() && !this.b.a().g()) {
            this.setVisibility(8);
            return;
        }
        this.f = this.b.b();
        if (TextUtils.isEmpty((CharSequence)this.f)) {
            this.f = "AdChoices";
        }
        final NativeAd.Image adChoicesIcon = this.b.getAdChoicesIcon();
        final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        this.setOnTouchListener((View.OnTouchListener)new View.OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    if (AdChoicesView.this.d) {
                        if (!TextUtils.isEmpty((CharSequence)AdChoicesView.this.b.getAdChoicesLinkUrl())) {
                            g.a(AdChoicesView.this.a, Uri.parse(AdChoicesView.this.b.getAdChoicesLinkUrl()), b.h());
                        }
                    }
                    else {
                        AdChoicesView.this.a();
                    }
                    return true;
                }
                return false;
            }
        });
        this.addView((View)(this.e = new TextView(this.a)));
        final RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-2, -2);
        if (b2 && adChoicesIcon != null) {
            layoutParams2.addRule(11, this.a(adChoicesIcon).getId());
            layoutParams2.width = 0;
            layoutParams.width = Math.round((adChoicesIcon.getWidth() + 4) * this.c.density);
            layoutParams.height = Math.round((adChoicesIcon.getHeight() + 2) * this.c.density);
            this.d = false;
        }
        else {
            this.d = true;
        }
        this.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        layoutParams2.addRule(15, -1);
        this.e.setLayoutParams((ViewGroup.LayoutParams)layoutParams2);
        this.e.setSingleLine();
        this.e.setText((CharSequence)this.f);
        this.e.setTextSize(10.0f);
        this.e.setTextColor(-4341303);
    }
    
    private ImageView a(final NativeAd.Image image) {
        final ImageView imageView = new ImageView(this.a);
        this.addView((View)imageView);
        final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(Math.round(image.getWidth() * this.c.density), Math.round(image.getHeight() * this.c.density));
        layoutParams.addRule(9);
        layoutParams.addRule(15, -1);
        layoutParams.setMargins(Math.round(4.0f * this.c.density), Math.round(2.0f * this.c.density), Math.round(2.0f * this.c.density), Math.round(2.0f * this.c.density));
        imageView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        NativeAd.downloadAndDisplayImage(image, imageView);
        return imageView;
    }
    
    private void a() {
        final Paint paint = new Paint();
        paint.setTextSize(this.e.getTextSize());
        final int round = Math.round(paint.measureText(this.f) + 4.0f * this.c.density);
        final int width = this.getWidth();
        final int n = width + round;
        this.d = true;
        final Animation animation = new Animation() {
            protected void applyTransformation(final float n, final Transformation transformation) {
                final int d = (int)(width + (n - width) * n);
                AdChoicesView.this.getLayoutParams().width = d;
                AdChoicesView.this.requestLayout();
                AdChoicesView.this.e.getLayoutParams().width = d - width;
                AdChoicesView.this.e.requestLayout();
            }
            
            public boolean willChangeBounds() {
                return true;
            }
        };
        animation.setAnimationListener((Animation.AnimationListener)new Animation.AnimationListener() {
            public void onAnimationStart(final Animation animation) {
            }
            
            public void onAnimationRepeat(final Animation animation) {
            }
            
            public void onAnimationEnd(final Animation animation) {
                new Handler().postDelayed((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        if (!AdChoicesView.this.d) {
                            return;
                        }
                        AdChoicesView.this.d = false;
                        final Animation animation = new Animation() {
                            protected void applyTransformation(final float n, final Transformation transformation) {
                                final int d = (int)(n + (width - n) * n);
                                AdChoicesView.this.getLayoutParams().width = d;
                                AdChoicesView.this.requestLayout();
                                AdChoicesView.this.e.getLayoutParams().width = d - width;
                                AdChoicesView.this.e.requestLayout();
                            }
                            
                            public boolean willChangeBounds() {
                                return true;
                            }
                        };
                        animation.setDuration(300L);
                        animation.setFillAfter(true);
                        AdChoicesView.this.startAnimation((Animation)animation);
                    }
                }, 3000L);
            }
        });
        animation.setDuration(300L);
        animation.setFillAfter(true);
        this.startAnimation((Animation)animation);
    }
}
