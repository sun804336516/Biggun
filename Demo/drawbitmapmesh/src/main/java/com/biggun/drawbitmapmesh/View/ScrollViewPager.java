package com.biggun.drawbitmapmesh.View;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 作者：孙贤武 on 2016/4/14 10:38
 * 邮箱：sun91985415@163.com
 */
public class ScrollViewPager extends ViewPager
{
    public ScrollViewPager(Context context)
    {
        super(context);
    }

    public ScrollViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}

