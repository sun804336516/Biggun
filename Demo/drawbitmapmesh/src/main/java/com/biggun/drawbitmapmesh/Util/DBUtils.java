package com.biggun.drawbitmapmesh.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteAccessPermException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 作者：孙贤武 on 2016/2/17 15:31
 * 邮箱：sun91985415@163.com
 */
public class DBUtils<T>
{
    private DBOpenHelper dbOpenHelper;
    private static DBUtils dbUtils;
    private SQLiteDatabase database;

    public static DBUtils getInstance(Context context)
    {
        if (dbUtils == null) {
            synchronized (DBUtils.class) {
                if (dbUtils == null) {
                    dbUtils = new DBUtils(context);
                }
            }
        }
        return dbUtils;
    }

    private DBUtils(Context context)
    {
        dbOpenHelper = new DBOpenHelper(context.getApplicationContext());
        database = dbOpenHelper.getWritableDatabase();
    }

    public void close()
    {
//        database.close();
        dbOpenHelper.close();
    }

    public void addTAble(List<Object> list)
    {
        if (!database.isOpen()) {
            throw new SQLiteAccessPermException("数据库已关闭");
        }
        if (list == null || list.size() == 0) {
            return;
        }
        for (Object obj:list)
        {
            addTAble(obj);
        }
    }

    public void addTAble(Object object)
    {
        if (!database.isOpen()) {
            throw new SQLiteAccessPermException("数据库已关闭");
        }
        String tablename = object.getClass().getSimpleName();
        database.execSQL(String.format(SQLConstant.CREATETABLE_SQL,tablename));

        Field[] fields =
                object.getClass().getDeclaredFields();
        ContentValues values = new ContentValues();
        for (int i = 0, len = fields.length; i < len; i++) {
            String name = fields[i].getName();
            String type = fields[i].getGenericType().toString();
            if (name.equals("CREATOR") || type.equals("android.os.Parcelable$Creator<" + tablename + ">")) {
                continue;
            }
            boolean accessible = fields[i].isAccessible();
            fields[i].setAccessible(true);
            try {
                Object o = fields[i].get(object);
                values.put(name, o.toString());
                if (containsColumn(tablename, name)) {
                    continue;
                }
                database.execSQL("alter table " + tablename + " add " + name + " varchar");//增加一列
            } catch (IllegalAccessException e) {
                Utils.LogE("insert error:" + e.getMessage());
                e.printStackTrace();
            }
            fields[i].setAccessible(accessible);
        }
        Utils.LogE("----" + values.toString());
        if (TextUtils.isEmpty(values.toString())) {
            return;
        }
        database.insert(tablename, null, values);
    }


    public void readDataBase(T t, List<T> list) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException
    {
        if (!database.isOpen()) {
            throw new SQLiteAccessPermException("数据库已关闭");
        }
        String databaseName = t.getClass().getSimpleName();
        if (!containsTable(databaseName)) {
            return;
        }
        Cursor cursor = database.rawQuery(String.format(SQLConstant.SELECT_SQL,databaseName), null);
        Object object = null;
        Method method = null;
        while (cursor.moveToNext()) {
            object = t.getClass().newInstance();
            for (String name : cursor.getColumnNames()) {
                if (name.equals("_id")) {
                    continue;
                }
                String value = cursor.getString(cursor.getColumnIndex(name));
                Utils.LogE(Utils.setSet(name) + ",name:" + name + ",value:" + value);
                if (Utils.isStringisNum1(value)) {
                    method = t.getClass().getDeclaredMethod(Utils.setSet(name), int.class);
                    method.invoke(object, Integer.parseInt(value));
                } else if (Utils.isStringisBoolean(value)) {
                    method = t.getClass().getDeclaredMethod(Utils.setSet(name), boolean.class);
                    method.invoke(object, Utils.isTrue(value));
                } else {
                    method = t.getClass().getDeclaredMethod(Utils.setSet(name), String.class);
                    method.invoke(object, value);
                }
//                Method getText = t.getClass().getDeclaredMethod("getText");
//                getText.invoke(object);

            }
            list.add((T) object);
        }
        cursor.close();
    }
    /**
     * 查询表中是否包含此列
     *
     * @param simpleName
     * @param name
     * @return
     */
    private boolean containsColumn(String simpleName, String name)
    {
        boolean result = false;
//        Cursor cursor = database.rawQuery("SELECT * FROM " + simpleName+ " LIMIT 0", null);
//        result = cursor != null && cursor.getColumnIndex(name) != -1;
//        Utils.LogE("cursor:"+cursor+"=="+cursor.getColumnIndex(name));
        Cursor cursor = database.rawQuery(SQLConstant.TABLE_CONTAIN_COLUMN_SQL
                , new String[]{simpleName, "%" + name + "%"});
        result = null != cursor && cursor.moveToFirst();
        cursor.close();
        return result;
    }

    /**
     * 检查某张表是否存在
     *
     * @param tableName
     * @return
     */
    public boolean containsTable(String tableName)
    {
        Cursor cursor = database.rawQuery(SQLConstant.CONTAIN_TABLE_SQL, null);
        while (cursor.moveToNext()) {
            //遍历出表名
            String name = cursor.getString(0);
            if (tableName.equals(name)) {
                cursor.close();
                return true;
            }
            Utils.LogE("===" + name);
        }
        cursor.close();
        return false;
//    Cursor cursor = db.rawQuery("select name from sqlite_master where type='table';", null);
//    while(cursor.moveToNext()){
//    //遍历出表名
//    String name = cursor.getString(0);
//    Log.i("System.out", name);
//    SELECT count(*) FROM sqlite_master WHERE type='table' AND name='tableName';
    }
}