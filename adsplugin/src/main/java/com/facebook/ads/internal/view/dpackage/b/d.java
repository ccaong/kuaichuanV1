// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view.dpackage.b;

import com.facebook.ads.internal.view.dpackage.a.b;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import com.facebook.ads.internal.view.dpackage.a.j;
import com.facebook.ads.internal.gpackage.q;
import com.facebook.ads.internal.view.dpackage.a.h;
import com.facebook.ads.internal.gpackage.s;
import com.facebook.ads.internal.view.dpackage.a.c;
import com.facebook.ads.internal.view.dpackage.a.k;
import com.facebook.ads.internal.view.dpackage.a.i;
import com.facebook.ads.internal.view.n;
import android.support.annotation.Nullable;
import android.view.View;
import android.os.Handler;
import android.annotation.TargetApi;

@TargetApi(12)
public class d implements m
{
    private final Handler a;
    private View b;
    @Nullable
    private aType c;
    @Nullable
    private n d;
    private final i e;
    private final k f;
    private final c g;
    private final s<com.facebook.ads.internal.view.dpackage.a.s> h;
    
    public d(final View b, final aType c) {
        this.e = new i() {
            @Override
            public void a(final h h) {
                com.facebook.ads.internal.view.dpackage.b.d.this.a.removeCallbacksAndMessages((Object)null);
                com.facebook.ads.internal.view.dpackage.b.d.this.b.clearAnimation();
                com.facebook.ads.internal.view.dpackage.b.d.this.b.setAlpha(1.0f);
                com.facebook.ads.internal.view.dpackage.b.d.this.b.setVisibility(0);
            }
        };
        this.f = new k() {
            @Override
            public void a(final j j) {
                if (com.facebook.ads.internal.view.dpackage.b.d.this.c == aType.FADE_OUT_ON_PLAY) {
                    com.facebook.ads.internal.view.dpackage.b.d.this.c = null;
                    com.facebook.ads.internal.view.dpackage.b.d.this.b.animate().alpha(0.0f).setDuration(2000L).setListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
                        public void onAnimationEnd(final Animator animator) {
                            com.facebook.ads.internal.view.dpackage.b.d.this.b.setVisibility(8);
                        }
                    });
                    return;
                }
                com.facebook.ads.internal.view.dpackage.b.d.this.a.removeCallbacksAndMessages((Object)null);
                com.facebook.ads.internal.view.dpackage.b.d.this.b.clearAnimation();
                com.facebook.ads.internal.view.dpackage.b.d.this.b.setAlpha(0.0f);
                com.facebook.ads.internal.view.dpackage.b.d.this.b.setVisibility(8);
            }
        };
        this.g = new c() {
            @Override
            public void a(final b b) {
                if (com.facebook.ads.internal.view.dpackage.b.d.this.c != aType.INVISIBLE) {
                    com.facebook.ads.internal.view.dpackage.b.d.this.b.setAlpha(1.0f);
                    com.facebook.ads.internal.view.dpackage.b.d.this.b.setVisibility(0);
                }
            }
        };
        this.h = new s<com.facebook.ads.internal.view.dpackage.a.s>() {
            @Override
            public void a(final com.facebook.ads.internal.view.dpackage.a.s s) {
                if (com.facebook.ads.internal.view.dpackage.b.d.this.d == null) {
                    return;
                }
                if (s.b().getAction() == 0) {
                    com.facebook.ads.internal.view.dpackage.b.d.this.a.removeCallbacksAndMessages((Object)null);
                    com.facebook.ads.internal.view.dpackage.b.d.this.b.setVisibility(0);
                    com.facebook.ads.internal.view.dpackage.b.d.this.b.animate().alpha(1.0f).setDuration(500L).setListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
                        public void onAnimationEnd(final Animator animator) {
                            com.facebook.ads.internal.view.dpackage.b.d.this.a.postDelayed((Runnable)new Runnable() {
                                @Override
                                public void run() {
                                    com.facebook.ads.internal.view.dpackage.b.d.this.b.animate().alpha(0.0f).setDuration(500L).setListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
                                        public void onAnimationEnd(final Animator animator) {
                                            com.facebook.ads.internal.view.dpackage.b.d.this.b.setVisibility(8);
                                        }
                                    });
                                }
                            }, 2000L);
                        }
                    });
                }
            }
            
            @Override
            public Class<com.facebook.ads.internal.view.dpackage.a.s> a() {
                return com.facebook.ads.internal.view.dpackage.a.s.class;
            }
        };
        this.b = b;
        this.c = c;
        this.a = new Handler();
        this.b.clearAnimation();
        if (c == aType.INVISIBLE) {
            this.b.setAlpha(0.0f);
            this.b.setVisibility(8);
        }
        else {
            this.b.setAlpha(1.0f);
            this.b.setVisibility(0);
        }
    }
    
    public void a(final View b, final aType c) {
        this.b = b;
        this.c = c;
        this.b.clearAnimation();
        if (c == aType.INVISIBLE) {
            this.b.setAlpha(0.0f);
            this.b.setVisibility(8);
        }
        else {
            this.b.setAlpha(1.0f);
            this.b.setVisibility(0);
        }
    }
    
    @Override
    public void a(final n d) {
        d.getEventBus().a(this.e);
        d.getEventBus().a(this.f);
        d.getEventBus().a(this.h);
        d.getEventBus().a(this.g);
        this.d = d;
    }
    
    public enum aType
    {
        VISIBLE, //a
        INVISIBLE, //b
        FADE_OUT_ON_PLAY;//c
    }
}
