package com.slt.sltca.widget;

import android.graphics.Rect;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.slt.sltca.R;
import com.slt.sltca.tools.LogUtils;

/**
 * 作者：孙贤武 on 2016/12/06 10:30
 * 邮箱：sun91985415@163.com
 * LIKE:YANSHUO
 * RecyclerView的item点击
 */
public abstract class OnitemClickListener implements RecyclerView.OnItemTouchListener
{
    private GestureDetectorCompat mMyDetector;
    private RecyclerView mRecyclerView;
    private String ChildClickTAG = null;

    public OnitemClickListener(RecyclerView recyclerView)
    {
        mRecyclerView = recyclerView;
        mMyDetector = new GestureDetectorCompat(recyclerView.getContext(), new MyDetectorListenr());
        ChildClickTAG = mRecyclerView.getContext()
                .getResources().getString(R.string.recyclerview_itemchild_onclick);
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
                if (childViewUnder instanceof ViewGroup) {
                    if (onChildClick(e.getRawX(), e.getRawY(), (ViewGroup) childViewUnder, mRecyclerView.findContainingViewHolder(childViewUnder))) {
                        return true;
                    } else {
                        onItemClick(childViewUnder, mRecyclerView.findContainingViewHolder(childViewUnder));
                    }
                } else {
                    onItemClick(childViewUnder, mRecyclerView.findContainingViewHolder(childViewUnder));
                }
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e)
        {
            View childViewUnder = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            if (childViewUnder != null) {
                onItemLongClick(childViewUnder, mRecyclerView.findContainingViewHolder(childViewUnder));
            }
            super.onLongPress(e);
        }
    }

    private boolean onChildClick(float x, float y, ViewGroup group, final RecyclerView.ViewHolder holder)
    {
        boolean returnis = false;
        Rect rect = new Rect();
        for (int i = 0, count = group.getChildCount(); i < count; i++) {
            final View view = group.getChildAt(i);
            view.getGlobalVisibleRect(rect);
            LogUtils.LOGE("rect:"+rect.toString()+"--"+x+"--"+y);
            if (view instanceof ViewGroup) {
                onChildClick(x, y, (ViewGroup) view, holder);
            } else {
                Object TAG = view.getTag();
                LogUtils.LOGE("tag1:" + ChildClickTAG);
                if (TAG != null) {
                    LogUtils.LOGE("tag2:" + TAG.toString());
                    if (TAG.toString().equals(ChildClickTAG) && view.isClickable() && rect.contains((int) x, (int) y)) {
                        LogUtils.LOGE("view_click");
                        returnis = onItemChildClick(view, view.getId(), holder);
                    }
                }
            }
        }
        return returnis;
    }

    public abstract void onItemClick(View view, RecyclerView.ViewHolder holder);

    public abstract void onItemLongClick(View view, RecyclerView.ViewHolder holder);

    public abstract boolean onItemChildClick(View view, int id, RecyclerView.ViewHolder holder);
}
