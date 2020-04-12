// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view.dpackage.c;

import com.facebook.ads.internal.util.b;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Timeline;
import android.view.MotionEvent;
import android.graphics.SurfaceTexture;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.facebook.ads.AdSettings;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import android.content.Context;
import android.view.View;
import android.widget.MediaController;
import android.support.annotation.Nullable;
import android.view.Surface;
import android.os.Handler;
import android.net.Uri;
import android.annotation.TargetApi;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ExoPlayer;
import android.view.TextureView;

@TargetApi(14)
public class a extends TextureView implements TextureView.SurfaceTextureListener, c, ExoPlayer.EventListener, SimpleExoPlayer.VideoListener
{
    private static final String a;
    private Uri b;
    private String c;
    private e d;
    private Handler e;
    private Surface f;
    @Nullable
    private SimpleExoPlayer g;
    private MediaController h;
    private d i;
    private d j;
    private View k;
    private boolean l;
    private boolean m;
    private long n;
    private long o;
    private long p;
    private int q;
    private int r;
    private float s;
    private int t;
    
    public a(final Context context) {
        super(context);
        this.i = com.facebook.ads.internal.view.dpackage.c.d.IDLE;
        this.j = com.facebook.ads.internal.view.dpackage.c.d.IDLE;
        this.l = false;
        this.m = false;
        this.s = 1.0f;
        this.t = -1;
        this.e = new Handler();
    }
    
    public void setup(final Uri b) {
        if (this.g != null && this.i != com.facebook.ads.internal.view.dpackage.c.d.PLAYBACK_COMPLETED) {
            this.d();
        }
        this.b = b;
        this.setSurfaceTextureListener((TextureView.SurfaceTextureListener)this);
        this.c();
    }
    
    public void onVideoSizeChanged(final int q, final int r, final int n, final float n2) {
        this.q = q;
        this.r = r;
        if (this.q != 0 && this.r != 0) {
            this.requestLayout();
        }
    }
    
    public void onRenderedFirstFrame() {
    }
    
    public void onVideoTracksDisabled() {
    }
    
    public int getCurrentPosition() {
        return (this.g != null) ? ((int)this.g.getCurrentPosition()) : 0;
    }
    
    public void start() {
        this.j = com.facebook.ads.internal.view.dpackage.c.d.STARTED;
        if (this.g == null) {
            this.setup(this.b);
        }
        else if (this.i == com.facebook.ads.internal.view.dpackage.c.d.PREPARED || this.i == com.facebook.ads.internal.view.dpackage.c.d.PAUSED || this.i == com.facebook.ads.internal.view.dpackage.c.d.PLAYBACK_COMPLETED) {
            this.g.setPlayWhenReady(true);
            this.setVideoState(com.facebook.ads.internal.view.dpackage.c.d.STARTED);
        }
    }
    
    public void pause() {
        if (this.g != null) {
            this.g.setPlayWhenReady(false);
        }
        else {
            this.setVideoState(com.facebook.ads.internal.view.dpackage.c.d.IDLE);
        }
    }
    
    public void seekTo(final int n) {
        if (this.g != null) {
            this.t = this.getCurrentPosition();
            this.g.seekTo((long)n);
        }
        else {
            this.p = n;
        }
    }
    
    public void setVideoStateChangeListener(final e d) {
        this.d = d;
    }
    
    public void a() {
        this.setVideoState(com.facebook.ads.internal.view.dpackage.c.d.PLAYBACK_COMPLETED);
    }
    
    public void b() {
        this.j = com.facebook.ads.internal.view.dpackage.c.d.IDLE;
        if (this.g != null) {
            this.g.stop();
            this.g.release();
            this.g = null;
        }
        this.setVideoState(com.facebook.ads.internal.view.dpackage.c.d.IDLE);
    }
    
    public long getInitialBufferTime() {
        return this.o;
    }
    
    public int getDuration() {
        if (this.g == null) {
            return 0;
        }
        return (int)this.g.getDuration();
    }
    
    public d getTargetState() {
        return this.j;
    }
    
    public d getState() {
        return this.i;
    }
    
    public void setRequestedVolume(final float n) {
        this.s = n;
        if (this.g != null && this.i != com.facebook.ads.internal.view.dpackage.c.d.PREPARING && this.i != com.facebook.ads.internal.view.dpackage.c.d.IDLE) {
            this.g.setVolume(n);
        }
    }
    
    public void setVideoMPD(final String c) {
        this.c = c;
    }
    
