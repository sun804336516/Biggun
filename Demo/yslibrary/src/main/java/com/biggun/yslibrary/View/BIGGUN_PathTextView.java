package com.biggun.yslibrary.View;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * 跳动的文字效果
 * 作者：孙贤武 on 2016/7/26 16:42
 * 邮箱：sun91985415@163.com
 * LIKE:YANSHUO
 */
public class BIGGUN_PathTextView extends View
{
    public BIGGUN_PathTextView(Context context)
    {
        this(context,null);
    }

    public BIGGUN_PathTextView(Context context, AttributeSet attrs)
    {
        this(context, attrs,0);
    }

    public BIGGUN_PathTextView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
    }
}
