package com.biggun.drawbitmapmesh.Activity;

import android.Manifest;
import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.widget.Toast;

import com.biggun.drawbitmapmesh.R;
import com.biggun.drawbitmapmesh.Util.PermissionUtils;

public class ClipRectActivity extends BaseActivity implements PermissionUtils.DoThings
{
    private static final int WIRITE_STORAGE = 1;
    private PermissionUtils permissionUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_rect);
        InitViews();
        InitDatas();
        InitListeners();
    }

    @Override
    protected void InitViews()
    {

    }

    @Override
    protected void InitDatas()
    {
//        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        if (permission == PackageManager.PERMISSION_GRANTED) {
//            callPhone();
//        } else {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                Toast.makeText(this, "请允许拨打电话权限", Toast.LENGTH_LONG).show();
//            } else {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WIRITE_STORAGE);
//            }
//        }
        permissionUtils = new PermissionUtils(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WIRITE_STORAGE);
        permissionUtils.requestPermission();
    }

    private void callPhone()
    {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + 10086));
        startActivity(intent);
    }

    @Override
    protected void InitListeners()
    {

    }

    @Override
    protected boolean OnToolbarItemClick(MenuItem item)
    {
        return false;
    }

    @Override
    protected void onToolBarAnimationEnd(Animator animator)
    {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void dothings()
    {
        callPhone();
    }

    @Override
    public void getPermissionDenied()
    {
//        permissionUtils.requestPermission();
        Toast.makeText(this, "权限被拒绝！", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        permissionUtils.destory();
    }
}
