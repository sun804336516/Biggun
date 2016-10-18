package com.biggun.yslibrary.View.RecyclerView;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 作者：孙贤武 on 2016/3/10 14:43
 * 邮箱：sun91985415@163.com
 */
public class ImageGridDecoration extends RecyclerView.ItemDecoration
{
    private Drawable drawablebottom, drawableright;

    public ImageGridDecoration(Drawable drawablebottom, Drawable drawableright)
    {
        this.drawablebottom = drawablebottom;
        this.drawableright = drawableright;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state)
    {
        super.onDrawOver(c, parent, state);
        drawbottom(c, parent);
        drawright(c, parent);
    }

    private void drawright(Canvas c, RecyclerView parent)
    {
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();

        for (int i = 0, len = parent.getChildCount(); i < len; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = child.getRight()+params.rightMargin;
            int right = left+drawableright.getIntrinsicWidth();
            drawableright.setBounds(left,top,right,bottom);
            drawableright.draw(c);
        }
    }

    private void drawbottom(Canvas c, RecyclerView parent)
    {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        for (int i = 0, len = parent.getChildCount(); i < len; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = layoutParams.bottomMargin + child.getBottom();
            int bottom = top + drawablebottom.getIntrinsicHeight();
            drawablebottom.setBounds(left, top, right, bottom);
            drawablebottom.draw(c);
        }
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
    {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0, 0, drawableright.getIntrinsicWidth(), drawablebottom.getIntrinsicHeight());
    }
}
