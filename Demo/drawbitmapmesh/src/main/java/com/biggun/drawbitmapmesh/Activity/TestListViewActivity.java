package com.biggun.drawbitmapmesh.Activity;

import android.animation.Animator;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.biggun.drawbitmapmesh.Adapter.ViewHolder;
import com.biggun.drawbitmapmesh.Bean.TestDatabaseBean;
import com.biggun.drawbitmapmesh.R;
import com.biggun.drawbitmapmesh.Util.DbUtils2;
import com.biggun.drawbitmapmesh.Util.Utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class TestListViewActivity extends BaseActivity
{
    ListView listView;
    DbUtils2<TestDatabaseBean> dbUtils2;
    List<TestDatabaseBean> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_list_view);
        dbUtils2 = new DbUtils2<>(this);
        InitViews();
        InitDatas();
        InitListeners();
    }

    @Override
    protected void InitViews()
    {
        listView = findView(R.id.listview);
    }

    @Override
    protected void InitDatas()
    {
        try {
            dbUtils2.readDataBase(new TestDatabaseBean(),list);
            listView.setAdapter(new listAdapter());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void InitListeners()
    {

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
    private class listAdapter extends BaseAdapter
    {
        @Override
        public int getCount()
        {
            return list.size();
        }

        @Override
        public Object getItem(int position)
        {
            return list.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public int getViewTypeCount()
        {
            return super.getViewTypeCount();
        }

        @Override
        public int getItemViewType(int position)
        {
            return super.getItemViewType(position);
        }
        private ViewHolder holder;
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            Utils.LogE("position-"+position);
            if(convertView == null)
            {
                convertView = LayoutInflater.from(TestListViewActivity.this).inflate(R.layout.browser_layout,parent,false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv.setText(list.get(position).getName());
            return convertView;
        }
        class ViewHolder
        {
            private TextView tv;

            public ViewHolder(View view)
            {
               tv = (TextView) view.findViewById(R.id.browser_tv);
            }
        }
    }
}
