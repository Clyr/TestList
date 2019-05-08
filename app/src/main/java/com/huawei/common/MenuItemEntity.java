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
package com.huawei.common;


/**
 * @author cWX223941
 */
public class MenuItemEntity
{
    // private OnClickListener onClickListener;

    private String title;

    private int action;


    public MenuItemEntity(String title)
    {
        this.title = title;
    }

    public MenuItemEntity(String title, int action)
    {
        this.title = title;
        this.action = action;
    }


    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public int getAction()
    {
        return action;
    }

    public void setAction(int action)
    {
        this.action = action;
    }


//    public OnClickListener getOnClickListener()
//    {
//        return onClickListener;
//    }
//
//    public void setOnClickListener(OnClickListener onClickListener)
//    {
//        this.onClickListener = onClickListener;
//    }
}
