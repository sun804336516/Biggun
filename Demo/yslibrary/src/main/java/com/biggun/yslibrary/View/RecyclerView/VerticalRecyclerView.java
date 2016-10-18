package com.biggun.yslibrary.View.RecyclerView;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.biggun.yslibrary.R;

/**
 * 作者：孙贤武 on 2016/5/5 17:16
 * 邮箱：sun91985415@163.com
 */
public class VerticalRecyclerView extends RecyclerView
{
    public VerticalRecyclerView(Context context)
    {
        this(context, null);
    }

    public VerticalRecyclerView(Context context, @Nullable AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public VerticalRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        addItemDecoration(new initDecoration(getResources().getDrawable(R.drawable.init_drawable)));
    }

    @Override
    public void setLayoutManager(LayoutManager layout)
    {
        if (layout instanceof LinearLayoutManager) {
            super.setLayoutManager(layout);
        }
    }

    public int getFirstVisiableItem()
    {
        return ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
    }

    /**
     * 获取触摸点所在的Position
     *
     * @param x
     * @param y
     * @return
     */
    public int Point2Position(int x, int y)
    {
        int childcount = getChildCount();
        Rect rect = new Rect();
        for (int i = 0; i < childcount; i++) {
            View view = getChildAt(i);
            if (view.getVisibility() == VISIBLE) {
                view.getHitRect(rect);
                if (rect.contains(x, y)) {
                    return i + getFirstVisiableItem();
                }
            }
        }
        return NO_POSITION;
    }
}
