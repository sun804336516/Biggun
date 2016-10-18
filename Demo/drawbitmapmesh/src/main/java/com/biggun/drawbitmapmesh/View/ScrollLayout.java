package com.biggun.drawbitmapmesh.View;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.provider.DocumentsContract;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.biggun.drawbitmapmesh.Interface.ScrollLayoutListener;
import com.biggun.drawbitmapmesh.Util.Utils;

/**
 * 仿ViewPager
 * 作者：孙贤武 on 2016/1/27 11:20
 * 邮箱：sun91985415@163.com
 */
public class ScrollLayout extends ViewGroup
{
    private Scroller mScroller;
    /**
     * 侧滑的最小距离
     */
    private int minSlop;
    /**
     * 第一次触摸的x
     */
    private int mDownx;
    /**
     * 上一次滑动的最终位置
     */
    private int lastMoveX;
    /**
     * 可滑动的左边距
     */
    private int leftBorder;
    /**
     * 可滑动的右边距
     */
    private int rightBorder;
    /**
     * up之后执行的动画是否结束
     */
    private boolean ScrollEnd = false;
    //    private int lastScrollX;
    private ScrollLayoutListener scrollLayoutListener;
    /**
     * 按下手指 当前处于的界面编号
     */
    private int curentIndex;
    /**
     * 滑动方向
     */
    private ScrollOrientation scrollorientation = ScrollOrientation.RIGHT;
    private enum ScrollOrientation
    {
        LEFT, RIGHT, ABORT;
    }

    public void setScrollLayoutListener(ScrollLayoutListener scrollLayoutListener)
    {
        this.scrollLayoutListener = scrollLayoutListener;
    }

    public ScrollLayout(Context context)
    {
        this(context, null);
    }

    public ScrollLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public ScrollLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
        minSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(ViewConfiguration.get(context));
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownx = (int) ev.getRawX();
                curentIndex = getcurrentIndex();
                break;
            case MotionEvent.ACTION_MOVE:
                lastMoveX = (int) ev.getRawX();
                int scrollx = (int) (lastMoveX - mDownx);
                if (scrollx < 0) {
                    scrollorientation = ScrollOrientation.RIGHT;
                } else {
                    scrollorientation = ScrollOrientation.LEFT;
                }
                if (Math.abs(scrollx) > minSlop) {
                    Utils.LogE("intecepter-----");
                    ScrollEnd = false;
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                Utils.LogE("touchevent-----");
                int movex = (int) (lastMoveX - event.getRawX());
                if (getScrollX() + movex < leftBorder) {
                    scrollTo(leftBorder, 0);
                    scrollorientation = ScrollOrientation.ABORT;
                    return true;
                } else if (getScrollX() + getWidth() + movex > rightBorder) {
                    scrollTo(rightBorder - getWidth(), 0);
                    scrollorientation = ScrollOrientation.ABORT;
                    return true;
                }
                scrollBy(movex, 0);
                lastMoveX = (int) event.getRawX();
                getScrollPercent();
                break;
            case MotionEvent.ACTION_UP:
                int targetIndex = (getScrollX() + getWidth() / 2) / getWidth();
                int dx = targetIndex * getWidth() - getScrollX();
//                Utils.LogE(dx+"==="+dx1);
//                mScroller.startScroll(getScrollX(), 0, dx, 0);

                boolean one = scrollorientation == ScrollOrientation.LEFT && dx > 0;
                boolean two = scrollorientation == ScrollOrientation.RIGHT && dx < 0;
                if (one || two) {
                    scrollorientation = ScrollOrientation.ABORT;
                }
                ValueAnimator animator = ValueAnimator.ofInt(getScrollX(), getScrollX() + dx);//使用此可以获取到滑动的百分比
                animator.setDuration(250);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
                {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation)
                    {
                        int scollx = (int) animation.getAnimatedValue();
                        scrollTo(scollx, 0);
                        getScrollPercent();
                    }
                });
                animator.addListener(new AnimatorListenerAdapter()
                {
                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        super.onAnimationEnd(animation);
                        ScrollEnd = true;
                        getScrollPercent();
                    }
                });
                animator.start();
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    private void getScrollPercent()
    {
        if (scrollorientation == ScrollOrientation.ABORT) {
            scrollLayoutListener.onScrollChanged(getcurrentIndex(), 0);
            return;
        }
        if (scrollLayoutListener != null) {
            if (ScrollEnd) {
                scrollLayoutListener.onScrollChanged(getcurrentIndex(), 1);
                return;
            }
            Utils.LogE("scollx:" + getScrollX());
            float percent = Math.abs((getScrollX() - curentIndex * getWidth()) / (float) getWidth());
            scrollLayoutListener.onScrollChanged(getcurrentIndex(), percent);
        }
    }

    @Override
    public void computeScroll()
    {
        super.computeScroll();
//        if (mScroller.computeScrollOffset()) {
//            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
//            invalidate();
//        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0, count = getChildCount(); i < count; i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        if (!changed) {
            return;
        }
        int left, top, right, bottom;
        for (int i = 0, count = getChildCount(); i < count; i++) {
            View child = getChildAt(i);
            left = i * child.getMeasuredWidth() + child.getPaddingLeft();
            top = ((getMeasuredHeight() - child.getMeasuredHeight()) >> 1) + child.getPaddingTop();
            right = (i + 1) * child.getMeasuredWidth() + child.getPaddingRight();
            bottom = ((getMeasuredHeight() + child.getMeasuredHeight()) >> 1)
                    + child.getPaddingBottom();
            Utils.LogE("padd:" + child.getPaddingLeft() + "--" + child.getPaddingTop() + "--" + child.getPaddingRight() + "--" + child.getPaddingBottom());
            Utils.LogE("layout:" + left + "--" + top + "--" + right + "--" + bottom);
            child.layout(left, top, right, bottom);
        }
        leftBorder = getChildAt(0).getLeft();
        rightBorder = getChildAt(getChildCount() - 1).getRight();
    }

    public int getcurrentIndex()
    {
        return getScrollX() / getWidth();
    }

    private void abortEvent(MotionEvent event)
    {
        MotionEvent ev = MotionEvent.obtain(event);
        ev.setAction(MotionEvent.ACTION_CANCEL);
        onTouchEvent(ev);
    }

}
