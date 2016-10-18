package com.biggun.drawbitmapmesh.View;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.biggun.drawbitmapmesh.R;
import com.biggun.drawbitmapmesh.Util.LogUtils;


/**
 * 作者：孙贤武 on 2016/6/2 11:46
 * 邮箱：sun91985415@163.com
 */
public class BIGGUN_ClipImageView extends ImageView implements ViewTreeObserver.OnGlobalLayoutListener
{
    private int MOVE = 20;
    /**
     * 裁剪矩形的最小宽高
     */
    private int RECT_MIN_WID = 0;
    private static final int LEFT_TOP = 1;
    private static final int RIGHT_TOP = LEFT_TOP << 1;
    private static final int LEFT_BOTTOM = RIGHT_TOP << 1;
    private static final int RIGHT_BOTTOM = LEFT_BOTTOM << 1;
    private static final int CENTER = RIGHT_BOTTOM << 1;
    private int currentType = -1;
    Rect realRect = new Rect();

    public static final int TYPE_RECT = 2;
    public static final int TYPE_CIRCLE = 1;
    int clipType = TYPE_RECT;

    /**
     * 设置剪裁模式
     *
     * @param clipType
     */
    public void setClipType(int clipType)
    {
        this.clipType = clipType;
        first = true;
        onGlobalLayout();
    }

    public int getClipType()
    {
        return clipType;
    }

    @IntDef({LEFT_TOP, RIGHT_TOP, LEFT_BOTTOM, RIGHT_BOTTOM, CENTER})
    private @interface TYPE
    {

    }

    public BIGGUN_ClipImageView(Context context)
    {
        this(context, null);
    }

    public BIGGUN_ClipImageView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    Matrix mMatrix = new Matrix();
    Rect mRect = new Rect();
    int dex = 0;
    Paint mPaint;
    int paintWid = 8;

