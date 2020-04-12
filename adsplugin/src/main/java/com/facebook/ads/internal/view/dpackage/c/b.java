// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view.dpackage.c;

import android.view.MotionEvent;
import android.graphics.SurfaceTexture;
import android.content.res.AssetFileDescriptor;
import java.io.IOException;
import android.util.Log;
import android.content.Context;
import android.view.View;
import android.support.annotation.Nullable;
import android.view.Surface;
import android.net.Uri;
import android.annotation.TargetApi;
import android.widget.MediaController;
import android.media.MediaPlayer;
import android.view.TextureView;

@TargetApi(14)
public class b extends TextureView implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnVideoSizeChangedListener, TextureView.SurfaceTextureListener, MediaController.MediaPlayerControl, c
{
    private Uri a;
    private e b;
    private Surface c;
    @Nullable
    private MediaPlayer d;
    private MediaController e;
    private d f;
    private d g;
    private View h;
    private int i;
    private long j;
    private int k;
    private int l;
    private float m;
    private boolean n;
    private int o;
    private static final String p;
    private int q;
    private boolean r;
    
    public b(final Context context) {
        super(context);
        this.f = com.facebook.ads.internal.view.dpackage.c.d.IDLE;
        this.g = com.facebook.ads.internal.view.dpackage.c.d.IDLE;
        this.i = 0;
        this.k = 0;
        this.l = 0;
        this.m = 1.0f;
        this.n = false;
        this.o = 3;
        this.q = 0;
        this.r = false;
    }
    
    public void setup(final Uri a) {
        this.a = a;
        MediaPlayer d;
        if (this.d != null) {
            this.d.reset();
            this.d.setSurface((Surface)null);
            d = this.d;
        }
        else {
            d = new MediaPlayer();
        }
        try {
            if (a.getScheme().equals("asset")) {
                AssetFileDescriptor openFd = null;
                try {
                    openFd = this.getContext().getAssets().openFd(a.getPath().substring(1));
                    d.setDataSource(openFd.getFileDescriptor(), openFd.getStartOffset(), openFd.getLength());
                }
                catch (SecurityException ex5) {}
                catch (IOException ex) {
                    Log.w(com.facebook.ads.internal.view.dpackage.c.b.p, "Failed to open assets " + ex);
                    this.setVideoState(com.facebook.ads.internal.view.dpackage.c.d.ERROR);
                    if (openFd != null) {
                        try {
                            openFd.close();
                        }
                        catch (IOException ex2) {
                            Log.w(com.facebook.ads.internal.view.dpackage.c.b.p, "Unable to close" + ex2);
                        }
                    }
                }
                finally {
                    if (openFd != null) {
                        try {
                            openFd.close();
                        }
                        catch (IOException ex3) {
                            Log.w(com.facebook.ads.internal.view.dpackage.c.b.p, "Unable to close" + ex3);
                        }
                    }
                }
            }
            else {
                d.setDataSource(this.getContext(), a);
            }
            d.setLooping(false);
            d.setOnBufferingUpdateListener((MediaPlayer.OnBufferingUpdateListener)this);
            d.setOnCompletionListener((MediaPlayer.OnCompletionListener)this);
            d.setOnErrorListener((MediaPlayer.OnErrorListener)this);
            d.setOnInfoListener((MediaPlayer.OnInfoListener)this);
            d.setOnPreparedListener((MediaPlayer.OnPreparedListener)this);
            d.setOnVideoSizeChangedListener((MediaPlayer.OnVideoSizeChangedListener)this);
            d.setOnSeekCompleteListener((MediaPlayer.OnSeekCompleteListener)this);
            d.prepareAsync();
            this.d = d;
            this.setVideoState(com.facebook.ads.internal.view.dpackage.c.d.PREPARING);
        }
        catch (Exception ex4) {
            this.setVideoState(com.facebook.ads.internal.view.dpackage.c.d.ERROR);
            d.release();
            Log.e(com.facebook.ads.internal.view.dpackage.c.b.p, "Cannot prepare media player with SurfaceTexture: " + ex4);
        }
        this.setSurfaceTextureListener((TextureView.SurfaceTextureListener)this);
        if (this.isAvailable()) {
            this.onSurfaceTextureAvailable(this.getSurfaceTexture(), 0, 0);
        }
    }
    
