package com.biggun.drawbitmapmesh.Util;

import android.os.FileObserver;

/**
 * 作者：孙贤武 on 2016/2/24 15:48
 * 邮箱：sun91985415@163.com
 */
public class MyFileObserver extends FileObserver
{
    public MyFileObserver(String path)
    {
        super(path);
    }
    @Override
    public void onEvent(int event, String path)
    {
        switch (event) {
            case FileObserver.ALL_EVENTS:
                break;
            case FileObserver.CREATE:
                Utils.LogE("fileCreated!");
                break;
            case FileObserver.DELETE:
                Utils.LogE("fileDeleted!");
                break;
        }
    }
}
