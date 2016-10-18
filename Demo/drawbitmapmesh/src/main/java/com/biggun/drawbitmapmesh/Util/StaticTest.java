package com.biggun.drawbitmapmesh.Util;

/**
 * 作者：孙贤武 on 2016/3/8 10:38
 * 邮箱：sun91985415@163.com
 * 静态内部类单例模式
 */
public class StaticTest
{
    private StaticTest()
    {

    }
    private static class Get
    {
        private static StaticTest test = new StaticTest();
    }

    public StaticTest getInstance()
    {
        return Get.test;
    }
}
