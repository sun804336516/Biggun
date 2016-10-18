package com.biggun.drawbitmapmesh.Activity;

import android.animation.Animator;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;

import com.biggun.drawbitmapmesh.Adapter.AppListLoader;
import com.biggun.drawbitmapmesh.Adapter.LoaderAdapter;
import com.biggun.drawbitmapmesh.Adapter.initDecoration;
import com.biggun.drawbitmapmesh.Bean.AppEntry;
import com.biggun.drawbitmapmesh.R;
import com.biggun.drawbitmapmesh.Util.Utils;
import com.biggun.drawbitmapmesh.View.SmothhRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LoadeTestActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<List<AppEntry>>
{
    private LoaderManager loaderManager;
    private static final int LOADER_ID = 100;
    private SmothhRecyclerView rcv;
    private LoaderAdapter adapter;
    private List<AppEntry> list = new ArrayList<>();
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_memory);
        initToolBar("Loader");
        collapseToolBar();
        InitViews();
        InitDatas();
        InitListeners();
    }

    @Override
    protected void InitViews()
    {
        rcv = findView(R.id.loader_rcv);
        rcv.setLayoutManager(new LinearLayoutManager(null,LinearLayoutManager.VERTICAL,false));
        rcv.addItemDecoration(new initDecoration(getResources().getDrawable(R.drawable.initdrcoration)));
        adapter = new LoaderAdapter(list,this,R.layout.traffic_layout);
        rcv.setAdapter(adapter);
        pd = getProgressDialog("提示!", "加载中，请稍候！");
    }

    @Override
    protected void InitDatas()
    {
        loaderManager = this.getLoaderManager();
        loaderManager.initLoader(LOADER_ID, null, this);
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

    private long time;
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        loaderManager.destroyLoader(LOADER_ID);
    }

    @Override
    public Loader<List<AppEntry>> onCreateLoader(int id, Bundle args)
    {
        pd.show();
        time = System.currentTimeMillis();
        return new AppListLoader(LoadeTestActivity.this);
    }

    @Override
    public void onLoadFinished(Loader<List<AppEntry>> loader, List<AppEntry> data)
    {
        pd.cancel();
        Utils.LogE((System.currentTimeMillis() - time) + "onLoadFinished:" + data.toString());
        adapter.setData(data);
    }

    @Override
    public void onLoaderReset(Loader<List<AppEntry>> loader)
    {
        adapter.setData(null);
    }
}
