package com.biggun.yslibrary.View;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * 作者：孙贤武 on 2016/6/21 15:29
 * 邮箱：sun91985415@163.com
 * LIKE:YANSHUO
 */
public class BIGGUN_MoreTextView extends TextView
{
    public BIGGUN_MoreTextView(Context context)
    {
        this(context, null);
    }

    public BIGGUN_MoreTextView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    static final int DEFAULT_LINE = 3;
    int linecount;
    int lineheight;
    int height;
    ValueAnimator ani;

    public BIGGUN_MoreTextView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        setLines(DEFAULT_LINE);
        setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AnimateToggle();
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        linecount = getLineCount();
        if (linecount < DEFAULT_LINE) {
            setLines(linecount);
        }
    }

    public void AnimateToggle()
    {
        lineheight = getLineHeight();
        height = getHeight();
        linecount = getLineCount();
        if (linecount <= DEFAULT_LINE) {
            return;
        }
        int start = height;
        int end = 0;
        if (linecount * lineheight > height) {
            // TODO: 2016/6/21 需要展开
            end = linecount * lineheight;
        } else {
            end = DEFAULT_LINE * lineheight;
        }
        ani = ValueAnimator.ofInt(start, end);
        ani.setDuration(300);
        ani.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                setHeight((int) animation.getAnimatedValue());
            }
        });
        ani.start();
    }
}
