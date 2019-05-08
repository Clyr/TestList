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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huawei.encrypt.sqlite.Person;
import com.matrix.myapplication.R;

import java.util.List;

/**
 * @author cWX223941
 */
public class SqliteQueryDataAdapter extends BaseAdapter
{
    private Context context;
    private List<Person> showList;

    public SqliteQueryDataAdapter(Context context, List<Person> showList)
    {
        this.context = context;
        this.showList = showList;
    }

    @Override
    public int getCount()
    {
        return showList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return showList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        SqliteDataItemHolder holder = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.sqlite_listview_item, null);
            holder = new SqliteDataItemHolder();
            holder.setId((TextView) convertView
                    .findViewById(R.id.tv_sqlite_listview_item_id));
            holder.setName((TextView) convertView
                    .findViewById(R.id.tv_sqlite_listview_item_name));
            holder.setAge((TextView) convertView
                    .findViewById(R.id.tv_sqlite_listview_item_age));
            convertView.setTag(holder);
        }
        else
        {
            holder = (SqliteDataItemHolder) convertView.getTag();
        }
        Person checkResult = showList.get(position);
        holder.getId().setText(checkResult.getId() + "");
        holder.getName().setText(checkResult.getName());
        holder.getAge().setText(checkResult.getAge() + "");
        return convertView;
    }

    private class SqliteDataItemHolder
    {
        private TextView id;
        private TextView name;
        private TextView age;

        public TextView getId()
        {
            return id;
        }

        public void setId(TextView id)
        {
            this.id = id;
        }

        public TextView getName()
        {
            return name;
        }

        public void setName(TextView name)
        {
            this.name = name;
        }

        public TextView getAge()
        {
            return age;
        }

        public void setAge(TextView age)
        {
            this.age = age;
        }
    }
}
