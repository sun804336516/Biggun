package com.biggun.drawbitmapmesh.Activity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.biggun.drawbitmapmesh.Adapter.BaseAdapter;
import com.biggun.drawbitmapmesh.Adapter.BrowserAdapter;
import com.biggun.drawbitmapmesh.Adapter.HolderHandle;
import com.biggun.drawbitmapmesh.Bean.BrowserBean;
import com.biggun.drawbitmapmesh.R;
import com.biggun.drawbitmapmesh.Util.LogUtils;
import com.biggun.drawbitmapmesh.Util.StringUtils;
import com.biggun.drawbitmapmesh.Util.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.zxing.Result;

public class SdcardBrowserActivity extends BaseActivity implements BaseAdapter.OnItemClick
{
    private RecyclerView rcView;
    private BrowserAdapter adapter;
    private List<BrowserBean> list = new ArrayList<>();
    private String currentPath = Environment.getExternalStorageDirectory().getPath();
    private String currentText = currentPath;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdcard_browser);

        InitViews();
        InitDatas();
        InitListeners();

    }

    @Override
    protected void InitViews()
    {
        rcView = (RecyclerView) this.findViewById(R.id.recyclerview);
        rcView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void InitDatas()
    {
        adapter = new BrowserAdapter(list, this, R.layout.browser_layout);
        rcView.setAdapter(adapter);
        refreshData(currentPath);
    }

    private void refreshData(String currentPath)
    {
        list.clear();
        BrowserBean browserBean = null;
        File[] files = new File(currentPath).listFiles();
        for (File file : files) {
            Utils.LogE("filename:" + file.getAbsolutePath());
            if (file.isDirectory()) {
                browserBean = new BrowserBean(file.getName(), R.drawable.directory_vector, false);
            } else if (file.isFile()) {
                browserBean = new BrowserBean(file.getName(), R.drawable.file_vector, true);
            }
            list.add(browserBean);
        }
        Collections.sort(list);
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void InitListeners()
    {
        adapter.setOnItemClickListener(this);
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
    public void onItemCkick(HolderHandle holderHandle, int position)
    {
        BrowserBean browserBean = list.get(position);
        currentText = currentText + File.separator + browserBean.getText();
        if (!browserBean.isfile()) {
            refreshData(currentText);
        } else {
            Utils.LogE(currentText);
            if(Utils.isTxt(currentText))
            {
                try {
                    LogUtils.e("Txt:"+ StringUtils.RandomRead(currentText));
                    StringUtils.Readzhangjie(StringUtils.RandomRead(currentText));
                } catch (IOException e) {
                    LogUtils.e("TxtError:"+e.getLocalizedMessage());
                    e.printStackTrace();
                }
                currentText = new File(currentText).getParent();//重置curenttext
                return;
            }else if (Utils.isImg(currentText)) {
                Result result = Utils.getQrCodeFromBitmap(currentText);
                Toast.makeText(this, result == null ? "该图中没有二维码信息" :"该图中二维码信息："+ result.getText(), Toast.LENGTH_LONG).show();
                currentText = new File(currentText).getParent();//重置curenttext
                return;
            }
            Intent intent = new Intent();
            intent.putExtra("apkpath", currentText);
            setResult(RESULT_OK, intent);
//            File f = new File(currentText);
//            String type = getMimeType(f);
//            String type = Utils.getMimeType(currentText);
//            Intent intent = new Intent();
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setAction(Intent.ACTION_VIEW);
//            if (!TextUtils.isEmpty(type)) {
//                intent.setDataAndType(Uri.fromFile(f), type);
//            }//未知类型文件怎么打开？？？
//            startActivity(intent);
            currentText = new File(currentText).getParent();//重置curenttext
            finish();
        }
    }

    @Override
    public void onBackPressed()
    {
        if (currentText.equals(currentPath)) {
            super.onBackPressed();
        } else {
            currentText = new File(currentText).getParent();
            refreshData(currentText);
        }
    }
}
