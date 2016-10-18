package com.biggun.drawbitmapmesh.Activity;

import android.animation.Animator;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;

import com.biggun.drawbitmapmesh.R;
import com.biggun.drawbitmapmesh.Util.StringUtils;
import com.biggun.drawbitmapmesh.Util.Utils;

import rx.Observable;
import rx.Subscriber;

public class CardActivity extends BaseActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_android);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs)
    {
        useSlide = false;
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected void InitViews()
    {

    }

    @Override
    protected void InitDatas()
    {
        /**
         * RxAndroid
         */
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>()
        {
            @Override
            public void call(Subscriber<? super String> subscriber)
            {
                subscriber.onNext("test rxandroid");
                subscriber.onCompleted();
            }
        });
        Subscriber<String> subscriber = new Subscriber<String>()
        {
            @Override
            public void onCompleted()
            {

            }

            @Override
            public void onError(Throwable e)
            {

            }

            @Override
            public void onNext(String s)
            {
                Utils.LogE(s);
            }
        };
        observable.subscribe(subscriber);

    }

    @Override
    protected void InitListeners()
    {

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
}
