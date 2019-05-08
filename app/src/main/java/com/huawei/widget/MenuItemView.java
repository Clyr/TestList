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
package com.huawei.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huawei.common.MenuItemEntity;
import com.matrix.myapplication.R;

import java.util.ArrayList;

/**
 * @author cWX223941
 */
public class MenuItemView extends RelativeLayout
{
    private static final String TAG = "MenuItemView";
    private RelativeLayout listItem;
    private TextView itemContent;
    private ImageView itemArrow;
    private ArrayList<MenuItemEntity> childList;
    private LayoutInflater inflater;
    private LinearLayout childContainer;
    private OnMenuItemClickListener itemListener;
    private boolean expanded = false;

    public MenuItemView(Context context)
    {
        super(context);
        init();
    }

    public MenuItemView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    private void init()
    {
        initView();
    }

    private void initView()
    {
        inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.common_list_item, this);
        itemContent = (TextView) findViewById(R.id.tv_common_list_item_con);
        itemArrow = (ImageView) findViewById(R.id.iv_common_list_item_arrow);
        listItem = (RelativeLayout) findViewById(R.id.layout_common_list_item);
        childContainer = (LinearLayout) findViewById(R.id.layout_common_list_item_childcontainer);
        //this.setOnClickListener(onClickListener);
    }

    private OnClickListener itemClicked = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            MenuItemView listItem = (MenuItemView) v;
            if (listItem != null)
            {
                MenuItemEntity entity = (MenuItemEntity) listItem.getTag();
                if (entity != null)
                {
                    if (itemListener != null)
                    {
                        itemListener.onMenuItemClicked(entity);
                    }
                }
            }
        }
    };


    public void setExpanded(boolean expand)
    {
        expanded = expand;


        if (childList != null && childList.size() > 0)
        {
            if (expanded)
            {
                childContainer.setVisibility(View.VISIBLE);
                itemArrow.setImageResource(R.drawable.icon_arrow_up);
            }
            else
            {
                childContainer.setVisibility(View.GONE);
                itemArrow.setImageResource(R.drawable.icon_arrow_down);
            }
        }
    }

    public boolean isExpanded()
    {
        return expanded;
    }

    public void setItemName(String title)
    {
        itemContent.setText(title);
    }

    public void setItemBackgroundColor(int color)
    {
        listItem.setBackgroundColor(getContext().getResources().getColor(color));
    }

    public void setItemBackgroundRes(int resId)
    {
        listItem.setBackgroundResource(resId);
    }

    public void setArrowRes(int resId)
    {
        itemArrow.setImageResource(resId);
    }

    /**
     * set child list and init child list view
     *
     * @param childList
     */
    public void setChildList(ArrayList<MenuItemEntity> childList)
    {
        this.childList = childList;
        initChildList();
    }

    private void initChildList()
    {
        if (null != childList && childList.size() > 0)
        {
            MenuItemView childItem = null;
            Log.i(TAG, "add menu item.");
            for (MenuItemEntity menuItem : childList)
            {
                childItem = new MenuItemView(getContext());
                childItem.setTag(menuItem);
                childItem.setItemName(menuItem.getTitle());
                //childItem.setItemOnClickListener(menuItem.getOnClickListener());
                childItem
                        .setItemBackgroundRes(R.drawable.bg_list_item_child_selected);
                childItem.setArrowRes(R.drawable.icon_arrow_right);
                childItem.setOnClickListener(this.itemClicked);
                childContainer.addView(childItem);
            }
            childContainer.invalidate();
        }
    }

    public void setItemClickListener(OnMenuItemClickListener itemListener)
    {
        this.itemListener = itemListener;
    }
}
