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
package com.huawei.wifi;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huawei.anyoffice.sdk.wifi.IWiFiCallback;
import com.huawei.anyoffice.sdk.wifi.WiFiConfigTool;
import com.huawei.anyoffice.sdk.wifi.WiFiConfiguration;
import com.huawei.common.BaseActivity;
import com.matrix.myapplication.R;

import java.security.cert.X509Certificate;

/**
 * @author cWX223941
 */
public class WiFiConfigActivity extends BaseActivity
{
    private static final String TAG = "WIFI";
    
    private TextView btnCheck, btnBack;
    
    private TextView tvCheckTitle;
    
    private TextView tvWiFiConfigurationTitle;
    
    private TextView tvWiFiCertificateTitle;
    
    private TextView tvWiFiInstallTitle;
    
    private LinearLayout resultBlock;
    
    // private MDMCheckResult mdmCheckResult;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
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
        btnBack = (TextView)findViewById(R.id.btn_wifi_back);
        btnCheck = (TextView)findViewById(R.id.btn_wifi_config);
        tvCheckTitle = (TextView)findViewById(R.id.tv_wifi_result);
        tvWiFiConfigurationTitle = (TextView)findViewById(R.id.tv_get_wifi_configuration_result);
        tvWiFiCertificateTitle = (TextView)findViewById(R.id.tv_get_wifi_certificate_result);
        tvWiFiInstallTitle = (TextView)findViewById(R.id.tv_install_wifi_configure_result);
        resultBlock = (LinearLayout)findViewById(R.id.layout_wifi_config_result_block);
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
                WiFiConfigActivity.this.finish();
            }
            else if (v.getId() == btnCheck.getId())
            {
                doWiFiConfig();
            }
        }
    };
    
    private void startWiFiConfig()
    {
        WiFiConfigTool configTool = new WiFiConfigTool(WiFiConfigActivity.this);
        configTool.configureWiFiAsync(new MyWiFiCallback());
    }
    
    private class MyWiFiCallback implements IWiFiCallback
    {
        
        @Override
        public boolean onConfigurationReceived(WiFiConfiguration configuration, int resultCode)
        {
            tvWiFiConfigurationTitle.setText("Get WiFi configuration finish, error code: " + resultCode);
            Log.i(TAG, "get configuration = " + configuration + ", resultCode = " + resultCode);
            
            return true;
        }
        
        @Override
        public boolean onCertificateReceived(X509Certificate certificate, int resultCode)
        {
            tvWiFiCertificateTitle.setText("Get WiFi certificate finish, error code: " + resultCode);
            Log.i(TAG, "get certificate resultCode = " + resultCode);
            WifiManager mWifiManager =
                (WifiManager)WiFiConfigActivity.this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            int i = 0;
            while ((!mWifiManager.isWifiEnabled()) && (i < 3))
            {
                if (mWifiManager.setWifiEnabled(true))
                {
                    Log.i(TAG, "wifi is open");
                }
                else
                {
                    Log.i(TAG, "wifi is closed");
                }
                i++;
            }
            
            if (!mWifiManager.isWifiEnabled())
            {
                return false;
            }
            
            return true;
        }
        
        @Override
        public void onWiFiConfigureCompleted(int resultCode)
        {
            tvWiFiInstallTitle.setText("Install WiFi configure finish, error code: " + resultCode);
            tvCheckTitle.setText(R.string.finish);
            Log.i(TAG, "install WiFi configure resultCode = " + resultCode);
        }
        
    }
    
    private void doWiFiConfig()
    {
        // set button enable
        btnCheck.setText(R.string.ing);
        btnCheck.setEnabled(false);
        tvCheckTitle.setText(R.string.configing);
        tvCheckTitle.setVisibility(View.VISIBLE);
        tvWiFiInstallTitle.setVisibility(View.VISIBLE);
        tvWiFiCertificateTitle.setVisibility(View.VISIBLE);
        tvWiFiInstallTitle.setVisibility(View.VISIBLE);
        resultBlock.setVisibility(View.VISIBLE);
        // start a thread to check
        // login task
        
        startWiFiConfig();
        
        btnCheck.setText(R.string.config_wifi);
        btnCheck.setEnabled(true);
    }
}
