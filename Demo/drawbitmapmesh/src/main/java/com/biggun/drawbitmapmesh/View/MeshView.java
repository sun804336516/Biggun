package com.biggun.drawbitmapmesh.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by sun92 on 2015/12/28.
 */
public class MeshView extends View
{
    public MeshView(Context context)
    {
        this(context, null);
    }

    public MeshView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    private Bitmap bitmap;
    private int meshWidth = 20;
    private int meshHeight = 20;
    private int count = (meshWidth + 1) * (meshHeight + 1);
    private float[] verts = new float[count * 2];
    private float[] origin = new float[count * 2];

    public MeshView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public void setBitmap(Bitmap bitmap)
    {
        this.bitmap = bitmap;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int index = 0;
        for (int y = 0; y <= meshHeight; y++)
        {
            float fy = height * y / meshHeight;
            for (int x = 0; x <= meshWidth; x++)
            {
                float fx = width * x / meshWidth;
                origin[index * 2 + 0] = verts[index * 2 + 0] = fx;
                origin[index * 2 + 1] = verts[index * 2 + 1] = fy;
                index++;
            }
        }
        setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.drawBitmapMesh(bitmap, meshWidth, meshHeight, verts, 0, null, 0, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        Vert(event.getX(), event.getY());
        return true;
    }

    private void Vert(float x, float y)
    {
        for (int i = 0; i < count; i += 2)
        {
            float dx = x - origin[i + 0];
            float dy = y - origin[i + 1];
            float dd = dx * dx + dy * dy;

            float d = (float) Math.sqrt(dd);

            float pull = 50000 / (dd * d);
            if (pull >= 1)
            {
                verts[i + 0] = x;
                verts[i + 1] = y;
            } else
            {
                verts[i + 0] = origin[i + 0] + dx * pull;
                verts[i + 1] = origin[i + 1] + dy * pull;
            }

        }
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
