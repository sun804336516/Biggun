package com.biggun.yslibrary.Net;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * 作者：孙贤武 on 2016/4/26 14:27
 * 邮箱：sun91985415@163.com
 * okhttp下载友好提示进度
 */
public abstract class ProgressResponseBody extends ResponseBody
{
    private BufferedSource mBufferedSource;
    private ResponseBody mResponseBody;

    public ProgressResponseBody(ResponseBody responseBody)
    {
        mResponseBody = responseBody;
    }

    @Override
    public MediaType contentType()
    {
        return mResponseBody.contentType();
    }

    @Override
    public long contentLength()
    {
        return mResponseBody.contentLength();
    }

    @Override
    public BufferedSource source()
    {
        if (mBufferedSource == null) {
            mBufferedSource = Okio.buffer(ProgressSource(mResponseBody.source()));
        }
        return mBufferedSource;
    }
    private Source ProgressSource(Source source)
    {

        return new ForwardingSource(source)
        {
            long totalBytesRead = 0;
            @Override
            public long read(Buffer sink, long byteCount) throws IOException
            {
                long bytesRead = super.read(sink, byteCount);
                //增加当前读取的字节数
                totalBytesRead += (bytesRead != -1 ? bytesRead : 0);
                //回调，如果contentLength()不知道长度，会返回-1
                onResponseListener(totalBytesRead, mResponseBody.contentLength(), bytesRead == -1);
                return bytesRead;
            }
        };
    }
    public abstract void onResponseListener(long current,long length,boolean done);
}
