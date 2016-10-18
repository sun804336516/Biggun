package com.biggun.drawbitmapmesh.Animation;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;

/**
 * 作者：孙贤武 on 2016/2/25 15:06
 * 邮箱：sun91985415@163.com
 */
public class ScrollBehavior extends CoordinatorLayout.Behavior<View>
{
    public ScrollBehavior(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes)
    {
//        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL)!=0;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed)
    {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        child.setScrollY(target.getScrollY());
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY)
    {
//        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
        ((NestedScrollView)child).fling((int) velocityY);
        return true;
    }
}
