package com.biggun.yslibrary.Base;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 作者：Administrator on 2016/6/13 11:56
 * 邮箱：sun91985415@163.com
 */
public abstract class BIGGUN_Fragment extends Fragment
{
    protected View view;
    protected MyHanler mHanler;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(getLayoutId(), container, false);
        InitViews();
        InitDatas();
        InitListeners();
        mHanler = new MyHanler(this);
        return view;
    }

    protected abstract int getLayoutId();

    protected abstract void InitViews();

    protected abstract void InitDatas();

    protected abstract void InitListeners();

    //==============================================findview====================================//
    protected abstract <T extends View> T findView(int id);

    //=============================================Handler=========================================//
    protected static class MyHanler extends BIGGUN_Handler<BIGGUN_Fragment>
    {
        public MyHanler(BIGGUN_Fragment BIGGUNFragment)
        {
            super(BIGGUNFragment);
        }

        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            BIGGUN_Fragment mBIGGUNFragment = mWeakReference.get();
            mBIGGUNFragment.ConvertMessage(mBIGGUNFragment, msg);
        }
    }

    /**
     * 内置handler的回调
     * @param BIGGUNFragment
     * @param msg
     */
    protected abstract void ConvertMessage(BIGGUN_Fragment BIGGUNFragment, Message msg);
}
