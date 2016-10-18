package com.biggun.drawbitmapmesh.View;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

import com.biggun.drawbitmapmesh.Common;

/**
 * 作者：孙贤武 on 2016/3/30 10:56
 * 邮箱：sun91985415@163.com
 */
public class BezierView extends BaseAniView
{
    private Path mPath, path;
    private PathMeasure mPathMesaure;
    private Paint mPaint, circlePaint, eyepaint;
    private int offset = 20;
    private float[] indexs = new float[]{offset, offset};
    private Point lefteyes, righteyes;

    public BezierView(Context context)
    {
        this(context, null);
    }

    public BezierView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public BezierView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);//pre4.4开启软件加速，不然可能不会显示路径
        }

        mPaint = getPaint(5, Color.BLACK, Paint.Style.STROKE);
        mPaint.setPathEffect(new CornerPathEffect(5));
        circlePaint = getPaint(5, Color.BLUE, Paint.Style.STROKE);
        circlePaint.setPathEffect(new CornerPathEffect(5));
        eyepaint = getPaint(5, Color.BLUE, Paint.Style.FILL);
        eyepaint.setPathEffect(new CornerPathEffect(5));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        path = new Path();
        mPath = new Path();
        mPath.moveTo(offset, offset);
        mPath.quadTo(w, offset, w, h);
        mPath.quadTo(offset, h, offset, offset);
        mPath.cubicTo(w - offset >> 1, offset, w - offset >> 1, h, w, h);
        mPath.cubicTo(w, h - offset >> 1, offset, h - offset >> 1, offset, offset);
        mPathMesaure = new PathMeasure(mPath, true);

        lefteyes = new Point(w - offset >> 2, h - offset >> 2);
        righteyes = new Point((w - offset >> 2) * 3, (h - offset >> 2) * 3);
        startAni();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
        canvas.drawPath(path, circlePaint);
        path.reset();
        canvas.drawCircle(indexs[0], indexs[1], 10, circlePaint);
        canvas.drawCircle(lefteyes.x, lefteyes.y, 20, eyepaint);
        canvas.drawCircle(righteyes.x, righteyes.y, 20, eyepaint);
    }

    public void startAni()
    {
        ani = ValueAnimator.ofFloat(0, mPathMesaure.getLength());
        ani.setDuration(Common.ANIMATORTIME_6000);
        ani.setRepeatCount(ValueAnimator.INFINITE);
        ani.setRepeatMode(ValueAnimator.RESTART);
        ani.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                float value = (float) animation.getAnimatedValue();
                mPathMesaure.getPosTan(value, indexs, null);
                mPathMesaure.getSegment(0, value, path, true);//占用cpu很大，同时 4.4.4相近的版本可能不会显示出效果
                invalidate();
            }
        });
        ani.start();

        ValueAnimator ani2 = ValueAnimator.ofInt(Color.BLUE, Color.RED);
        ani2.setDuration(Common.ANIMATORTIME_3000);
        ani2.setEvaluator(new ArgbEvaluator());
        ani2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                int value = (int) animation.getAnimatedValue();
                eyepaint.setColor(value);
                invalidate();
            }
        });
        ani2.start();
    }

    @Override
    protected Parcelable onSaveInstanceState()
    {
        Bundle bundle = new Bundle();
        bundle.putParcelable("111", super.onSaveInstanceState());
        bundle.putString("sxw", "孙贤武");
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state)//此控件在布局中的id绝对不可少
    {
        if (state instanceof Bundle) {
            String str = ((Bundle) state).getString("sxw");
            super.onRestoreInstanceState(((Bundle) state).getParcelable("111"));
            return;
        }
        super.onRestoreInstanceState(state);
    }
}
