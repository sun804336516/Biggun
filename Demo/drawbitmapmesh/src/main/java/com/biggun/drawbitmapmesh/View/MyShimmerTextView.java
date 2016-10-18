package com.biggun.drawbitmapmesh.View;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

import com.biggun.drawbitmapmesh.Util.Utils;

/**
 * 作者：孙贤武 on 2016/2/26 10:07
 * 邮箱：sun91985415@163.com
 * 闪光的TextView（上下左右）
 */
public class MyShimmerTextView extends TextView
{
    public interface onSizeCallback
    {
        void onsizechanged();
    }

    onSizeCallback onSizeCallback;

    public void setOnSizeCallback(MyShimmerTextView.onSizeCallback onSizeCallback)
    {
        this.onSizeCallback = onSizeCallback;
    }

    public enum Orientation
    {
        LEFT, RIGHT;
    }

    private Orientation orientation = Orientation.LEFT;

    /**
     * 动画设置是否反向的
     *
     * @param orientation 正常Orientation.LEFT,Orientation.RIGHT反转
     */
    public void setOrientation(Orientation orientation)
    {
        this.orientation = orientation;
    }

    private boolean up = false;

    public void setUpShimmer(boolean up)
    {
        this.up = up;
    }

    private LinearGradient linearGradient;
    private Paint mPaint;
    private Matrix matrix;
    private int width = 0;
    private int height = 0;
    private float gradientX, gradientY;
    private boolean canAni = false;
    private ValueAnimator ani;
    private long AniDuration = 2500;

    public void setAniDuration(long aniDuration)
    {
        AniDuration = aniDuration;
    }

    private int primaryColor = 0x44ffffff;
    private int gradientColor = 0xffffffff;

    public void setPrimaryColor(int primaryColor)
    {
        this.primaryColor = primaryColor;
        if (canAni) {
            RefreshLinearGradient();
        }
    }

    public void setGradientColor(int gradientColor)
    {
        this.gradientColor = gradientColor;
        if (canAni) {
            RefreshLinearGradient();
        }
    }

    public MyShimmerTextView(Context context)
    {
        this(context, null);
    }

    public MyShimmerTextView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public MyShimmerTextView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        mPaint = getPaint();
        matrix = new Matrix();
        getLineCount();
        getLineHeight();
        getHeight();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        Utils.LogE("width-height-shimmer-onSizeChanged");
        super.onSizeChanged(w, h, oldw, oldh);
        if (width == 0 || height == 0) {
            width = getMeasuredWidth();
            height = getMeasuredHeight();
            if (width > 0) {
                RefreshLinearGradient();
                canAni = true;
                if (onSizeCallback != null) {
                    onSizeCallback.onsizechanged();
                }
            }
        }
    }

    /**
     * 更新线性渐变的参数
     */
    private void RefreshLinearGradient()
    {
        if (up) {
            linearGradient = new LinearGradient(0, -height, 0, 0, new int[]{primaryColor, gradientColor, primaryColor}, new float[]{0, 0.5f, 1.0f}, Shader.TileMode.CLAMP);
        } else {
            linearGradient = new LinearGradient(-width, 0, 0, 0, new int[]{primaryColor, gradientColor, primaryColor}, new float[]{0, 0.5f, 1.0f}, Shader.TileMode.CLAMP);
        }
        mPaint.setShader(linearGradient);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        if (linearGradient != null) {
            if (!up) {
                matrix.setTranslate(gradientX, 0);
            } else {
                matrix.setTranslate(0, gradientY);
            }
            linearGradient.setLocalMatrix(matrix);
        }
    }

    /**
     * 启动动画
     */
    public void start()
    {
        if (isAnimating()) {
            return;
        }
        final Runnable run = new Runnable()
        {
            @Override
            public void run()
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

        };
        //考虑到 有可能onSizeChanged会优先start加载或滞后加载
        if (canAni) {
            run.run();
        } else {
            setOnSizeCallback(new onSizeCallback()
            {
                @Override
                public void onsizechanged()
                {
                    run.run();
                }
            });
        }
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        cancelAni();
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

}
