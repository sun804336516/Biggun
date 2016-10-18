package com.biggun.drawbitmapmesh.Animation;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * 作者：孙贤武 on 2016/2/25 14:10
 * 邮箱：sun91985415@163.com
 */
public class MyBehavior extends CoordinatorLayout.Behavior<View>
{
    public MyBehavior(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency)
    {
        return dependency instanceof TextView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency)
    {
        int offset = dependency.getTop() - child.getTop();
        ViewCompat.offsetTopAndBottom(child,offset);
        return super.onDependentViewChanged(parent, child, dependency);
    }
}
