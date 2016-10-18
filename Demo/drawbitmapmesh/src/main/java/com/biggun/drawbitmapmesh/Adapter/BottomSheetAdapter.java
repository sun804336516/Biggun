package com.biggun.drawbitmapmesh.Adapter;

import android.content.Context;

import com.biggun.drawbitmapmesh.Bean.TestDatabaseBean;
import com.biggun.drawbitmapmesh.R;

import java.util.List;

/**
 * 作者：孙贤武 on 2016/2/29 17:11
 * 邮箱：sun91985415@163.com
 */
public class BottomSheetAdapter extends BaseAdapter<TestDatabaseBean>
{
    public BottomSheetAdapter(List<TestDatabaseBean> mList, Context mContext, int mLayoutId)
    {
        super(mList, mContext, mLayoutId);
    }

    @Override
    protected void Convert(HolderHandle holderHandle, int position)
    {
        TestDatabaseBean testDatabaseBean = mList.get(position);
        holderHandle.setText(R.id.bottom_sheet_name, testDatabaseBean.getName()).setText(R.id.bottom_sheet_address, testDatabaseBean.getAddress());
    }
}
