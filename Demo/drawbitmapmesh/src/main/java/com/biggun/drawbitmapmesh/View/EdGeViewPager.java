package com.biggun.drawbitmapmesh.View;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.biggun.drawbitmapmesh.Util.Utils;

/**
 * Created by 孙贤武 on 2016/1/11.
 */
public class EdGeViewPager extends ViewPager
{
    private float mDownx, mDowny;
    private int minSlop;
    private int childCount;
    private EdgeListener edgeListener;
    public static final int LEFT = 0;
    public static final int RIGHT = 1;

    private static final float DEGREE = 1 / 3.0f;

    public EdGeViewPager(Context context)
    {
        this(context, null);
    }

    public EdGeViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        minSlop = configuration.getScaledTouchSlop();
        Utils.LogE("====:" + minSlop);
        setClipChildren(false);
        setPageMargin(10);
    }

    public void setChildCount(int childCount)
    {
        this.childCount = childCount;
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event)
    {
        return super.onInterceptHoverEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownx = ev.getRawX();
                mDowny = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (edgeListener != null) {
                    float disX = ev.getRawX() - mDownx;
                    float disY = ev.getRawY() - mDowny;

                    if (disX > 0 && disX >= getWidth() * DEGREE && getCurrentItem() == 0) {
                        edgeListener.edge(LEFT);
                    }
                    if (disX < 0 && disX <= -getWidth() * DEGREE && getCurrentItem() == childCount - 1) {
                        edgeListener.edge(RIGHT);
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void setEdgeListener(EdgeListener edgeListener)
    {
        this.edgeListener = edgeListener;
    }

    private boolean isEdge()
    {
        int currentItem = getCurrentItem();
        return currentItem == 0 || currentItem == childCount - 1;
    }

    public interface EdgeListener
    {
        void edge(int edge);
    }
}
