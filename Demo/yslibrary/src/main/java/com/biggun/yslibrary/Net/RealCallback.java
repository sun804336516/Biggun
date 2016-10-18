package com.biggun.yslibrary.Net;

import java.util.List;

/**
 * 作者：孙贤武 on 2016/5/5 18:13
 * 邮箱：sun91985415@163.com
 */
public interface RealCallback<T>
{
    public void LoadError(String error, String code);

    public void LoadString(String response);

    public void LoadT(String response, T t);

    public void LoadList(String response, List<T> list);
}
