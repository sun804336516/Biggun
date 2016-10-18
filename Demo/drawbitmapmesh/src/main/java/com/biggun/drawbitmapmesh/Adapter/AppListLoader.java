package com.biggun.drawbitmapmesh.Adapter;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;

import com.biggun.drawbitmapmesh.Bean.AppEntry;
import com.biggun.drawbitmapmesh.Receiver.InstalledAppsObserver;
import com.biggun.drawbitmapmesh.Receiver.SystemLocaleObserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author 孙贤武
 *         参考cursorloader
 */
public class AppListLoader extends AsyncTaskLoader<List<AppEntry>>
{
    public final PackageManager mPm;

    private List<AppEntry> mApps;

    public AppListLoader(Context ctx)
    {
        super(ctx);
        mPm = getContext().getPackageManager();
    }

    @Override
    public List<AppEntry> loadInBackground()
    {
        List<ApplicationInfo> apps = mPm.getInstalledApplications(PackageManager.GET_ACTIVITIES);

        if (apps == null) {
            apps = new ArrayList<ApplicationInfo>();
        }

        List<AppEntry> entries = new ArrayList<AppEntry>(apps.size());
        AppEntry entry;
        for (ApplicationInfo info : apps) {
            long uidTxBytes = TrafficStats.getUidTxBytes(info.uid);
            long uidRxBytes = TrafficStats.getUidRxBytes(info.uid);//下载
            entry = new AppEntry();
            CharSequence cs = info.loadLabel(mPm);
            Drawable icon = info.loadIcon(mPm);
            entry.setmLabel(cs == null ? info.packageName : cs.toString());
            entry.setmIcon(icon == null ? getContext().getResources().getDrawable(android.R.drawable.sym_def_app_icon) : icon);
            entry.setRxBytes(uidRxBytes);
            entry.setTxBytes(uidTxBytes);
            entries.add(entry);
        }
        Collections.sort(entries);
        return entries;
    }

    @Override
    public void deliverResult(List<AppEntry> apps)
    {
        if (isReset()) {
            if (apps != null) {
                releaseResources(apps);
                return;
            }
        }
        List<AppEntry> oldApps = mApps;
        mApps = apps;

        if (isStarted()) {
            super.deliverResult(apps);
        }

        if (oldApps != null && oldApps != apps) {
            releaseResources(oldApps);
        }
    }

    @Override
    protected void onStartLoading()
    {

        if (mApps != null) {
            deliverResult(mApps);
        }

        if (mAppsObserver == null) {
            mAppsObserver = new InstalledAppsObserver(this);
        }

        if (mLocaleObserver == null) {
            mLocaleObserver = new SystemLocaleObserver(this);
        }

        if (takeContentChanged() || mApps == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading()
    {
        cancelLoad();
    }

    @Override
    protected void onReset()
    {

        onStopLoading();

        if (mApps != null) {
            releaseResources(mApps);
            mApps = null;
        }

        if (mAppsObserver != null) {
            getContext().unregisterReceiver(mAppsObserver);
            mAppsObserver = null;
        }

        if (mLocaleObserver != null) {
            getContext().unregisterReceiver(mLocaleObserver);
            mLocaleObserver = null;
        }
    }

    @Override
    public void onCanceled(List<AppEntry> apps)
    {

        super.onCanceled(apps);

        releaseResources(apps);
    }

    @Override
    public void forceLoad()
    {
        super.forceLoad();
    }

    /**
     * 释放资源
     *
     * @param apps
     */
    private void releaseResources(List<AppEntry> apps)
    {
        // For a simple List, there is nothing to do. For something like a Cursor,
        // we would close it in this method. All resources associated with the
        // Loader should be released here.
    }

    private InstalledAppsObserver mAppsObserver;

    private SystemLocaleObserver mLocaleObserver;

}
