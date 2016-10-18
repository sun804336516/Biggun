package com.biggun.drawbitmapmesh.Adapter;

import android.content.Context;

import com.biggun.drawbitmapmesh.R;

import java.util.List;

/**
 * 作者：孙贤武 on 2016/3/14 11:33
 * 邮箱：sun91985415@163.com
 */
public class VoiceAdapter extends BaseAdapter<String>
{
    public VoiceAdapter(List<String> mList, Context mContext, int mLayoutId)
    {
        super(mList, mContext, mLayoutId);
    }

    @Override
    protected void Convert(HolderHandle holderHandle, int position)
    {
        holderHandle.setText(R.id.voice_tv,mList.get(position));
    }
}
