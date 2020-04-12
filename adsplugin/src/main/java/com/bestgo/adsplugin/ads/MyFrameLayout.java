package com.bestgo.adsplugin.ads;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import com.bestgo.adsplugin.animation.AbstractAnimator;

public class MyFrameLayout extends FrameLayout{
    private long mUseTime;
    private int mScreenVisibility = INVISIBLE;

    private AbstractAnimator mAnimator;

    public MyFrameLayout(Context context) {
        super(context);
    }

    public MyFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        try {
            super.onLayout(changed, left, top, right, bottom);
        } catch (Exception ex) {
//            setVisibility(GONE);
            ex.printStackTrace();
            Log.e("MyFrameLayout", ex.getMessage());
        }
    }

    public void setUseTime() {
        mUseTime = System.currentTimeMillis();
    }

    public boolean canReused() {
        return Math.abs(System.currentTimeMillis() - mUseTime) >= 1000;
    }

    protected void onWindowVisibilityChanged(final int visibility) {
        mScreenVisibility = visibility;
    }

    public boolean isScreenVisible() {
        return mScreenVisibility == VISIBLE;
    }

    public void setAnimator(AbstractAnimator animator) {
        this.mAnimator = animator;
    }

    public AbstractAnimator getAnimator() {
        return mAnimator;
    }
}
