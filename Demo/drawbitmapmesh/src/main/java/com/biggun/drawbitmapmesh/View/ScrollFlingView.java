package com.biggun.drawbitmapmesh.View;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.biggun.drawbitmapmesh.Common;
import com.biggun.drawbitmapmesh.R;
import com.biggun.drawbitmapmesh.Util.Utils;

/**
 * 作者：孙贤武 on 2016/2/26 15:38
 * 邮箱：sun91985415@163.com
 */
public class ScrollFlingView extends ViewGroup
{
    public ScrollFlingView(Context context)
    {
        this(context, null);
    }

    public ScrollFlingView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    private int interval = 20;
    private View touchView;
    private int width, height;
    private RectF rectF;

    private static final int NUM = 3;
    private int showNum;

    public ScrollFlingView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
//        interval = (int) dp2px(20);
//        requestDisallowInterceptTouchEvent(true);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScrollFlingView);
        showNum = typedArray.getInteger(R.styleable.ScrollFlingView_shownumb, NUM);
        typedArray.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        if (getChildCount() < 3) {
            throw new IllegalArgumentException("子View最少三个");
        }
        if (showNum > getChildCount()) {
            throw new IllegalArgumentException("showNum 必须小于Child个数");
        }
        View childView = null;
        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = 0;
        for (int i = 0, len = getChildCount(); i < len; i++) {
            childView = getChildAt(i);
            MarginLayoutParams mlp = (MarginLayoutParams) childView.getLayoutParams();

            Utils.LogE(getMeasuredWidth() + "--" + getMeasuredHeight() + "--" + width + "--" + height + "--" + left + "--" + top);
            if (i <= len - showNum) {
                left = mlp.leftMargin + (showNum - 1) * interval + (getMeasuredWidth() - width) >> 1;
                top = mlp.topMargin + (getMeasuredHeight() - height) >> 1;
                right = left + childView.getMeasuredWidth();
                bottom = top + childView.getMeasuredHeight();
            } else {
                left = (len-1 - i) * interval + mlp.leftMargin + (getMeasuredWidth() - width) >> 1;
                top = (i - len + showNum) * interval + mlp.topMargin + (getMeasuredHeight() - height) >> 1;
                right = left + childView.getMeasuredWidth();
                bottom = top + childView.getMeasuredHeight();
            }
            Utils.LogE("left:" + left + "-top:" + top + "-right:" + right + "-botom:" + bottom);
            childView.layout(left, top, right, bottom);
        }
        rectF = new RectF((getMeasuredWidth() - width) >> 1, (getMeasuredHeight() - height) >> 1, (getMeasuredWidth() + width) >> 1, (getMeasuredHeight() + height) >> 1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widmode = MeasureSpec.getMode(widthMeasureSpec);
        int heimode = MeasureSpec.getMode(heightMeasureSpec);
        int wid = MeasureSpec.getSize(widthMeasureSpec);
        int hei = MeasureSpec.getSize(heightMeasureSpec);

        View childView = null;
        for (int i = 0, len = getChildCount(); i < len; i++) {
            childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
        View child = getChildAt(0);
        MarginLayoutParams mlp = (MarginLayoutParams) child.getLayoutParams();
        measureChild(child, widthMeasureSpec, heightMeasureSpec);
        width = child.getMeasuredWidth() + mlp.leftMargin + mlp.rightMargin + (getChildCount() - showNum) * interval;
        height = child.getMeasuredHeight() + mlp.topMargin + mlp.bottomMargin + (getChildCount() - showNum) * interval;
        Utils.LogE("childWidth:" + child.getMeasuredWidth() + "-childheight:" + child.getMeasuredHeight() + "-width:" + width + "-height:" + height + "-interval:" + interval);
        setMeasuredDimension(widmode == MeasureSpec.EXACTLY ? wid : width, heimode == MeasureSpec.EXACTLY ? hei : height);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new MarginLayoutParams(getContext(), attrs);
    }

    private float dp2px(int dp)
    {
//        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,dp,getResources().getDisplayMetrics());
        return getResources().getDisplayMetrics().density * dp + 0.5f;
    }

    private float mDownX;
    private boolean upScroll = false;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!rectF.contains(event.getX(), event.getY())) {
                    return false;
                }
                touchView = getChildAt(getChildCount() - 1);
                upScroll = event.getY() > (getMeasuredHeight() >> 1);
                mDownX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX() - mDownX;
                float rotate = moveX / width * 90;
                touchView.animate().translationX(moveX).rotation(upScroll ? -rotate : rotate).setDuration(0);

                Utils.LogE(width + "movex:" + moveX + "-rotate:" + rotate);
                break;
            case MotionEvent.ACTION_UP:
                touchView.animate().translationX(0).rotation(0).setDuration(Common.ANIMATORTIME_250);
                break;
        }
        return true;
    }
}