    public void start() {
        this.g = com.facebook.ads.internal.view.dpackage.c.d.STARTED;
        if (this.f == com.facebook.ads.internal.view.dpackage.c.d.STARTED || this.f == com.facebook.ads.internal.view.dpackage.c.d.PREPARED || this.f == com.facebook.ads.internal.view.dpackage.c.d.IDLE || this.f == com.facebook.ads.internal.view.dpackage.c.d.PAUSED || this.f == com.facebook.ads.internal.view.dpackage.c.d.PLAYBACK_COMPLETED) {
            if (this.d == null) {
                this.setup(this.a);
            }
            else {
                if (this.i > 0) {
                    this.d.seekTo(this.i);
                }
                this.d.start();
                this.setVideoState(com.facebook.ads.internal.view.dpackage.c.d.STARTED);
            }
        }
        if (this.isAvailable()) {
            this.onSurfaceTextureAvailable(this.getSurfaceTexture(), 0, 0);
        }
    }
    
    public int getCurrentPosition() {
        int currentPosition = 0;
        if (this.d != null) {
            currentPosition = this.d.getCurrentPosition();
        }
        return currentPosition;
    }
    
    public void b() {
        this.g = com.facebook.ads.internal.view.dpackage.c.d.IDLE;
        if (this.d != null) {
            final int currentPosition = this.d.getCurrentPosition();
            if (currentPosition > 0) {
                this.i = currentPosition;
            }
            this.d.stop();
            this.d.reset();
            this.d.release();
            this.d = null;
            if (this.e != null) {
                this.e.hide();
                this.e.setEnabled(false);
            }
        }
        this.setVideoState(com.facebook.ads.internal.view.dpackage.c.d.IDLE);
    }
    
    public void a() {
        this.setVideoState(com.facebook.ads.internal.view.dpackage.c.d.PLAYBACK_COMPLETED);
        this.b();
        this.i = 0;
    }
    
    public void pause() {
        if (this.d != null) {
            if (!this.canPause()) {
                return;
            }
            this.d.pause();
            if (this.f != com.facebook.ads.internal.view.dpackage.c.d.PLAYBACK_COMPLETED) {
                this.setVideoState(com.facebook.ads.internal.view.dpackage.c.d.PAUSED);
            }
        }
        else {
            this.setVideoState(com.facebook.ads.internal.view.dpackage.c.d.IDLE);
        }
    }
    
    private boolean c() {
        return this.f == com.facebook.ads.internal.view.dpackage.c.d.PREPARED || this.f == com.facebook.ads.internal.view.dpackage.c.d.STARTED || this.f == com.facebook.ads.internal.view.dpackage.c.d.PAUSED || this.f == com.facebook.ads.internal.view.dpackage.c.d.PLAYBACK_COMPLETED;
    }
    
    public void seekTo(final int n) {
        if (this.d != null && this.c()) {
            if (n < this.getDuration() && n > 0) {
                this.q = this.getCurrentPosition();
                this.i = n;
                this.d.seekTo(n);
            }
        }
        else {
            this.i = n;
        }
    }
    
    public void onSurfaceTextureAvailable(final SurfaceTexture surfaceTexture, final int n, final int n2) {
        if (this.c == null) {
            this.c = new Surface(surfaceTexture);
        }
        if (this.d == null) {
            return;
        }
        this.d.setSurface(this.c);
        if (this.f == com.facebook.ads.internal.view.dpackage.c.d.PAUSED && this.g != com.facebook.ads.internal.view.dpackage.c.d.PAUSED) {
            this.start();
        }
    }
    
    public void onSurfaceTextureSizeChanged(final SurfaceTexture surfaceTexture, final int n, final int n2) {
    }
    
    public boolean onSurfaceTextureDestroyed(final SurfaceTexture surfaceTexture) {
        if (this.c != null) {
            this.c.release();
            this.c = null;
        }
        if (this.f != com.facebook.ads.internal.view.dpackage.c.d.PAUSED) {
            this.g = (this.n ? com.facebook.ads.internal.view.dpackage.c.d.STARTED : this.f);
            this.pause();
        }
        return true;
    }
    
    public void onSurfaceTextureUpdated(final SurfaceTexture surfaceTexture) {
    }
    
    public void onBufferingUpdate(final MediaPlayer mediaPlayer, final int n) {
    }
    
