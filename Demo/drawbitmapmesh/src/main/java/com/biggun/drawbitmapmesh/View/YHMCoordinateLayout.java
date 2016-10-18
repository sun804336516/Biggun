package com.biggun.drawbitmapmesh.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.biggun.drawbitmapmesh.R;
import com.biggun.drawbitmapmesh.Util.Utils;

/**
 * 作者：孙贤武 on 2016/5/9 13:52
 * 邮箱：sun91985415@163.com
 */
public class YHMCoordinateLayout extends ListView
{
    public YHMCoordinateLayout(Context context)
    {
        this(context, null);
    }

    public YHMCoordinateLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public YHMCoordinateLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }
    LinearLayout head1,head2,head3;
    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        head1 = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.header1, null);
        head2 = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.header2, null);
        head3 = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.header2, null);
        measureView(head3);
//        setPaddingTop(-YHMHeight >> 1);
        addHeaderView(head1);
        addHeaderView(head2);
        setOnScrollListener(new AbsListView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
                Utils.LogE("========" + firstVisibleItem);
                if (firstVisibleItem == 1) {
                    if (head3.getParent() == null) {
                        ((ViewGroup)getParent()).addView(head3);
                    }
                }
                if (firstVisibleItem == 0)
                {
                    ((ViewGroup)getParent()).removeView(head3);
                }
            }
        });
    }

    private void measureView(View view)
    {
        ViewGroup.LayoutParams p = view.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int width = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int height;
        int tempHeight = p.height;
        if (tempHeight > 0) {
            height = View.MeasureSpec.makeMeasureSpec(tempHeight,
                    View.MeasureSpec.EXACTLY);
        } else {
            height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        }
        view.measure(width, height);
    }
}
