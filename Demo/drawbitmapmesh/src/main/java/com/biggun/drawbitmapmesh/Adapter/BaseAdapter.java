package com.biggun.drawbitmapmesh.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by 孙贤武 on 2016/1/14.
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<ViewHolder>
{
    protected List<T> mList;
    protected Context mContext;
    protected LayoutInflater mInfater;
    protected int mLayoutId;
    protected OnItemClick onItemClick;
    protected int ScreenHeight;
    protected int ScreenWidth;

    public void setOnItemClickListener(OnItemClick onItemClick)
    {
        this.onItemClick = onItemClick;
    }

    public BaseAdapter(List<T> mList, Context mContext, int mLayoutId)
    {
        this.mLayoutId = mLayoutId;
        this.mList = mList;
        this.mContext = mContext;
        this.mInfater = LayoutInflater.from(mContext);
        ScreenHeight = mContext.getResources().getDisplayMetrics().heightPixels;
        ScreenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new ViewHolder(mInfater.inflate(mLayoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        final HolderHandle holderHandle = holder.getHolderHandle();
        Convert(holderHandle, position);
        if (onItemClick != null) {
            holderHandle.getConvertView().setClickable(true);
            holderHandle.getConvertView().setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onItemClick.onItemCkick(holderHandle, position);
                }
            });
        }
    }
    public void clearBitmapCache()
    {

    }
    protected abstract void Convert(HolderHandle holderHandle, int position);

    @Override
    public int getItemCount()
    {
        return mList.size();
    }

    public interface OnItemClick
    {
        void onItemCkick(HolderHandle holderHandle, int position);
    }
}
