package com.biggun.drawbitmapmesh.View;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 作者：孙贤武 on 2016/4/7 11:23
 * 邮箱：sun91985415@163.com
 * 基础的画背景View
 */
public class BaseBitmapView extends BaseView
{
    public BaseBitmapView(Context context)
    {
        this(context,null);
    }

    public BaseBitmapView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public BaseBitmapView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }
}
