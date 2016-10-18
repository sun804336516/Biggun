package com.biggun.yslibrary.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 作者：孙贤武 on 2016/6/29 17:16
 * 邮箱：sun91985415@163.com
 * LIKE:YANSHUO
 * 遮罩的ImageView,同样可以添加叉号
 */
public class BIGGUN_MaskImageView extends ImageView
{
    public BIGGUN_MaskImageView(Context context)
    {
        this(context, null);
    }

    public BIGGUN_MaskImageView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    private boolean NeedMask = false;
    private int MaskColor = 0x86222222;

    public BIGGUN_MaskImageView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        if (NeedMask) {
            canvas.drawColor(MaskColor);
            // TODO: 2016/6/29 添加叉号？
        } else {
            canvas.drawColor(Color.TRANSPARENT);
        }
    }

    /**
     * 添加遮罩
     *
     * @param mask
     */
    public void setMask(boolean mask)
    {
        this.NeedMask = mask;
        invalidate();
    }
}
