package com.biggun.yslibrary.View.RecyclerView;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 作者：孙贤武 on 2016/3/4 16:45
 * 邮箱：sun91985415@163.com
 * 现在是垂直方向---------------
 */
public class initDecoration extends RecyclerView.ItemDecoration
{
    private Drawable drawable;

    public initDecoration(Drawable drawable)
    {
        this.drawable = drawable;
    }

    /**
     * itemView绘制之后绘制，这部分UI盖在itemView上面
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state)
    {
        super.onDrawOver(c, parent, state);
        drawVertical(c, parent);
    }

    private void drawVertical(Canvas c, RecyclerView parent)
    {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        for (int i = 0,len = parent.getChildCount();i<len;i++)
        {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom()+lp.bottomMargin;
            int bottom = top+drawable.getIntrinsicHeight();
            drawable.setBounds(left,top,right,bottom);
            drawable.draw(c);
        }
    }

    /**
     * 设置itemView上下左右的间距
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
    {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0,0,0,drawable.getIntrinsicHeight());
    }
}
