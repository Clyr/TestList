package com.matrix.myapplication.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

/**
 * Created by clyr on 2018/3/12 0012.
 * 每次使用数据库之前先清理，保证只有一个用户使用
 * 数据库存储的是大量数据
 */

public class DBHolder {
    private static String mSql = "sql_db";
    private static Context mContext = null; //todo 通过Applaction获取context
    /** 创建数据库 */
    public static void createDB(){
        DBHelper dbHelper = new DBHelper(mContext, mSql, null, 2);
        //得到一个可读的SQLiteDatabase对象
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Toast.makeText(mContext,"创建数据库",Toast.LENGTH_SHORT).show();
    }
    /** 插入数据 */
    public static void insertDB(){
        DBHelper dbHelper = new DBHelper(mContext, mSql, null, 2);
        //得到一个可写的数据库
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //生成ContentValues对象 //key:列名，value:想插入的值
        ContentValues cv = new ContentValues();
        //往ContentValues对象存放数据，键-值对模式
        cv.put("dbkey", "xiaoming");//建议使用用户名和特殊字符拼接
        cv.put("dbvalue", "male");
        //调用insert方法，将数据插入数据库
        db.insert("date_table", null, cv);
        //关闭数据库
        db.close();
        Toast.makeText(mContext,"插入数据",Toast.LENGTH_SHORT).show();
    }
    /** 查询数据 */
    public static void queryDB(String string){
        DBHelper dbHelper = new DBHelper(mContext, mSql, null, 2);
        //得到一个可写的数据库
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //参数1：表名
        //参数2：要想显示的列
        //参数3：where子句
        //参数4：where子句对应的条件值
        //参数5：分组方式
        //参数6：having条件
        //参数7：排序方式
        Cursor cursor = db.query("date_table", new String[]{"dbkey", "dbvalue"}, "dbkey=?", new String[]{string}, null, null, null);
        while (cursor.moveToNext()) {//设置只有一条
            String name = cursor.getString(cursor.getColumnIndex("dbvalue"));
        }
        //关闭数据库
        db.close();
        Toast.makeText(mContext,"查询数据",Toast.LENGTH_SHORT).show();
    }
    /** 修改数据 */
    public static void updateDB(){
        DBHelper dbHelper = new DBHelper(mContext, mSql, null, 2);
        //得到一个可写的数据库
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("sage", "23");
        //where 子句 "?"是占位符号，对应后面的"1",
        String whereClause = "dbkey=?";
        String[] whereArgs = {String.valueOf(1)};
        //参数1 是要更新的表名
        //参数2 是一个ContentValeus对象
        //参数3 是where子句
        db.update("date_table", cv, whereClause, whereArgs);
        Toast.makeText(mContext,"修改数据",Toast.LENGTH_SHORT).show();
    }
    /** 删除数据 */
    public static void deleteDB(){
        DBHelper dbHelper = new DBHelper(mContext, mSql, null, 2);
        //得到一个可写的数据库
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String whereClauses = "dbkey=?";
        String[] whereArgs = {"1"};
        //调用delete方法，删除数据
        db.delete("date_table", whereClauses, whereArgs);
//            db.execSQL("delete from date_table");//删除表中所有数据
//            db.delete("date_table", "id=?", new String[]{"1"});
        Toast.makeText(mContext,"删除数据",Toast.LENGTH_SHORT).show();
    }
}
