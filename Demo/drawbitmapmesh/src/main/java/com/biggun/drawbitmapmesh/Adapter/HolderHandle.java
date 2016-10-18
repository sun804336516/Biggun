package com.biggun.drawbitmapmesh.Adapter;

import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 孙贤武 on 2016/1/14.
 */
public class HolderHandle
{
    private View convertView;
    private SparseArray<View> sparseArray;

    private HolderHandle(View convertView)
    {
        this.convertView = convertView;
        convertView.setTag(this);
        sparseArray = new SparseArray<>();
    }

    public static HolderHandle getInstance(View convertView)
    {
        HolderHandle holderHandle = (HolderHandle) convertView.getTag();
        if (holderHandle == null) {
            holderHandle = new HolderHandle(convertView);
        }
        return holderHandle;
    }

    public View getConvertView()
    {
        return convertView;
    }

    private <T extends View> T getView(int resId)
    {
        View view = sparseArray.get(resId);
        if (view == null) {
            view = convertView.findViewById(resId);
            sparseArray.put(resId, view);
        }
        return (T) view;
    }

    public ImageView getImageView(int resId)
    {
        return getView(resId);
    }

    public TextView getTextView(int resId)
    {
        return getView(resId);
    }

    public HolderHandle setImage(int resId, int mapId)
    {
        getImageView(resId).setImageResource(mapId);
        return this;
    }
    public HolderHandle setImage(int resId, Drawable drawable)
    {
        getImageView(resId).setImageDrawable(drawable);
        return this;
    }
    public HolderHandle setText(int resId, String text)
    {
        getTextView(resId).setText(text);
        return this;
    }
}
