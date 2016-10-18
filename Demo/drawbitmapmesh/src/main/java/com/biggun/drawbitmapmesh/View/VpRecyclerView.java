package com.biggun.drawbitmapmesh.View;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * 作者：孙贤武 on 2016/4/14 10:44
 * 邮箱：sun91985415@163.com
 */
public class VpRecyclerView extends RecyclerView
{
    public VpRecyclerView(Context context)
    {
        this(context,null);
    }

    public VpRecyclerView(Context context, @Nullable AttributeSet attrs)
    {
        this(context, attrs,0);
    }

    public VpRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        setLayoutManager(new LinearLayoutManager(null,LinearLayoutManager.HORIZONTAL,false));
    }

}
