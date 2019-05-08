package com.matrix.myapplication.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.huawei.AppVpnBaseActivity;

import com.huawei.BaseUtil;
import com.huawei.IAppVpnStatusObserver;
import com.huawei.SDKAuthenticator;
import com.huawei.SDKAuthenticatorFactory;
import com.huawei.SDKConfiguration;
import com.huawei.anyoffice.sdk.log.Log;
import com.huawei.esdk.byod.anyoffice.AnyOfficeSDK;
import com.matrix.myapplication.R;
import com.matrix.myapplication.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
//java code
//App VPN自动拉起及SDK认证流程
//步骤一：继承AppVpnBaseActivity，以实现自动拉起App Vpn功能。

public class AnyOfficeActivity extends AppVpnBaseActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, SDKAuthenticator.IAuthenticationListener, IAppVpnStatusObserver {

    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.button)
    Button button;
    private int authStatus = 0;

    private int sdkAuthResult = 0;

    private int appVpnStatus = IAppVpnStatusObserver.VPN_STATUS_CONNECTED;
    private String TAG = "AnyOfficeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_any_office);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.text, R.id.button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.text:
                break;
            case R.id.button:
                doAuthentication();
                break;
        }
    }


    /**
     * 步骤二：调用AppVpnBaseActivity中实现的启动App Vpn接口，并设置回调以监听Vpn状态。
     */
    private void doAuthentication() {
        //可以在启动页面的onCreate中调用以下接口。
        setAppVpnStatusObserver(this);
        checkAndStartAppVpn();
    }

    /**
     * 步骤三：实现IAppVpnStatusObserver接口，接收App Vpn状态回调
     * 回调返回App Vpn状态
     *
     * @param vpnStatus app vpn状态
     */
    @Override
    public void onAppVpnStatusChanged(int vpnStatus) {
        authStatus |= 0x2;
        appVpnStatus = vpnStatus;
        // app vpn启动失败直接结束
        if (appVpnStatus != IAppVpnStatusObserver.VPN_STATUS_CONNECTED) {
            // App VPN启动失败，相关状态参考IAppVpnStatusObserver中的定义。
            authenticationComplete();
        }
        // app vpn上线，开始进行SDK认证。
        doSDKAuthentication();
        if ((authStatus & 0x3) == 0x3) {
            authenticationComplete();
        }
    }

    /**
     * 步骤四：调用SDKAuthenticator的authenticate方法开始SDK认证。
     * SDK认证
     */
    private void doSDKAuthentication() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SDKAuthenticator authenticator = SDKAuthenticatorFactory.getInstance();
                authenticator.setAuthInBackground(true);//身份验证在后台
                if (false)//手动输入
                {
                    EditText etGateway = (EditText) findViewById(R.id.et_auth_gateway_ex);
                    EditText etUsername = (EditText) findViewById(R.id.et_auth_username_ex);
                    EditText etPassword = (EditText) findViewById(R.id.et_auth_password_ex);
                    authenticator.setAccount(etUsername.getText().toString().trim(), etPassword.getText().toString().trim());
                    authenticator.setGatewayAddress(etGateway.getText().toString().trim());
                }
                authenticator.setListener(AnyOfficeActivity.this);
                sdkAuthResult = -1;
                authenticator.authenticate(AnyOfficeActivity.this);
            }
        }).start();
    }

    /**
     * 步骤五：实现IAuthenticationListener接口，接收SDK认证结果。认证完成，开始业务处理。
     * SDK认证返回的回调
     *
     * @param result SDK认证结果
     */
    @Override
    public int onAuthenticationResult(final int result) {
        authStatus |= 0x1;
        sdkAuthResult = result;
        if ((authStatus & 0x3) == 0x3) {
            // SDK认证完成，可以通过App VPN访问内网了。
            authenticationComplete();
        }
        return 0;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    private void authenticationComplete() {
        int tmpResult;
        if (sdkAuthResult == 0 && appVpnStatus == IAppVpnStatusObserver.VPN_STATUS_CONNECTED) {
            tmpResult = 0;
        } else if (sdkAuthResult != 0) {
            Log.e(TAG, "sdk authentication error: " + sdkAuthResult);
            tmpResult = sdkAuthResult;
        } else {
            Log.e(TAG, "app vpn error: " + appVpnStatus);
            tmpResult = appVpnStatus - 1;
        }

        final int result = tmpResult;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BaseUtil.showToast("authenticate return:" + result, AnyOfficeActivity.this);
                if (result == 0) {
                    if (SDKConfiguration.getInstance().sdkMode == SDKConfiguration.SDK_MODE_L4VPN) {
                        String tunnelIp = AnyOfficeSDK.getTunnelIPAddress();
                        ToastUtils.showLong(tunnelIp);
                    }
                }
            }
        });
    }
}
