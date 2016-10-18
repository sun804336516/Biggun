package com.biggun.yslibrary.Base;


import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * 作者：Administrator on 2016/6/13 11:43
 * 邮箱：sun91985415@163.com
 */
public class BIGGUN_Handler<T> extends Handler
{
    protected WeakReference<T> mWeakReference;

    public BIGGUN_Handler(T t)
    {
        mWeakReference = new WeakReference<T>(t);
    }

    @Override
    public void handleMessage(Message msg)
    {
        super.handleMessage(msg);
    }
}
