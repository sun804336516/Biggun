package com.biggun.drawbitmapmesh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.biggun.drawbitmapmesh.Activity.BaseActivity;

/**
 * 作者：孙贤武 on 2016/3/1 15:32
 * 邮箱：sun91985415@163.com
 */
public class InitBean
{
    private String name;
    private Class cls;

    public InitBean()
    {
    }

    public InitBean(String name, Class cls)
    {
        this.name = name;
        this.cls = cls;
    }

    public String getName()
    {
        return name;
    }

    public Class getCls()
    {
        return cls;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setCls(Class cls)
    {
        this.cls = cls;
    }
}
