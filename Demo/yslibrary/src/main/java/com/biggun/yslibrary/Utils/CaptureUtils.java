package com.biggun.yslibrary.Utils;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * 作者：孙贤武 on 2016/6/30 14:24
 * 邮箱：sun91985415@163.com
 * LIKE:YANSHUO
 */
public class CaptureUtils
{
    /**
     * 获取一个拍照的Intent
     * @param uri
     * @return
     */
    public static Intent getCaptureIntent(Uri uri)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(uri != null)
        {
            intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        }
        return intent;
    }

    /**
     * 根据图片在数据库中的id获取它对应的uri
     * @param imgid
     * @return
     */
    public static Uri getImageUri(int imgid)
    {
        return Uri.parse("content://media/external/images/media/"
                + imgid);
    }
}
