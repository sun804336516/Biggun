package com.biggun.drawbitmapmesh.Activity;

import android.animation.Animator;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.biggun.drawbitmapmesh.Adapter.Vp_Rcv_Adapter;
import com.biggun.drawbitmapmesh.R;
import com.biggun.drawbitmapmesh.Util.Utils;
import com.biggun.drawbitmapmesh.View.ScrollViewPager;
import com.biggun.drawbitmapmesh.View.VpRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAndRecyclerViewActivity extends BaseActivity
{
    ScrollViewPager mScrollViewPager;
    Vp_Rcv_Adapter mVp_rcv_adapter;
    List<Integer> mList = new ArrayList<>();
    List<VpRecyclerView> mViewList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_and_recycler_view);
        InitViews();
        InitDatas();
        InitListeners();
    }

    @Override
    protected void InitViews()
    {
        mScrollViewPager = findView(R.id.Scrolll_viewpager);
    }

    @Override
    protected void InitDatas()
    {
        mList.add(R.mipmap.test1);
        mList.add(R.mipmap.test2);
        mList.add(R.mipmap.test3);
        mList.add(R.mipmap.test4);
        mList.add(R.mipmap.test5);
        mList.add(R.mipmap.test6);
        mVp_rcv_adapter = new Vp_Rcv_Adapter(mList, this, R.layout.vp_recyclerview_layout);

        VpRecyclerView inflate = (VpRecyclerView) LayoutInflater.from(this).inflate(R.layout.vp_recyclerview, null);
        inflate.setAdapter(mVp_rcv_adapter);
        mViewList.add(inflate);
        mViewList.add((VpRecyclerView) LayoutInflater.from(this).inflate(R.layout.vp_recyclerview, null));
        mViewList.add((VpRecyclerView) LayoutInflater.from(this).inflate(R.layout.vp_recyclerview, null));
    }

    @Override
    protected void InitListeners()
    {
        mScrollViewPager.setAdapter(new PagerAdapter()
        {
            @Override
            public int getCount()
            {
                return mViewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object)
            {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object)
            {
                container.removeView(mViewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position)
            {
                VpRecyclerView vpRecyclerView = mViewList.get(position);
                container.addView(vpRecyclerView);
                return vpRecyclerView;
            }
        });
        mScrollViewPager.setCurrentItem(0);
        mScrollViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                Utils.LogE("onPageScrolled:" + position);
            }

            @Override
            public void onPageSelected(int position)
            {
                Utils.LogE("onPageSelected:" + position);
                VpRecyclerView vpRecyclerView = mViewList.get(position);
                if (vpRecyclerView.getAdapter() == null) {
                    vpRecyclerView.setAdapter(mVp_rcv_adapter);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });
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
}
