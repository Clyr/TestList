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
package com.huawei.checknettype;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.anyoffice.sdk.login.LoginAgent;
import com.huawei.anyoffice.sdk.login.LoginParam.LoginAddressType;
import com.huawei.common.BaseActivity;
import com.matrix.myapplication.R;


/**
 * @author cWX223941
 */
public class CheckNetworkTypeActivity extends BaseActivity
{
    private static final String TAG = "CheckNetworkTypeActivity";
    
    private TextView btnCheck, btnBack;
    
    private TextView tvCheckTitle;
    
    private LinearLayout resultBlock;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_net_type);
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
        btnBack = (TextView)findViewById(R.id.btn_checknettype_back);
        btnCheck = (TextView)findViewById(R.id.btn_checknettype_start);
        tvCheckTitle = (TextView)findViewById(R.id.tv_checknettype_result);
        resultBlock = (LinearLayout)findViewById(R.id.layout_checknettype_result_block);
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
                CheckNetworkTypeActivity.this.finish();
            }
            else if (v.getId() == btnCheck.getId())
            {
                btnCheck.setEnabled(false);
                
                String addressTypeStr = "";
                //网关接入类型
                int addressType = LoginAgent.getInstance().getAddressType();
                if (addressType == LoginAddressType.login_inner_network.getType())
                {
                    addressTypeStr = "内网";
                }
                else if (addressType == LoginAddressType.login_outer_network.getType())
                {
                    addressTypeStr = "外网";
                }
                
                Toast.makeText(CheckNetworkTypeActivity.this,
                    "Get access address type is：" + addressTypeStr,
                    Toast.LENGTH_LONG).show();
                
                tvCheckTitle.setVisibility(View.VISIBLE);
                resultBlock.setVisibility(View.VISIBLE);
                tvCheckTitle.setText(String.format(CheckNetworkTypeActivity.this.getResources()
                    .getString(R.string.checknettype_result),
                    addressTypeStr));
                btnCheck.setEnabled(true);
            }
        }
    };
}
