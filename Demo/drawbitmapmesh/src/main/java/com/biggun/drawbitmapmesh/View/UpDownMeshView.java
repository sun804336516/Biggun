package com.biggun.drawbitmapmesh.View;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;

import com.biggun.drawbitmapmesh.R;

/**
 * Created by sun92 on 2015/12/30.
 */
public class UpDownMeshView extends View
{
    private Bitmap bitmap;
    private final int WIDTH = 80;
    private final int HEIGHT = 80;
    private float[] verts = new float[(WIDTH + 1) * (HEIGHT + 1) * 2];
    private float downY;

    public UpDownMeshView(Context context)
    {
        this(context, null);
    }

    public UpDownMeshView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public UpDownMeshView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.tree);
        updateMesh(0);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.translate((getWidth() - bitmap.getWidth()) >> 1, (getHeight() - bitmap.getHeight()) >> 1);
        canvas.drawBitmapMesh(bitmap, WIDTH, HEIGHT, verts, 0, null, 0, null);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                updateMesh(0);
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                updateMesh((event.getY() - downY) / bitmap.getHeight());
                break;
            case MotionEvent.ACTION_UP:
                ValueAnimator anitor = ValueAnimator.ofFloat((event.getY() - downY) / bitmap.getHeight(), 0);
                anitor.setInterpolator(new AnticipateOvershootInterpolator());
                anitor.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
                {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation)
                    {
                        float ratio = (float) animation.getAnimatedValue();
                        updateMesh(ratio);
                        invalidate();
                    }
                });
                anitor.start();
                break;
        }
        invalidate();
        return true;
    }

    private void updateMesh(float ratio)
    {

        //开口向下的抛物线 y = 1-x*;
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        float XStep = bitmapWidth / WIDTH;
        float MIDDLE = bitmapWidth >> 1;
        float YRatioHeight = ratio * bitmapHeight;

        for (int x = 0; x <= WIDTH; x++)
        {
            float XIndex = x * XStep;
            float XDistance = XIndex <= MIDDLE ? XIndex : bitmapWidth - XIndex;
            float XRatio = 1f - XDistance / MIDDLE;
            float YTop = YRatioHeight * (1f - XRatio * XRatio);

            for (int y = 0; y <= HEIGHT; y++)
            {
                float YIndex = YTop + (bitmapHeight - YTop) / HEIGHT * y;
                int index = (WIDTH + 1) * y * 2 + x * 2;
                verts[index] = XIndex;
                verts[index + 1] = YIndex;
            }
        }
    }
}
