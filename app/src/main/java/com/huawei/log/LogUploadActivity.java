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
package com.huawei.log;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huawei.common.BaseActivity;
import com.matrix.myapplication.R;


/**
 * @author cWX223941
 */
public class LogUploadActivity extends BaseActivity
{
    private static final String TAG = "LOG";
    
    private TextView btnCheck, btnBack;
    
    private TextView tvCheckTitle;
    
    private LinearLayout resultBlock;
    
    // private MDMCheckResult mdmCheckResult;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
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
        btnBack = (TextView)findViewById(R.id.btn_log_back);
        btnCheck = (TextView)findViewById(R.id.btn_log_upload);
        tvCheckTitle = (TextView)findViewById(R.id.tv_log_result);
        resultBlock = (LinearLayout)findViewById(R.id.layout_log_upload_result_block);
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
                LogUploadActivity.this.finish();
            }
            else if (v.getId() == btnCheck.getId())
            {
                doLogUpload();
            }
        }
    };
    
    private void doLogUpload()
    {
        // set button enable
        btnCheck.setText(R.string.ing);
        btnCheck.setEnabled(false);
        tvCheckTitle.setText(R.string.uploading);
        tvCheckTitle.setVisibility(View.VISIBLE);
        resultBlock.setVisibility(View.VISIBLE);
        // start a thread to check
        // login task
        new Thread(new Runnable()
        {
            
            @Override
            public void run()
            {
                int result = com.huawei.anyoffice.sdk.log.Log.uploadSdkLog(60);
                if (0 == result)
                {
                    tvCheckTitle.setText(R.string.upload_success);
                    btnCheck.setText(R.string.upload_log);
                    btnCheck.setEnabled(true);
                }
                else
                {
                    tvCheckTitle.setText(String.format(LogUploadActivity.this.getResources()
                        .getString(R.string.upload_false),
                        result));
                    btnCheck.setText(R.string.upload_log);
                    btnCheck.setEnabled(true);
                }
            }
        }).start();
        
    }
}
