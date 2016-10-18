package com.biggun.drawbitmapmesh.View;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.biggun.drawbitmapmesh.Util.Utils;

/**
 * 作者：孙贤武 on 2016/3/16 13:43
 * 邮箱：sun91985415@163.com
 * 水平滑动的TextView，适用于简书的代码区
 */
public class HorizontextView extends HorizontalScrollView
{
    public HorizontextView(Context context)
    {
        this(context, null);
    }

    public HorizontextView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    private TextView textView;
    private Paint paint;

    public HorizontextView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public void setText(String text)
    {
        removeAllViews();
        textView = new TextView(getContext());
        paint = textView.getPaint();
//        int textWidth = measureTextWidth(text);//突然发现计不计算没什卵用
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setText(text);
        addView(textView, lp);
    }

    private int measureTextWidth(String text)
    {
        int textWidth = 0;
        String[] strs = text.split("\\n");
        for (String s : strs) {
            textWidth = (int) Math.max(textWidth, paint.measureText(s));
            Utils.LogE("TextWidth:" + textWidth);
        }
        return textWidth;
    }
}
