package com.biggun.yslibrary.Base;


import android.content.Intent;
import android.widget.Toast;

import com.biggun.yslibrary.Utils.LogUtils;

import java.util.LinkedList;

/**
 * Created by 孙贤武 on 2016/1/8.
 */
public enum ActivityManager
{
    Instances;
    private LinkedList<BIGGUN_Activity> activityList = new LinkedList<>();

    public static ActivityManager getInstance()
    {
        return Instances;
    }

    public void addAc(BIGGUN_Activity activity)
    {
        if (activity == null) {
            return;
        }
        activityList.add(activity);
    }

    public void remmoveAc(BIGGUN_Activity activity)
    {
        if (activity == null || !activityList.contains(activity)) {
            return;
        }
        activityList.remove(activity);
        activity = null;
    }

    public BIGGUN_Activity getCurrentActivity()
    {
        BIGGUN_Activity activity = null;
        if (!activityList.isEmpty()) {
            activity = activityList.getLast();
        }
        return activity;
    }

    public void ExitAll()
    {
        for (BIGGUN_Activity activity : activityList) {
            LogUtils.LogE(activity.toString());
            activity.finish();
        }
        System.exit(1);
    }

    /**
     * 普通跳转
     *
     * @param cls
     */
    public void startActivity(Class<? extends BIGGUN_Activity> cls)
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
    public void startActivity(Intent intent, Class<? extends BIGGUN_Activity> cls)
    {
        BIGGUN_Activity currentActivity = getCurrentActivity();
        intent.setClass(currentActivity, cls);
        currentActivity.startActivity(intent);
//        currentActivity.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    public void Toast(String msg)
    {
        Toast.makeText(getCurrentActivity(), msg, Toast.LENGTH_SHORT).show();
    }

}
