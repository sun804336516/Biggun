package com.biggun.drawbitmapmesh.Adapter;

import android.content.Context;

import com.biggun.drawbitmapmesh.Bean.AppEntry;
import com.biggun.drawbitmapmesh.Bean.TrafficBean;
import com.biggun.drawbitmapmesh.R;

import java.util.List;

/**
 * 作者：孙贤武 on 2016/3/25 17:03
 * 邮箱：sun91985415@163.com
 */
public class LoaderAdapter extends BaseAdapter<AppEntry>
{
    public LoaderAdapter(List<AppEntry> mList, Context mContext, int mLayoutId)
    {
        super(mList, mContext, mLayoutId);
    }

    @Override
    protected void Convert(HolderHandle holderHandle, int position)
    {
        AppEntry appEntry = mList.get(position);
        holderHandle.setText(R.id.traffic_name, appEntry.getmLabel())
                .setText(R.id.traffic_tv, appEntry.getRxBytes() + "," + appEntry.getTxBytes())
                .setImage(R.id.traffic_logo, appEntry.getmIcon());
    }

    public void setData(List<AppEntry> mList)
    {
        this.mList.clear();
        if (mList != null) {
            this.mList.addAll(mList);
            notifyDataSetChanged();
        }
    }
}
