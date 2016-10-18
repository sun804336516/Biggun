package com.biggun.drawbitmapmesh.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.biggun.drawbitmapmesh.R;
import com.biggun.drawbitmapmesh.Util.DrawUtils;
import com.biggun.drawbitmapmesh.Util.Utils;


/**
 * 作者：孙贤武 on 2016/4/5 10:25
 * 邮箱：sun91985415@163.com
 */
public class ClipPathView extends BaseBitmapView
{
    private static final boolean DEFAULT_NEEDROUND = false;
    private static final int DEFAULT_RADIUS = 40;
    private boolean needRound = DEFAULT_NEEDROUND;
    private int radius = DEFAULT_RADIUS;

    private static final String TEXT = "CLIPPATH设置圆角";
    private int textWid;
    private Rect textRect = new Rect();

    private Path canvasPath;
    private Paint canvasPaint, pathPaint;
    private Bitmap bitmap;
    private float pathIndex = 0f;

    public ClipPathView(Context context)
    {
        this(context, null);
    }

    public ClipPathView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public ClipPathView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ClipPathView);
        radius = ta.getDimensionPixelSize(R.styleable.ClipPathView_radius, DEFAULT_RADIUS);
        needRound = ta.getBoolean(R.styleable.ClipPathView_needradius, DEFAULT_NEEDROUND);
        Drawable drawable = ta.getDrawable(R.styleable.ClipPathView_drawable);
        ta.recycle();
        bitmap = Utils.getBitmap(drawable);

        canvasPath = new Path();
        canvasPaint = getPaint(3, 0x33000000, Paint.Style.STROKE);
        canvasPaint.setTextSize(40);
        canvasPaint.setPathEffect(new CornerPathEffect(5));
        textWid = (int) canvasPaint.measureText(TEXT, 0, TEXT.length());
        canvasPaint.getTextBounds(TEXT, 0, TEXT.length(), textRect);

        pathPaint = getPaint(4, Color.WHITE, Paint.Style.STROKE);
    }

    public void setBitmap(Bitmap bitmap)
    {
        this.bitmap = bitmap;
        requestLayout();
        invalidate();
    }

    public void setCanvasPath(Path canvasPath)
    {
        this.canvasPath = canvasPath;
        needRound = true;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        if (needRound) {
            canvas.clipPath(canvasPath);
        }
        //设置图片抗锯齿
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        drawBackground(canvas);
        pathPaint.setPathEffect(new DashPathEffect(new float[]{10, 10}, pathIndex));
        canvas.drawPath(canvasPath, pathPaint);
        pathIndex--;
        invalidate();
    }

    /**
     * 画背景
     *
     * @param canvas
     */
    private void drawBackground(Canvas canvas)
    {
        canvas.drawText(TEXT, (getWidth() - textWid) >> 1, (getHeight() + textRect.height()) >> 1, canvasPaint);
        if (bitmap == null) {
            return;
        }
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasPath = DrawUtils.getRoundPath(w, h, radius);
//        canvasPath = DrawUtils.getCirclePath(w,h);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            setMeasuredDimension(width, height);
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}

