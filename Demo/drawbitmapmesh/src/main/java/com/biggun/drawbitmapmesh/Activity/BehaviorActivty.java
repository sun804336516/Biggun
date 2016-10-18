package com.biggun.drawbitmapmesh.Activity;

import android.animation.Animator;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.biggun.drawbitmapmesh.R;

public class BehaviorActivty extends BaseActivity implements View.OnClickListener
{
    private TextView behaviorDependencyTv;
    private TextView behaviorChildTv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_behavior_activty);
        initToolBar("Behavior");
        InitViews();
        InitDatas();
        InitListeners();
    }

    @Override
    protected void InitViews()
    {
        behaviorDependencyTv = (TextView) findViewById(R.id.behavior_dependency);
        behaviorChildTv = (TextView) findViewById(R.id.behavior_child);
    }

    @Override
    protected void InitDatas()
    {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onToolBarAnimationEnd(Animator animator)
    {

    }

    @Override
    protected void InitListeners()
    {
        behaviorDependencyTv.setOnClickListener(this);
        behaviorChildTv.setOnClickListener(this);
    }

    @Override
    protected boolean OnToolbarItemClick(MenuItem item)
    {
        return false;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.behavior_dependency:
                ViewCompat.offsetTopAndBottom(v,100);
                break;
        }
    }
}
