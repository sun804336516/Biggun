package com.biggun.drawbitmapmesh.Activity;

import android.animation.Animator;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.biggun.drawbitmapmesh.R;
import com.biggun.drawbitmapmesh.View.BIGGUN_ClipImageView;

public class ClipImgActivity extends BaseActivity
{
    ImageView showclipImg;
    BIGGUN_ClipImageView clipImg;
    Button clipBtn, checkBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_img);
        InitViews();
        InitDatas();
        InitListeners();
    }

    @Override
    protected void InitViews()
    {
        showclipImg = findView(R.id.clip_show);
        clipBtn = findView(R.id.clip_btn);
        checkBtn = findView(R.id.clip_btn2);
        clipImg = findView(R.id.clip_img);
    }

    @Override
    protected void InitDatas()
    {

    }

    @Override
    protected void InitListeners()
    {
        clipBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Bitmap bitmap = clipImg.getBitmap();
                showclipImg.setImageBitmap(bitmap);
            }
        });
        checkBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                clipImg.setClipType(clipImg.getClipType() == BIGGUN_ClipImageView.TYPE_CIRCLE ? BIGGUN_ClipImageView.TYPE_RECT
                        : BIGGUN_ClipImageView.TYPE_CIRCLE);
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
