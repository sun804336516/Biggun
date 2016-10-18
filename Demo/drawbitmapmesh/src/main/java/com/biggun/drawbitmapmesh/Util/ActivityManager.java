package com.biggun.drawbitmapmesh.Util;


import android.content.Intent;
import android.widget.Toast;

import com.biggun.drawbitmapmesh.Activity.BaseActivity;

import java.util.LinkedList;
import java.util.Stack;

/**
 * Created by 孙贤武 on 2016/1/8.
 */
public enum ActivityManager
{
    Instances;
    private LinkedList<BaseActivity> activityList = new LinkedList<>();

    public static ActivityManager getInstance()
    {
        return Instances;
    }

    public void addAc(BaseActivity activity)
    {
        if (activity == null) {
            return;
        }
        activityList.add(activity);
    }

    public void remmoveAc(BaseActivity activity)
    {
        if (activity == null || !activityList.contains(activity)) {
            return;
        }
        activityList.remove(activity);
        activity = null;
    }

    public BaseActivity getCurrentActivity()
    {
        BaseActivity activity = null;
        if (!activityList.isEmpty()) {
            activity = activityList.getLast();
        }
        return activity;
    }

    public void ExitAll()
    {
        for (BaseActivity activity : activityList) {
            Utils.LogE(activity.toString());
            activity.finish();
        }
        System.exit(1);
//        while(true)
//        {
//            BaseActivity activity = getCurrentActivity();
//            if(activity == null)
//            {
//                break;
//            }
//            activity.finish();
//            remmoveac(activity);
//        }
    }

    /**
     * 普通跳转
     *
     * @param cls
     */
    public void startActivity(Class<? extends BaseActivity> cls)
    {
        Intent intent = new Intent();
        startActivity(intent, cls);

    }

    /**
     * 自定义Intent跳转
     *
     * @param intent
     * @param cls
     */
    public void startActivity(Intent intent, Class<? extends BaseActivity> cls)
    {
        BaseActivity currentActivity = getCurrentActivity();
        intent.setClass(currentActivity, cls);
        currentActivity.startActivity(intent);
//        currentActivity.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    public void Toast(String msg)
    {
        Toast.makeText(getCurrentActivity(), msg, Toast.LENGTH_SHORT).show();
    }

}
