package com.biggun.yslibrary.View.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biggun.yslibrary.Base.BIGGUN_Adapter;
import com.biggun.yslibrary.R;
import com.biggun.yslibrary.View.BIGGUN_MyShimmerTextView;
import com.biggun.yslibrary.View.RecyclerView.VerticalRecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 作者：孙贤武 on 2016/5/19 15:34
 * 邮箱：sun91985415@163.com
 * LIKE:YANSHUO
 * 下拉刷新的RecyclerView
 */
public class BIGGUN_PullRefrshLayout extends LinearLayout
{
    public BIGGUN_PullRefrshLayout(Context context)
    {
        this(context, null);
    }

    public BIGGUN_PullRefrshLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public BIGGUN_PullRefrshLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        ViewConfiguration mConfiguration = ViewConfiguration.get(context);
        touchY = mConfiguration.getScaledTouchSlop();
        refreshView = LayoutInflater.from(context).inflate(R.layout.refresh_layout, null);
        promptTv = (BIGGUN_MyShimmerTextView) refreshView.findViewById(R.id.refresh_prompttv);
        lastRefreshTv = (TextView) refreshView.findViewById(R.id.refresh_lasttv);
        meaSureView(refreshView);
        refreshHeight = refreshView.getMeasuredHeight();
        defaultHeight = (refreshHeight << 1) + refreshHeight;
        setrefreshPaddingTop(-refreshHeight);
        addView(refreshView);
        mrcv = new VerticalRecyclerView(context);
        addView(mrcv);
        lastTime = System.currentTimeMillis();

        mrcv.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                if (mMoveDownListener != null) {
                    View view = mrcv.getChildAt(0);
                    if (mrcv.getFirstVisiableItem() == 0) {
                        mMoveDownListener.movedown(view.getTop(), view.getHeight());
                    } else {
                        mMoveDownListener.movedown(-view.getHeight(), view.getHeight());
                    }
                }
            }
        });
    }

    public void setAdapter(BIGGUN_Adapter adapter)
    {
        if (mrcv == null) {
            return;
        }
        mrcv.setAdapter(adapter);
    }

    static final long ANI_DURATION = 400;
    float touchY;
    float donwY;
    float moveY;
    VerticalRecyclerView mrcv;
    View refreshView;
    BIGGUN_MyShimmerTextView promptTv;
    TextView lastRefreshTv;
    int refreshHeight;
    int defaultHeight;
    long lastTime;

    ReFreshListener mFreshListener;
    private boolean isRefreshing = false;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                promptTv.seekTo(0);
                donwY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = ev.getY() - donwY;
                if (!isRefreshing && moveY > 0 && Math.abs(moveY) > touchY
                        && mrcv.getFirstVisiableItem() == 0 && mrcv.getChildAt(0).getTop() >= 0) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int del = (defaultHeight >> 1) + (defaultHeight >> 2);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                moveY = event.getY() - donwY;
                lastRefreshTv.setText("上次刷新时间:" + formatlong2Time(lastTime + ""));
                if (moveY < del) {
                    promptTv.setText("下拉刷新");
                } else {
                    promptTv.setText("松开刷新");
                }
                setrefreshPaddingTop(DampingEffect(moveY / defaultHeight) * refreshHeight - refreshHeight);
                if (moveY <= (defaultHeight << 1)) {
                    promptTv.seekTo(moveY / (defaultHeight << 1));
                }
                break;
            case MotionEvent.ACTION_UP:
                if (moveY < del) {//还原位置
                    Animaterefresh(true);
                } else {
                    if (mFreshListener != null) {
                        promptTv.setText("正在刷新");
                        isRefreshing = true;
                        Animaterefresh(false);
                    } else {
                        Animaterefresh(true);
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }


    public void refreshComplete()
    {
        Animaterefresh(true);
        promptTv.setText("刷新完成");
        isRefreshing = false;
        lastTime = System.currentTimeMillis();
    }


    private void meaSureView(View view)
    {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int widspec = ViewGroup.getChildMeasureSpec(0, 0, lp.width);
        int hei = lp.height;
        int heispec = 0;
        if (hei > 0) {
            heispec = MeasureSpec.makeMeasureSpec(hei, MeasureSpec.EXACTLY);
        } else {
            heispec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        view.measure(widspec, heispec);
    }

    private void setrefreshPaddingTop(float top)
    {
        if (refreshView == null) {
            return;
        }
        refreshView.setPadding(refreshView.getPaddingLeft(), (int) top, refreshView.getPaddingRight(), refreshView.getPaddingBottom());
    }

    private void Animaterefresh(final boolean isDown)
    {
        final ValueAnimator ani = ValueAnimator.ofFloat(refreshView.getPaddingTop(), isDown ? -refreshHeight : 0);
        ani.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                float value = (float) animation.getAnimatedValue();
                setrefreshPaddingTop(value);
            }
        });
        ani.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);
                if (!isDown) {
                    mFreshListener.onRefreshing();
                }
            }
        });
        ani.setDuration(ANI_DURATION);
        ani.start();

        ValueAnimator ani2 = ValueAnimator.ofFloat(promptTv.getCurrentFraction(), isDown ? 0 : 1);
        ani2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                float value = (float) animation.getAnimatedValue();
                promptTv.seekTo(value);
            }
        });
        ani2.setDuration(ANI_DURATION);
        ani2.start();
    }

    public abstract class ReFreshListener
    {
        public abstract void onRefreshing();
    }

    public void setFreshListener(ReFreshListener freshListener)
    {
        mFreshListener = freshListener;
    }

    /**
     * 添加阻尼效果
     *
     * @param x
     * @return
     */
    private float DampingEffect(float x)

    {
//        return (float) Math.sqrt(x);
        return (float) (x <= 1 ? 3 / 4 * x * Math.pow(1 - x, 2) + 9 / 4 * Math.pow(x, 2) * (1 - x) + Math.pow(x, 3)
                : Math.sqrt(x));
    }


    MoveDownListener mMoveDownListener;

    public void setMoveDownListener(MoveDownListener moveDownListener)
    {
        mMoveDownListener = moveDownListener;
    }

    public abstract class MoveDownListener
    {
        public abstract void movedown(float down, float height);
    }

    /**
     * long转换为字符串日期
     *
     * @param time
     * @return
     */
    public static String formatlong2Time(String time)
    {
        SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT);
        return format.format(new Date(System.currentTimeMillis()));
    }

    private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
}
