package com.biggun.drawbitmapmesh.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by 孙贤武 on 2016/1/14.
 */
public class ViewHolder extends RecyclerView.ViewHolder
{
    private HolderHandle holderHandle;
    public ViewHolder(View itemView)
    {
        super(itemView);
        holderHandle = HolderHandle.getInstance(itemView);
        AutoUtils.autoSize(itemView);
    }

    public HolderHandle getHolderHandle()
    {
        return holderHandle;
    }
}
