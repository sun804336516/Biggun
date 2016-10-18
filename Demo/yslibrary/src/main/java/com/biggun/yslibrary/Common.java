package com.biggun.yslibrary;

import android.net.Uri;
import android.provider.MediaStore;

/**
 * 作者：Administrator on 2016/6/13 13:22
 * 邮箱：sun91985415@163.com
 */
public class Common
{
    public static final Uri IMAGE_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    public static final Uri VIDEO_URI = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    public static final Uri FILES_URI = MediaStore.Files.getContentUri("external");

}
