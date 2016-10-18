package com.biggun.drawbitmapmesh.Util;

import android.graphics.Path;
import android.graphics.RectF;

/**
 * 作者：孙贤武 on 2016/4/7 11:08
 * 邮箱：sun91985415@163.com
 */
public class DrawUtils
{
    private static int Angle_90 = 90;

    /**
     * 获取一个圆角path
     *
     * @param width  宽
     * @param height 高
     * @param radius 圆角半径
     * @return
     */
    public static Path getRoundPath(int width, int height, int radius)
    {
        Path path = new Path();
        path.moveTo(0, radius);
        // 左上
        RectF arcTopLeft = new RectF(0, 0, radius << 1, radius << 1);
        path.arcTo(arcTopLeft, -(Angle_90 << 1), Angle_90);
        // 右上.
        RectF arcTopRight = new RectF(width - (radius << 1), 0, width, radius << 1);
        path.arcTo(arcTopRight, -Angle_90, Angle_90);
        // 右下.
        RectF arcBottomRight = new RectF(width - (radius << 1),
                height - (radius << 1), width, height);
        path.arcTo(arcBottomRight, 0, Angle_90);
        // 左下.
        RectF arc = new RectF(0, height - (radius << 1), radius << 1, height);
        path.arcTo(arc, Angle_90, Angle_90);
        path.close();//首尾相连
        return path;
    }

    /**
     * 获取一个圆形path
     * @param width
     * @param height
     * @return
     */
    public static Path getCirclePath(int width, int height)
    {
        Path path = new Path();
        int radius = Math.min(width, height);
        path.addCircle(width >> 1, height >> 1, radius >> 1, Path.Direction.CCW);
        return path;
    }
}
