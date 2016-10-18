package com.biggun.drawbitmapmesh.Bean;

import java.io.File;
import java.text.Collator;
import java.util.Comparator;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.MenuItem;

import com.biggun.drawbitmapmesh.Adapter.AppListLoader;
import com.biggun.drawbitmapmesh.R;
import com.biggun.drawbitmapmesh.Util.Utils;

/**
 * @author 孙贤武
 */
public class AppEntry implements Comparable<AppEntry>
{
    private String mLabel;
    private Drawable mIcon;
    private long RxBytes;//下载流量
    private long TxBytes;//上传流量

    public String getmLabel()
    {
        return mLabel;
    }

    public Drawable getmIcon()
    {
        return mIcon;
    }

    public void setmLabel(String mLabel)
    {
        this.mLabel = mLabel;
    }

    public void setmIcon(Drawable mIcon)
    {
        this.mIcon = mIcon;
    }

    @Override
    public String toString()
    {
        return "AppEntry{" +
                "名称='" + mLabel + '\'' +
                '}';
    }
    public String getRxBytes()
    {
        return "下载的流量:"+ Utils.Formate(RxBytes);
    }

    public void setRxBytes(long rxBytes)
    {
        RxBytes = rxBytes;
    }

    public void setTxBytes(long txBytes)
    {
        TxBytes = txBytes;
    }

    public String getTxBytes()
    {
        return "上传的流量:"+Utils.Formate(TxBytes);
    }
    @Override
    public int compareTo(AppEntry another)
    {
        return Collator.getInstance().compare(this.getmLabel(),another.getmLabel());
    }
}