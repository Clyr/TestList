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
package com.huawei.checkbind;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huawei.SDKAuthenticator;
import com.huawei.SDKAuthenticatorFactory;
import com.huawei.anyoffice.sdk.login.LoginAgent;
import com.huawei.common.BaseActivity;
import com.matrix.myapplication.R;

/**
 * @author cWX223941
 */
public class CheckBindActivity extends BaseActivity
{
    private static final String TAG = "CheckBindActivity";

    private static final int SUCCESS_CHECKBIND = 1;

    private static final int FAILED_CHECKBIND = 2;

    private int result;

    private TextView btnCheck, btnBack;

    private TextView tvCheckTitle;

    private LinearLayout resultBlock;

    private Handler handler = new Handler(){

        public void handleMessage(Message msg){
            switch (msg.what){
                case SUCCESS_CHECKBIND:
                    tvCheckTitle.setText(R.string.checkbind_success);
                    btnCheck.setText(R.string.CheckBindStart);
                    btnCheck.setEnabled(true);
                    break;
                case FAILED_CHECKBIND:
                    tvCheckTitle.setText(String.format(CheckBindActivity.this.getResources()
                                    .getString(R.string.checkbind_false),
                            result));
                    btnCheck.setText(R.string.CheckBindStart);
                    btnCheck.setEnabled(true);
                    break;
                default:

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_bind);
        init();
    }

    private void init()
    {
        initView();
        initData();
        initListener();
    }

    private void initView()
    {
        btnBack = (TextView)findViewById(R.id.btn_checkbind_back);
        btnCheck = (TextView)findViewById(R.id.btn_checkbind_start);
        tvCheckTitle = (TextView)findViewById(R.id.tv_checkbind_result);
        resultBlock = (LinearLayout)findViewById(R.id.layout_checkbind_result_block);
    }

    private void initData()
    {

    }

    private void initListener()
    {
        btnBack.setOnClickListener(onClickListener);
        btnCheck.setOnClickListener(onClickListener);
    }

    private OnClickListener onClickListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (v.getId() == btnBack.getId())
            {
                CheckBindActivity.this.finish();
            }
            else if (v.getId() == btnCheck.getId())
            {
                doCheckBind();
            }
        }
    };

    private void doCheckBind()
    {
        // set button enable
        btnCheck.setText(R.string.ing);
        btnCheck.setEnabled(false);
        tvCheckTitle.setText(R.string.checkbinding);
        tvCheckTitle.setVisibility(View.VISIBLE);
        resultBlock.setVisibility(View.VISIBLE);

        // start a thread to check
        new Thread(new Runnable()
        {

            @Override
            public void run()
            {
                SDKAuthenticator authenticator = SDKAuthenticatorFactory.getInstance();
                Log.e(TAG, "checkbind = " + authenticator.loginParam.isCheckBindWhenLogin()
                + ", InternetAddress = " + authenticator.loginParam.getInternetAddress()
                + ", IntranetAddress = " + authenticator.loginParam.getIntranetAddress());
                result = LoginAgent.getInstance().checkBindWith(authenticator.loginParam);

                if (1 == result)
                {
                    Message message = new Message();
                    message.what = SUCCESS_CHECKBIND;
                    handler.sendMessage(message);
                }
                else
                {
                    Message message = new Message();
                    message.what = FAILED_CHECKBIND;
                    handler.sendMessage(message);
                }
            }
        }).start();

    }
}
