package biggun.yanshuo.picture;

import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.biggun.yslibrary.Base.BIGGUN_Activity;
import com.biggun.yslibrary.Utils.Utils;
import com.biggun.yslibrary.View.BIGGUN_MaskImageView;
import com.biggun.yslibrary.View.RecyclerView.ImageGridDecoration;
import com.biggun.yslibrary.View.RecyclerView.OnitemClickListener;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import biggun.yanshuo.picture.Adpter.ImageAdapter;
import biggun.yanshuo.picture.Bean.ImageBean;
import biggun.yanshuo.picture.Bean.ImageDirectory;

public class ImageActivity extends BIGGUN_Activity
{
    ImageDirectory mDirectory;
    RecyclerView mRecyclerView;
    ImageAdapter mImageAdapter;
    String parent;
    List<ImageBean> mBeanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        InitViews();
        InitDatas();
        InitListeners();
    }

    @Override
    protected void InitViews()
    {
        mRecyclerView = findView(R.id.image_activity_recview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(null, 4);
        mRecyclerView.addItemDecoration(new ImageGridDecoration(getResources().getDrawable(R.drawable.drawablebottom),
                getResources().getDrawable(R.drawable.drawableright)));
        mRecyclerView.setLayoutManager(gridLayoutManager);
    }

    @Override
    protected void InitDatas()
    {
        mImageAdapter = new ImageAdapter(mBeanList, this);
        mRecyclerView.setAdapter(mImageAdapter);

        mDirectory = getIntent().getParcelableExtra("imagedirectory");
        parent = mDirectory.getParentPath() + File.separator;
        File file = new File(mDirectory.getParentPath());
        String[] list = file.list(new FilenameFilter()
        {
            @Override
            public boolean accept(File dir, String filename)
            {
                return Utils.isImg(filename);
            }
        });
        ImageBean bean = null;
        File file1 = null;
        for (String str : list) {
            file1 = new File(parent + File.separator + str);
            bean = new ImageBean(str,
                    file1.length(), parent, getTimefromFile(file1));
            mBeanList.add(bean);
        }
        int index = mBeanList.size() % 4;
        Collections.sort(mBeanList);
        if (index != 0) {
            for (int i = 0; i < 4 - index; i++) {
                mBeanList.add(new ImageBean("", 0, null, 0));
            }
        }
        mImageAdapter.notifyDataSetChanged();

//        // TODO: 2016/7/4 试试Exifinterface
//        try {
//            ExifInterface exifInterface = new ExifInterface(parent + mBeanList.get(0).getName());
//            int attributeInt = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
//            float[] latlng = new float[2];
//            exifInterface.getLatLong(latlng);
//            LogUtils.LogE(attributeInt + "----" + latlng[0] + "----" + latlng[1]);
//            Geocoder geocoder = new Geocoder(this);
//            List<Address> location = geocoder.getFromLocation(latlng[0], latlng[1], 1);
//            for (Address address : location) {
//                LogUtils.LogE(address.toString());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void InitListeners()
    {
        mRecyclerView.addOnItemTouchListener(new OnitemClickListener(mRecyclerView)
        {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder)
            {
            }

            @Override
            public void onItemLongClick(View view, RecyclerView.ViewHolder holder)
            {
                BIGGUN_MaskImageView img = (BIGGUN_MaskImageView) view.findViewById(R.id.image_adapter_layout_img);
                int position = holder.getLayoutPosition();
                ImageBean imageBean = mBeanList.get(position);
                img.setMask(!imageBean.islongClick());
                imageBean.setIslongClick(!imageBean.islongClick());
            }
        });
    }

    @Override
    protected void ConvertMessage(BIGGUN_Activity BIGGUNActivity, Message msg)
    {
        Settings.Secure.getString(getContentResolver(),"bluetooth_address");//4.2-6.0
    };

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mImageAdapter.clearBitmapCache();
    }

    private long getTimefromFile(File file)
    {
        return file.lastModified();
    }
}
