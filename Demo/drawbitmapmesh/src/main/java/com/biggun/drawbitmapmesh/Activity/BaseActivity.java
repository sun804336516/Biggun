package com.biggun.drawbitmapmesh.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biggun.drawbitmapmesh.Common;
import com.biggun.drawbitmapmesh.R;
import com.biggun.drawbitmapmesh.Util.ActivityManager;
import com.biggun.drawbitmapmesh.Util.ImageLoader;
import com.biggun.drawbitmapmesh.Util.Utils;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by 孙贤武 on 2016/1/8.
 */
public abstract class BaseActivity extends AppCompatActivity
{
    private ActivityManager manager = null;
    private static final String LINEARLAYOUT = "LinearLayout";
    private static final String RELATIVELAYOUT = "RelativeLayout";
    private static final String FRAMELAYOUT = "FrameLayout";
    protected boolean useSlide = true;
    protected Toolbar toolbar;
    ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        manager = ActivityManager.getInstance();
        manager.addAc(this);
        mImageLoader = ImageLoader.getInstance(5, ImageLoader.FIFO);
        mImageLoader.setCacheFile(getExternalCacheDir() + "Bitmap");
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs)
    {
        //重写此方法可以替换掉布局中自己想替换的ViewGroup,不用在布局中更改
        Utils.LogE(parent != null ? parent.toString() : "" + "----ViewName:" + name);
//        if (useSlide) {
        if (name.equals(LINEARLAYOUT)) {
//                return new SlideFinishLinearLayout(context, attrs);
            return new AutoLinearLayout(context, attrs);
        } else if (name.equals(RELATIVELAYOUT)) {
//                return new SlideFinishRelativeLayout(context, attrs);
            return new AutoRelativeLayout(context, attrs);
        } else if (name.equals(FRAMELAYOUT)) {
//                return new SlideFinishFrameLayout(context, attrs);
            return new AutoFrameLayout(context, attrs);
        }
//        }
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
        Utils.LogE(this.getLocalClassName() + "---onStart");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Utils.LogE(this.getLocalClassName() + "---onResume");
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
//---ToolBar相关--------------------------------------------------------------------------------------------

    /**
     * 初始化Toolbar
     *
     * @param title
     */
    protected void initToolBar(String title)
    {
        toolbar = findView(R.id.toolbar);
//        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                return OnToolbarItemClick(item);
            }
        });
        TextView toolbarViewById = (TextView) toolbar.findViewById(R.id.toolbar_tv);
        toolbarViewById.setText(title);
        AutoUtils.auto(toolbarViewById);
//        final TextView tv = findTextView(R.id.tool);
//        AutoUtils.auto(tv);
//        tv.setText(title);
//        Toast.makeText(this, "toolbar.getLayoutParams().height:" + toolbar.getLayoutParams().height, Toast.LENGTH_SHORT).show();
    }

    /**
     * ToolBar收缩动画结束的回调
     *
     * @param animator
     */
    protected abstract void onToolBarAnimationEnd(Animator animator);

    /**
     * ToolBar上menuItem的点击回调
     *
     * @param item
     * @return
     */
    protected abstract boolean OnToolbarItemClick(MenuItem item);


    /**
     * ToolBar的收缩动画
     */
    public void collapseToolBar()
    {
        if (toolbar == null) {
            return;
        }
        if (true) {
            return;
        }
        final int toolbarHeight = getToolbarSize();
        Utils.LogE("toolbarsize:" + toolbarHeight);
        final ValueAnimator animator = ValueAnimator.ofInt(toolbarHeight << 1, toolbarHeight);
        animator.setDuration(Common.ANIMATORTIME_1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                ViewGroup.LayoutParams layoutParams = toolbar.getLayoutParams();
                layoutParams.height = (int) animation.getAnimatedValue();
                Utils.LogE("fration:" + animation.getAnimatedFraction() + "==toolbarAniamtion==" + layoutParams.height);
                toolbar.setLayoutParams(layoutParams);
//                toolbar.requestLayout();
            }
        });
        animator.start();
        animator.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);
                onToolBarAnimationEnd(animator);
            }
        });
    }

    protected int getToolbarSize()
    {
        if (toolbar == null) {
            return 0;
        }
        TypedValue tv = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
        return TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
    }

    //---------------------------------------------findViewById------------------------------------//
    protected <T extends View> T findView(int resId)
    {
        return (T) this.findViewById(resId);
    }

    protected TextView findTextView(int resId)
    {
        return findView(resId);
    }

    protected LinearLayout findLinearLayout(int resId)
    {
        return findView(resId);
    }

    protected ImageView findImageView(int resId)
    {
        return findView(resId);
    }

    protected Button findButton(int resId)
    {
        return findView(resId);
    }

    protected Button findRelativeLayout(int resId)
    {
        return findView(resId);
    }

    protected EditText findEditText(int resId)
    {
        return findView(resId);
    }
}
