package com.biggun.yslibrary.View.RecyclerView;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * 作者：孙贤武 on 2016/6/21 14:16
 * 邮箱：sun91985415@163.com
 * LIKE:YANSHUO
 * RecyclerView的item点击
 */
public abstract class OnitemClickListener implements RecyclerView.OnItemTouchListener
{
    GestureDetectorCompat mMyDetector;
    RecyclerView mRecyclerView;

    public OnitemClickListener(RecyclerView recyclerView)
    {
        mRecyclerView = recyclerView;
        mMyDetector = new GestureDetectorCompat(recyclerView.getContext(), new MyDetectorListenr());
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e)
    {
        mMyDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e)
    {
        mMyDetector.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept)
    {
    }

    class MyDetectorListenr extends GestureDetector.SimpleOnGestureListener
    {
        @Override
        public boolean onSingleTapUp(MotionEvent e)
        {
            View childViewUnder = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            if (childViewUnder != null) {
                onItemClick(childViewUnder,mRecyclerView.findContainingViewHolder(childViewUnder));
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e)
        {
            View childViewUnder = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            if (childViewUnder != null) {
                onItemLongClick(childViewUnder,mRecyclerView.findContainingViewHolder(childViewUnder));
            }
            super.onLongPress(e);
        }
    }

    public abstract void onItemClick(View view,RecyclerView.ViewHolder holder);

    public abstract void onItemLongClick(View view,RecyclerView.ViewHolder holder);
}
