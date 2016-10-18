package com.biggun.yslibrary.Base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.biggun.yslibrary.Net.ImageLoader;
import com.biggun.yslibrary.Utils.LogUtils;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;


/**
 * Created by 孙贤武 on 2016/1/8.
 */
public abstract class BIGGUN_Activity extends AppCompatActivity
{
    private ActivityManager manager = null;
    private static final String LINEARLAYOUT = "LinearLayout";
    private static final String RELATIVELAYOUT = "RelativeLayout";
    private static final String FRAMELAYOUT = "FrameLayout";
    protected boolean useSlide = true;
    protected ImageLoader mImageLoader;
    protected Myhandler mhandler;
    /**
     * 设置屏幕是否能被截屏
     */
    protected boolean canScreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        LayoutInflaterCompat.setFactory(LayoutInflater.from(this), new LayoutInflaterFactory()
        {
            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs)
            {
                AppCompatDelegate delegate = getDelegate();
                View view = delegate.createView(parent, name, context, attrs);
                return view;
            }
        });
        super.onCreate(savedInstanceState);
        if (canScreen) {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        }
        manager = ActivityManager.getInstance();
        manager.addAc(this);
        mImageLoader = ImageLoader.getInstance(5, ImageLoader.FIFO);
        mImageLoader.setCacheFile(getExternalCacheDir() + "Bitmap");
        mhandler = new Myhandler(this);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs)
    {
        //重写此方法可以替换掉布局中自己想替换的ViewGroup,不用在布局中更改
        LogUtils.LogE(parent != null ? "----ViewGroupName:" + parent.toString() : "----ViewName:" + name);
        if (name.equals(LINEARLAYOUT)) {
            return new AutoLinearLayout(context, attrs);
        } else if (name.equals(RELATIVELAYOUT)) {
            return new AutoRelativeLayout(context, attrs);
        } else if (name.equals(FRAMELAYOUT)) {
            return new AutoFrameLayout(context, attrs);
        }
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        manager.remmoveAc(this);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        LogUtils.LogE(this.getLocalClassName() + "---onStart");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        LogUtils.LogE(this.getLocalClassName() + "---onResume");
    }

    /**
     * 查看是否有符合的Activity
     *
     * @param intent
     */
    protected void resolveActivity(Intent intent)
    {
        getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
    }

    //---------abstract-----------------------------------------------------------------------//
    protected abstract void InitViews();

    protected abstract void InitDatas();

    protected abstract void InitListeners();

    /**
     * 左上角的返回键
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void startActivity(Class cls)
    {
        startActivity(new Intent(), cls);
    }

    protected void startActivity(Intent intent, Class cls)
    {
        intent.setClass(this, cls);
        startActivity(intent);
    }
//-----Dialog相关--------------------------------------------------------------------------------------

    /**
     * 获取一个ProgressDialog 提示框
     *
     * @param title
     * @param message
     * @return
     */
    protected ProgressDialog getProgressDialog(String title, String message)
    {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setTitle(title);
        pd.setMessage(message);
        return pd;
    }

    /**
     * 获取一个AlertDialog
     *
     * @param title
     * @param message
     * @param icon
     * @param view
     * @return
     */
    protected AlertDialog getAlertDialog(String title, String message, int icon, View view)
    {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(message);
        if (icon > -1) {
            builder.setIcon(icon);
        }
        if (null != view) {
            builder.setView(view);
        }
        dialog = builder.create();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    private static final long ANIMATORTIME_1000 = 1000;

    protected int getToolbarSize()
    {
        TypedValue tv = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
        return TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
    }

    //---------------------------------------------findViewById------------------------------------//
    protected <T extends View> T findView(int resId)
    {
        return (T) this.findViewById(resId);
    }

    //=============================================Handler=========================================//
    protected static class Myhandler extends BIGGUN_Handler<BIGGUN_Activity>
    {
        public Myhandler(BIGGUN_Activity BIGGUNActivity)
        {
            super(BIGGUNActivity);
        }

        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            BIGGUN_Activity BIGGUNActivity = mWeakReference.get();
            BIGGUNActivity.ConvertMessage(BIGGUNActivity, msg);
        }
    }

    /**
     * 内置handler的回调
     *
     * @param BIGGUNActivity
     * @param msg
     */
    protected abstract void ConvertMessage(BIGGUN_Activity BIGGUNActivity, Message msg);
}
