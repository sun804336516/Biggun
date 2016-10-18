package com.biggun.yslibrary.Net;

import android.os.Environment;
import android.support.annotation.IntDef;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.biggun.yslibrary.Utils.Utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 作者：孙贤武 on 2016/5/27 14:54
 * 邮箱：sun91985415@163.com
 */
public class OkUtils<T>
{
    private static final int LOCAL = 1;
    private static final int NET = 0;

    /**
     * 请求的类型
     */
    @IntDef({LOCAL, NET})
    private @interface TYPE
    {
    }

    private static final String app = "application/octet-stream";
    private OkHttpClient mOkHttpClient;
    private long cacheSize = 10 * 1024 * 1024;

    private OkUtils()
    {
        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(10 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(10 * 1000, TimeUnit.MILLISECONDS).writeTimeout(10 * 1000, TimeUnit.MILLISECONDS);
        // TODO: 2016/6/12 加上包名
        File cacheFile = new File(Environment.getExternalStorageDirectory()
                + File.separator + "Android"
                + File.separator + "data"
                + File.separator + "okcache");
        builder.cache(new Cache(cacheFile, cacheSize));
        mOkHttpClient = builder.build();
    }

    private static class GET
    {
        private static OkUtils sOkUtils = new OkUtils();
    }

    public static OkUtils getInstance()
    {
        return GET.sOkUtils;
    }

    /**
     * 构造Get请求的Builder
     *
     * @param url
     * @return
     */
    private Request.Builder getRequestBuilder(String url)
    {
        Request.Builder builder = new Request.Builder().get().url(url).cacheControl(CacheControl.FORCE_NETWORK);
        return builder;
    }

    /**
     * 构造Post请求的builer
     *
     * @param url
     * @param postMaps
     * @param upLoadInterface
     * @return
     */
    private Request.Builder postRequestBuilder(String url, HashMap<String, String> postMaps, UpLoadInterface upLoadInterface)
    {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : postMaps.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        Request.Builder builder1 = new Request.Builder().url(url)
                .post(upLoadInterface != null ? upProgress(builder.build(), upLoadInterface) : builder.build())
                .cacheControl(CacheControl.FORCE_NETWORK);
        return builder1;
    }

    /**
     * 构造分块Post请求的builder
     *
     * @param url
     * @param postMaps
     * @param fileMap
     * @param upLoadInterface
     * @return
     */
    private Request.Builder partPostRequestBuilder(String url, HashMap<String, String> postMaps, HashMap<String, File> fileMap, UpLoadInterface upLoadInterface)
    {
        MultipartBody.Builder mBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (Map.Entry<String, File> entry : fileMap.entrySet()) {
            mBuilder.addFormDataPart(entry.getKey(), entry.getValue().getName(), RequestBody.create(MediaType.parse(app), entry.getValue()));
        }
        for (Map.Entry<String, String> entry : postMaps.entrySet()) {
            mBuilder.addFormDataPart(entry.getKey(), entry.getValue());
        }
        Request.Builder builder = new Request.Builder().url(url)
                .post(upLoadInterface != null ? upProgress(mBuilder.build(), upLoadInterface) : mBuilder.build())
                .cacheControl(CacheControl.FORCE_NETWORK);
        return builder;
    }

    private RequestBody upProgress(RequestBody body, final UpLoadInterface upLoadInterface)
    {
        return new ProgressRequestBody(body)
        {
            @Override
            public void onRequestListener(long current, long length, boolean done)
            {
                if (upLoadInterface != null) {
                    upLoadInterface.UpLoad(current, length, done);
                }
            }
        };
    }

    private OkHttpClient downProgress(final DownLoadInterface downLoadInterface)
    {
        OkHttpClient.Builder builder = mOkHttpClient.newBuilder();
        builder.addNetworkInterceptor(new Interceptor()
        {
            @Override
            public Response intercept(Chain chain) throws IOException
            {
                Response response = chain.proceed(chain.request());
                return response.newBuilder().body(new ProgressResponseBody(response.body())
                {
                    @Override
                    public void onResponseListener(long current, long length, boolean done)
                    {
                        if (downLoadInterface != null) {
                            downLoadInterface.DownLoad(current, length, done);
                        }
                    }
                }).build();
            }
        });
        return builder.build();
    }

    //-----------------------------------------------------------


    /**
     * 普通的GET请求
     *
     * @param url
     * @param realCallBack
     */
    public void GET(final String url, final RealCallBackImpl<T> realCallBack)
    {
        Request.Builder builder = getRequestBuilder(url);
        Call call = mOkHttpClient.newCall(builder.build());
        ExcuteCall(call, builder, realCallBack);
    }

    /**
     * 异步下载
     *
     * @param url
     * @param downLoadInterface
     * @param fos
     * @param downLoadCallBack
     */
    public void DOWNLOAD(String url, DownLoadInterface downLoadInterface, final FileOutputStream fos, final DownLoadCallBack downLoadCallBack)
    {
        downProgress(downLoadInterface).newCall(getRequestBuilder(url).build()).enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                // TODO: 2016/5/27 下载失败
                downLoadCallBack.LoadError("下载失败：" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                BufferedInputStream bis = new BufferedInputStream(response.body().byteStream());
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = bis.read(bytes)) != -1) {
                    bos.write(bytes, 0, len);
                }
                bos.flush();
                bis.close();
                bos.close();
                downLoadCallBack.LoadSuccess();
            }
        });
    }

    /**
     * 下载到文件中
     *
     * @param url
     * @param file
     * @return
     */
    public boolean DOWNLOAD2OS(String url, String file)
    {
        try {
            return DOWNLOAD2OS(url, new File(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 下载到文件中
     *
     * @param url
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public boolean DOWNLOAD2OS(String url, File file) throws FileNotFoundException
    {
        if (!file.exists()) {
            file.mkdirs();
        }
        return DOWNLOAD2OS(url, new FileOutputStream(file));
    }

    /**
     * 下载文件到输出流中，同步
     *
     * @param url
     * @param os
     * @return
     */
    public boolean DOWNLOAD2OS(String url, OutputStream os)
    {
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        try {
            Response response = mOkHttpClient.newCall(getRequestBuilder(url).build()).execute();
            bis = new BufferedInputStream(response.body().byteStream(), 8 * 1024);
            bos = new BufferedOutputStream(os, 8 * 1024);
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = bis.read(bytes)) != -1) {
                bos.write(bytes, 0, len);
            }
            bos.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Utils.CloseIo(bis);
            Utils.CloseIo(bos);
        }
        return false;
    }


    /**
     * 普通的POST请求
     *
     * @param url
     * @param postMaps
     * @param realCallBack
     */
    public void POST(String url, HashMap<String, String> postMaps, RealCallBackImpl<T> realCallBack)
    {
        POST(url, postMaps, null, realCallBack);
    }

    /**
     * 有上传进度的POST请求
     *
     * @param url
     * @param postMaps
     * @param upLoadInterface
     * @param realCallBack
     */
    public void POST(final String url, final HashMap<String, String> postMaps, UpLoadInterface upLoadInterface, final RealCallBackImpl<T> realCallBack)
    {
        Request.Builder builder = postRequestBuilder(url, postMaps, upLoadInterface);
        Call call = mOkHttpClient.newCall(builder.build());
        ExcuteCall(call, builder, realCallBack);
    }

    /**
     * 普通的分块请求
     *
     * @param url
     * @param postMaps
     * @param fileMap
     * @param realCallBack
     */
    public void PARTPOST(String url, HashMap<String, String> postMaps, HashMap<String, File> fileMap, RealCallBackImpl<T> realCallBack)
    {
        PARTPOST(url, postMaps, fileMap, null, realCallBack);
    }

    /**
     * 带有上传进度的分块请求
     *
     * @param url
     * @param postMaps
     * @param fileMap
     * @param upLoadInterface
     * @param realCallBack
     */
    public void PARTPOST(final String url, final HashMap<String, String> postMaps, final HashMap<String, File> fileMap, UpLoadInterface upLoadInterface, final RealCallBackImpl<T> realCallBack)
    {
        Request.Builder builder = partPostRequestBuilder(url, postMaps, fileMap, upLoadInterface);
        Call call = mOkHttpClient.newCall(builder.build());
        ExcuteCall(call, builder, realCallBack);
    }

    /**
     * 处理一个Call请求
     *
     * @param call
     * @param builder
     * @param realCallBack
     */
    private void ExcuteCall(Call call, Request.Builder builder, final RealCallBackImpl<T> realCallBack)
    {
        final Request.Builder builder1 = builder;
        call.enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                call.cancel();
                CACHEGETORPOST(builder1.cacheControl(CacheControl.FORCE_CACHE), realCallBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                call.cancel();
                ExcuteCallback(NET, response.body().string(), realCallBack);
            }
        });
    }

    /**
     * 缓存中请求
     *
     * @param builder
     * @param realCallBack
     */
    private void CACHEGETORPOST(Request.Builder builder, final RealCallBackImpl<T> realCallBack)
    {
        mOkHttpClient.newCall(builder.build()).enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                call.cancel();
                // TODO: 2016/5/27 本地请求失败
                realCallBack.LoadError("请求失败" + e.getMessage(), CODE_LOCAL_GETERROR);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                call.cancel();
                ExcuteCallback(LOCAL, response.body().string(), realCallBack);
            }
        });
    }

    /**
     * 结果的处理
     *
     * @param type
     * @param response
     * @param realCallBack
     */
    private void ExcuteCallback(@TYPE int type, String response, RealCallBackImpl<T> realCallBack)
    {
        switch (type) {
            case NET:
                Utils.LogE("网络response:" + response);
                break;
            case LOCAL:
                Utils.LogE("本地response:" + response);
                break;
        }
        if (TextUtils.isEmpty(response)) {
            realCallBack.LoadError("无网络，本地无缓存", CODE_NOCACHE);
            return;
        }
        try {
            JSONObject jsonObject = JSON.parseObject(response);
            Load(jsonObject, realCallBack);
        } catch (Exception e) {
            e.printStackTrace();
            realCallBack.LoadError(e.getLocalizedMessage(), CODE_PASER_ERROR);
        }
    }

    protected void Load(JSONObject jsonObject, RealCallBackImpl<T> realCallBack)
    {
        Class<T> cls = realCallBack.getClazz();
        //// TODO: 2016/6/12 关于json解析的逻辑代码

    }

    static final String CODE_NOCACHE = "0001";
    static final String CODE_LOCAL_GETERROR = "0002";
    static final String CODE_PASER_ERROR = "0003";
}
