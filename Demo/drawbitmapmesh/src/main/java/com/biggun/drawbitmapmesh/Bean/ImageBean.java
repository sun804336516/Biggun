package com.biggun.drawbitmapmesh.Bean;

import android.net.Uri;

/**
 * 作者：孙贤武 on 2016/3/8 11:00
 * 邮箱：sun91985415@163.com
 */
public class ImageBean
{
    private int _id;
    private String path;
    private String name;
    private long size;
    private Uri uri;

    public ImageBean(int _id, String path, String name, long size, Uri uri)
    {
        this._id = _id;
        this.path = path;
        this.name = name;
        this.size = size;
        this.uri = uri;
    }

    public Uri getUri()
    {
        return uri;
    }

    public int get_id()
    {
        return _id;
    }

    public String getName()
    {
        return name;
    }

    public long getSize()
    {
        return size;
    }

    public String getPath()
    {
        return path;
    }
}