    public BIGGUN_ClipImageView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ClipImageView);
        clipType = ta.getInt(R.styleable.ClipImageView_clip_rect, TYPE_RECT);
        ta.recycle();
        MOVE = dp2px(MOVE);
        RECT_MIN_WID = MOVE << 2;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(paintWid);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.clipPath(getPathFromRect(mRect), Region.Op.DIFFERENCE);
        canvas.drawColor(0x86222222);
        mPaint.setPathEffect(new DashPathEffect(new float[]{10, 5}, dex));
        dex--;
        canvas.drawPath(getPathFromRect(mRect), mPaint);
        invalidate();
    }

    float downX, downY;

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                currentType = getType((int) downX, (int) downY);
                break;
            case MotionEvent.ACTION_MOVE:
                resetRect((int) (event.getX() - downX), (int) (event.getY() - downY));
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    private int getType(int downX, int downY)
    {
        RectF left_top = new RectF(mRect.left - MOVE, mRect.top - MOVE, mRect.left + MOVE, mRect.top + MOVE);
        RectF right_top = new RectF(mRect.right - MOVE, mRect.top - MOVE, mRect.right + MOVE, mRect.top + MOVE);
        RectF left_bottom = new RectF(mRect.left - MOVE, mRect.bottom - MOVE, mRect.left + MOVE, mRect.bottom + MOVE);
        RectF right_bottom = new RectF(mRect.right - MOVE, mRect.bottom - MOVE, mRect.right + MOVE, mRect.bottom + MOVE);
        if (left_top.contains(downX, downY)) {
            return LEFT_TOP;
        } else if (right_top.contains(downX, downY)) {
            return RIGHT_TOP;
        } else if (left_bottom.contains(downX, downY)) {
            return LEFT_BOTTOM;
        } else if (right_bottom.contains(downX, downY)) {
            return RIGHT_BOTTOM;
        } else if (mRect.contains(downX, downY)) {
            return CENTER;
        }
        return -1;
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
    public void onGlobalLayout()
    {
        if (first) {
            Drawable d = getDrawable();
            float drawWid = d.getIntrinsicWidth();
            float drawHei = d.getIntrinsicHeight();
            int wid = getWidth();
            int hei = getHeight();
            LogUtils.e(wid + "===" + hei + "-----------" + drawWid + "===" + drawHei);
            realRect.set(0, 0, wid, hei);
            Bitmap bitmap = Bitmap.createBitmap(wid, hei, d.getOpacity()
                    != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            d.setBounds(0, 0, wid, hei);
            d.draw(canvas);
            setImageBitmap(bitmap);

            int centerX = wid >> 1;
            int centerY = hei >> 1;
            mRect = new Rect(centerX - RECT_MIN_WID, centerY - RECT_MIN_WID,
                    centerX + RECT_MIN_WID, centerY + RECT_MIN_WID);
            if (clipType == TYPE_CIRCLE) {
                int cirwid = Math.min(centerX, centerY);
                mRect = new Rect(cirwid - RECT_MIN_WID, cirwid - RECT_MIN_WID,
                        cirwid + RECT_MIN_WID, cirwid + RECT_MIN_WID);
            }
        }
        first = false;
    }

    private Path getPathFromRect(Rect rectF)
    {
        if (rectF == null || rectF.isEmpty()) {
            return null;
        }
        Path path = new Path();
        switch (clipType) {
            case TYPE_RECT:
                path.moveTo(rectF.left, rectF.top);
                path.lineTo(rectF.right, rectF.top);
                path.lineTo(rectF.right, rectF.bottom);
                path.lineTo(rectF.left, rectF.bottom);
                break;
            case TYPE_CIRCLE:
                path.addCircle(mRect.centerX(), mRect.centerY(), Math.min(mRect.width(), mRect.height()) >> 1, Path.Direction.CCW);
                break;
        }
        path.close();
        return path;
    }

    private void resetRect(int dx, int dy)
    {
        int left = mRect.left;
        int top = mRect.top;
        int right = mRect.right;
        int bottom = mRect.bottom;
        switch (currentType) {
            case LEFT_BOTTOM:
                left += dx;
                bottom += dy;
                break;
            case LEFT_TOP:
                left += dx;
                top += dy;
                break;
            case RIGHT_BOTTOM:
                right += dx;
                bottom += dy;
                break;
            case RIGHT_TOP:
                top += dy;
                right += dx;
                break;
            case CENTER:
                top += dy;
                bottom += dy;
                left += dx;
                right += dx;
                break;
        }
        // TODO: 2016/6/3 碰到边界缩小问题  考虑在这里解决(已解决)
        if (left < realRect.left+paintWid) {
            left = realRect.left+paintWid;
            right = mRect.right;
        }
        if (right > realRect.right-paintWid) {
            right = realRect.right-paintWid;
            left = mRect.left;
        }
        if (top < realRect.top+paintWid) {
            top = realRect.top+paintWid;
            bottom = mRect.bottom;
        }
        if (bottom > realRect.bottom-paintWid) {
            bottom = realRect.bottom-paintWid;
            top = mRect.top;
        }
        // TODO: 2016/6/3 限制大小
        if ((right - left) <= RECT_MIN_WID) {
            right = left + RECT_MIN_WID;
        }
        if ((bottom - top) <= RECT_MIN_WID) {
            bottom = top + RECT_MIN_WID;
        }
        mRect.set(left, top, right, bottom);
        // TODO: 2016/6/3 圆形的特殊处理
        if (clipType == TYPE_CIRCLE) {
            int min = Math.min(mRect.width(), mRect.height());
            right = left + min;
            bottom = top + min;
            mRect.set(left, top, right, bottom);
        }
        Log.e("==", mRect.width() + "====" + mRect.height());
    }

    public Bitmap getBitmap()
    {
        Bitmap bitmap = getBitmapFromDrawable(getDrawable());
        bitmap = Bitmap.createBitmap(bitmap, mRect.left - realRect.left, mRect.top - realRect.top, mRect.width(), mRect.height(), null, false);
        if (clipType == TYPE_CIRCLE) {

            // TODO: 2016/6/3 利用xfermode制作圆形bitmap速度快，没有锯齿
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            canvas.drawARGB(0, 0, 0, 0);
            final Rect rect = new Rect(0, 0, mRect.width(), mRect.height());
            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            paint.setDither(true);
            //在画布上绘制一个圆
            canvas.drawCircle(mRect.width() >> 1, mRect.height() >> 1, mRect.height() >> 1, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
            return output;
            // TODO: 2016/6/3 考虑增加圆形图片.考虑弄一个rect跟一个圆，涉及到点是否在圆中的计算，圆内的点像素采用图片，圆外的采取透明色。
//            int[] pixels = new int[bitmap.getHeight() * bitmap.getWidth()];
//            for (int i = 0; i < bitmap.getWidth(); i++) {
//                for (int j = 0; j < bitmap.getHeight(); j++) {
//                    if (canInsert(i, j)) {
//                        pixels[j * bitmap.getWidth() + i] = bitmap.getPixel(i, j);
//                    } else {
//                        pixels[j * bitmap.getWidth() + i] = Color.TRANSPARENT;
//                    }
//                }
//            }
            // TODO: 2016/6/6  RGB_565没有alpha通道，图片保存用png或者webp格式
//            bitmap = Bitmap.createBitmap(pixels, mRect.width(), mRect.height(), Bitmap.Config.ARGB_8888);
        }
        return bitmap;
    }

    /**
     * 判断点是否在圆上
     *
     * @return
     */
    private boolean canInsert(int x, int y)
    {
        // TODO: 2016/6/3 圆心mrect.wid>>1,mrect.hei>>1,半径mrect.wid>>1
        double power = Math.sqrt(Math.pow(x - (mRect.width() >> 1), 2) + Math.pow(y - (mRect.height() >> 1), 2));
        double index = mRect.width() >> 1;
        if (clipType == TYPE_CIRCLE && power < index) {
            return true;
        }
        return false;
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable)
    {
        Bitmap bitmap = null;
        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity()
                    != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            drawable.draw(canvas);
        }
        return bitmap;
    }

    /**
     * dp转px
     *
     * @param value
     * @return
     */
    private int dp2px(float value)
    {
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        return (int) (metrics.density * value + 0.5f);
    }
}
