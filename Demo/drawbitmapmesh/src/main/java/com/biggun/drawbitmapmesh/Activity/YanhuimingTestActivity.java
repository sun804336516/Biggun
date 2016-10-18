package com.biggun.drawbitmapmesh.Activity;

import android.animation.Animator;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.biggun.drawbitmapmesh.R;
import com.biggun.drawbitmapmesh.View.YHMCoordinateLayout;

import java.util.ArrayList;
import java.util.List;

public class YanhuimingTestActivity extends BaseActivity
{
    YHMCoordinateLayout mListView;
    List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yanhuiming_test);
        InitViews();
        InitDatas();
        InitListeners();
    }

    @Override
    protected void InitViews()
    {
        mListView = findView(R.id.list);
        for (int i = 0; i < 100; i++) {
            list.add("测试" + i);
        }
    }

    @Override
    protected void InitDatas()
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.yhmlayout, R.id.yhmtv, list);
        mListView.setAdapter(adapter);
    }

    @Override
    protected void InitListeners()
    {
    }

    @Override
    protected void onToolBarAnimationEnd(Animator animator)
    {

    }

    @Override
    protected boolean OnToolbarItemClick(MenuItem item)
    {
        return false;
    }


    /**
     * 通知父布局，占用的宽，高；
     */
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
