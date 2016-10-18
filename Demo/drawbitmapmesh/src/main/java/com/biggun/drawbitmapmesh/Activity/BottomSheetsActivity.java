package com.biggun.drawbitmapmesh.Activity;

import android.animation.Animator;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.biggun.drawbitmapmesh.Adapter.BottomSheetAdapter;
import com.biggun.drawbitmapmesh.Bean.TestDatabaseBean;
import com.biggun.drawbitmapmesh.R;
import com.biggun.drawbitmapmesh.Util.Utils;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetsActivity extends BaseActivity
{
    private CoordinatorLayout coordinatorlayout;
    private RecyclerView recyelerview;
    private BottomSheetAdapter adapter;
    private List<TestDatabaseBean> list = new ArrayList<>();
    private BottomSheetBehavior bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_sheets);


        initToolBar("BottomSheets");
        InitViews();
        InitDatas();
        InitListeners();
    }

    @Override
    protected void InitViews()
    {
        coordinatorlayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);
        recyelerview = (RecyclerView) findViewById(R.id.recyelerview);
        recyelerview.setLayoutManager(new LinearLayoutManager(null, LinearLayoutManager.VERTICAL, false));
        bottomSheetBehavior = BottomSheetBehavior.from(recyelerview);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback()
        {
            @Override
            public void onStateChanged(View bottomSheet, int newState)
            {
                Utils.LogE("bottom_sheet_state:" + newState);
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset)
            {
                float scale = bottomSheetBehavior.getPeekHeight() * 1.0f / bottomSheet.getHeight();
                Utils.LogE("scale:" + scale + "-slideOffset:" + slideOffset);
//                bottomSheet.setScaleX(slideOffset);
//                bottomSheet.setScaleY(slideOffset);
            }
        });
    }

    @Override
    protected void InitDatas()
    {
        TestDatabaseBean testDatabaseBean = null;
        for (int i = 0; i < 30; i++) {
            testDatabaseBean = new TestDatabaseBean("孙贤武" + i, "中关村" + i);
            list.add(testDatabaseBean);
        }
        adapter = new BottomSheetAdapter(list, this, R.layout.bottomsheet_layout);
        recyelerview.setAdapter(adapter);
    }

    @Override
    protected void InitListeners()
    {

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
