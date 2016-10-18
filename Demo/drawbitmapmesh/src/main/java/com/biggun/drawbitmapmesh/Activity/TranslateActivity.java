package com.biggun.drawbitmapmesh.Activity;

import android.animation.Animator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.biggun.drawbitmapmesh.Bean.MedicBean;
import com.biggun.drawbitmapmesh.Bean.TranslateBean;
import com.biggun.drawbitmapmesh.Common;
import com.biggun.drawbitmapmesh.R;
import com.biggun.drawbitmapmesh.Util.OkHttpUtils;
import com.biggun.drawbitmapmesh.Util.Utils;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URLDecoder;

public class TranslateActivity extends BaseActivity implements View.OnClickListener
{
    private EditText transEt;
    private ImageView transImg;
    private TextView transTv;
    private OkHttpUtils okHttpUtils;
    private MyHandler handler;

    private void assignViews()
    {
        transEt = (EditText) findViewById(R.id.trans_et);
        transImg = (ImageView) findViewById(R.id.trans_img);
        transTv = (TextView) findViewById(R.id.trans_tv);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        initToolBar("翻译");
        okHttpUtils = new OkHttpUtils();
        handler = new MyHandler(this);
        InitViews();
        InitDatas();
        InitListeners();
    }

    @Override
    protected void InitViews()
    {
        assignViews();
    }

    @Override
    protected void InitDatas()
    {
        int page = 1,count = 20;
        String url = Common.MEDIC_URL+"?"+String.format(Common.MEDIC_ARG,"铜陵",page,count);
        okHttpUtils.okGet(url, "apikey", Common.BAIDU_APIKEY, okHttpUtils.new OnHttp()
        {
            @Override
            protected void onFailure(Request request, IOException e)
            {

            }

            @Override
            protected void onResponse(Response response) throws IOException
            {
                String str = response.body().string();
                MedicBean medicBean = JSON.parseObject(str, MedicBean.class);
                Utils.LogE("medicbean:"+medicBean.toString());
            }
        });
    }

    @Override
    protected void InitListeners()
    {
        transImg.setOnClickListener(this);
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
            case R.id.trans_img:
                String url;
                String text = transEt.getText().toString().trim();
                if (!Utils.isMobileNum(text)) {
                    url = Common.TRANSLATE_URL + "?" + String.format(Common.TRANSLATE_ARG, text.replaceAll("\\s", "+"));
                    Utils.LogE("url:" + url);
                    okHttpUtils.okGet(url, "apikey", Common.BAIDU_APIKEY, okHttpUtils.new OnHttp()
                    {

                        @Override
                        protected void onFailure(Request request, IOException e)
                        {
                            Utils.LogE("err:" + request);
                            Message msg = handler.obtainMessage(1000);
                            msg.obj = request.toString();
                            msg.sendToTarget();
                        }

                        @Override
                        protected void onResponse(Response response) throws IOException
                        {
                            String string = URLDecoder.decode(response.body().string(), "utf-8");
                            Utils.LogE(string);
                            TranslateBean translateBean = JSON.parseObject(string, TranslateBean.class);
                            Utils.LogE("translatebean:" + translateBean.toString());
                            Message msg = handler.obtainMessage(1000);
                            msg.obj = translateBean.toString();
                            msg.sendToTarget();
                        }
                    });
                } else {
                    url = Common.MOBILE_URL + "?" + String.format(Common.MOBILE_ARG, text);
                    Utils.LogE("url:" + url);
                    okHttpUtils.okGet(url, "apikey", Common.BAIDU_APIKEY, okHttpUtils.new OnHttp()
                    {
                        @Override
                        protected void onFailure(Request request, IOException e)
                        {
                            Utils.LogE("err:" + request);
                            Message msg = handler.obtainMessage(1000);
                            msg.obj = request.toString();
                            msg.sendToTarget();
                        }

                        @Override
                        protected void onResponse(Response response) throws IOException
                        {
                            String string = URLDecoder.decode(response.body().string(), "utf-8");
                            Utils.LogE(string);
                            Message msg = handler.obtainMessage(1000);
                            msg.obj = string;
                            msg.sendToTarget();
                        }
                    });
                }

                break;
        }
    }

    private static class MyHandler extends Handler
    {
        private WeakReference<TranslateActivity> wk;

        public MyHandler(TranslateActivity translateActivity)
        {
            this.wk = new WeakReference<>(translateActivity);
        }

        @Override
        public void dispatchMessage(Message msg)
        {
            super.dispatchMessage(msg);
            TranslateActivity translateActivity = wk.get();
            translateActivity.transTv.setText("结果:\n" + msg.obj);
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        handler.removeCallbacksAndMessages(1000);
    }
}
