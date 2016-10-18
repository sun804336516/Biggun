package com.biggun.yslibrary.Base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;

import com.biggun.yslibrary.Utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 孙贤武 on 2016/1/7.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler
{
    private static final String message = "程序发生未知错误，正在退出！";
    private HashMap<String, String> infos = new HashMap<>();
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
    private SimpleDateFormat formatter = null;
    private String pathname;

    private static CrashHandler crashHandler;

    private CrashHandler()
    {

    }

    public static CrashHandler getinstance()
    {
        if (crashHandler == null) {
            synchronized (CrashHandler.class) {
                if (crashHandler == null) {
                    crashHandler = new CrashHandler();
                }
            }
        }
        return crashHandler;
    }

    public void init()
    {
        formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        pathname = System.currentTimeMillis() + "-" + formatter.format(new Date()) + ".txt";
        uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex)
    {
        if (!handleException(ex) && uncaughtExceptionHandler != null) {
            uncaughtExceptionHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            但是如果不是在第一个 Activity 中调用，如 ActivityA 启动 ActivityB ，你在 ActivityB 中调用
//            Process.killProcess 或 System.exit(0) 当前进程确实也被 kill 掉了，但 app 会重新启动，
//            android.os.Process.killProcess(android.os.Process.myPid());
//            0正常退出，非0非正常退出
//            System.exit(1);
            ActivityManager.getInstance().ExitAll();
        }
    }

    private boolean handleException(Throwable throwable)
    {
        if (throwable == null) {
            return false;
        }
        collectionDevicesinfo();
        saveLogInfo(throwable);
        new Thread()
        {
            @Override
            public void run()
            {
                super.run();
                Looper.prepare();
//                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityManager.getInstance().getCurrentActivity());
                AlertDialog alertDialog = builder.setTitle("程序发生未知错误").setMessage(message).setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        ActivityManager.getInstance().ExitAll();
                    }
                }).create();
//                alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);//值得注意的一句话
                alertDialog.show();
                Looper.loop();
            }
        }.start();

        return true;
    }

    /**
     * 保存日志到文件
     *
     * @param throwable
     */
    private void saveLogInfo(Throwable throwable)
    {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            builder.append(entry.getKey() + "=" + entry.getValue() + "\n");
        }

        Throwable cause = throwable.getCause();
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        while (cause != null) {
            cause.printStackTrace(pw);
            cause = cause.getCause();
        }
        pw.close();
        String result = writer.toString();
        builder.append(result);

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File dir = new File(Environment.getExternalStorageDirectory(), "孙贤武");
            File f = new File(dir, pathname);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);
                Utils.LogE(f.getAbsolutePath());
                fos.write(builder.toString().getBytes());
            } catch (Exception e) {
                Utils.LogE("write log error" + e.getMessage());
                e.printStackTrace();

            } finally {
                Utils.CloseIo(fos);
            }
        }
    }

    /**
     * 收集设备信息
     */
    private void collectionDevicesinfo()
    {

        PackageManager pm = ActivityManager.getInstance().getCurrentActivity().getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(ActivityManager.getInstance().getCurrentActivity().getPackageName(), PackageManager.GET_ACTIVITIES);
            int versionCode = info.versionCode;
            String versionName = info.versionName;
            infos.put("VersionName", versionName == null ? "null" : versionName);
            infos.put("VersionCode", versionCode + "");

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field f : fields) {
            try {
                f.setAccessible(true);
                infos.put(f.getName(), f.get(null).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
