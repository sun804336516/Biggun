package com.biggun.yslibrary.View;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import com.biggun.yslibrary.R;

/**
 * 作者：孙贤武 on 2016/6/15 18:03
 * 邮箱：sun91985415@163.com
 * LIKE:YANSHUO
 * 水波纹背景TextView
 */
public class BIGGUN_WaveTextView extends BIGGUN_AniTextView
{
    public BIGGUN_WaveTextView(Context context)
    {
        this(context, null);
    }

    public BIGGUN_WaveTextView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    private Drawable mDrawable;
    private BitmapShader mBitmapShader;

    public BIGGUN_WaveTextView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 考虑到水平跟竖直方向上均有动画，采取2个动画分开刷新会出现误差，导致画面不连贯，故采用TypeEvaluator
     */
    class WaveTypeEvaluator implements TypeEvaluator<PointF>
    {
        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue)
        {
            PointF pointF = new PointF();
            pointF.x = startValue.x + (endValue.x - startValue.x) * fraction * 30;
            pointF.y = startValue.y + (endValue.y - startValue.y) * fraction;
            return pointF;
        }
    }

    @Override
    protected void RefreshGradient()
    {
        if (mDrawable == null) {
            mDrawable = getResources().getDrawable(R.drawable.wave);
        }
        int intrinsicWidth = mDrawable.getIntrinsicWidth();
        int intrinsicHeight = mDrawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(getCurrentTextColor());
        mDrawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
        mDrawable.draw(canvas);

        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        mPaint.setShader(mBitmapShader);
        offsetY = (height - intrinsicHeight) >> 1;
    }

    @Override
    protected void AnimationRun()
    {
        ani = ValueAnimator.ofObject(new WaveTypeEvaluator(), new PointF(0, 0), new PointF(200, -height));
        ani.setDuration(AniDuration << 1);
        ani.setRepeatCount(ValueAnimator.INFINITE);
        ani.setRepeatMode(ValueAnimator.REVERSE);
        ani.setInterpolator(new LinearInterpolator());
        ani.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                PointF pointF = (PointF) animation.getAnimatedValue();
                dex = pointF.x;
                dey = pointF.y;
                postInvalidate();
            }
        });
        ani.start();
    }

    float dex = 0, dey = 0, offsetY = 0;

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        if (mBitmapShader != null) {
            mPaint.setShader(mBitmapShader);
            mMatrix.setTranslate(dex, dey + offsetY);
            mBitmapShader.setLocalMatrix(mMatrix);
        } else {
            mPaint.setShader(null);
        }
    }
}
