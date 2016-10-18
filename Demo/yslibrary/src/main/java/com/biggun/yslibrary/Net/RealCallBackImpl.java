package com.biggun.yslibrary.Net;

import com.biggun.yslibrary.Utils.Utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 作者：孙贤武 on 2016/5/10 12:25
 * 邮箱：sun91985415@163.com
 * 空实现RealCallBack
 */
public class RealCallBackImpl<T> implements RealCallback<T>
{
    private Class<T> clazz;

    public RealCallBackImpl()
    {
        Class clazz = getClass();
        while (clazz != Object.class) {
            Type t = clazz.getGenericSuperclass();
            if (t instanceof ParameterizedType) {
                Type[] args = ((ParameterizedType) t).getActualTypeArguments();
                if (args[0] instanceof Class) {
                    this.clazz = (Class<T>) args[0];
                    break;
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    @Override
    public void LoadError(String error, String code)
    {
    }

    @Override
    public void LoadString(String response)
    {

    }

    @Override
    public void LoadT(String response, T t)
    {

    }

    @Override
    public void LoadList(String response, List<T> list)
    {

    }

    public Class<T> getClazz()
    {
        Utils.LogE("clazz:" + clazz);
        return clazz;
    }
}
