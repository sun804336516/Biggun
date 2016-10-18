package com.biggun.drawbitmapmesh.Activity;

import android.animation.Animator;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.biggun.drawbitmapmesh.R;
import com.biggun.drawbitmapmesh.View.PathView;

import java.util.Timer;
import java.util.TimerTask;

public class CanvasActivity extends BaseActivity
{
    PathView pathView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);

        InitViews();
        InitDatas();
        InitListeners();
    }

    @Override
    protected void InitViews()
    {
        pathView = findView(R.id.pathview);
    }

    @Override
    protected void InitDatas()
    {
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask()
//        {
//            @Override
//            public void run()
//            {
//                runOnUiThread(new Runnable()
//                {
//                    @Override
//                    public void run()
//                    {
//                        pathView.startAnimation();
//                    }
//                });
//            }
//        }, 500, 1000);
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