    public void onWindowFocusChanged(final boolean b) {
        super.onWindowFocusChanged(b);
        if (this.d == null) {
            return;
        }
        if (this.e != null && this.e.isShowing()) {
            return;
        }
        if (!b) {
            if (this.f != com.facebook.ads.internal.view.dpackage.c.d.PAUSED) {
                this.g = (this.n ? com.facebook.ads.internal.view.dpackage.c.d.STARTED : this.f);
                this.pause();
            }
        }
        else if (this.f == com.facebook.ads.internal.view.dpackage.c.d.PAUSED && this.g != com.facebook.ads.internal.view.dpackage.c.d.PAUSED) {
            this.start();
        }
    }
    
    public void onCompletion(final MediaPlayer mediaPlayer) {
        if (this.d != null) {
            this.d.pause();
        }
        this.setVideoState(com.facebook.ads.internal.view.dpackage.c.d.PLAYBACK_COMPLETED);
        this.seekTo(0);
        this.i = 0;
    }
    
    public boolean onError(final MediaPlayer mediaPlayer, final int n, final int n2) {
        if (this.o > 0 && this.getState() == com.facebook.ads.internal.view.dpackage.c.d.STARTED) {
            --this.o;
            this.b();
            this.start();
        }
        else {
            this.setVideoState(com.facebook.ads.internal.view.dpackage.c.d.ERROR);
            this.b();
        }
        return true;
    }
    
    public boolean onInfo(final MediaPlayer mediaPlayer, final int n, final int n2) {
        switch (n) {
            case 701: {
                this.setVideoState(com.facebook.ads.internal.view.dpackage.c.d.BUFFERING);
                break;
            }
            case 702: {
                this.setVideoState(com.facebook.ads.internal.view.dpackage.c.d.STARTED);
                break;
            }
        }
        return false;
    }
    
    public void onPrepared(final MediaPlayer mediaPlayer) {
        this.setVideoState(com.facebook.ads.internal.view.dpackage.c.d.PREPARED);
        if (this.n && !this.r) {
            (this.e = new MediaController(this.getContext())).setAnchorView((View)((this.h == null) ? this : this.h));
            this.e.setMediaPlayer((MediaController.MediaPlayerControl)this);
            this.e.setEnabled(true);
        }
        this.setRequestedVolume(this.m);
        this.k = mediaPlayer.getVideoWidth();
        this.l = mediaPlayer.getVideoHeight();
        if (this.i > 0) {
            if (this.i >= this.d.getDuration()) {
                this.i = 0;
            }
            this.d.seekTo(this.i);
            this.i = 0;
        }
        if (this.g == com.facebook.ads.internal.view.dpackage.c.d.STARTED) {
            this.start();
        }
    }
    
    public void setVideoStateChangeListener(final e b) {
        this.b = b;
    }
    
