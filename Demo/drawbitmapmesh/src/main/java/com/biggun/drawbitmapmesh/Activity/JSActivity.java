package com.biggun.drawbitmapmesh.Activity;

import android.animation.Animator;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;

import com.biggun.drawbitmapmesh.R;
import com.biggun.drawbitmapmesh.Util.Utils;

public class JSActivity extends BaseActivity implements View.OnClickListener
{
    private WebView mWebView;
    private Button js_btn1, js_btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_js);
        InitViews();
        InitDatas();
        InitListeners();
    }

    @Override
    protected void InitViews()
    {
        mWebView = findView(R.id.js_webview);
        js_btn1 = findView(R.id.js_button_1);
        js_btn2 = findView(R.id.js_button_2);
    }

    @Override
    protected void InitDatas()
    {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.loadUrl("file:///android_asset/test.html");

        JavaScriptinterface jsInterface = new JavaScriptinterface();
//        mWebView.addJavascriptInterface(this,"android");
        mWebView.addJavascriptInterface(jsInterface, "Android");
    }

    @Override
    protected void InitListeners()
    {
        js_btn1.setOnClickListener(this);
        js_btn2.setOnClickListener(this);
    }

    @Override
    protected void onToolBarAnimationEnd(Animator animator)
    {

    }

    @Override
    protected boolean OnToolbarItemClick(MenuItem item)
    {
        return false;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.js_button_1:
                mWebView.loadUrl("javascript:javacalljs()");
                break;
            case R.id.js_button_2:
                String str = "我是Android的王大枪";
                String url = "javascript:javacalljswith('" + str + "')";
                Utils.LogE("urL:"+url);
                mWebView.loadUrl(url);
                break;
        }
    }

    public class JavaScriptinterface
    {
        Context mContext;

        public JavaScriptinterface(Context context)
        {
            mContext = context;
        }

        public JavaScriptinterface()
        {
        }

        @JavascriptInterface
        public void show()
        {
            getAlertDialog("js无参方法调Android", "成功", -1, null).show();
        }

        @JavascriptInterface
        public void show(String text)
        {
            getAlertDialog("js有参方法调Android", text, -1, null).show();
        }
    }


    @JavascriptInterface
    public void startFunction()
    {
        getAlertDialog("js无参方法调Android", "成功", -1, null).show();
    }

    @JavascriptInterface
    public void startFunction(String text)
    {
        getAlertDialog("js有参方法调Android", text, -1, null).show();
    }
}
