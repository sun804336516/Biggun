package com.biggun.drawbitmapmesh.Bean;

import android.graphics.drawable.Drawable;

import com.biggun.drawbitmapmesh.Util.Utils;

import java.text.Collator;

/**
 * 作者：孙贤武 on 2016/3/25 16:58
 * 邮箱：sun91985415@163.com
 * 应用流量Bean
 */
public class TrafficBean implements Comparable<TrafficBean>
{
    private String name;//应用name
    private long RxBytes;//下载流量
    private long TxBytes;//上传流量
    private Drawable drawable;//应用logo

    public TrafficBean(String name, long rxBytes, long txBytes, Drawable drawable)
    {
        this.name = name;
        RxBytes = rxBytes;
        TxBytes = txBytes;
        this.drawable = drawable;
    }

    public String getName()
    {
        return name;
    }

    public String getRxBytes()
    {
        return "下载的流量:"+ Utils.Formate(RxBytes);
    }

    public String getTxBytes()
    {
        return "上传的流量:"+Utils.Formate(TxBytes);
    }

    public Drawable getDrawable()
    {
        return drawable;
    }

    @Override
    public int compareTo(TrafficBean another)
    {
        return Collator.getInstance().compare(this.getName(),another.getName());
    }
}
