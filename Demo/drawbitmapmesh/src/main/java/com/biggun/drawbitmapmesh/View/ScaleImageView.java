package com.biggun.drawbitmapmesh.View;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * 作者：Administrator on 2016/5/31 18:29
 * 邮箱：sun91985415@163.com
 */
public class ScaleImageView extends ImageView implements ViewTreeObserver.OnGlobalLayoutListener
{
    Matrix mMatrix = new Matrix();
    float maxScale = 3.0f;
    float midScale = 1.5f;
    float minScale = 1.0f;
    ScaleGestureDetector mScaleGestureDetector;
    GestureDetector mDetector;

    public ScaleImageView(Context context)
    {
        this(context, null);
    }

    public ScaleImageView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public ScaleImageView(Context context, AttributeSet attrs, final int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        setScaleType(ScaleType.MATRIX);
        mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.OnScaleGestureListener()
        {
            @Override
            public boolean onScale(ScaleGestureDetector detector)
            {
                float scaleFactor = detector.getScaleFactor();
                float currentScale = getCurrentScale();
                // TODO: 2016/6/2 控制缩放范围
                if ((currentScale <= maxScale && scaleFactor > 1.0f) || (currentScale >= minScale && scaleFactor < 1.0f)) {
                    if (scaleFactor * currentScale > maxScale) {
                        scaleFactor = maxScale / currentScale;
                    } else if (scaleFactor * currentScale < minScale) {
                        scaleFactor = minScale / currentScale;
                    }
                    mMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
                    setImageMatrix(mMatrix);
                    checkBound();
                }
                return true;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector)
            {
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector)
            {
            }
        });
        mDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener()
        {
            @Override
            public boolean onDoubleTap(MotionEvent e)
            {
                float scale = getCurrentScale();
                if (scale < midScale) {
                    ScaleAnimated(scale, midScale, e.getX(), e.getY());
                } else if (scale < maxScale) {
                    ScaleAnimated(scale, maxScale, e.getX(), e.getY());
                } else {
                    ScaleAnimated(maxScale, minScale, e.getX(), e.getY());
                }
                return true;
            }
        });
    }

    long scaleAniDuration = 500;
    boolean isCanAni = true;

    private void ScaleAnimated(final float startScale, final float endScale, final float centerx, final float centery)
    {
        isCanAni = false;
        ValueAnimator ani = ValueAnimator.ofFloat(startScale, endScale);
        ani.setDuration(scaleAniDuration);
        ani.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                float scale = (float) animation.getAnimatedValue() / getCurrentScale();
                mMatrix.postScale(scale, scale, centerx, centery);
                checkBound();
                setImageMatrix(mMatrix);
            }
        });
        ani.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);
                isCanAni = true;
            }
        });
        ani.start();
    }

    float mLastX, mLastY;
    int lastPointerCount;
    boolean isCanDrag, isCheckLeftAndRight, isCheckTopAndBottom;

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        mScaleGestureDetector.onTouchEvent(event);
        mDetector.onTouchEvent(event);
        float x = 0, y = 0;
        // 拿到触摸点的个数
        final int pointerCount = event.getPointerCount();
        // 得到多个触摸点的x与y均值
        for (int i = 0; i < pointerCount; i++) {
            x += event.getX(i);
            y += event.getY(i);
        }
        x = x / pointerCount;
        y = y / pointerCount;

        /**
         * 每当触摸点发生变化时，重置mLasX , mLastY
         */
        if (pointerCount != lastPointerCount) {
            isCanDrag = false;
            mLastX = x;
            mLastY = y;
        }
        lastPointerCount = pointerCount;

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dx = x - mLastX;
                float dy = y - mLastY;

                if (!isCanDrag) {
                    isCanDrag = isCanDrag(dx, dy);
                }
                if (isCanDrag) {
                    RectF rectF = getMatrixRectf();
                    if (getDrawable() != null) {
                        isCheckLeftAndRight = isCheckTopAndBottom = true;
                        // 如果宽度小于屏幕宽度，则禁止左右移动
                        if (rectF.width() < getWidth()) {
                            dx = 0;
                            isCheckLeftAndRight = false;
                        }
                        // 如果高度小于屏幕高度，则禁止上下移动
                        if (rectF.height() < getHeight()) {
                            dy = 0;
                            isCheckTopAndBottom = false;
                        }
                        mMatrix.postTranslate(dx, dy);
                        checkMatrixBounds();
                        setImageMatrix(mMatrix);
                    }
                }
                mLastX = x;
                mLastY = y;
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                lastPointerCount = 0;
                break;
        }
        return true;
    }

    float[] values = new float[9];

    private float getCurrentScale()
    {
        mMatrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    boolean first = true;

    @Override
    public void setImageBitmap(Bitmap bm)
    {
        super.setImageBitmap(bm);
    }

    @Override
    public void onGlobalLayout()
    {
        if (first) {
            Drawable drawable = getDrawable();
            if (drawable == null) {
                return;
            }
            int imgWid = getWidth();
            int imgHei = getHeight();
            int draWid = drawable.getIntrinsicWidth();
            int draHei = drawable.getIntrinsicHeight();
            float scale = 1.0f;
            // TODO: 2016/6/2 图片宽高均大于控件宽高
            if (draWid >= imgWid && draHei >= imgHei) {
                scale = Math.max(draWid / imgWid, draHei / imgHei);
            } else if (draWid > imgWid && draHei < imgHei) {
                scale = draWid / imgWid;
            } else if (draWid < imgWid && draHei > imgHei) {
                scale = draHei / imgHei;
            }
            mMatrix.postTranslate((imgWid - draWid) >> 1, (imgHei - draHei) >> 1);
            mMatrix.postScale(scale, scale);
            setImageMatrix(mMatrix);
            first = false;
        }
    }

    private void checkMatrixBounds()
    {
        RectF rect = getMatrixRectf();

        float deltaX = 0, deltaY = 0;
        final float viewWidth = getWidth();
        final float viewHeight = getHeight();
        // 判断移动或缩放后，图片显示是否超出屏幕边界
        if (rect.top > 0 && isCheckTopAndBottom) {
            deltaY = -rect.top;
        }
        if (rect.bottom < viewHeight && isCheckTopAndBottom) {
            deltaY = viewHeight - rect.bottom;
        }
        if (rect.left > 0 && isCheckLeftAndRight) {
            deltaX = -rect.left;
        }
        if (rect.right < viewWidth && isCheckLeftAndRight) {
            deltaX = viewWidth - rect.right;
        }
        mMatrix.postTranslate(deltaX, deltaY);
    }

    /**
     * 是否是推动行为
     *
     * @param dx
     * @param dy
     * @return
     */
    private boolean isCanDrag(float dx, float dy)
    {
        return Math.sqrt((dx * dx) + (dy * dy)) >= 10;
    }

    /**
     * 检查边缘
     */
    private void checkBound()
    {
        RectF rectf = getMatrixRectf();
        int wid = getWidth();
        int hei = getHeight();
        float delx = 0.0f;
        float delY = 0.0f;
        if (rectf.width() > wid) {
            if (rectf.left < 0) {
                delx = -rectf.left;
            }
            if (rectf.right < wid) {
                delx = wid - rectf.right;
            }
        }
        if (rectf.height() > hei) {
            if (rectf.top < 0) {
                delY = -rectf.top;
            }
            if (rectf.bottom < hei) {
                delY = hei - rectf.bottom;
            }
        }
        if (rectf.width() < wid) {
            delx = (wid >> 1) - rectf.right + rectf.width() * 0.5f;
        }
        if (rectf.height() < hei) {
            delY = (hei >> 1) - rectf.bottom + rectf.height() * 0.5f;
        }
        mMatrix.postTranslate(delx, delY);
    }

    private RectF getMatrixRectf()
    {
        RectF rectF = new RectF();
        Drawable d = getDrawable();
        Matrix matrix = mMatrix;
        if (d != null) {
            rectF.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            matrix.mapRect(rectF);
        }
        return rectF;
    }
}
