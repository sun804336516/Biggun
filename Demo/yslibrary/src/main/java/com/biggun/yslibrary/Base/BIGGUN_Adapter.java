package com.biggun.yslibrary.Base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.biggun.yslibrary.Net.ImageLoader;
import com.biggun.yslibrary.Utils.StringUtils;

import java.util.List;

/**
 * Created by 孙贤武 on 2016/1/14.
 */
public abstract class BIGGUN_Adapter<T> extends RecyclerView.Adapter<BIGGUN_ViewHolder>
{
    protected List<T> mList;
    protected Context mContext;
    protected LayoutInflater mInfater;
    protected ImageLoader mImageLoader;
    protected int screenWid, screenHei;
    /*
     * imageloader加载图片线程开启数
     */
    protected static final int LOAD_COUNT = 3;

    public BIGGUN_Adapter(List<T> mList, Context mContext)
    {
        this.mList = mList;
        this.mContext = mContext;
        this.mInfater = LayoutInflater.from(mContext);
        screenWid = mContext.getResources().getDisplayMetrics().widthPixels;
        screenHei = mContext.getResources().getDisplayMetrics().heightPixels;
        mImageLoader = ImageLoader.getInstance(LOAD_COUNT, ImageLoader.FIFO);
        mImageLoader.setCacheFile(StringUtils.getCacheFile(mContext, "Bitmap"));
    }


    @Override
    public BIGGUN_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new BIGGUN_ViewHolder(mInfater.inflate(getViewId(viewType), parent, false));
    }

    @Override
    public void onBindViewHolder(BIGGUN_ViewHolder holder, final int position)
    {
        final BIGGUN_HolderHandle BIGGUN_HolderHandle = holder.getBIGGUN_HolderHandle();
        Convert(BIGGUN_HolderHandle, position, mList.get(position));
    }

    /**
     * 清除lrucache的内存缓存
     */
    public void clearBitmapCache()
    {
        if (mImageLoader == null) {
            return;
        }
        mImageLoader.clearCache();
    }

    protected abstract int getViewId(int viewType);

    protected abstract void Convert(BIGGUN_HolderHandle BIGGUN_HolderHandle, int position, T oject);

    @Override
    public int getItemCount()
    {
        return mList.size();
    }

}