    private void setVideoState(final d f) {
        if (f != this.f) {
            this.f = f;
            if (this.b != null) {
                this.b.a(f);
            }
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        int n3 = getDefaultSize(this.k, n);
        int n4 = getDefaultSize(this.l, n2);
        if (this.k > 0 && this.l > 0) {
            final int mode = View.MeasureSpec.getMode(n);
            final int size = View.MeasureSpec.getSize(n);
            final int mode2 = View.MeasureSpec.getMode(n2);
            final int size2 = View.MeasureSpec.getSize(n2);
            if (mode == 1073741824 && mode2 == 1073741824) {
                n3 = size;
                n4 = size2;
                if (this.k * n4 < n3 * this.l) {
                    n3 = n4 * this.k / this.l;
                }
                else if (this.k * n4 > n3 * this.l) {
                    n4 = n3 * this.l / this.k;
                }
            }
            else if (mode == 1073741824) {
                n3 = size;
                n4 = n3 * this.l / this.k;
                if (mode2 == Integer.MIN_VALUE && n4 > size2) {
                    n4 = size2;
                }
            }
            else if (mode2 == 1073741824) {
                n4 = size2;
                n3 = n4 * this.k / this.l;
                if (mode == Integer.MIN_VALUE && n3 > size) {
                    n3 = size;
                }
            }
            else {
                n3 = this.k;
                n4 = this.l;
                if (mode2 == Integer.MIN_VALUE && n4 > size2) {
                    n4 = size2;
                    n3 = n4 * this.k / this.l;
                }
                if (mode == Integer.MIN_VALUE && n3 > size) {
                    n3 = size;
                    n4 = n3 * this.l / this.k;
                }
            }
        }
        this.setMeasuredDimension(n3, n4);
    }
    
    public void onVideoSizeChanged(final MediaPlayer mediaPlayer, final int n, final int n2) {
        this.k = mediaPlayer.getVideoWidth();
        this.l = mediaPlayer.getVideoHeight();
        if (this.k != 0 && this.l != 0) {
            this.requestLayout();
        }
    }
    
    public long getInitialBufferTime() {
        return this.j;
    }
    
    public int getDuration() {
        if (this.d == null) {
            return 0;
        }
        if (this.getState() != com.facebook.ads.internal.view.dpackage.c.d.STARTED && this.getState() != com.facebook.ads.internal.view.dpackage.c.d.PAUSED && this.getState() != com.facebook.ads.internal.view.dpackage.c.d.PREPARED && this.getState() != com.facebook.ads.internal.view.dpackage.c.d.PLAYBACK_COMPLETED) {
            return 0;
        }
        return this.d.getDuration();
    }
    
    public d getState() {
        return this.f;
    }
    
    public void setRequestedVolume(final float m) {
        this.m = m;
        if (this.d != null && this.f != com.facebook.ads.internal.view.dpackage.c.d.PREPARING && this.f != com.facebook.ads.internal.view.dpackage.c.d.IDLE) {
            this.d.setVolume(m, m);
        }
    }
    
    public void setVideoMPD(final String s) {
    }
    
    public d getTargetState() {
        return this.g;
    }
    
    public void setFullScreen(final boolean n) {
        this.n = n;
        if (this.n && !this.r) {
            this.setOnTouchListener((View.OnTouchListener)new View.OnTouchListener() {
                public boolean onTouch(final View view, final MotionEvent motionEvent) {
                    if (com.facebook.ads.internal.view.dpackage.c.b.this.r) {
                        return true;
                    }
                    if (com.facebook.ads.internal.view.dpackage.c.b.this.e != null && motionEvent.getAction() == 1) {
                        if (com.facebook.ads.internal.view.dpackage.c.b.this.e.isShowing()) {
                            com.facebook.ads.internal.view.dpackage.c.b.this.e.hide();
                        }
                        else {
                            com.facebook.ads.internal.view.dpackage.c.b.this.e.show();
                        }
                    }
                    return true;
                }
            });
        }
    }
    
    public void a(final boolean b) {
        if (this.e != null) {
            this.e.setVisibility(GONE);
        }
        this.r = true;
    }
    
    public int getVideoHeight() {
        return this.l;
    }
    
    public int getVideoWidth() {
        return this.k;
    }
    
    public boolean isPlaying() {
        return this.d != null && this.d.isPlaying();
    }
    
    public int getBufferPercentage() {
        return 0;
    }
    
    public boolean canPause() {
        return this.f != com.facebook.ads.internal.view.dpackage.c.d.PREPARING && this.f != com.facebook.ads.internal.view.dpackage.c.d.PREPARED;
    }
    
    public boolean canSeekBackward() {
        return true;
    }
    
    public boolean canSeekForward() {
        return true;
    }
    
    public int getAudioSessionId() {
        return (this.d != null) ? this.d.getAudioSessionId() : 0;
    }
    
    public void setControlsAnchorView(final View h) {
        (this.h = h).setOnTouchListener((View.OnTouchListener)new View.OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                if (com.facebook.ads.internal.view.dpackage.c.b.this.r) {
                    return true;
                }
                if (com.facebook.ads.internal.view.dpackage.c.b.this.e != null && motionEvent.getAction() == 1) {
                    if (com.facebook.ads.internal.view.dpackage.c.b.this.e.isShowing()) {
                        com.facebook.ads.internal.view.dpackage.c.b.this.e.hide();
                    }
                    else {
                        com.facebook.ads.internal.view.dpackage.c.b.this.e.show();
                    }
                }
                return true;
            }
        });
    }
    
    public View getView() {
        return (View)this;
    }
    
    public float getVolume() {
        return this.m;
    }
    
    public void onSeekComplete(final MediaPlayer mediaPlayer) {
        if (this.b == null) {
            return;
        }
        this.b.a(this.q, this.i);
        this.i = 0;
    }
    
    static {
        p = b.class.getSimpleName();
    }
}
