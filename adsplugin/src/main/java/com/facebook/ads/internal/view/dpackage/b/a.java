package com.facebook.ads.internal.view.dpackage.b;

import android.os.Handler;
import android.view.animation.Transformation;
import android.view.animation.Animation;
import android.graphics.Paint;
import android.view.ViewGroup;
import com.facebook.ads.internal.util.t;
import com.facebook.ads.internal.util.r;
import com.facebook.ads.internal.util.g;
import android.net.Uri;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.graphics.drawable.GradientDrawable;
import android.widget.TextView;
import android.widget.ImageView;
import android.util.DisplayMetrics;
import android.widget.RelativeLayout;
import android.view.View;
import android.content.Context;

public class a extends n
{
    private final aClass b;
    
    public a(final Context context, final String s, final String s2, final float[] array) {
        super(context);
        this.addView((View)(this.b = new aClass(context, "AdChoices", s, array, s2)));
    }
    
    public static class aClass extends RelativeLayout
    {
        private final String a;
        private final String b;
        private final String c;
        private final DisplayMetrics d;
        private ImageView e;
        private TextView f;
        private boolean g;
        
        public aClass(final Context context, final String a, final String b, final float[] array, final String c) {
            super(context);
            this.g = false;
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = context.getResources().getDisplayMetrics();
            final GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(-16777216);
            gradientDrawable.setAlpha(178);
            gradientDrawable.setCornerRadii(new float[] { array[0] * this.d.density, array[0] * this.d.density, array[1] * this.d.density, array[1] * this.d.density, array[2] * this.d.density, array[2] * this.d.density, array[3] * this.d.density, array[3] * this.d.density });
            if (Build.VERSION.SDK_INT >= 16) {
                this.setBackground((Drawable)gradientDrawable);
            }
            else {
                this.setBackgroundDrawable((Drawable)gradientDrawable);
            }
            this.a();
            this.b();
            this.c();
            this.setMinimumWidth(Math.round(20.0f * this.d.density));
            this.setMinimumHeight(Math.round(18.0f * this.d.density));
        }
        
        private void a() {
            this.setOnTouchListener((View.OnTouchListener)new View.OnTouchListener() {
                public boolean onTouch(final View view, final MotionEvent motionEvent) {
                    if (motionEvent.getAction() == 0) {
                        if (com.facebook.ads.internal.view.dpackage.b.a.aClass.this.g) {
                            if (!TextUtils.isEmpty((CharSequence)com.facebook.ads.internal.view.dpackage.b.a.aClass.this.b)) {
                                com.facebook.ads.internal.util.g.a(com.facebook.ads.internal.view.dpackage.b.a.aClass.this.getContext(), Uri.parse(com.facebook.ads.internal.view.dpackage.b.a.aClass.this.b), com.facebook.ads.internal.view.dpackage.b.a.aClass.this.c);
                            }
                        }
                        else {
                            com.facebook.ads.internal.view.dpackage.b.a.aClass.this.d();
                        }
                        return true;
                    }
                    return false;
                }
            });
        }
        
        private void b() {
            final Context context = this.getContext();
            (this.e = new ImageView(context)).setImageBitmap(t.a(context, r.IC_AD_CHOICES));
            this.addView((View)this.e);
            final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(Math.round(16.0f * this.d.density), Math.round(16.0f * this.d.density));
            layoutParams.addRule(9);
            layoutParams.addRule(15, -1);
            layoutParams.setMargins(Math.round(4.0f * this.d.density), Math.round(2.0f * this.d.density), Math.round(2.0f * this.d.density), Math.round(2.0f * this.d.density));
            this.e.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        }
        
        private void c() {
            this.addView((View)(this.f = new TextView(this.getContext())));
            final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
            layoutParams.width = 0;
            layoutParams.leftMargin = (int)(20.0f * this.d.density);
            layoutParams.addRule(9);
            layoutParams.addRule(15, -1);
            this.f.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
            this.f.setSingleLine();
            this.f.setText((CharSequence)this.a);
            this.f.setTextSize(10.0f);
            this.f.setTextColor(-4341303);
        }
        
        private void d() {
            final Paint paint = new Paint();
            paint.setTextSize(this.f.getTextSize());
            final int round = Math.round(paint.measureText(this.a) + 4.0f * this.d.density);
            final int width = this.getWidth();
            final int n = width + round;
            this.g = true;
            final Animation animation = new Animation() {
                protected void applyTransformation(final float n, final Transformation transformation) {
                    final int d = (int)(width + (n - width) * n);
                    com.facebook.ads.internal.view.dpackage.b.a.aClass.this.getLayoutParams().width = d;
                    com.facebook.ads.internal.view.dpackage.b.a.aClass.this.requestLayout();
                    com.facebook.ads.internal.view.dpackage.b.a.aClass.this.f.getLayoutParams().width = d - width;
                    com.facebook.ads.internal.view.dpackage.b.a.aClass.this.f.requestLayout();
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
                            if (!com.facebook.ads.internal.view.dpackage.b.a.aClass.this.g) {
                                return;
                            }
                            com.facebook.ads.internal.view.dpackage.b.a.aClass.this.g = false;
                            final Animation animation = new Animation() {
                                protected void applyTransformation(final float n, final Transformation transformation) {
                                    final int d = (int)(n + (width - n) * n);
                                    com.facebook.ads.internal.view.dpackage.b.a.aClass.this.getLayoutParams().width = d;
                                    com.facebook.ads.internal.view.dpackage.b.a.aClass.this.requestLayout();
                                    com.facebook.ads.internal.view.dpackage.b.a.aClass.this.f.getLayoutParams().width = d - width;
                                    com.facebook.ads.internal.view.dpackage.b.a.aClass.this.f.requestLayout();
                                }
                                
                                public boolean willChangeBounds() {
                                    return true;
                                }
                            };
                            animation.setDuration(300L);
                            animation.setFillAfter(true);
                            com.facebook.ads.internal.view.dpackage.b.a.aClass.this.startAnimation((Animation)animation);
                        }
                    }, 3000L);
                }
            });
            animation.setDuration(300L);
            animation.setFillAfter(true);
            this.startAnimation((Animation)animation);
        }
    }
}
