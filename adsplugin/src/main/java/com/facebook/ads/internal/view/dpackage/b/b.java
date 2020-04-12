package com.facebook.ads.internal.view.dpackage.b;

import com.facebook.ads.internal.view.dpackage.a.j;
import com.facebook.ads.internal.view.dpackage.a.h;
import com.facebook.ads.internal.gpackage.q;
import android.content.Context;
import com.facebook.ads.internal.view.dpackage.a.k;
import com.facebook.ads.internal.view.dpackage.a.i;
import com.facebook.ads.internal.view.dpackage.a.c;
import java.lang.ref.WeakReference;
import android.media.AudioManager;

public class b extends n implements AudioManager.OnAudioFocusChangeListener
{
    private WeakReference<AudioManager.OnAudioFocusChangeListener> b;
    private final c c;
    private final i d;
    private final k e;
    
    public b(final Context context) {
        super(context);
        this.b = null;
        this.c = new c() {
            @Override
            public void a(final com.facebook.ads.internal.view.dpackage.a.b b) {
                ((AudioManager)b.this.getContext().getApplicationContext().getSystemService("audio")).abandonAudioFocus((b.this.b == null) ? null : ((AudioManager.OnAudioFocusChangeListener)b.this.b.get()));
            }
        };
        this.d = new i() {
            @Override
            public void a(final h h) {
                ((AudioManager)com.facebook.ads.internal.view.dpackage.b.b.this.getContext().getApplicationContext().getSystemService("audio")).abandonAudioFocus((com.facebook.ads.internal.view.dpackage.b.b.this.b == null) ? null : ((AudioManager.OnAudioFocusChangeListener)com.facebook.ads.internal.view.dpackage.b.b.this.b.get()));
            }
        };
        this.e = new k() {
            @Override
            public void a(final j j) {
                if (com.facebook.ads.internal.view.dpackage.b.b.this.b == null || com.facebook.ads.internal.view.dpackage.b.b.this.b.get() == null) {
                    com.facebook.ads.internal.view.dpackage.b.b.this.b = new WeakReference((AudioManager.OnAudioFocusChangeListener)new AudioManager.OnAudioFocusChangeListener() {
                        public void onAudioFocusChange(final int n) {
                            if (com.facebook.ads.internal.view.dpackage.b.b.this.getVideoView() == null) {
                                return;
                            }
                            if (n <= 0) {
                                com.facebook.ads.internal.view.dpackage.b.b.this.getVideoView().e();
                            }
                        }
                    });
                }
                ((AudioManager)com.facebook.ads.internal.view.dpackage.b.b.this.getContext().getApplicationContext().getSystemService("audio")).requestAudioFocus((AudioManager.OnAudioFocusChangeListener)com.facebook.ads.internal.view.dpackage.b.b.this.b.get(), 3, 1);
            }
        };
    }
    
    @Override
    protected void a_(final com.facebook.ads.internal.view.n n) {
        n.getEventBus().a(this.e);
        n.getEventBus().a(this.c);
        n.getEventBus().a(this.d);
        super.a_(n);
    }
    
    public void onAudioFocusChange(final int n) {
        if (this.getVideoView() == null) {
            return;
        }
        if (n <= 0) {
            this.getVideoView().e();
        }
    }
    
    protected void onDetachedFromWindow() {
        ((AudioManager)this.getContext().getApplicationContext().getSystemService("audio")).abandonAudioFocus((this.b == null) ? null : this.b.get());
        super.onDetachedFromWindow();
    }
}
