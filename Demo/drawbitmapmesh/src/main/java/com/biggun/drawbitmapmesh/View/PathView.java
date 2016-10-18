package com.biggun.drawbitmapmesh.View;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;

import com.biggun.drawbitmapmesh.Util.LooperTimer;
import com.biggun.drawbitmapmesh.Util.Utils;

/**
 * 作者：孙贤武 on 2016/1/29 14:55
 * 邮箱：sun91985415@163.com
 */
public class PathView extends View
{
    public PathView(Context context)
    {
        this(context, null);
    }

    public PathView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    private Point startPoint = new Point(200, 200);
    private Point rightPoint = new Point(400, 100);
    private Point leftPoint = new Point(0, 100);
    private Point endPoint = new Point(200, 400);
    private float[] floats = new float[2];
    Path path;
    Paint paint;
    PathMeasure pathMeasure;
    ValueAnimator animator;
    CountDownTimer timer1;
    LooperTimer looperTimer;
    public PathView(final Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        path = new Path();
        path.moveTo(startPoint.x, startPoint.y);
        path.quadTo(rightPoint.x, rightPoint.y, endPoint.x, endPoint.y);
        path.quadTo(leftPoint.x, leftPoint.y, startPoint.x, startPoint.y);

        pathMeasure = new PathMeasure(path, true);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
//        timer1 = new CountDownTimer(10000,500)
//        {
//            @Override
//            public void onTick(long millisUntilFinished)
//            {
//                Utils.LogE("time:"+millisUntilFinished);
//                startAnimation();
//            }
//
//            @Override
//            public void onFinish()
//            {
//
//            }
//        };

        looperTimer = new LooperTimer(500,20)
        {
            @Override
            public void onFinished()
            {
                Utils.LogE("PathView--Animation-finished");
            }

            @Override
            public void onTick(int count)
            {
                Utils.LogE("tick-count:"+count);
                startAnimation();
            }
        };
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
//        timer1.start();
        looperTimer.start();
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
//        timer1.cancel();
        looperTimer.cancel();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(400, 400);
    }

    public void startAnimation()
    {
        animator = ValueAnimator.ofFloat(0, pathMeasure.getLength());
        animator.setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                float value = (float) animation.getAnimatedValue();
                pathMeasure.getPosTan(value, floats, null);
                postInvalidate();
            }
        });
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
        canvas.drawCircle(floats[0], floats[1], 10, paint);
    }

    /**
     * 自己的插值器
     */
    private class MyInterpolator implements Interpolator
    {
        @Override
        public float getInterpolation(float input)
        {
            return input;
        }
    }
}
