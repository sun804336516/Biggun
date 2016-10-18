package com.biggun.drawbitmapmesh.Animation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.biggun.drawbitmapmesh.Util.Utils;

/**
 * 作者：孙贤武 on 2016/2/15 15:12
 * 邮箱：sun91985415@163.com
 */
public class Rotate3DAnimation extends Animation
{
    int centerX,centerY;
    int fromDegress,toDegress;
    int dephZ;
    Camera camera;

    public Rotate3DAnimation(int centerX, int centerY, int fromDegress, int toDegress, int dephZ)
    {
        this.centerX = centerX;
        this.centerY = centerY;
        this.fromDegress = fromDegress;
        this.toDegress = toDegress;
        this.dephZ = dephZ;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight)
    {
        super.initialize(width, height, parentWidth, parentHeight);
        camera = new Camera();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t)
    {
        super.applyTransformation(interpolatedTime, t);

        Matrix matrix = t.getMatrix();
        camera.save();

        float degress = fromDegress +(toDegress - fromDegress)*interpolatedTime;
        Utils.LogE("degress:"+degress);
        camera.translate(0.0f,0.0f,dephZ*interpolatedTime);
        camera.rotateZ(degress);
        camera.getMatrix(matrix);
        camera.restore();

        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX,centerY);
    }
}
