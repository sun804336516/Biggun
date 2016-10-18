package com.biggun.drawbitmapmesh.View;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;

/**
 * 作者：孙贤武 on 2016/3/30 11:14
 * 邮箱：sun91985415@163.com
 * 基础的Aniview
 */
public abstract class BaseAniView extends BaseView
{
    public BaseAniView(Context context)
    {
        super(context);
    }

    public BaseAniView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public BaseAniView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    protected ValueAnimator ani;

    public void cancleAni()
    {
        if (null != ani && isAniRunning()) {
            ani.cancel();
        }
    }

    /**
     * 跳转到指定的进度
     * @param ratio
     */
    public void seekTo(float ratio)
    {
        if (ani == null) {
            return;
        }
        ani.setCurrentPlayTime((long) (ani.getDuration() * ratio));
    }
    protected abstract void startAni();

    public boolean isAniRunning()
    {
        return null != ani && ani.isRunning();
    }

    /**
     * 清空画布
     *
     * @param canvas
     */
    public void clearCanvas(Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawPaint(paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        cancleAni();
    }

}
