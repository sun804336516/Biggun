package biggun.yanshuo.picture;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Environment;

import com.alipay.euler.andfix.patch.PatchManager;
import com.biggun.yslibrary.Utils.LogUtils;
import com.zhy.autolayout.config.AutoLayoutConifg;

import java.io.File;
import java.io.IOException;

/**
 * 作者：Administrator on 2016/6/12 14:14
 * 邮箱：sun91985415@163.com
 */
public class MyApplication extends Application
{
    private PatchManager mPatchManager;
    private String pathName = "yanshuo.apatch";

    @Override
    public void onCreate()
    {
        super.onCreate();
        AutoLayoutConifg.getInstance().useDeviceSize();
//        CrashHandler.getinstance().init();
        mPatchManager = new PatchManager(this);
        mPatchManager.init("1.0");
        mPatchManager.loadPatch();
        try {
            File file = new File(Environment.getExternalStorageDirectory(), pathName);
            mPatchManager.addPatch(file.getAbsolutePath());
            LogUtils.LogE("loadpath-success");
            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.LogE("loadpath-error");
        }
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks()
        {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState)
            {
                LogUtils.LogE("activity-onActivityCreated:" + activity.getClass().getSimpleName());
            }

            @Override
            public void onActivityStarted(Activity activity)
            {
                LogUtils.LogE("activity-onActivityStarted:" + activity.getClass().getSimpleName());
            }

            @Override
            public void onActivityResumed(Activity activity)
            {
                LogUtils.LogE("activity-onActivityResumed:" + activity.getClass().getSimpleName());
            }

            @Override
            public void onActivityPaused(Activity activity)
            {
                LogUtils.LogE("activity-onActivityPaused:" + activity.getClass().getSimpleName());
            }

            @Override
            public void onActivityStopped(Activity activity)
            {
                LogUtils.LogE("activity-onActivityStopped:" + activity.getClass().getSimpleName());
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState)
            {
                LogUtils.LogE("activity-onActivitySaveInstanceState:" + activity.getClass().getSimpleName());
            }

            @Override
            public void onActivityDestroyed(Activity activity)
            {
                LogUtils.LogE("activity-onActivityDestroyed:" + activity.getClass().getSimpleName());
            }
        });
    }
}
