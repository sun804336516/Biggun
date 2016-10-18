package com.biggun.yslibrary.Base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by 孙贤武 on 2016/1/14.
 */
public class BIGGUN_ViewHolder extends RecyclerView.ViewHolder
{
    private BIGGUN_HolderHandle mBIGGUN_HolderHandle;
    public BIGGUN_ViewHolder(View itemView)
    {
        super(itemView);
        mBIGGUN_HolderHandle = BIGGUN_HolderHandle.getInstance(itemView);
        AutoUtils.autoSize(itemView);
    }

    public BIGGUN_HolderHandle getBIGGUN_HolderHandle()
    {
        return mBIGGUN_HolderHandle;
    }
}
