// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads;

import android.graphics.Color;
import com.facebook.ads.internal.view.j;
import com.facebook.ads.internal.gpackage.f;
import java.util.Iterator;
import android.text.TextUtils;
import android.os.Build;
import com.facebook.ads.internal.util.p;
import android.support.v7.widget.RecyclerView;
import com.facebook.ads.internal.adapters.g;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import com.facebook.ads.internal.view.hscroll.b;
import com.facebook.ads.internal.view.i;
import com.facebook.ads.internal.view.e;
import android.support.annotation.Nullable;
import android.widget.RelativeLayout;

public class MediaView extends RelativeLayout
{
    private static final String a;
    private static final int b;
    @Nullable
    private MediaViewListener c;
    private final e d;
    private final i e;
    private final b f;
    private boolean g;
    @Deprecated
    private boolean h;
    
    public MediaView(final Context context, final AttributeSet set) {
        super(context, set);
        this.g = false;
        this.h = true;
        this.setBackgroundColor(MediaView.b);
        final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        (this.d = new e(context)).setVisibility(8);
        this.addView((View)this.d, (ViewGroup.LayoutParams)layoutParams);
        (this.e = new i(context, this.getAdEventManager())).setVisibility(8);
        layoutParams.addRule(13);
        this.addView((View)this.e, (ViewGroup.LayoutParams)layoutParams);
        final float density = context.getResources().getDisplayMetrics().density;
        final int round = Math.round(density * 4.0f);
        final int round2 = Math.round(density * 12.0f);
        (this.f = new b(this.getContext())).setChildSpacing(round);
        this.f.setPadding(0, round2, 0, round2);
        this.f.setVisibility(8);
        this.addView((View)this.f, (ViewGroup.LayoutParams)layoutParams);
    }
    
    public MediaView(final Context context) {
        this(context, null);
    }
    
    public void setNativeAd(final NativeAd nativeAd) {
        nativeAd.a(this);
        nativeAd.setMediaViewAutoplay(this.h);
        if (this.g) {
            this.d.a(null, null);
            this.g = false;
        }
        final String image = (nativeAd.getAdCoverImage() != null) ? nativeAd.getAdCoverImage().getUrl() : null;
        if (this.b(nativeAd)) {
            this.d.setVisibility(8);
            this.e.setVisibility(8);
            this.f.setVisibility(0);
            this.bringChildToFront((View)this.f);
            this.f.setCurrentPosition(0);
            this.f.setAdapter(new g(this.f, nativeAd.g()));
        }
        else if (this.a(nativeAd)) {
            final String c = nativeAd.c();
            final String d = nativeAd.d();
            this.e.setImage(null);
            this.d.setVisibility(8);
            this.e.setVisibility(0);
            this.f.setVisibility(8);
            this.bringChildToFront((View)this.e);
            this.g = true;
            this.e.setAutoplay(this.h);
            this.e.setIsAutoPlayFromServer(nativeAd.f());
            if (image != null) {
                this.e.setImage(image);
            }
            this.e.a(nativeAd.e(), nativeAd.h());
            this.e.setVideoMPD(d);
            this.e.setVideoURI(c);
        }
        else if (image != null) {
            this.d.setVisibility(0);
            this.e.setVisibility(8);
            this.f.setVisibility(8);
            this.bringChildToFront((View)this.d);
            this.g = true;
            new p(this.d).a(image);
        }
    }
    
    private boolean a(final NativeAd nativeAd) {
        return Build.VERSION.SDK_INT >= 14 && !TextUtils.isEmpty((CharSequence)nativeAd.c());
    }
    
    private boolean b(final NativeAd nativeAd) {
        if (nativeAd.g() == null) {
            return false;
        }
        final Iterator<NativeAd> iterator = nativeAd.g().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getAdCoverImage() == null) {
                return false;
            }
        }
        return true;
    }
    
    protected f getAdEventManager() {
        return com.facebook.ads.internal.gpackage.g.a(this.getContext());
    }
    
    @Deprecated
    public boolean isAutoplay() {
        return this.h;
    }
    
    @Deprecated
    public void setAutoplayOnMobile(final boolean isAutoplayOnMobile) {
        this.e.setIsAutoplayOnMobile(isAutoplayOnMobile);
    }
    
    @Deprecated
    public void setAutoplay(final boolean b) {
        this.h = b;
        this.e.setAutoplay(b);
    }
    
    public void setListener(final MediaViewListener c) {
        this.c = c;
        if (c == null) {
            this.e.setListener(null);
            return;
        }
        this.e.setListener(new j() {
            @Override
            public void a() {
                c.onVolumeChange(MediaView.this, MediaView.this.e.getVolume());
            }
            
            @Override
            public void b() {
                c.onPause(MediaView.this);
            }
            
            @Override
            public void c() {
                c.onPlay(MediaView.this);
            }
            
            @Override
            public void d() {
                c.onFullscreenBackground(MediaView.this);
            }
            
            @Override
            public void e() {
                c.onFullscreenForeground(MediaView.this);
            }
            
            @Override
            public void f() {
                c.onExitFullscreen(MediaView.this);
            }
            
            @Override
            public void g() {
                c.onEnterFullscreen(MediaView.this);
            }
            
            @Override
            public void h() {
                c.onComplete(MediaView.this);
            }
        });
    }
    
    public void destroy() {
        this.e.g();
    }
    
    static {
        a = MediaView.class.getSimpleName();
        b = Color.argb(51, 145, 150, 165);
    }
}
