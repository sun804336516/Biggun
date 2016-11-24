package com.shiletao.common.View;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * 作者：孙贤武 on 2016/11/23 14:14
 * 邮箱：sun91985415@163.com
 * LIKE:YANSHUO
 */
public class PathView extends TextView
{
    public PathView(Context context)
    {
        this(context, null);
    }

    public PathView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }
    //Я живу в пекине, мне нравится писать код
    String text = "一个人有多不正经就有多深情";
    Path mPath;
    Paint mPaint;
    int wid;
    int hei;
    Rect textRect = new Rect();

    public PathView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        wid = getContext().getResources().getDisplayMetrics().widthPixels;
        hei = getContext().getResources().getDisplayMetrics().heightPixels;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(dp2px(20));
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPath = new Path();
        mPaint.getTextBounds(text, 0, text.length(), textRect);
        mPath.moveTo((wid - textRect.width()) >> 1, hei >> 1);
        mPath.lineTo((wid + textRect.width()) >> 1, hei >> 1);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
//        canvas.drawPath(mPath,mPaint);
        canvas.drawTextOnPath(text, mPath, 0, 0, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(wid, hei);
    }

    boolean isAni = false;
    int touchX,touchY;
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (isAni) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchX = (int) event.getX();
                touchY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                resetPath(new Point((int) event.getX() - touchX+((wid - textRect.width()) >> 1), (int) event.getY()-touchY+(hei>>1)));
                break;
            case MotionEvent.ACTION_UP:
                textReset((int) event.getX()-touchX+((wid - textRect.width()) >> 1),(int) event.getY()-touchY+(hei>>1));
                break;
        }
        return true;
    }

    private void resetPath(Point p)
    {
        mPath.reset();
        mPath.moveTo((wid - textRect.width()) >> 1, hei >> 1);
        mPath.quadTo(p.x, p.y,(wid + textRect.width()) >> 1, hei >> 1);
        invalidate();
    }
    ValueAnimator ani;
    private void textReset(int x, int y)
    {
        ani = ValueAnimator.ofObject(new MyType(), new Point(x, y), new Point((wid - textRect.width()) >> 1, hei >> 1));
        ani.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                Point p = (Point) animation.getAnimatedValue();
                resetPath(p);
            }
        });
        ani.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {
                isAni = true;
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation)
            {
                isAni = false;
                super.onAnimationEnd(animation);
            }
        });
        ani.setDuration(500);
        ani.start();
    }

    private class MyType implements TypeEvaluator<Point>
    {
        @Override
        public Point evaluate(float fraction, Point startValue, Point endValue)
        {
            Point p = new Point();
            p.x = (int) (startValue.x + (endValue.x - startValue.x) * fraction);
            p.y = (int) (startValue.y + (endValue.y - startValue.y) * fraction);
            return p;
        }
    }
    private int dp2px(int dp)
    {
        float dpi = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp*dpi+0.5f);
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        if(ani != null)
        {
            ani.cancel();
        }
    }
}
