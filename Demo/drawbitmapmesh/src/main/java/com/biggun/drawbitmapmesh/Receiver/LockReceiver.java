package com.biggun.drawbitmapmesh.Receiver;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 锁屏的Reciver
 * 作者：孙贤武 on 2016/1/27 13:13
 * 邮箱：sun91985415@163.com
 */
public class LockReceiver extends DeviceAdminReceiver
{
    @Override
    public void onEnabled(Context context, Intent intent)
    {
        super.onEnabled(context, intent);
    }

    @Override
    public void onDisabled(Context context, Intent intent)
    {
        super.onDisabled(context, intent);
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        super.onReceive(context, intent);
    }
}
