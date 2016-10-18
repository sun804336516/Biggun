package com.biggun.drawbitmapmesh.Adapter;

import android.content.Context;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biggun.drawbitmapmesh.Bean.BrowserBean;
import com.biggun.drawbitmapmesh.R;

import java.util.List;

/**
 * Created by 孙贤武 on 2016/1/14.
 */
public class BrowserAdapter extends BaseAdapter<BrowserBean>
{
    public BrowserAdapter(List<BrowserBean> mList, Context mContext, int mLayoutId)
    {
        super(mList, mContext, mLayoutId);
    }

    @Override
    protected void Convert(HolderHandle holderHandle, int position)
    {
        BrowserBean browserBean = mList.get(position);

        ImageView imageView = holderHandle.getImageView(R.id.browser_img);
        TextView tv = holderHandle.getTextView(R.id.browser_tv);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        layoutParams.height = ScreenHeight/10;
        layoutParams.width = ScreenWidth/7;
        imageView.setLayoutParams(layoutParams);
        imageView.setImageDrawable(VectorDrawableCompat.create(mContext.getResources(),browserBean.getMapId(),mContext.getTheme()));
        tv.setText(browserBean.getText());
    }
}
