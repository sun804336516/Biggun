package com.biggun.drawbitmapmesh.Bean;

/**
 * 作者：孙贤武 on 2016/2/18 11:23
 * 邮箱：sun91985415@163.com
 */
public class TestDatabaseBean
{
    String name;
    String address;

    public TestDatabaseBean()
    {
    }

    public TestDatabaseBean(String name, String address)
    {
        this.name = name;
        this.address = address;
    }

    public String getName()
    {
        return name;
    }

    public String getAddress()
    {
        return address;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    @Override
    public String toString()
    {
        return name+"==="+address;
    }
}
