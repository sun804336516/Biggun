package biggun.yanshuo.picture;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.view.View;

import com.biggun.yslibrary.Base.BIGGUN_Activity;
import com.biggun.yslibrary.Utils.CaptureUtils;
import com.biggun.yslibrary.Utils.LogUtils;

import java.io.File;

public class CaptureActivity extends BIGGUN_Activity
{
    File file = new File(Environment.getExternalStorageDirectory() + "/my.jpg");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);

        InitViews();
        InitDatas();
        InitListeners();
    }

    @Override
    protected void InitViews()
    {
    }

    @Override
    protected void InitDatas()
    {

    }

    @Override
    protected void InitListeners()
    {

    }

    @Override
    protected void ConvertMessage(BIGGUN_Activity BIGGUNActivity, Message msg)
    {

    }

    static final int hasUri = 1;
    static final int noUri = hasUri << 1;

    public void nouri(View view)
    {
        startActivityForResult(CaptureUtils.getCaptureIntent(null), noUri);
    }

    public void hasuri(View view)
    {
        startActivityForResult(CaptureUtils.getCaptureIntent(Uri.fromFile(file)), hasUri);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // TODO: 2016/6/30 data为null则是指定了uri，不为null则是没有指定uri
        LogUtils.LogE("data:"+data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case hasUri:
                    Bitmap bitmap1 = BitmapFactory.decodeFile(file.getAbsolutePath());
                    LogUtils.LogE(bitmap1.getWidth() + "==" + bitmap1.getHeight());
                    break;
                case noUri:
                    Bitmap bitmap2 = (Bitmap) data.getExtras().get("data");
                    LogUtils.LogE(bitmap2.getWidth() + "==" + bitmap2.getHeight());
                    break;
            }
        }
    }
}
