package com.biggun.drawbitmapmesh;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;

import com.zhy.autolayout.config.AutoLayoutConifg;

/**
 * Created by 孙贤武 on 2016/1/8.
 */
public class MyApplication extends Application
{
//    private RefWatcher refWatcher;
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onCreate()
    {
        super.onCreate();
        AutoLayoutConifg.getInstance().useDeviceSize();
//        refWatcher = LeakCanary.install(this);
//        refWatcher.watch(this);
//        CrashHandler.getinstance().init(getApplicationContext());//测试可能会出现抓不到log
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks()
        {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState)
            {

            }

            @Override
            public void onActivityStarted(Activity activity)
            {

            }

            @Override
            public void onActivityResumed(Activity activity)
            {

            }

            @Override
            public void onActivityPaused(Activity activity)
            {

            }

            @Override
            public void onActivityStopped(Activity activity)
            {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState)
            {

            }

            @Override
            public void onActivityDestroyed(Activity activity)
            {

            }
        });
    }
//    public static RefWatcher getRefWatcher(Context context) {
//        MyApplication application = (MyApplication) context
//                .getApplicationContext();
//        return application.refWatcher;
//    }
}
