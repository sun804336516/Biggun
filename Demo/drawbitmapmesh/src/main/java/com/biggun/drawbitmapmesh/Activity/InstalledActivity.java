package com.biggun.drawbitmapmesh.Activity;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;

import com.biggun.drawbitmapmesh.Adapter.TrafficAdapter;
import com.biggun.drawbitmapmesh.Adapter.initDecoration;
import com.biggun.drawbitmapmesh.Bean.TrafficBean;
import com.biggun.drawbitmapmesh.R;
import com.biggun.drawbitmapmesh.Util.Utils;
import com.biggun.drawbitmapmesh.View.SmothhRecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InstalledActivity extends BaseActivity
{
    private SmothhRecyclerView rcv;
    private List<TrafficBean> list = new ArrayList<>();
    private TrafficAdapter adapter;
    private MyHandler handler;
    private static final int HANDLER_GET = 1000;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installed);
        initToolBar("已安装的程序");
        collapseToolBar();
        handler = new MyHandler(this);
        InitViews();
        InitDatas();
        InitListeners();
    }

    @Override
    protected void InitViews()
    {
        rcv = findView(R.id.installed_rcv);
        rcv.setLayoutManager(new LinearLayoutManager(null, LinearLayoutManager.VERTICAL, false));
        rcv.addItemDecoration(new initDecoration(getResources().getDrawable(R.drawable.initdrcoration)));
        pd = getProgressDialog("提示!", "加载中，请稍候！");
    }

    private long time;
    @Override
    protected void InitDatas()
    {
        pd.show();
        time = System.currentTimeMillis();
        new MyThread(this).start();
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

    private void getApplicationFlow()
    {
        PackageManager pm = getPackageManager();
        List<ApplicationInfo> infos = pm.getInstalledApplications(PackageManager.GET_ACTIVITIES);
        TrafficBean trafficBean = null;
        for (ApplicationInfo info : infos) {
            long uidRxBytes = TrafficStats.getUidRxBytes(info.uid);
            long uidTxBytes = TrafficStats.getUidTxBytes(info.uid);
            String name = info.loadLabel(pm).toString();
            Drawable logo = info.loadIcon(pm);
            trafficBean = new TrafficBean(name, uidRxBytes, uidTxBytes, logo == null ?
                    getResources().getDrawable(android.R.drawable.sym_def_app_icon) : logo);
            list.add(trafficBean);
            Utils.LogE("(" + name + ")下载流量:" + uidRxBytes + "--(" + name + ")上传流量:" + uidTxBytes);
        }
        Collections.sort(list);
        long totalRxBytes = TrafficStats.getTotalRxBytes();//总的下载流量
        long totalTxBytes = TrafficStats.getTotalTxBytes();//总的上传流量
        Utils.LogE("总的下载流量:" + totalRxBytes);
        Utils.LogE("总的上传流量:" + totalTxBytes);

        long mobileRxBytes = TrafficStats.getMobileRxBytes();//移动网络的下载流量
        long mobileTxBytes = TrafficStats.getMobileTxBytes();//移动网络的上传流量
        Utils.LogE("移动网络下载流量:" + mobileRxBytes);
        Utils.LogE("移动网络上传流量:" + mobileTxBytes);
    }

    private static class MyHandler extends android.os.Handler
    {
        private WeakReference<InstalledActivity> weakReference;

        public MyHandler(InstalledActivity installedActivity)
        {
            weakReference = new WeakReference<>(installedActivity);
        }

        @Override
        public void dispatchMessage(Message msg)
        {
            super.dispatchMessage(msg);
            InstalledActivity installedActivity = weakReference.get();
            if (installedActivity != null) {
                installedActivity.pd.cancel();
                Utils.LogE("耗时："+(System.currentTimeMillis() - installedActivity.time));
                installedActivity.adapter = new TrafficAdapter(installedActivity.list, installedActivity, R.layout.traffic_layout);
                installedActivity.rcv.setAdapter(installedActivity.adapter);
            }
        }
    }

    private static class MyThread extends Thread
    {
        WeakReference<InstalledActivity> wk;

        public MyThread(InstalledActivity activity)
        {
            wk = new WeakReference<InstalledActivity>(activity);
        }

        @Override
        public void run()
        {
            super.run();
            wk.get().getApplicationFlow();
            Message message = wk.get().handler.obtainMessage(HANDLER_GET);
            message.sendToTarget();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        handler.removeCallbacksAndMessages(HANDLER_GET);
    }

}