    private void c() {
        final DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        (this.g = ExoPlayerFactory.newSimpleInstance(this.getContext(), (TrackSelector)new DefaultTrackSelector(this.e, (TrackSelection.Factory)new AdaptiveVideoTrackSelection.Factory((BandwidthMeter)defaultBandwidthMeter)), (LoadControl)new DefaultLoadControl())).setVideoListener((SimpleExoPlayer.VideoListener)this);
        this.g.addListener((ExoPlayer.EventListener)this);
        this.g.setPlayWhenReady(false);
        if (this.m) {
            (this.h = new MediaController(this.getContext())).setAnchorView((View)((this.k == null) ? this : this.k));
            this.h.setMediaPlayer((MediaController.MediaPlayerControl)new MediaController.MediaPlayerControl() {
                public void start() {
                    com.facebook.ads.internal.view.dpackage.c.a.this.start();
                }
                
                public void pause() {
                    com.facebook.ads.internal.view.dpackage.c.a.this.pause();
                }
                
                public int getDuration() {
                    return com.facebook.ads.internal.view.dpackage.c.a.this.getDuration();
                }
                
                public int getCurrentPosition() {
                    return com.facebook.ads.internal.view.dpackage.c.a.this.getCurrentPosition();
                }
                
                public void seekTo(final int n) {
                    com.facebook.ads.internal.view.dpackage.c.a.this.seekTo(n);
                }
                
                public boolean isPlaying() {
                    return com.facebook.ads.internal.view.dpackage.c.a.this.g.getPlayWhenReady();
                }
                
                public int getBufferPercentage() {
                    return com.facebook.ads.internal.view.dpackage.c.a.this.g.getBufferedPercentage();
                }
                
                public boolean canPause() {
                    return true;
                }
                
                public boolean canSeekBackward() {
                    return true;
                }
                
                public boolean canSeekForward() {
                    return true;
                }
                
                public int getAudioSessionId() {
                    return com.facebook.ads.internal.view.dpackage.c.a.this.g.getAudioSessionId();
                }
            });
            this.h.setEnabled(true);
        }
        if (this.c == null || this.c.length() <= 0 || AdSettings.isTestMode(this.getContext())) {
            this.g.prepare((MediaSource)new ExtractorMediaSource(this.b, (DataSource.Factory)new DefaultDataSourceFactory(this.getContext(), Util.getUserAgent(this.getContext(), "ads"), (TransferListener)defaultBandwidthMeter), (ExtractorsFactory)new DefaultExtractorsFactory(), (Handler)null, (ExtractorMediaSource.EventListener)null));
        }
        this.setVideoState(com.facebook.ads.internal.view.dpackage.c.d.PREPARING);
        if (this.isAvailable()) {
            this.onSurfaceTextureAvailable(this.getSurfaceTexture(), 0, 0);
        }
    }
    
    private void setVideoState(final d i) {
        if (i != this.i) {
            this.i = i;
            if (this.i == com.facebook.ads.internal.view.dpackage.c.d.STARTED) {
                this.l = true;
            }
            if (this.d != null) {
                this.d.a(i);
            }
        }
    }
    
    private void d() {
        if (this.f != null) {
            this.f.release();
            this.f = null;
        }
        if (this.g != null) {
            this.g.release();
            this.g = null;
        }
        this.h = null;
        this.l = false;
        this.setVideoState(com.facebook.ads.internal.view.dpackage.c.d.IDLE);
    }
    
    public boolean onSurfaceTextureDestroyed(final SurfaceTexture surfaceTexture) {
        if (this.f != null) {
            this.f.release();
            this.f = null;
            if (this.g != null) {
                this.g.setVideoSurface((Surface)null);
            }
        }
        if (this.i != com.facebook.ads.internal.view.dpackage.c.d.PAUSED) {
            this.j = (this.m ? com.facebook.ads.internal.view.dpackage.c.d.STARTED : this.i);
            this.pause();
        }
        return true;
    }
    
    public void onSurfaceTextureAvailable(final SurfaceTexture surfaceTexture, final int n, final int n2) {
        this.f = new Surface(surfaceTexture);
        if (this.f == null) {
            this.f = new Surface(surfaceTexture);
        }
        if (this.g == null) {
            return;
        }
        this.g.setVideoSurface(this.f);
        if (this.i == com.facebook.ads.internal.view.dpackage.c.d.PAUSED && this.j != com.facebook.ads.internal.view.dpackage.c.d.PAUSED) {
            this.start();
        }
    }
    
    public void onSurfaceTextureSizeChanged(final SurfaceTexture surfaceTexture, final int n, final int n2) {
    }
    
    public void onSurfaceTextureUpdated(final SurfaceTexture surfaceTexture) {
    }
    
    public void onWindowFocusChanged(final boolean b) {
        super.onWindowFocusChanged(b);
        if (this.g == null) {
            return;
        }
        if (!b) {
            if (this.i != com.facebook.ads.internal.view.dpackage.c.d.PAUSED) {
                this.j = (this.m ? com.facebook.ads.internal.view.dpackage.c.d.STARTED : this.i);
                this.pause();
            }
        }
        else if (this.i == com.facebook.ads.internal.view.dpackage.c.d.PAUSED && this.j != com.facebook.ads.internal.view.dpackage.c.d.PAUSED) {
            this.start();
        }
    }
    
