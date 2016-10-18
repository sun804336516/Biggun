package com.biggun.drawbitmapmesh.View;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.biggun.drawbitmapmesh.Util.Utils;

/**
 * 作者：孙贤武 on 2016/3/7 12:17
 * 邮箱：sun91985415@163.com
 */
public class SmothhRecyclerView extends RecyclerView
{
    private LinearLayoutManager layoutManager;
    private View firstView;
    private boolean canMove = false;
    private int toolbarSize = -1;
    private int moveIndex = -1;

    public SmothhRecyclerView(Context context)
    {
        this(context, null);
    }

    public SmothhRecyclerView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public SmothhRecyclerView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        layoutManager = (LinearLayoutManager) getLayoutManager();
        addOnScrollListener(new MyRcvListener());
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
    }

    public void smooth2position(int position, int toolbarSize)
    {
        if (layoutManager == null || !(getLayoutManager() instanceof LinearLayoutManager)) {
            return;
        }
        int firstposition = layoutManager.findFirstVisibleItemPosition();
        int lastposition = layoutManager.findLastVisibleItemPosition();
        moveIndex = position;
        this.toolbarSize = toolbarSize;
        Utils.LogE(position + "---" + firstposition + "---" + lastposition);
        if (position <= firstposition) {
            smoothScrollToPosition(position);
        } else if (position <= lastposition) {
            int index = getChildAt(position - firstposition).getTop();
            smoothScrollBy(0, index - toolbarSize);
        } else {
            smoothScrollToPosition(position);
            canMove = true;
        }
    }

    public class MyRcvListener extends RecyclerView.OnScrollListener
    {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState)
        {
            super.onScrollStateChanged(recyclerView, newState);
            if (layoutManager == null) {
                return;
            }
            if (canMove && newState == SCROLL_STATE_IDLE) {
                canMove = false;
                int index = moveIndex - layoutManager.findFirstVisibleItemPosition();
                if (0 <= index && index <= getChildCount()) {
                    smoothScrollBy(0, getChildAt(index).getTop() - toolbarSize);
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy)
        {
            super.onScrolled(recyclerView, dx, dy);
            if (layoutManager == null) {
                return;
            }
            if (canMove) {
                canMove = false;
                int index = moveIndex - layoutManager.findFirstVisibleItemPosition();
                if (0 <= index && index <= getChildCount()) {
                    smoothScrollBy(0, getChildAt(index).getTop() - toolbarSize);
                }
            }
            if (mRecyclerViewDown != null) {
                firstView = getChildAt(0);
                if (layoutManager.findFirstVisibleItemPosition() == 0) {
                    mRecyclerViewDown.down(firstView.getTop(),firstView.getHeight());
                } else {
                    mRecyclerViewDown.down(firstView.getHeight(),firstView.getHeight());
                }
            }
        }
    }

    RecyclerViewDown mRecyclerViewDown;

    /**
     * 获取到RecyclerView滑动距离的方法
     *
     * @param recyclerViewDown
     */
    public void setRecyclerViewDownDis(RecyclerViewDown recyclerViewDown)
    {
        mRecyclerViewDown = recyclerViewDown;
    }

    public abstract class RecyclerViewDown
    {
        protected abstract void down(float y,float height);
    }
}
