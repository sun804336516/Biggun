package com.biggun.drawbitmapmesh.Util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：孙贤武 on 2016/2/22 10:47
 * 邮箱：sun91985415@163.com
 */
public class PermissionUtils
{
    private DoThings dothings;
    private int requestCode = -1;
    private String[] permissions;
    private Activity activity;
    private static PermissionUtils permissionUtils;

//    public static PermissionUtils getInstance(Activity activity, String[] permissions, int requestCode)
//    {
//        if (permissionUtils == null) {
//            synchronized (PermissionUtils.class) {
//                if (permissionUtils == null) {
//                    permissionUtils = new PermissionUtils(activity, permissions, requestCode);
//                }
//            }
//        }
//        return permissionUtils;
//    }

    public void setDothings(DoThings dothings)
    {
        this.dothings = dothings;
    }

    public PermissionUtils(Activity activity, String[] permissions, int requestCode)
    {
        this.requestCode = requestCode;
        this.permissions = permissions;
        this.activity = activity;
        setDothings((DoThings) activity);
    }

    public void requestPermission()
    {
        if (!Utils.isOverMarshmallow()) {
            // TODO: 2016/2/22
            dothings.dothings();
            return;
        }

        List<String> deniedPermissions = findDeniedPermissions(activity, permissions);
        if (deniedPermissions.size() > 0) {
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
        } else {
            // TODO: 2016/2/22
            dothings.dothings();
        }
    }

    private List<String> findDeniedPermissions(Activity activity, String... perissions)
    {
        List<String> list = new ArrayList<>();
        for (String str : perissions) {
            if (ContextCompat.checkSelfPermission(activity, str) != PackageManager.PERMISSION_GRANTED) {
                list.add(str);
            }
        }
        return list;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] granied)
    {
        if (this.requestCode == requestCode) {
            if (granied[0] == PackageManager.PERMISSION_GRANTED) {
                dothings.dothings();
            } else {
                dothings.getPermissionDenied();
            }
        }
    }

    public interface DoThings
    {
        void dothings();

        void getPermissionDenied();
    }

    public void destory()
    {
        if (activity != null) {
            dothings = null;
            activity = null;
        }
    }
}
