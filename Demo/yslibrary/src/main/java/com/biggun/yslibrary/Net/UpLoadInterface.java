package com.biggun.yslibrary.Net;

/**
 * 作者：孙贤武 on 2016/5/4 15:07
 * 邮箱：sun91985415@163.com
 * 上传图片的进度回调
 */
public interface UpLoadInterface
{
    void UpLoad(long current, long length, boolean done);
}
