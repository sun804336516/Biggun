package com.biggun.drawbitmapmesh.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.RegionIterator;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by 孙贤武 on 2016/1/19.
 */
public class RegionView extends View
{
    private Paint mPaint;
    private static final int RADIUS = 100;
    private int screenWidth;

    public RegionView(Context context)
    {
        this(context, null);
    }

    public RegionView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public RegionView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(5);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);

        DisplayMetrics displayMetrics =
                getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(screenWidth, RADIUS * 10);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

//        canvas.translate(getMeasuredWidth() / 2 - RADIUS / 2, 0);
        canvas.translate((getMeasuredWidth() - RADIUS) >> 1, 0);
        Path ovalPath = new Path();
        RectF rectf = new RectF(0, 0, RADIUS, RADIUS * 5);
        ovalPath.addOval(rectf, Path.Direction.CW);
        Region region = new Region();
        region.setPath(ovalPath, new Region(0, 0, RADIUS, RADIUS * 2));
        mPaint.setColor(Color.RED);
        DrawRegion(canvas, region);


        canvas.translate(RADIUS/2-getMeasuredWidth()/2,0);
        mPaint.setStyle(Paint.Style.STROKE);
        Rect r1 = new Rect(0,RADIUS*6,RADIUS*3,RADIUS*7);
        canvas.drawRect(r1,mPaint);
        Rect r2 = new Rect(RADIUS,RADIUS*5,RADIUS*2,RADIUS*8);
        canvas.drawRect(r2,mPaint);

        Region region1 = new Region(r1);
        Region region2 = new Region(r2);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.GREEN);
        region1.op(region2, Region.Op.XOR);
        DrawRegion(canvas, region1);

       /* ValueAnimator animator;
        scrollBy();
        scrollTo();
        Scroller scroller;
        scroller.startScroll();*/
    }

    private void DrawRegion(Canvas canvas, Region region)
    {
        RegionIterator regionIterator = new RegionIterator(region);
        Rect rect = new Rect();
        while (regionIterator.next(rect)) {
            canvas.drawRect(rect, mPaint);
        }
    }
    /**
     * 假设用region1  去组合region2
     public enum Op {
     DIFFERENCE(0), //最终区域为region1 与 region2不同的区域
     INTERSECT(1), // 最终区域为region1 与 region2相交的区域
     UNION(2),      //最终区域为region1 与 region2组合一起的区域
     XOR(3),        //最终区域为region1 与 region2相交之外的区域
     REVERSE_DIFFERENCE(4), //最终区域为region2 与 region1不同的区域
     REPLACE(5); //最终区域为为region2的区域
     }
     */
}
