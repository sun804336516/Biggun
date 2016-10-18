package com.biggun.yslibrary.Net;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * 作者：孙贤武 on 2016/4/26 14:28
 * 邮箱：sun91985415@163.com
 * okhttp上传友好提示进度
 */
public abstract class ProgressRequestBody extends RequestBody
{
    private RequestBody mRequestBody;
    private BufferedSink mBufferedSink;

    public ProgressRequestBody(RequestBody requestBody)
    {
        mRequestBody = requestBody;
    }

    @Override
    public MediaType contentType()
    {
        return mRequestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException
    {
        return mRequestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException
    {
        if (mBufferedSink == null) {
            mBufferedSink = Okio.buffer(ProgressShink(sink));
        }
        mRequestBody.writeTo(mBufferedSink);
        mBufferedSink.flush();
    }

    private Sink ProgressShink(BufferedSink sink)
    {
        return new ForwardingSink(sink)
        {
            long length = 0;
            long current = 0;

            @Override
            public void write(Buffer source, long byteCount) throws IOException
            {
                super.write(source, byteCount);
                if (length == 0) {
                    length = contentLength();
                }
                current += byteCount;
                onRequestListener(current, length, current == length);
            }
        };
    }

    public abstract void onRequestListener(long current, long length, boolean done);
}
