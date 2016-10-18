package com.biggun.drawbitmapmesh.Service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.biggun.drawbitmapmesh.R;
import com.biggun.drawbitmapmesh.Util.Utils;

/**
 * 作者：孙贤武 on 2016/2/16 16:09
 * 邮箱：sun91985415@163.com
 * 悬浮窗Service
 */
public class FloatService extends Service implements View.OnClickListener, View.OnLongClickListener
{
    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    WindowManager wm;
    WindowManager.LayoutParams wmlP;
    ImageView floatImg;

    @Override
    public void onCreate()
    {
        super.onCreate();
        wm = (WindowManager) getSystemService(getApplicationContext().WINDOW_SERVICE);
        wmlP = new WindowManager.LayoutParams();
        wmlP.type = WindowManager.LayoutParams.TYPE_TOAST;//窗口提示
        wmlP.format = PixelFormat.TRANSLUCENT;
        wmlP.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//        LayoutParams.FLAG_NOT_TOUCH_MODAL 不影响后面的事件
//        LayoutParams.FLAG_NOT_FOCUSABLE 不可聚焦
//        LayoutParams.FLAG_NOT_TOUCHABLE 不可触摸
        wmlP.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmlP.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wmlP.gravity = Gravity.RIGHT | Gravity.TOP;
        wmlP.x = 0;
        wmlP.y = 0;
        floatImg = new ImageView(getApplicationContext());
        floatImg.setOnClickListener(this);
        floatImg.setOnLongClickListener(this);
        floatImg.setImageResource(R.mipmap.skala);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if (floatImg.getParent() == null) {
            wm.addView(floatImg, wmlP);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        wm.removeView(floatImg);
    }

    @Override
    public void onClick(View v)
    {
        if (v.equals(floatImg)) {
//            Toast.makeText(getApplicationContext(),"float",Toast.LENGTH_SHORT).show();
            Utils.LogE("float");
            back();
            //弹出popupwindow
        }
    }

    private void back()
    {
//        new Thread(new Runnable()//INJECT_EVENTS权限
//        {
//            @Override
//            public void run()
//            {
//                try {
//                    Instrumentation ist = new Instrumentation();
//                    ist.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
//                } catch (Exception e) {
//                    Utils.LogE("back error:"+e.getMessage());
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//        Runtime runtime = Runtime.getRuntime();
//        try {
//            runtime.exec("input keyevent "+KeyEvent.KEYCODE_BACK);
//        } catch (IOException e) {
//            Utils.LogE("back error:"+e.getMessage());
//            e.printStackTrace();
//        }
    }

    @Override
    public boolean onLongClick(View v)
    {
        Intent intent = new Intent(v.getContext(), FloatService.class);
        stopService(intent);
        return false;
    }
}
