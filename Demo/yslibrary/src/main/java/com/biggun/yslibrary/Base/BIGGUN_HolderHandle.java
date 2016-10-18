package com.biggun.yslibrary.Base;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 孙贤武 on 2016/1/14.
 */
public class BIGGUN_HolderHandle
{
    private View convertView;
    private SparseArray<View> sparseArray;

    private BIGGUN_HolderHandle(View convertView)
    {
        this.convertView = convertView;
        convertView.setTag(this);
        sparseArray = new SparseArray<>();
    }

    public static BIGGUN_HolderHandle getInstance(View convertView)
    {
        BIGGUN_HolderHandle BIGGUN_HolderHandle = (BIGGUN_HolderHandle) convertView.getTag();
        if (BIGGUN_HolderHandle == null) {
            BIGGUN_HolderHandle = new BIGGUN_HolderHandle(convertView);
        }
        return BIGGUN_HolderHandle;
    }

    public View getConvertView()
    {
        return convertView;
    }

    public <T extends View> T getView(int resId)
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

    public BIGGUN_HolderHandle setImage(int resId, int mapId)
    {
        getImageView(resId).setImageResource(mapId);
        return this;
    }

    public BIGGUN_HolderHandle setImage(int resId, Drawable drawable)
    {
        getImageView(resId).setImageDrawable(drawable);
        return this;
    }

    public BIGGUN_HolderHandle setImage(int resId, Bitmap bitmap)
    {
        getImageView(resId).setImageBitmap(bitmap);
        return this;
    }

    public BIGGUN_HolderHandle setText(int resId, String text)
    {
        getTextView(resId).setText(text);
        return this;
    }
}
