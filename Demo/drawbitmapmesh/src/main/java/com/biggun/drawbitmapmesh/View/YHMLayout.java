package com.biggun.drawbitmapmesh.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 作者：孙贤武 on 2016/5/9 13:54
 * 邮箱：sun91985415@163.com
 */
public class YHMLayout extends LinearLayout
{
    public YHMLayout(Context context)
    {
        this(context, null);
    }

    public YHMLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public YHMLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    private boolean isHide = false;

    public void setHidehalf(boolean hide)
    {
        isHide = hide;
    }

    public boolean isHide()
    {
        return isHide;
    }
}
