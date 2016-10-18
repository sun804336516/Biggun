package com.biggun.drawbitmapmesh.Adapter;

import android.content.Context;
import android.widget.ImageView;

import java.util.List;

/**
 * 作者：孙贤武 on 2016/4/14 10:51
 * 邮箱：sun91985415@163.com
 */
public class Vp_Rcv_Adapter extends BaseAdapter<Integer>
{
    public Vp_Rcv_Adapter(List<Integer> mList, Context mContext, int mLayoutId)
    {
        super(mList, mContext, mLayoutId);
    }

    @Override
    protected void Convert(HolderHandle holderHandle, int position)
    {
        ((ImageView)holderHandle.getConvertView()).setImageResource(mList.get(position));
    }
}
