package com.biggun.drawbitmapmesh.Activity;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;

import com.biggun.drawbitmapmesh.R;

public class VectorDrawableActivity extends BaseActivity implements View.OnClickListener
{
    private ImageView vectImg, vectImg2, vectImg3, vectHeartImg, trimEndImg;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vector_drawable);

        initToolBar("VectorDrawable");
        InitViews();
        InitDatas();
        InitListeners();

    }

    @Override
    protected void InitViews()
    {
        vectImg = findImageView(R.id.vector_img);
        vectImg2 = findImageView(R.id.vector_img2);
        vectImg3 = findImageView(R.id.vector_cloths);
        vectHeartImg = findImageView(R.id.vector_heart);
        trimEndImg = findImageView(R.id.vector_trimpathend);
    }

    @Override
    protected void InitDatas()
    {
        startDrawable(vectImg, R.drawable.vectoranimate, R.drawable.vector);
        startDrawable(vectHeartImg, R.drawable.heartanimate, R.drawable.heart);
        startDrawable(vectImg2, R.drawable.squareanimate, R.drawable.square);
        startDrawable(vectImg3, R.drawable.clothsanimate, R.drawable.cloths);
        startDrawable(trimEndImg, R.drawable.trimani, R.drawable.image_load_bitmap);
        collapseToolBar();
    }

    @Override
    protected void InitListeners()
    {
        vectImg.setOnClickListener(this);
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

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    private void closeDrawable(Drawable drawable)
    {
        if (drawable != null && drawable instanceof Animatable) {
            ((Animatable) drawable).stop();
        }
    }

    /**
     * StartVector动画
     * @param img 动画承载体
     * @param AnivecId 动画资源id
     * @param vecId 5.0之前Start动画失败设置的背景
     */
    private void startDrawable(ImageView img, int AnivecId, int vecId)
    {
        try {
            img.setImageDrawable(AnimatedVectorDrawableCompat.create(this, AnivecId));
            Drawable drawable = img.getDrawable();
            ((Animatable) drawable).start();
        } catch (Exception e) {
            e.printStackTrace();
            img.setImageDrawable(VectorDrawableCompat.create(getResources(), vecId, getTheme()));
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.vector_img:
                Animator reveal = ViewAnimationUtils.createCircularReveal(vectImg, 0, 0, 0, (float) Math.hypot(vectImg.getWidth(), vectImg.getHeight()));
                reveal.setDuration(1000);
                reveal.start();
                break;
        }
    }
}
