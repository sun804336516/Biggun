package com.biggun.drawbitmapmesh.View;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;

import com.biggun.drawbitmapmesh.Common;
import com.biggun.drawbitmapmesh.R;

/**
 * 作者：孙贤武 on 2016/4/13 13:11
 * 邮箱：sun91985415@163.com
 * <p/>
 * 贝塞尔曲线拟圆公式（0-180）：
 * angle /= 2;
 * h = 4/3*((1-cos(angle))/sin(angle))
 */
public class OvalBezierView extends BaseAniView
{
    Path mPath;
    Paint mPaint;
    PointF leftPoint, rightPoint;
    private float width, height;
    private Bitmap mBitmap = null;
    private boolean start = true;

    public void setStart(boolean start)
    {
        this.start = start;
    }

    public OvalBezierView(Context context)
    {
        this(context, null);
    }

    public OvalBezierView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public OvalBezierView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        mPath = new Path();
        mPaint = getPaint(2, Color.BLUE, Paint.Style.STROKE);
    }

    @Override
    protected void startAni()
    {
        ani = ValueAnimator.ofFloat(0, height);
        ani.setDuration(Common.ANIMATORTIME_3000);
        ani.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                float index = (float) animation.getAnimatedValue();
                getPath(index);
            }
        });
        if (start) {
            ani.start();
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.clipPath(mPath);
        canvas.drawPath(mPath, mPaint);
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        mBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.test2),w,h,true);
        getPath(0);
        startAni();
    }

    private void getPath(float hei)
    {
        mPath.reset();
        mPath.moveTo(0, 0);
        mPath.lineTo(0, height);
        leftPoint = new PointF(0, hei);
        rightPoint = new PointF(width, hei);
        //(0,0),(width,0)是弯曲最大，（0,height),(width,height)弯曲最小！
        mPath.cubicTo(leftPoint.x, leftPoint.y, rightPoint.x, rightPoint.y, width, height);
        mPath.lineTo(width, 0);
        mPath.close();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int wid = MeasureSpec.getSize(widthMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(wid, (int) (wid * ratio));//根据公式，模拟半圆的话，宽高比是2/3(宽是圆的直径)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
