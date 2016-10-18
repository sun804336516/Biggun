package com.biggun.yslibrary.Utils;

import android.util.Log;

/**
 * 作者：Administrator on 2016/6/12 10:48
 * 邮箱：sun91985415@163.com
 */
public class LogUtils
{
    private static final boolean isDebuge = false;
    private static final String TAG = "==";
    public static void LogE(String str)
    {
        if(isDebuge)
        {
            return;
        }
        Log.e(TAG,str);
    }
}
