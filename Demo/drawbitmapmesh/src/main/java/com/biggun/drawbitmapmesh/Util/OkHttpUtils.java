package com.biggun.drawbitmapmesh.Util;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 作者：孙贤武 on 2016/3/28 16:05
 * 邮箱：sun91985415@163.com
 */
public class OkHttpUtils
{
    private OkHttpClient okHttpClient;
    public OkHttpUtils()
    {
        okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(120, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(120, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(120, TimeUnit.SECONDS);
    }
    public void okGet(String url,String header,String headerValue, final OnHttp onHttp)
    {
        final Request request = new Request.Builder().url(url).addHeader(header, headerValue).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback()
        {
            @Override
            public void onFailure(Request request, IOException e)
            {
                onHttp.onFailure(request,e);
            }

            @Override
            public void onResponse(Response response) throws IOException
            {
                onHttp.onResponse(response);
            }
        });
    }
    public abstract class OnHttp
    {
        protected abstract void onFailure(Request request, IOException e);
        protected abstract void onResponse(Response response) throws IOException;
    }
}
