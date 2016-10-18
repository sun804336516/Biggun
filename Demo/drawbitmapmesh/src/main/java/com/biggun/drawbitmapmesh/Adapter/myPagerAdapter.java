package com.biggun.drawbitmapmesh.Adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by 孙贤武 on 2016/1/12.
 */
public class myPagerAdapter extends PagerAdapter
{
    private List<ImageView> list = null;

    public myPagerAdapter(List<ImageView> list)
    {
        this.list = list;
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        ImageView img = list.get(position);
        container.addView(img);
        return img;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.removeView(list.get(position));
//        super.destroyItem(container, position, object);
    }
}
