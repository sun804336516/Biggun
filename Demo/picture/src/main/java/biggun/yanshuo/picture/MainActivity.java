package biggun.yanshuo.picture;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.biggun.yslibrary.Base.BIGGUN_Activity;
import com.biggun.yslibrary.Utils.LogUtils;
import com.biggun.yslibrary.View.RecyclerView.OnitemClickListener;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import biggun.yanshuo.picture.Adpter.ImageDireAdapter;
import biggun.yanshuo.picture.Bean.FileBean;
import biggun.yanshuo.picture.Bean.ImageDirectory;

public class MainActivity extends BIGGUN_Activity
{
    RecyclerView mRecyclerView;
    ImageDireAdapter dirAdapter;

    List<ImageDirectory> mImageDirectoryList = new ArrayList<>();
    List<FileBean> mFileBeanList = new ArrayList<>();

    EditText ed;
    DecimalFormat decimalFormat = new DecimalFormat(",###,###.##");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitViews();
        InitDatas();
        InitListeners();
    }

    @Override
    protected void InitViews()
    {
        mRecyclerView = findView(R.id.recyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(null, 2);
//        mRecyclerView.addItemDecoration(new ImageGridDecoration(getResources().getDrawable(R.drawable.drawablebottom),
//                getResources().getDrawable(R.drawable.drawableright)));
        mRecyclerView.setLayoutManager(gridLayoutManager);

        ed = findView(R.id.testtext);
        ed.addTextChangedListener(new MyWatcher(ed));
    }

    @Override
    protected void InitDatas()
    {
        dirAdapter = new ImageDireAdapter(mImageDirectoryList, this);
        mRecyclerView.setAdapter(dirAdapter);
        MediaStoreUtils.getImageList(this, new String[]{"image/jpeg", "image/png"}, mImageDirectoryList);
        LogUtils.LogE("parent:" + mImageDirectoryList.toString());
        dirAdapter.notifyDataSetChanged();
//        MediaStoreUtils.getFileList(this, mFileBeanList, new String[]{"%.png", "%.jpeg", "%.jpg"});
        LogUtils.LogE("text:" + mFileBeanList.toString());
    }

    @Override
    protected void InitListeners()
    {
        mRecyclerView.addOnItemTouchListener(new OnitemClickListener(mRecyclerView)
        {
            @Override
            public void onItemClick(View view,RecyclerView.ViewHolder holder)
            {
                ImageDirectory directory = mImageDirectoryList.get(holder.getAdapterPosition());
                LogUtils.LogE("directory:" + directory.toString());
                Intent intent = new Intent(MainActivity.this, ImageActivity.class);
                intent.putExtra("imagedirectory", directory);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view,RecyclerView.ViewHolder holder)
            {

            }
        });
    }

    @Override
    protected void ConvertMessage(BIGGUN_Activity BIGGUNActivity, Message msg)
    {
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        dirAdapter.clearBitmapCache();
    }

    class MyWatcher implements TextWatcher
    {
        EditText mEditText;

        public MyWatcher(EditText editText)
        {
            mEditText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            String string = s.toString();
            int lastIndexOf = string.lastIndexOf(".");
            int length = string.length();
            LogUtils.LogE(lastIndexOf + "---" + length);
            if (lastIndexOf == length - 2 || lastIndexOf == length - 1) {
                return;
            }
            if (mEditText.getId() == R.id.testtext && !TextUtils.isEmpty(string)) {
                String s1 = string.replaceAll(",", "");
                BigDecimal bigDecimal = new BigDecimal(s1);

                String format = decimalFormat.format(bigDecimal);
                LogUtils.LogE("format:" + format);
                mEditText.removeTextChangedListener(this);
                mEditText.setText(format);
                mEditText.setSelection(format.length());
                mEditText.addTextChangedListener(this);
            }
        }

        @Override
        public void afterTextChanged(Editable s)
        {
        }
    }
}
