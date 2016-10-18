package com.biggun.drawbitmapmesh;

import android.net.Uri;
import android.provider.MediaStore;

/**
 * 作者：孙贤武 on 2016/1/27 11:20
 * 邮箱：sun91985415@163.com
 */
public class Common
{
    /**
     * 侧滑动画持续时间
     */
    public static final long ANIMATORTIME_250 = 250;
    public static final long ANIMATORTIME_500 = 500;
    public static final long ANIMATORTIME_1000 = 1*1000;
    public static final long ANIMATORTIME_2000 = 2*1000;
    public static final long ANIMATORTIME_3000 = 3*1000;
    public static final long ANIMATORTIME_4000 = 4*1000;
    public static final long ANIMATORTIME_5000 = 5*1000;
    public static final long ANIMATORTIME_6000 = 6*1000;
    public static final long ANIMATORTIME_10000 = 10*1000;

    public static final Uri IMAGE_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    public static final String[] IMAGE_PROJECTION = new String[]{
            MediaStore.Images.Media._ID
            , MediaStore.Images.Media.DISPLAY_NAME
            , MediaStore.Images.Media.DATA
            , MediaStore.Images.Media.SIZE};
    public static final String IMAGE_SELECTION = MediaStore.Images.Media.MIME_TYPE + " = ?";
    public static final String[] IMAGE_SELECTION_ARGS = new String[]{"image/jpeg"};
    public static final String IMAGE_SORT_ORDER = MediaStore.Images.Media.DEFAULT_SORT_ORDER + " DESC";
    //百度----翻译
    public static final String BAIDU_APIKEY = "ad9d00e153a304977a7a1136761d0455";
    public static final String TRANSLATE_URL = "http://apis.baidu.com/apistore/tranlateservice/translate";
    public static final String TRANSLATE_ARG = "query=%s&from=en&to=zh";
    //手机号码查询
    public static final String MOBILE_URL = "http://apis.baidu.com/showapi_open_bus/mobile/find";
    public static final String MOBILE_ARG = "num=%s";
    //医药制厂查询
    public static final String MEDIC_URL = " http://apis.baidu.com/showapi_open_bus/medicine/medic_company";
    public static final String MEDIC_ARG = "factoryName=%s&page=%d&limit=%d";
}
