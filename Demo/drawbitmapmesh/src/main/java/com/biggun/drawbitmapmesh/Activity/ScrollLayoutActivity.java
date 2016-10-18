package com.biggun.drawbitmapmesh.Activity;

import android.animation.Animator;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.biggun.drawbitmapmesh.Interface.ScrollLayoutListener;
import com.biggun.drawbitmapmesh.R;
import com.biggun.drawbitmapmesh.Util.Utils;
import com.biggun.drawbitmapmesh.View.ScrollLayout;

public class ScrollLayoutActivity extends BaseActivity
{
    ScrollLayout scrollLayout;
    TextView percentTv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_layout);
        InitViews();
        InitDatas();
        InitListeners();
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs)
    {
        useSlide = false;
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected void InitViews()
    {
        scrollLayout = findView(R.id.scrolllayout);
        percentTv = findTextView(R.id.percenttv);
    }

    @Override
    protected void InitDatas()
    {

    }

    @Override
    protected void InitListeners()
    {
        scrollLayout.setScrollLayoutListener(new ScrollLayoutListener()
        {
            @Override
            public void onScrollChanged(int curretIndex, float percent)
            {
                percentTv.setText("当前页："+curretIndex+"滑动进度："+percent);
            }
        });
    }

    @Override
    protected boolean OnToolbarItemClick(MenuItem item)
    {
        return false;
    }

    @Override
    protected void onToolBarAnimationEnd(Animator animator)
    {

    }
}
