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
package com.huawei;

import android.content.Context;
import android.widget.TextView;

import com.matrix.myapplication.R;


/**
 * @author cWX223941
 */
public class ThemeUtil
{
//    public static void setListItemChildTheme(MyListItem childItem)
//    {
//        if (null != childItem)
//        {
//            //            childItem.setItemBackgroundColor(R.color.list_item_child_bg);
//            childItem
//                    .setItemBackgroundRes(R.drawable.bg_list_item_child_selected);
//            childItem.setArrowRes(R.drawable.icon_arrow_right);
//        }
//    }

//    public static void setMDMCheckingTheme(MyCheckListItem checkItem)
//    {
//        if (null != checkItem)
//        {
//            checkItem.setCheckBlockVisiable(View.VISIBLE);
//            checkItem.setCheckTitle(R.string.checking);
//        }
//    }

    //    public static void setHttpLoginResultTheme(HttpLoginFragment loginFragment){
    //        if(null != loginFragment){
    //            return;
    //        }
    //        loginFragment.
    //    }
    public static void setBtnToEnable(TextView button, Context context)
    {
        if (null != button && null != context)
        {
            if (!button.isEnabled())
            {
                button.setEnabled(true);
                button.setTextColor(context.getResources().getColor(
                        R.color.btn_normal_withbg));
                button.setBackgroundResource(R.drawable.bg_btn_enabled);
            }
        }
    }

    public static void setBtnToUnable(TextView button, Context context)
    {
        if (null != button && null != context)
        {
            if (button.isEnabled())
            {
                button.setEnabled(false);
                button.setTextColor(context.getResources().getColor(
                        R.color.btn_content_unable));
                button.setBackgroundResource(R.drawable.bg_btn_disabled);
            }
        }
    }
}
