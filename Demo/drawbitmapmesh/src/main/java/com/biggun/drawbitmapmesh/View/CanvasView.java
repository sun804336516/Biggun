package com.biggun.drawbitmapmesh.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.lang.reflect.MalformedParameterizedTypeException;

/**
 * Created by 孙贤武 on 2016/1/19.
 */
public class CanvasView extends View
{
    private static final int CIRCLERADIUS = 100;
    private static final String text = "孙贤武";
    private Rect tRect = new Rect();
    private Paint mPaint;

    private float[] lines = {CIRCLERADIUS,CIRCLERADIUS,CIRCLERADIUS*2,CIRCLERADIUS*2,CIRCLERADIUS,CIRCLERADIUS,0,CIRCLERADIUS*2};
    public CanvasView(Context context)
    {
        this(context, null);
    }

    public CanvasView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public CanvasView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(CIRCLERADIUS * 6, CIRCLERADIUS * 7);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        canvas.drawColor(Color.WHITE);
        canvas.drawCircle(CIRCLERADIUS, CIRCLERADIUS, CIRCLERADIUS, mPaint);
        //画文字
        mPaint.setShadowLayer(10, 10, 10, Color.BLACK);
        mPaint.setTextSize(30);
        mPaint.getTextBounds(text, 0, text.length(), tRect);
        mPaint.setFakeBoldText(true);//粗体
        mPaint.setUnderlineText(true);//下划线
        mPaint.setStrikeThruText(true);//删除线
        mPaint.setTextSkewX(0.25f);//左倾斜0.25
        mPaint.setTextScaleX(2);//水平拉伸2倍
        canvas.drawText(text, 0, CIRCLERADIUS * 2 + tRect.height(), mPaint);
        //画直线
        mPaint.setColor(Color.BLACK);
        canvas.drawLine(0, 0, CIRCLERADIUS, CIRCLERADIUS, mPaint);
        mPaint.setColor(Color.GRAY);
        canvas.drawLines(lines, mPaint);
        //画矩形
        mPaint.setColor(Color.BLACK);
        Rect rect = new Rect(CIRCLERADIUS*2,0,CIRCLERADIUS*3,CIRCLERADIUS);
        canvas.drawRect(rect, mPaint);
        //画椭圆
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        RectF rectF = new RectF(0,CIRCLERADIUS*3,CIRCLERADIUS*2,CIRCLERADIUS*4);
        canvas.drawRect(rectF, mPaint);
        canvas.drawOval(rectF, mPaint);
        //画圆弧
        RectF ArcrectF = new RectF(0,CIRCLERADIUS*5,CIRCLERADIUS*2,CIRCLERADIUS*7);//正方形  圆弧，长方形  斜弧
        canvas.drawArc(ArcrectF, 0, 100, false, mPaint);
        canvas.drawRect(ArcrectF, mPaint);
        //画路径
        Path path = new Path();
        path.moveTo(CIRCLERADIUS * 3, CIRCLERADIUS * 3);
        path.lineTo(CIRCLERADIUS * 4, CIRCLERADIUS * 3);
        path.lineTo(CIRCLERADIUS * 4, CIRCLERADIUS * 4);
        path.close();
        mPaint.setColor(Color.MAGENTA);
        canvas.drawPath(path, mPaint);

        //根据路径画文字
        mPaint.setStrokeWidth(2);
        Path cwPth = new Path();//顺时针
        RectF cwRect = new RectF(CIRCLERADIUS*4,CIRCLERADIUS*4,CIRCLERADIUS*5,CIRCLERADIUS*5);
        cwPth.addRect(cwRect,Path.Direction.CW);
        canvas.drawRect(cwRect,mPaint);
        canvas.drawTextOnPath(text,cwPth,0,0,mPaint);

        Path ccwPth = new Path();//逆时针
        RectF ccwRect = new RectF(CIRCLERADIUS*5,CIRCLERADIUS*5,CIRCLERADIUS*6,CIRCLERADIUS*6);
        ccwPth.addRect(ccwRect, Path.Direction.CCW);
        canvas.drawRect(ccwRect, mPaint);
        canvas.drawTextOnPath(text,ccwPth,0,tRect.height(),mPaint);
    }
}
