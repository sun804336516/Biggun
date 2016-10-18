package com.biggun.drawbitmapmesh.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.biggun.drawbitmapmesh.R;

/**
 * Created by 孙贤武 on 2016/1/12.
 */
public class TestFragment extends Fragment
{
    private View rootView;
    private ImageView img;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragmentlayout,container,false);
        initView();

        Bundle arguments = getArguments();
        img.setImageResource(arguments.getInt("img"));
        return rootView;
    }

    private void initView()
    {
        img = (ImageView) rootView.findViewById(R.id.fragment_img);
    }
}
