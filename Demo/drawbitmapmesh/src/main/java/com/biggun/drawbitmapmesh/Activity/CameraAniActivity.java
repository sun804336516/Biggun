package com.biggun.drawbitmapmesh.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.biggun.drawbitmapmesh.Animation.Rotate3DAnimation;
import com.biggun.drawbitmapmesh.R;
import com.biggun.drawbitmapmesh.Service.FloatService;
import com.biggun.drawbitmapmesh.Util.Utils;
import com.biggun.drawbitmapmesh.View.MyShimmerTextView;

public class CameraAniActivity extends BaseActivity implements View.OnClickListener
{
    private ImageView img;
    Button addFLoatBtn, removeFloatBtn;
    MyShimmerTextView colorTv,colorTv2;
    TextView picTv;
    ValueAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_ani);
        InitViews();
        InitDatas();
        InitListeners();
    }

    @Override
    protected void InitViews()
    {
        img = findImageView(R.id.cameraimg);
        picTv = findView(R.id.pictextview);
        colorTv = findView(R.id.colortextview);
        colorTv2 = findView(R.id.colortextview2);
        addFLoatBtn = findButton(R.id.addFloatView);
        removeFloatBtn = findButton(R.id.removeFloatView);

    }

    @Override
    protected void InitDatas()
    {
        String source = "<font color='red'>王大枪</font><img src='" + R.mipmap.skala + "'/>";
        CharSequence text = Html.fromHtml(source, new Html.ImageGetter()
        {
            @Override
            public Drawable getDrawable(String source)
            {
                int resId = Integer.parseInt(source);
                Drawable d = getResources().getDrawable(resId);
                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                return d;
            }
        }, null);

        picTv.setText(text);
        SpannableString text2 = Utils.setkeyWordColor(Color.BLUE, "王大枪-----====孙贤武看大家那算了肯德基记录卡没多少了肯定没来看 那里的喀纳斯来得及啊", "孙贤武");
        colorTv.setText(text2);
        colorTv.start();

        colorTv2.setText("据尼泊尔媒体25日报道,24日坠毁的尼泊尔塔拉航空飞机上的23名乘客遗体已在当地时间25日下午被全部找到,将用直升机送往博卡拉");
        colorTv2.setUpShimmer(true);
        colorTv2.setOrientation(MyShimmerTextView.Orientation.RIGHT);
        colorTv2.start();


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.test1);
        img.setImageBitmap(Utils.blur(this, bitmap, 20));

    }

    @Override
    protected void InitListeners()
    {
        addFLoatBtn.setOnClickListener(this);
        removeFloatBtn.setOnClickListener(this);
    }

    @Override
    protected boolean OnToolbarItemClick(MenuItem item)
    {
        return false;
    }

    @Override
    protected void onToolBarAnimationEnd(Animator animator)
    {

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.addFloatView:
                Intent intent = new Intent(this, FloatService.class);
                startService(intent);
                break;
            case R.id.removeFloatView:
                Intent intent2 = new Intent(this, FloatService.class);
                stopService(intent2);
                break;
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
//        colorTv.cancelAni();
//        colorTv2.cancelAni();
    }
}
