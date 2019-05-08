/*
 * Copyright 2015 Huawei Technologies Co., Ltd. All rights reserved.
 * eSDK is licensed under the Apache License, Version 2.0 ^(the "License"^);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *         http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huawei.encrypt.sqlite;

import android.content.Context;

//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;


/**
 * 数据库辅助类
 *
 * @author miao
 */
public class DBHelper extends com.huawei.svn.sdk.sqlite.SQLiteOpenHelper
{
    /*
     * @param context 上下文
     *
     * @param name 数据库名字
     *
     * @param factory 游标工厂对象，没指定就设置为null
     *
     * @param version 版本号
     */
    // public DBHelper(Context context, String name, CursorFactory factory,
    // int version) {
    // super(context, name, factory, version);
    // }
    private static final String DB_NAME = "person.db";
    private static final int VERSION = 1;

    public DBHelper(Context context)
    {

        super(context, DB_NAME, null, VERSION);
    }

    /**
     * 第一次运行的时候创建
     */
    @Override
    public void onCreate(com.huawei.svn.sdk.sqlite.SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE IF NOT EXISTS person (personid integer primary key autoincrement, name text, age INTEGER)");
    }

    /**
     * 更新的时候
     */
    @Override
    public void onUpgrade(com.huawei.svn.sdk.sqlite.SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS person");
        onCreate(db);
    }
}
