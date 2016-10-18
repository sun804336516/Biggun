package com.biggun.drawbitmapmesh.Interface;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * 作者：Administrator on 2016/6/7 10:28
 * 邮箱：sun91985415@163.com
 */
public interface LoadBitmap
{
    void LoadSuccess(String url, ImageView imageView, Bitmap bitmap);

    void LoadError(String url, ImageView imageView);
}
