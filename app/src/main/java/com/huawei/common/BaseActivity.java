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

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

import com.huawei.anyoffice.sdk.sandbox.SDKClipboard;
import com.matrix.myapplication.activity.FragmentActivity;

/**
 * @author cWX223941
 */
public class BaseActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //		MyApplication.getInstance().addActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SDKClipboard.getInstance().onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SDKClipboard.getInstance().onPause(this);
    }
//
//    public void startCommonThread(ThreadCommonIface threadCommon,
//            Handler handler, int type)
//    {
//        AsyncCommon asyncCommon = new AsyncCommon(threadCommon, handler, type);
//        asyncCommon.start();
//    }
//
//    public void startCommonThread(ThreadCommonIface threadCommon,
//            Handler handler, int type, Serializable object)
//    {
//        AsyncCommon asyncCommon = new AsyncCommon(threadCommon, handler, type,
//                object);
//        asyncCommon.start();
//    }

    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub
        super.onLowMemory();
        Log.e("Memory", "onLowMemory()");
    }
}
