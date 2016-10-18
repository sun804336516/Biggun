package com.biggun.yslibrary.View;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.FloatRange;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 作者：孙贤武 on 2016/6/15 17:30
 * 邮箱：sun91985415@163.com
 */
public abstract class BIGGUN_AniTextView extends TextView
{
    public BIGGUN_AniTextView(Context context)
    {
        this(context, null);
    }

    public BIGGUN_AniTextView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public BIGGUN_AniTextView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        mPaint = getPaint();
        mMatrix = new Matrix();
    }

    protected boolean canAni = false;
    protected ValueAnimator ani;
    protected long AniDuration = 4000;
    protected Matrix mMatrix;
    protected Paint mPaint;
    protected int width = 0;
    protected int height = 0;
    protected onSizeCallback onSizeCallback;

    public void setOnSizeCallback(BIGGUN_MyShimmerTextView.onSizeCallback onSizeCallback)
    {
        this.onSizeCallback = onSizeCallback;
    }

    /**
     * 动画可以开始的回调
     */
    protected interface onSizeCallback
    {
        void onsizechanged();
    }

    /**
     * 跳转到指定位置
     *
     * @param index
     */
    public void seekTo(@FloatRange(from = 0.0, to = 1.0f) float index)
    {
        if (ani == null) {
            return;
        }
        cancelAni();
        ani.setCurrentPlayTime((long) (index * ani.getDuration()));
    }

    public float getCurrentFraction()
    {
        if (ani == null) {
            return 0;
        }
        return ani.getAnimatedFraction();
    }

    public void cancelAni()
    {
        if (ani != null && isAnimating()) {
            ani.cancel();
        }
    }

    public boolean isAnimating()
    {
        return ani != null && ani.isRunning();
    }

    /**
     * 初始化Paint的shader
     */
    protected abstract void RefreshGradient();

    /**
     * 启动动画
     *
     * @return
     */
    protected abstract void AnimationRun();

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        if (width == 0 || height == 0) {
            width = getMeasuredWidth();
            height = getMeasuredHeight();
            if (width > 0) {
                RefreshGradient();
                canAni = true;
                if (onSizeCallback != null) {
                    onSizeCallback.onsizechanged();
                }
            }
        }
    }

    /**
     * 启动动画
     */
    protected final void start()
    {
        if (isAnimating()) {
            return;
        }
        if (canAni) {
            AnimationRun();
        } else {
            setOnSizeCallback(new onSizeCallback()
            {
                @Override
                public void onsizechanged()
                {
                    AnimationRun();
                }
            });
        }
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        start();
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        cancelAni();
    }
}
