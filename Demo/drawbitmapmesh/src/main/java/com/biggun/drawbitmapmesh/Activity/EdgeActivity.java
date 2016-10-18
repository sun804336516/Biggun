package com.biggun.drawbitmapmesh.Activity;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.biggun.drawbitmapmesh.Adapter.myPagerAdapter;
import com.biggun.drawbitmapmesh.R;
import com.biggun.drawbitmapmesh.Util.ActivityManager;
import com.biggun.drawbitmapmesh.Util.Utils;
import com.biggun.drawbitmapmesh.View.BezierView2;
import com.biggun.drawbitmapmesh.View.EdGeViewPager;

import java.util.ArrayList;
import java.util.List;

public class EdgeActivity extends BaseActivity
{
    private EdGeViewPager viewPager;
    private myPagerAdapter adapter;
    private int[] imgs = {R.mipmap.test1,R.mipmap.test2,R.mipmap.test3,R.mipmap.test4,R.mipmap.tree,R.mipmap.test6};
    private List<ImageView> list = new ArrayList<>();
    private BezierView2 bezierView2;
    private List<View> views;


    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs)
    {
        useSlide = false;
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        InitViews();
        InitDatas();
        InitListeners();
    }

    @Override
    protected void InitViews()
    {
        viewPager = (EdGeViewPager) this.findViewById(R.id.edgeViewpager);
        viewPager.setOffscreenPageLimit(imgs.length);
        bezierView2 = findView(R.id.edge_bezierview);
    }

    @Override
    protected void InitDatas()
    {
        ImageView img = null;
        for (int id:imgs)
        {
            img = new ImageView(this);
            img.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),id),300,300,true));
            list.add(img);
        }
        adapter = new myPagerAdapter(list);
        viewPager.setAdapter(adapter);
        viewPager.setChildCount(imgs.length);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(0xFFFF0000);
        colors.add(0xFF00FF00);
        colors.add(0xFF0000FF);
        colors.add(0xFFFF00FF);
        colors.add(0xFF00ffFF);
        colors.add(0xFF98acFF);
        bezierView2.setColors(colors);
        bezierView2.setPagerCount(list.size());
        bezierView2.setMode(BezierView2.MODE_NORMAL);

//        13601079998
    }

    @Override
    protected void InitListeners()
    {
        viewPager.setEdgeListener(new EdGeViewPager.EdgeListener()
        {
            @Override
            public void edge(int edge)
            {
                switch (edge)
                {
                    case EdGeViewPager.LEFT:
                        Utils.LogE("返回上一界面");
                        finish();
                        break;
                    case EdGeViewPager.RIGHT:
                        Utils.LogE("进入下一界面");
                        ActivityManager.getInstance().startActivity(MeshViewActivity.class);
                        break;
                }
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                bezierView2.setPositionAndOffset(position,positionOffset);
            }

            @Override
            public void onPageSelected(int position)
            {

            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

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
