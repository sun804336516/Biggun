package com.biggun.drawbitmapmesh.Activity;

import android.animation.Animator;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;

import com.biggun.drawbitmapmesh.R;
import com.biggun.drawbitmapmesh.Util.Utils;
import com.biggun.drawbitmapmesh.photoview.PhotoView;

public class ScaleImgActivity extends BaseActivity
{
    String path;
    PhotoView img;
    Bitmap bit;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale_img);
        InitViews();
        InitDatas();
        InitListeners();
    }

    @Override
    protected void InitViews()
    {
        img = findView(R.id.scale_img);
    }

    @Override
    protected void InitDatas()
    {
        path = getIntent().getStringExtra("imgpath");
        bit = Utils.compressBitmap(path,this);
        img.setImageBitmap(bit);
//        mImageLoader.loadBitmap(path,img);
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
    protected void onDestroy()
    {
        super.onDestroy();
        if(bit != null && !bit.isRecycled())
        {
            bit.recycle();
        }
    }

    @Override
    protected boolean OnToolbarItemClick(MenuItem item)
    {
        return false;
    }
}
