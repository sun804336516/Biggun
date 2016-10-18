package com.biggun.drawbitmapmesh.Activity;

import android.animation.Animator;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.view.MenuItem;

import com.biggun.drawbitmapmesh.Adapter.BaseAdapter;
import com.biggun.drawbitmapmesh.Adapter.HolderHandle;
import com.biggun.drawbitmapmesh.Adapter.ImageAdater;
import com.biggun.drawbitmapmesh.Adapter.ImageGridDecoration;
import com.biggun.drawbitmapmesh.Bean.ImageBean;
import com.biggun.drawbitmapmesh.Common;
import com.biggun.drawbitmapmesh.R;
import com.biggun.drawbitmapmesh.View.SmothhRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImageBrowserActivity extends BaseActivity
{
    private SmothhRecyclerView srcv;
    private ImageAdater adater;
    private List<ImageBean> imageBeanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_browser);
        initToolBar("本地图片浏览");
        InitViews();
        InitDatas();
        InitListeners();
    }

    @Override
    protected void InitViews()
    {
        srcv = findView(R.id.imagebrowse_rcv);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(null, 3, GridLayoutManager.VERTICAL, false);
        srcv.setLayoutManager(gridLayoutManager);
        srcv.addItemDecoration(new ImageGridDecoration(getResources().getDrawable(R.drawable.drawablebottom), getResources().getDrawable(R.drawable.drawableright)));
    }

    @Override
    protected void InitDatas()
    {
        Cursor imagecursor = getContentResolver().query(Common.IMAGE_URI, Common.IMAGE_PROJECTION
                , Common.IMAGE_SELECTION, Common.IMAGE_SELECTION_ARGS, Common.IMAGE_SORT_ORDER);
        if (imagecursor != null) {
            ImageBean bean = null;
            while (imagecursor.moveToNext()) {
                long size = imagecursor.getLong(imagecursor.getColumnIndex(MediaStore.Images.Media.SIZE));
                if (size <= 0) {
                    continue;
                }
                String path = imagecursor.getString(imagecursor.getColumnIndex(MediaStore.Images.Media.DATA));
                String name = imagecursor.getString(imagecursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                int _id = imagecursor.getInt(imagecursor.getColumnIndex(MediaStore.Images.Media._ID));
                Uri uri_temp = Uri.parse("content://media/external/images/media/"
                        + _id);
                bean = new ImageBean(_id, path, name, size, uri_temp);
                imageBeanList.add(bean);
                Collections.reverse(imageBeanList);
            }
            imagecursor.close();
        }
        adater = new ImageAdater(imageBeanList, this, R.layout.image_layout, srcv);
        srcv.setAdapter(adater);
    }

    @Override
    protected void InitListeners()
    {
        adater.setOnItemClickListener(new BaseAdapter.OnItemClick()
        {
            @Override
            public void onItemCkick(HolderHandle holderHandle, int position)
            {
//                Intent intent = new Intent();
//                Bundle bundle = new Bundle();
//                bundle.putString("data", imageBeanList.get(position).getPath());
//                Utils.LogE(imageBeanList.get(position).getPath());
//                intent.putExtras(bundle);
//                intent.setData(imageBeanList.get(position).getUri());
//                setResult(RESULT_OK, intent);
//                finish();
                Intent intent = new Intent(ImageBrowserActivity.this, ScaleImgActivity.class);
                intent.putExtra("imgpath", imageBeanList.get(position).getPath());
                startActivity(intent);
            }
        });
    }

    @Override
    protected boolean OnToolbarItemClick(MenuItem item)
    {
        return false;
    }

    @Override
    protected void onToolBarAnimationEnd(Animator animator)
    {

    }

    @Override
    public void finish()
    {
        super.finish();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        adater.clearBitmapCache();
    }
}
