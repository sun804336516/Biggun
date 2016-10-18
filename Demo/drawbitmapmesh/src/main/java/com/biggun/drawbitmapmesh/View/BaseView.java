package com.biggun.drawbitmapmesh.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.view.View;

import com.biggun.drawbitmapmesh.Util.Utils;

/**
 * 作者：孙贤武 on 2016/4/12 16:29
 * 邮箱：sun91985415@163.com
 */
public class BaseView extends View
{

    public BaseView(Context context)
    {
        this(context, null);
    }

    public BaseView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public BaseView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 获取一个画笔对象
     *
     * @param strokeWid 线粗
     * @param color     颜色
     * @param style     样式
     * @return
     */
    protected Paint getPaint(int strokeWid, int color, Paint.Style style)
    {
        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);
        mPaint.setStrokeWidth(strokeWid);
        mPaint.setColor(color);
        mPaint.setStyle(style);
        mPaint.setStrokeCap(Paint.Cap.ROUND);//设置笔头样式
        mPaint.setStrokeJoin(Paint.Join.ROUND);//设置连接处形态
        return mPaint;
    }

    protected float ratio = 2 / 3.0f;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widmode = MeasureSpec.getMode(widthMeasureSpec);
        int heimode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        Utils.LogE(this.getClass().getSimpleName() + ",wid:" + width + ",hei:" + height);
        if (widmode == MeasureSpec.EXACTLY && heimode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(width, height);
        } else {
            width = (int) (getResources().getDisplayMetrics().widthPixels * ratio);
            height = (int) (getResources().getDisplayMetrics().heightPixels * ratio);
            setMeasuredDimension(width, height);
        }
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
//        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.setDrawFilter(new PaintFlagsDrawFilter(Paint.ANTI_ALIAS_FLAG, Paint.FILTER_BITMAP_FLAG));
    }
}
