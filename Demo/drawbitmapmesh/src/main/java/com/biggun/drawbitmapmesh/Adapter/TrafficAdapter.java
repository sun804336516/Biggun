package com.biggun.drawbitmapmesh.Adapter;

import android.content.Context;

import com.biggun.drawbitmapmesh.Bean.TrafficBean;
import com.biggun.drawbitmapmesh.R;

import java.util.List;

/**
 * 作者：孙贤武 on 2016/3/25 17:03
 * 邮箱：sun91985415@163.com
 */
public class TrafficAdapter extends BaseAdapter<TrafficBean>
{
    public TrafficAdapter(List<TrafficBean> mList, Context mContext, int mLayoutId)
    {
        super(mList, mContext, mLayoutId);
    }

    @Override
    protected void Convert(HolderHandle holderHandle, int position)
    {
        TrafficBean trafficBean = mList.get(position);
        holderHandle.setText(R.id.traffic_name, trafficBean.getName())
                .setText(R.id.traffic_tv, trafficBean.getRxBytes() +","+ trafficBean.getTxBytes())
                .setImage(R.id.traffic_logo, trafficBean.getDrawable());
    }
}
