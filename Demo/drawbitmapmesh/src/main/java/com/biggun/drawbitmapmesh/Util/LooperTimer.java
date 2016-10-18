package com.biggun.drawbitmapmesh.Util;

import android.os.Handler;
import android.os.Message;

/**
 * 作者：孙贤武 on 2016/2/2 16:24
 * 邮箱：sun91985415@163.com
 * 有循环次数跟间隔时间的循环器
 */
public abstract class LooperTimer
{
    private long Interval;
    private int LooperCount;
    private thisHandler handler;
    private boolean isCancel = false;
    private static final int MSG = 1;

    public LooperTimer(long Interval, int looperCount)
    {
        this.Interval = Interval;
        this.LooperCount = looperCount;
        handler = new thisHandler();
    }

    public void start()
    {
        if (LooperCount <= 0) {
            onFinished();
            return;
        }
        isCancel = false;
        handler.sendMessage(handler.obtainMessage(MSG));
    }

    public void cancel()
    {
        onFinished();
        isCancel = true;
        handler.removeMessages(MSG);
    }

    private class thisHandler extends Handler
    {
        @Override
        public void dispatchMessage(Message msg)
        {
            super.dispatchMessage(msg);
            if (isCancel) {
                return;
            }

            if (LooperCount <= 0) {
                onFinished();
            } else {
                onTick(LooperCount);
                LooperCount--;
                sendMessageDelayed(handler.obtainMessage(MSG), Interval);
            }
        }
    }

    public abstract void onFinished();
    public abstract void onTick(int count);
}
