package com.biggun.drawbitmapmesh.Adapter;

import android.content.Context;

import com.biggun.drawbitmapmesh.Bean.InitBean;
import com.biggun.drawbitmapmesh.R;

import java.util.List;

/**
 * 作者：孙贤武 on 2016/3/1 15:46
 * 邮箱：sun91985415@163.com
 */
public class InitAdapter extends BaseAdapter<InitBean>
{

    public InitAdapter(List<InitBean> mList, Context mContext, int mLayoutId)
    {
        super(mList, mContext, mLayoutId);
    }

    @Override
    protected void Convert(HolderHandle holderHandle, int position)
    {
        InitBean initBean = mList.get(position);
        holderHandle.setText(R.id.init_textview,initBean.getName());
    }
}
