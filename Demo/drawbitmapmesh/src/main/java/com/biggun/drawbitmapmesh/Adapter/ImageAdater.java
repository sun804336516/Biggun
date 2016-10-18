package com.biggun.drawbitmapmesh.Adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.biggun.drawbitmapmesh.Bean.ImageBean;
import com.biggun.drawbitmapmesh.R;
import com.biggun.drawbitmapmesh.Util.ImageLoader;
import com.biggun.drawbitmapmesh.View.SmothhRecyclerView;

import java.util.List;

/**
 * 作者：孙贤武 on 2016/3/8 10:59
 * 邮箱：sun91985415@163.com
 */
public class ImageAdater extends BaseAdapter<ImageBean>
{
    SmothhRecyclerView rcv;
    private ImageLoader imageLoader;

    public ImageAdater(List<ImageBean> mList, Context mContext, int mLayoutId, SmothhRecyclerView rcv)
    {
        super(mList, mContext, mLayoutId);
        this.rcv = rcv;
        imageLoader = ImageLoader.getInstance(5, ImageLoader.FIFO);
        imageLoader.setCacheFile(mContext.getExternalCacheDir() + "Bitmap");
    }

    @Override
    protected void Convert(HolderHandle holderHandle, int position)
    {
        ImageView imageView = holderHandle.getImageView(R.id.imagebrowser_img);
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.width = ScreenWidth / 3;
        layoutParams.height = ScreenWidth / 3;
        imageView.setLayoutParams(layoutParams);
//        if (Utils.isOverLollipop()) {
//            imageView.setImageDrawable(VectorDrawableCompat.create(mContext.getResources(), R.drawable.image_load_bitmap, mContext.getTheme()));
//        } else {
        imageView.setImageResource(R.drawable.load_drawable);
//        }
        imageLoader.loadBitmap(mList.get(position).getPath(), imageView);
    }

    @Override
    public void clearBitmapCache()
    {
        super.clearBitmapCache();
        imageLoader.clearCache();
    }
}