    public void onPlayerStateChanged(final boolean b, final int n) {
        switch (n) {
            case 1: {
                this.setVideoState(com.facebook.ads.internal.view.dpackage.c.d.IDLE);
                break;
            }
            case 2: {
                if (this.t >= 0) {
                    final int t = this.t;
                    this.t = -1;
                    this.d.a(t, this.getCurrentPosition());
                    break;
                }
                break;
            }
            case 3: {
                if (this.n != 0L) {
                    this.o = System.currentTimeMillis() - this.n;
                }
                this.setRequestedVolume(this.s);
                if (this.p > 0L && this.p < this.g.getDuration()) {
                    this.g.seekTo(this.p);
                    this.p = 0L;
                }
                if (this.g.getCurrentPosition() != 0L && !b && this.l) {
                    this.setVideoState(com.facebook.ads.internal.view.dpackage.c.d.PAUSED);
                    break;
                }
                if (b || this.i == com.facebook.ads.internal.view.dpackage.c.d.PLAYBACK_COMPLETED) {
                    break;
                }
                this.setVideoState(com.facebook.ads.internal.view.dpackage.c.d.PREPARED);
                if (this.j == com.facebook.ads.internal.view.dpackage.c.d.STARTED) {
                    this.start();
                    this.j = com.facebook.ads.internal.view.dpackage.c.d.IDLE;
                    break;
                }
                break;
            }
            case 4: {
                if (b) {
                    this.setVideoState(com.facebook.ads.internal.view.dpackage.c.d.PLAYBACK_COMPLETED);
                }
                if (this.g != null) {
                    this.g.setPlayWhenReady(false);
                    if (!b) {
                        this.g.seekToDefaultPosition();
                    }
                }
                this.l = false;
                break;
            }
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        int n3 = getDefaultSize(this.q, n);
        int n4 = getDefaultSize(this.r, n2);
        if (this.q > 0 && this.r > 0) {
            final int mode = View.MeasureSpec.getMode(n);
            final int size = View.MeasureSpec.getSize(n);
            final int mode2 = View.MeasureSpec.getMode(n2);
            final int size2 = View.MeasureSpec.getSize(n2);
            if (mode == 1073741824 && mode2 == 1073741824) {
                n3 = size;
                n4 = size2;
                if (this.q * n4 < n3 * this.r) {
                    n3 = n4 * this.q / this.r;
                }
                else if (this.q * n4 > n3 * this.r) {
                    n4 = n3 * this.r / this.q;
                }
            }
            else if (mode == 1073741824) {
                n3 = size;
                n4 = n3 * this.r / this.q;
                if (mode2 == Integer.MIN_VALUE && n4 > size2) {
                    n4 = size2;
                }
            }
            else if (mode2 == 1073741824) {
                n4 = size2;
                n3 = n4 * this.q / this.r;
                if (mode == Integer.MIN_VALUE && n3 > size) {
                    n3 = size;
                }
            }
            else {
                n3 = this.q;
                n4 = this.r;
                if (mode2 == Integer.MIN_VALUE && n4 > size2) {
                    n4 = size2;
                    n3 = n4 * this.q / this.r;
                }
                if (mode == Integer.MIN_VALUE && n3 > size) {
                    n3 = size;
                    n4 = n3 * this.r / this.q;
                }
            }
        }
        this.setMeasuredDimension(n3, n4);
    }
    
    public void setFullScreen(final boolean m) {
        this.m = m;
        if (m) {
            this.setOnTouchListener((View.OnTouchListener)new View.OnTouchListener() {
                public boolean onTouch(final View view, final MotionEvent motionEvent) {
                    if (com.facebook.ads.internal.view.dpackage.c.a.this.h != null && motionEvent.getAction() == 1) {
                        if (com.facebook.ads.internal.view.dpackage.c.a.this.h.isShowing()) {
                            com.facebook.ads.internal.view.dpackage.c.a.this.h.hide();
                        }
                        else {
                            com.facebook.ads.internal.view.dpackage.c.a.this.h.show();
                        }
                    }
                    return true;
                }
            });
        }
    }
    
    public void setControlsAnchorView(final View k) {
        (this.k = k).setOnTouchListener((View.OnTouchListener)new View.OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                if (com.facebook.ads.internal.view.dpackage.c.a.this.h != null && motionEvent.getAction() == 1) {
                    if (com.facebook.ads.internal.view.dpackage.c.a.this.h.isShowing()) {
                        com.facebook.ads.internal.view.dpackage.c.a.this.h.hide();
                    }
                    else {
                        com.facebook.ads.internal.view.dpackage.c.a.this.h.show();
                    }
                }
                return true;
            }
        });
    }
    
    public void a(final boolean b) {
    }
    
    public int getVideoHeight() {
        return this.r;
    }
    
    public int getVideoWidth() {
        return this.q;
    }
    
    public View getView() {
        return (View)this;
    }
    
    public float getVolume() {
        return this.s;
    }
    
    public void onLoadingChanged(final boolean b) {
    }
    
    public void onTimelineChanged(final Timeline timeline, final Object o) {
    }
    
    public void onPlayerError(final ExoPlaybackException ex) {
        this.setVideoState(com.facebook.ads.internal.view.dpackage.c.d.ERROR);
        ex.printStackTrace();
        com.facebook.ads.internal.util.c.a(com.facebook.ads.internal.util.b.a((Throwable)ex, "[ExoPlayer] Error during playback of ExoPlayer"));
    }
    
    public void onPositionDiscontinuity() {
    }
    
    static {
        a = a.class.getSimpleName();
    }
}
