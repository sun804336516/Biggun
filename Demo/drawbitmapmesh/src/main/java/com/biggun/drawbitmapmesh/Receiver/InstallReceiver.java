package com.biggun.drawbitmapmesh.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.biggun.drawbitmapmesh.Adapter.BrowserAdapter;
import com.biggun.drawbitmapmesh.Util.Utils;

/**
 * 作者：孙贤武 on 2016/2/24 16:21
 * 邮箱：sun91985415@163.com
 * 监听其他程序安装/卸载
 */
public class InstallReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        String dataString = intent.getDataString();
        switch (intent.getAction()) {
            case Intent.ACTION_PACKAGE_ADDED:
                Utils.LogE("addApk:"+dataString);
                break;
            case Intent.ACTION_PACKAGE_REMOVED:
                Utils.LogE("removedApk:"+dataString);
                break;
        }
    }
}
