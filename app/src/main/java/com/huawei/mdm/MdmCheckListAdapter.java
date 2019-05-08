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
package com.huawei.mdm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huawei.common.CommonItemHolder;
import com.matrix.myapplication.R;

import java.util.List;

/**
 * @author cWX223941
 */
public class MdmCheckListAdapter extends BaseAdapter
{
    private Context context;
    private List<MdmCheckResultEntity> showList;

    public MdmCheckListAdapter(Context context,
                               List<MdmCheckResultEntity> showList)
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
        CommonItemHolder holder = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.mdm_listview_item, null);
            holder = new CommonItemHolder();
            holder.setItemContent((TextView) convertView
                    .findViewById(R.id.tv_mdm_listview_con));
            holder.setItemIcon((ImageView) convertView
                    .findViewById(R.id.iv_mdm_listview_icon));
            convertView.setTag(holder);
        }
        else
        {
            holder = (CommonItemHolder) convertView.getTag();
        }
        MdmCheckResultEntity checkResult = showList.get(position);
        holder.getItemContent().setText(checkResult.getContent());
        if (checkResult.isCheckOk())
        {
            holder.getItemIcon().setImageResource(R.drawable.icon_check_ok);
        }
        else
        {
            holder.getItemIcon().setImageResource(R.drawable.icon_check_wrong);
        }
        return convertView;
    }
}
