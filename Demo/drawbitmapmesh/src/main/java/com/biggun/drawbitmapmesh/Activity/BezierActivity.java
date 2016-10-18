package com.biggun.drawbitmapmesh.Activity;

import android.animation.Animator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.biggun.drawbitmapmesh.R;
import com.biggun.drawbitmapmesh.Util.Utils;
import com.biggun.drawbitmapmesh.View.BezierView;
import com.biggun.drawbitmapmesh.View.ClipPathView;
import com.biggun.drawbitmapmesh.View.OvalBezierView;

public class BezierActivity extends BaseActivity implements View.OnClickListener
{
    private BezierView bezierView;
    private ClipPathView mClipPathView;
    OvalBezierView mOvalBezierView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier);
        initToolBar("贝塞尔曲线");
        collapseToolBar();
        InitViews();
        InitDatas();
        InitListeners();

        //无关计算
        float densityDpi = getResources().getDisplayMetrics().densityDpi;
        float density = getResources().getDisplayMetrics().density;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mybrother);
        Utils.LogE(density + "---" + densityDpi + "bitmap占用内存：" + bitmap.getByteCount());
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float v = width * height * 4;//注意 width*=densityDpi/drawable所在目录的dpi
        Utils.LogE("计算内存：" + v);
    }

    @Override
    protected void InitViews()
    {
        bezierView = findView(R.id.bezier_view);
        mClipPathView = findView(R.id.clip_view);
        mOvalBezierView = findView(R.id.oval_view);
    }

    @Override
    protected void InitDatas()
    {
    }


    @Override
    protected void InitListeners()
    {
        mClipPathView.setOnClickListener(this);
        mOvalBezierView.setClickable(true);
        mOvalBezierView.setOnClickListener(this);
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

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.clip_view:
//                Toast.makeText(this, "toast", Toast.LENGTH_SHORT).show();
                break;
            case R.id.oval_view:
                Toast.makeText(this, "oval", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
