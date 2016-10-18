package com.biggun.drawbitmapmesh.Activity;

import android.animation.Animator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.biggun.drawbitmapmesh.R;
import com.biggun.drawbitmapmesh.Util.ActivityManager;
import com.biggun.drawbitmapmesh.Util.Utils;

import java.io.File;

public class SilentinstallActivity extends BaseActivity implements View.OnClickListener
{
    private Button secondInstallBtn, silentInstallBtn, openSilentBtn, selectButton;
    private TextView apkPathTv;
    private String apkPath = "";
    private static final int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_silentinstall);

        InitViews();
        InitDatas();
        InitListeners();
    }

    @Override
    protected void InitViews()
    {
        secondInstallBtn = findButton(R.id.secondinstallbtn);
        silentInstallBtn = findButton(R.id.silentinstallbtn);
        openSilentBtn = findButton(R.id.opensilentinstall);
        apkPathTv = findTextView(R.id.apkpathTv);
        selectButton = findButton(R.id.select_apkpath);
    }

    @Override
    protected void InitDatas()
    {
        apkPath = Environment.getExternalStorageDirectory() + "/youyaoqimanhua.apk";
        apkPathTv.setText("apk路径:" + apkPath);
    }

    @Override
    protected void InitListeners()
    {
        secondInstallBtn.setOnClickListener(this);
        silentInstallBtn.setOnClickListener(this);
        openSilentBtn.setOnClickListener(this);
        selectButton.setOnClickListener(this);
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
    public void onClick(View v)
    {
        if (Utils.isDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.secondinstallbtn:
                if (TextUtils.isEmpty(apkPath)) {

                    return;
                }
                if (Utils.isRoot()) {
                    new Thread()
                    {
                        @Override
                        public void run()
                        {
                            super.run();
                            boolean isinstall = Utils.secondInstall(apkPath);
                            Message msg = handler.obtainMessage();
                            msg.obj = isinstall;
                            handler.sendMessage(msg);
                        }
                    }.start();
                } else {
                    Utils.LogE("没有root权限");
                }
                break;
            case R.id.silentinstallbtn:
                File file = new File(apkPath);
                Uri uri = Uri.fromFile(file);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                startActivity(intent);
                break;
            case R.id.opensilentinstall:
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                break;
            case R.id.select_apkpath:
                startActivityForResult(new Intent(this, SdcardBrowserActivity.class), REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            apkPath = data.getStringExtra("apkpath");
            apkPathTv.setText("apk路径:" + apkPath);
        }
    }

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            boolean isinstall = (boolean) msg.obj;
            if (isinstall) {
                ActivityManager.getInstance().Toast("安装成功");
            } else {
                ActivityManager.getInstance().Toast("安装失败");
            }
        }
    };
}
