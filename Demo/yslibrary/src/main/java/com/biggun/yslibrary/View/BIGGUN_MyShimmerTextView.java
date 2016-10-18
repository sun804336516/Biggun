package com.biggun.yslibrary.View;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.support.annotation.IntDef;
import android.util.AttributeSet;

/**
 * 作者：孙贤武 on 2016/2/26 10:07
 * 邮箱：sun91985415@163.com
 * LIKE:YANSHUO
 * 闪光的TextView（上下左右）
 */
public class BIGGUN_MyShimmerTextView extends BIGGUN_AniTextView
{
    @IntDef({LEFT, RIGHT})
    public @interface Orientation
    {
    }

    private static final int LEFT = 1;
    private static final int RIGHT = 2;

    private int orientation = LEFT;

    /**
     * 动画设置是否反向的
     *
     * @param orientation 正常Orientation.LEFT,Orientation.RIGHT反转
     */
    public void setOrientation(@Orientation int orientation)
    {
        this.orientation = orientation;
    }

    private boolean up = false;

    public void setUpShimmer(boolean up)
    {
        this.up = up;
    }

    private LinearGradient linearGradient;
    private float gradientX, gradientY;

    public void setAniDuration(long aniDuration)
    {
        AniDuration = aniDuration;
    }

    private int primaryColor = 0xff000000;
    private int gradientColor = 0xffffffff;

    public void setPrimaryColor(int primaryColor)
    {
        this.primaryColor = primaryColor;
        if (canAni) {
            RefreshGradient();
        }
    }

    public void setGradientColor(int gradientColor)
    {
        this.gradientColor = gradientColor;
        if (canAni) {
            RefreshGradient();
        }
    }

    public BIGGUN_MyShimmerTextView(Context context)
    {
        this(context, null);
    }

    public BIGGUN_MyShimmerTextView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public BIGGUN_MyShimmerTextView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void RefreshGradient()
    {
        if (up) {
            linearGradient = new LinearGradient(0, -height, 0, 0, new int[]{primaryColor, gradientColor, primaryColor}, new float[]{0, 0.5f, 1.0f}, Shader.TileMode.CLAMP);
        } else {
            linearGradient = new LinearGradient(-width, 0, 0, 0, new int[]{primaryColor, gradientColor, primaryColor}, new float[]{0, 0.5f, 1.0f}, Shader.TileMode.CLAMP);
        }
        mPaint.setShader(linearGradient);
    }

    @Override
    protected void AnimationRun()
    {
        float start = 0;
        float end = 0;
        if (up) {
            end = height << 1;
        } else {
            end = width << 1;
        }

        switch (orientation) {
            case LEFT:
                break;
            case RIGHT:
                start = start + end;
                end = start - end;
                start = start - end;
                break;
        }
        ani = ValueAnimator.ofFloat(start, end);
        ani.setDuration(AniDuration);
        ani.setRepeatMode(ValueAnimator.RESTART);
        ani.setRepeatCount(ValueAnimator.INFINITE);
        ani.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                float value = (float) animation.getAnimatedValue();
                gradientX = value;
                gradientY = value;
                postInvalidate();
            }
        });
        ani.start();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        if (linearGradient != null) {
            if (!up) {
                mMatrix.setTranslate(gradientX, 0);
            } else {
                mMatrix.setTranslate(0, gradientY);
            }
            linearGradient.setLocalMatrix(mMatrix);
        }
    }
}
