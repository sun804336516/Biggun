package com.biggun.drawbitmapmesh.View;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.biggun.drawbitmapmesh.Common;
import com.biggun.drawbitmapmesh.Util.Utils;

/**
 * 作者：孙贤武 on 2016/5/10 15:24
 * 邮箱：sun91985415@163.com
 */
public class LiudongView extends BaseAniView
{
    public LiudongView(Context context)
    {
        this(context, null);
    }

    public LiudongView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    PathMeasure cirMeasure, lineMeasure, lastMeasure;
    Path cirPath, cirPath2;
    Path linePath, linePath2,linePath3;
    Path lastPath, lastPath2;
    Paint mPaint, mPaint2;

    public LiudongView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        cirPath = new Path();
        cirPath2 = new Path();
        linePath = new Path();
        linePath2 = new Path();
        linePath3 = new Path();
        lastPath = new Path();
        lastPath2 = new Path();

        mPaint = getPaint(4, Color.BLUE, Paint.Style.STROKE);
        mPaint2 = getPaint(4, Color.BLACK, Paint.Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);

        RectF rectF = new RectF(0, 0, 200, 200);
        cirPath.arcTo(rectF, 90, -359);
        linePath.moveTo(100, 200);
        linePath.lineTo(728, 200);
        RectF lastRect = new RectF(628, 0, 828, 200);
        lastPath.addArc(lastRect, 90, -359);

        cirMeasure = new PathMeasure(cirPath, false);
        lineMeasure = new PathMeasure(linePath, false);
        lastMeasure = new PathMeasure(lastPath, false);
        startAni();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(1080, 400);
    }

    @Override
    protected void startAni()
    {
        ani = ValueAnimator.ofFloat(0, cirMeasure.getLength());
        ani.setDuration(Common.ANIMATORTIME_500);
        ani.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                float value = (float) animation.getAnimatedValue();
                Utils.LogE(cirMeasure.getLength()+"==========="+value);
                cirMeasure.getSegment(0, value, cirPath2, true);
                invalidate();
            }
        });
        ValueAnimator ani2 = ValueAnimator.ofFloat(0, lineMeasure.getLength());
        ani2.setDuration(Common.ANIMATORTIME_500);
        ani2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                float value = (float) animation.getAnimatedValue();
                Utils.LogE(lineMeasure.getLength()+"==========="+value);
                lineMeasure.getSegment(0, value, linePath2, true);
                invalidate();
            }
        });
        ani.start();
        ani2.start();

        final ValueAnimator  ani3 = ValueAnimator.ofFloat(0, lastMeasure.getLength());
        ani3.setDuration(Common.ANIMATORTIME_500);
        ani3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                float value = (float) animation.getAnimatedValue();
                Utils.LogE(lastMeasure.getLength()+"==========="+value);
                lastMeasure.getSegment(0, value, lastPath2, true);
                invalidate();
            }
        });

        final ValueAnimator ani4 = ValueAnimator.ofFloat(0, lineMeasure.getLength());
        ani4.setDuration(Common.ANIMATORTIME_500);
        ani4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                float value = (float) animation.getAnimatedValue();
                lineMeasure.getSegment(0, value, linePath3, true);
                invalidate();
            }
        });
        ani.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);
                ani3.start();
                ani4.start();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.drawPath(cirPath, mPaint);
        canvas.drawPath(cirPath2, mPaint2);
        canvas.drawPath(linePath, mPaint2);
        canvas.drawPath(linePath2, mPaint);
        canvas.drawPath(linePath3, mPaint2);

        canvas.drawPath(lastPath,mPaint2);
        canvas.drawPath(lastPath2,mPaint);
    }
}
