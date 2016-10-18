package com.biggun.drawbitmapmesh.Util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 作者：孙贤武 on 2016/2/17 11:17
 * 邮箱：sun91985415@163.com
 */
public class DBOpenHelper extends SQLiteOpenHelper
{

    public DBOpenHelper(Context context)
    {
        super(context, SQLConstant.DATA_NAME, null, SQLConstant.DATA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(String.format(SQLConstant.CREATETABLE_SQL,"normal"));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS normal");
        onCreate(db);
    }
}
