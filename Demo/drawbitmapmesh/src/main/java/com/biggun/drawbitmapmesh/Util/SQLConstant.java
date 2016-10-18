package com.biggun.drawbitmapmesh.Util;

/**
 * 作者：孙贤武 on 2016/2/22 14:08
 * 邮箱：sun91985415@163.com
 * 数据库的一些基本数据
 */
public class SQLConstant
{
    /**
     * 数据库名称
     */
    public static final String DATA_NAME = "BIGGUN";
    /**
     * 数据库版本
     */
    public static final int DATA_VERSION = 1;
    /**
     * 查找语句
     */
    public static final String SELECT_SQL = "SELECT * FROM %s";
    /**
     * 建表语句
     */
    public static final String CREATETABLE_SQL = "CREATE TABLE IF NOT EXISTS %s(_id integer primary key autoincrement)";
    /**
     * 查看表中是否包含某列
     */
    public static final String TABLE_CONTAIN_COLUMN_SQL = "select * from sqlite_master where name = ? and sql like ?";
    /**
     * 遍历出数据库中的表名
     */
    public static final String CONTAIN_TABLE_SQL = "select name from sqlite_master where type ='table'";
}
