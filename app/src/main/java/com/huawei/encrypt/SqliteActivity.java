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

/**
 *
 */
package com.huawei.encrypt;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.huawei.BaseUtil;
import com.huawei.ThemeUtil;
import com.huawei.common.BaseActivity;
import com.huawei.common.Constants;
import com.huawei.encrypt.sqlite.Person;
import com.huawei.encrypt.sqlite.PersonDao;
import com.matrix.myapplication.R;

import java.util.ArrayList;

/**
 * @author cWX223941
 */
public class SqliteActivity extends BaseActivity
{
    private static final String TAG = "SqliteActivity";
    protected EditText etName, etAge;
    protected TextView btnNavInsert, btnNavDelete, btnNavUpdate, btnNavQuery;
    protected TextView btnInsert, btnDelete, btnUpdate, btnQuery, btnBack;
    protected TextView tvId;
    protected int currentPosition = -1;
    protected ListView dataListView;
    protected TextView dataTitle;
    protected ArrayList<Person> dataList;
    protected SqliteQueryDataAdapter dataAdapter;
    // Dao
    protected PersonDao personDao;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlitehw);
        init();
    }

    private void init()
    {
        initView();
        initData();
    }

    private void initView()
    {
        etName = (EditText) findViewById(R.id.et_sqlite_name);
        etAge = (EditText) findViewById(R.id.et_sqlite_age);
        tvId = (TextView) findViewById(R.id.tv_sqlite_id);
        btnNavInsert = (TextView) findViewById(R.id.btn_nav_insert);
        btnNavDelete = (TextView) findViewById(R.id.btn_nav_delete);
        btnNavUpdate = (TextView) findViewById(R.id.btn_nav_update);
        btnNavQuery = (TextView) findViewById(R.id.btn_nav_query);


        btnInsert = (TextView) findViewById(R.id.btn_sqlite_insert);
        btnDelete = (TextView) findViewById(R.id.btn_sqlite_delete);
        btnUpdate = (TextView) findViewById(R.id.btn_sqlite_update);
        btnQuery = (TextView) findViewById(R.id.btn_sqlite_query);
        btnBack = (TextView) findViewById(R.id.btn_sqlite_back);
        dataListView = (ListView) findViewById(R.id.lv_sqlite_querydata);
        dataTitle = (TextView) findViewById(R.id.tv_sqlite_datatitle);

        btnNavInsert.setOnClickListener(onNavButtonClickListener);
        btnNavDelete.setOnClickListener(onNavButtonClickListener);
        btnNavUpdate.setOnClickListener(onNavButtonClickListener);
        btnNavQuery.setOnClickListener(onNavButtonClickListener);


        btnInsert.setOnClickListener(onClickListener);
        btnDelete.setOnClickListener(onClickListener);
        btnUpdate.setOnClickListener(onClickListener);
        btnQuery.setOnClickListener(onClickListener);
        btnBack.setOnClickListener(onClickListener);
        dataListView.setOnItemClickListener(onItemListener);

        setBtnSelectedStatus(btnNavInsert);
    }

    @SuppressLint("SdCardPath")
    private void initData()
    {
        //create database folder
//        File file = new File("/data/data/" + getPackageName() + "/databases");
//        if (!file.exists())
//        {
//            file.mkdirs();
//        }
        // 实例化dao
        personDao = new PersonDao(this);
        // 得到所有的记录
        Cursor cursor = personDao.getAllPerson();
        dataList = getPersonListFromCursor(cursor);
        dataAdapter = new SqliteQueryDataAdapter(this, dataList);
        dataListView.setAdapter(dataAdapter);

        Cursor cursor2 = personDao.getTest();
        if (cursor2 != null)
        {
            while (cursor2.moveToNext())
            {
                System.out.println("count = " + cursor2.getInt(0));
            }
        }
    }

    private OnClickListener onNavButtonClickListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            clearAllNavBtnBg();

            setBtnSelectedStatus((TextView) v);

        }
    };


    public void setBtnSelectedStatus(TextView btn)
    {
        btnInsert.setVisibility(View.GONE);
        btnDelete.setVisibility(View.GONE);
        btnUpdate.setVisibility(View.GONE);
        btnQuery.setVisibility(View.GONE);

        if (btn.getId() == btnNavInsert.getId())
        {
            btn.setBackgroundResource(R.drawable.bg_navigate_left_selected);
            btnInsert.setVisibility(View.VISIBLE);
        }
        else if (btn.getId() == btnNavDelete.getId())
        {
            btn.setBackgroundResource(R.drawable.bg_navigate_selected);
            btnDelete.setVisibility(View.VISIBLE);
        }
        else if (btn.getId() == btnNavUpdate.getId())
        {
            btn.setBackgroundResource(R.drawable.bg_navigate_selected);
            btnUpdate.setVisibility(View.VISIBLE);
        }
        else if (btn.getId() == btnNavQuery.getId())
        {
            btn.setBackgroundResource(R.drawable.bg_navigate_right_selected);
            btnQuery.setVisibility(View.VISIBLE);
        }


        btn.setTextColor(getResources().getColor(R.color.white));
    }


    /**
     * clear all navigations's background and set text's color to button content
     */
    public void clearAllNavBtnBg()
    {

        btnNavInsert.setBackgroundResource(0);
        btnNavDelete.setBackgroundResource(0);
        btnNavUpdate.setBackgroundResource(0);
        btnNavQuery.setBackgroundResource(0);

        btnNavInsert.setTextColor(getResources().getColor(R.color.btn_content));
        btnNavDelete.setTextColor(getResources().getColor(R.color.btn_content));
        btnNavUpdate.setTextColor(getResources().getColor(R.color.btn_content));
        btnNavQuery.setTextColor(getResources().getColor(R.color.btn_content));
    }


    private OnClickListener onClickListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            dataListView.setEnabled(false);
            if (v.getId() == btnBack.getId())
            {
                SqliteActivity.this.finish();
            }
            else if (v.getId() == btnInsert.getId())
            {
                doInsert();
            }
            else if (v.getId() == btnDelete.getId())
            {
                doDelete();
            }
            else if (v.getId() == btnUpdate.getId())
            {
                doUpdate();
            }
            else if (v.getId() == btnQuery.getId())
            {
                doQuery();
            }
        }
    };
    private OnItemClickListener onItemListener = new OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id)
        {
            view.setSelected(true);
            //            ListView lst = (ListView) parent;
            Person person = dataList.get(position);
            tvId.setText("" + person.getId());
            etName.setText("" + person.getName());
            etAge.setText("" + person.getAge());
            currentPosition = position;
        }
    };


    private void doInsert()
    {
        //set insert button to be unable
        ThemeUtil.setBtnToUnable(btnInsert, this);
        Person person = getPerson();
        int result = insert(person);
        handlerInsertResult(result);
    }

    private void doDelete()
    {
        //set insert button to be unable
        ThemeUtil.setBtnToUnable(btnDelete, this);
        Person person = getPerson();
        int result = delete(person);
        handlerDeleteResult(result);
    }

    private void doUpdate()
    {
        //set insert button to be unable
        ThemeUtil.setBtnToUnable(btnUpdate, this);
        Person person = getPerson();
        int result = update(person);
        handlerUpdateResult(result);
    }

    private void doQuery()
    {
        //set insert button to be unable
        ThemeUtil.setBtnToUnable(btnQuery, this);
        int result = query();
        handlerQueryResult(result);
    }

    private int insert(Person prePerson)
    {
        int status = Constants.STATUS_REQUEST_FALSE;
        try
        {
            String name = prePerson.getName();
            int age = prePerson.getAge();
            if (name.length() > 0 && age > 0)
            {
                Person person = new Person(-1, name, age);
                personDao.save(person);
                Cursor cursor = personDao.getAllPerson();
                ArrayList<Person> newList = getPersonListFromCursor(cursor);
                dataList.clear();
                copyList(newList, dataList);
                status = Constants.STATUS_REQUEST_SUCCESS;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return status;
    }

    private int delete(Person prePerson)
    {
        int status = Constants.STATUS_REQUEST_FALSE;
        try
        {
            int id = prePerson.getId();
            if (id <= 0 || currentPosition < 0)
            {
                return status;
            }
            personDao.delete(id);
            dataList.remove(dataList.get(currentPosition));
            initKeyData();
            status = Constants.STATUS_REQUEST_SUCCESS;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return status;
    }

    private int update(Person prePerson)
    {
        int status = Constants.STATUS_REQUEST_FALSE;
        try
        {
            int id = prePerson.getId();
            if (id <= 0 || currentPosition < 0)
            {
                return status;
            }
            String name = prePerson.getName();
            int age = prePerson.getAge();
            if (name.length() > 0 && age > 0)
            {
                Person person = dataList.get(currentPosition);
                person.setName(name);
                person.setAge(age);
                personDao.update(person);
                //                initKeyData(sqliteActivity);
                status = Constants.STATUS_REQUEST_SUCCESS;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return status;
    }

    private int query()
    {
        int status = Constants.STATUS_REQUEST_FALSE;
        try
        {
            String name = etName.getText().toString().trim();
            int age = BaseUtil.getIntegerFromString(etAge.getText().toString()
                    .trim());
            Cursor cursor = personDao.find(name, age);
            ArrayList<Person> newList = getPersonListFromCursor(cursor);
            cursor.close();
            dataList.clear();
            copyList(newList, dataList);
            status = Constants.STATUS_REQUEST_SUCCESS;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return status;
    }


    private void handlerInsertResult(int status)
    {
        switch (status)
        {
            case Constants.STATUS_REQUEST_SUCCESS:
                Log.e(TAG, "insert success.");
                //update list view
                refreshListView();
                BaseUtil.showToast(R.string.insert_success, this);
                break;
            case Constants.STATUS_REQUEST_FALSE:
                Log.e(TAG, "insert failed.");
                BaseUtil.showToast(R.string.insert_false, this);
                break;
            default:
                break;
        }
        dataListView.setEnabled(true);
        ThemeUtil.setBtnToEnable(btnInsert, this);
    }

    private void handlerDeleteResult(int status)
    {
        switch (status)
        {
            case Constants.STATUS_REQUEST_SUCCESS:
                Log.e(TAG, "delete success.");
                refreshListView();
                BaseUtil.showToast(R.string.delete_success, this);
                break;
            case Constants.STATUS_REQUEST_FALSE:
                Log.e(TAG, "delete failed.");
                BaseUtil.showToast(R.string.delete_false, this);
                break;
            default:
                break;
        }
        dataListView.setEnabled(true);
        ThemeUtil.setBtnToEnable(btnDelete, this);
    }

    private void handlerUpdateResult(int status)
    {
        switch (status)
        {
            case Constants.STATUS_REQUEST_SUCCESS:
                Log.e(TAG, "update success.");
                refreshListView();
                BaseUtil.showToast(R.string.update_success, this);
                break;
            case Constants.STATUS_REQUEST_FALSE:
                Log.e(TAG, "update failed.");
                BaseUtil.showToast(R.string.update_false, this);
                break;
            default:
                break;
        }
        dataListView.setEnabled(true);
        ThemeUtil.setBtnToEnable(btnUpdate, this);
    }

    private void handlerQueryResult(int status)
    {
        switch (status)
        {
            case Constants.STATUS_REQUEST_SUCCESS:
                Log.e(TAG, "query success.");
                refreshListView();
                BaseUtil.showToast(R.string.query_success, this);
                break;
            case Constants.STATUS_REQUEST_FALSE:
                Log.e(TAG, "query failed.");
                BaseUtil.showToast(R.string.query_false, this);
                break;
            default:
                break;
        }
        dataListView.setEnabled(true);
        ThemeUtil.setBtnToEnable(btnQuery, this);
    }

    private void refreshListView()
    {
        dataAdapter.notifyDataSetChanged();
        dataListView.invalidate();
    }

    private ArrayList<Person> getPersonListFromCursor(Cursor cursor)
    {
        ArrayList<Person> userInfoList = null;
        if (null != cursor)
        {
            userInfoList = new ArrayList<Person>();
            Person userInfo = null;
            while (cursor.moveToNext())
            {
                userInfo = new Person();
                userInfo.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                userInfo.setName(cursor.getString(cursor.getColumnIndex("name")));
                userInfo.setAge(cursor.getInt(cursor.getColumnIndex("age")));
                userInfoList.add(userInfo);
            }
        }
        return userInfoList;
    }

    private void copyList(ArrayList<Person> fromList, ArrayList<Person> toList)
    {
        if (null != fromList && null != toList && fromList.size() > 0)
        {
            for (Person person : fromList)
            {
                toList.add(person);
            }
        }
    }

    private void initKeyData()
    {
        tvId.setText("");
        currentPosition = -1;
    }

    private Person getPerson()
    {
        Person person = new Person();
        try
        {
            person.setId(BaseUtil.getIntegerFromString(tvId.getText()
                    .toString().trim()));
            person.setName(etName.getText().toString().trim());
            person.setAge(BaseUtil.getIntegerFromString(etAge.getText()
                    .toString().trim()));
        }
        catch (Exception e)
        {
            BaseUtil.showToast("格式不对", this);
            Log.e(TAG, "格式不对");
        }
        return person;
    }
}
