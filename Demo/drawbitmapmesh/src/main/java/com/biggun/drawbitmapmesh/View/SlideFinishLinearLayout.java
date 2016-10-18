package com.biggun.drawbitmapmesh.View;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

import com.biggun.drawbitmapmesh.Common;
import com.biggun.drawbitmapmesh.Interface.SlideFinish;
import com.biggun.drawbitmapmesh.Util.ActivityManager;
import com.biggun.drawbitmapmesh.Util.Utils;


/**
 * Created by 孙贤武 on 2016/1/26.
 */
public class SlideFinishLinearLayout extends LinearLayout
{
    public SlideFinishLinearLayout(Context context)
    {
        this(context, null);
    }

    public SlideFinishLinearLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    private int mDownx;
    private int lastMovex;
    private int mSlop;
    ValueAnimator animator = null;
    SlideFinish slideFinish;
    boolean isfinish = false;

    public void setSlideFinish(SlideFinish slideFinish)
    {
        this.slideFinish = slideFinish;
    }

    public SlideFinishLinearLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        mSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(ViewConfiguration.get(context));
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownx = (int) ev.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                lastMovex = (int) ev.getRawX();
                int movex = lastMovex - mDownx;
                Utils.LogE("moveX:" + movex);
                if (movex > mSlop) {
                    return true;
                }
                return false;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) (lastMovex - event.getRawX());
                Utils.LogE("Action_Move");
                scrollBy(moveX, 0);
                lastMovex = (int) event.getRawX();
                return true;
            case MotionEvent.ACTION_UP:
                Utils.LogE(getWidth() + "getScrollX:" + getScrollX());
                if (Math.abs(getScrollX()) > getWidth() / 2) {
                    animator = ValueAnimator.ofInt(getScrollX(), -getWidth());
                    isfinish = true;
                } else {
                    isfinish = false;
                    animator = ValueAnimator.ofInt(getScrollX(), 0);
                }
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
                {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation)
                    {
                        scrollTo((int) animation.getAnimatedValue(), 0);
                    }
                });
                animator.addListener(new AnimatorListenerAdapter()
                {
                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        super.onAnimationEnd(animation);
                        Utils.LogE(isfinish + "===");
                        if (slideFinish != null) {
                            slideFinish.onSlide(isfinish);
                        }
                        if (isfinish) {
                            ActivityManager.getInstance().getCurrentActivity().finish();
                        }
                    }
                });
                animator.setDuration(Common.ANIMATORTIME_250);
                animator.start();
                break;
        }
        return super.onTouchEvent(event);
    }
}
