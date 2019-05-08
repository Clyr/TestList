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
package com.huawei.http;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * @author cWX223941
 */
public class HttpPagerAdapter extends FragmentPagerAdapter
{
    //private ArrayList<HttpInfoEntity> showList;

    //    private ArrayList<BaseFragment> fragmentCache;
    public HttpPagerAdapter(FragmentManager fm)
    {
        super(fm);
        //this.showList = showList;
        //        fragmentCache = new ArrayList<BaseFragment>();
    }

    @Override
    public Fragment getItem(int position)
    {
        //        Fragment ft = new MainPageChildFragment();
        //        Bundle args = new Bundle();
        //        args.putSerializable(Constants.FRAGMENT_BUNDLE_MAINPAGE, firstCategory.get(position));
        //        ft.setArguments(args);
        Fragment ft = null;
        switch (position)
        {
            case 0:
                ft = new HttpLoginFragment();
                break;
            case 1:
                ft = new UserInfoFragment();
                break;
            case 2:
                ft = new DownloadFragment();
                break;
            case 3:
                ft = new UploadFragment();
                break;
            default:
                break;
        }
        //        fragmentCache.add(ft);
        return ft;
    }

    @Override
    public int getCount()
    {
        return 4;
    }

}
