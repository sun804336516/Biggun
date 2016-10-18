package com.biggun.drawbitmapmesh.Activity;

import android.animation.Animator;
import android.os.Bundle;
import android.view.MenuItem;

import com.biggun.drawbitmapmesh.R;

public class MeshViewActivity extends BaseActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesh_view);
        initToolBar("MeshView");
        InitViews();
        InitDatas();
        InitListeners();
//        UpDownMeshView view1 = new UpDownMeshView(this);
//        setContentView(view1);
//        int i = 600 / 0;//测试CrashHandler

    }

    @Override
    protected void InitViews()
    {
//        MeshView view = (MeshView) this.findViewById(R.id.meshview);
//        view.setBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.test1));
    }

    @Override
    protected void InitDatas()
    {
        collapseToolBar();
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
