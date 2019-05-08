package com.matrix.myapplication.activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.huawei.BaseUtil;
import com.huawei.IAppVpnStatusObserver;
import com.huawei.SDKAuthenticator;
import com.huawei.SDKAuthenticatorFactory;
import com.huawei.SDKConfiguration;
import com.huawei.SDKInitializer;
import com.huawei.anyoffice.sdk.sandbox.SDKScreenShot;
import com.huawei.anyoffice.sdk.ui.AlertDialog;
import com.huawei.anyoffice.vpn.aidl.AnyOfficeVpnServiceProvider;
import com.huawei.esdk.byod.anyoffice.AnyOfficeSDK;
import com.matrix.myapplication.R;
import com.matrix.myapplication.utils.ToastUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AnyOfficeActivity2 extends AppCompatActivity
        implements SDKAuthenticator.IAuthenticationListener, IAppVpnStatusObserver {

    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.button)
    Button button;
    private String TAG = "AnyOffice";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_any_office);
        ButterKnife.bind(this);
        checkAndRequestPermissions();
        SDKScreenShot.disableScreenShot(this);
    }

    @OnClick({R.id.text, R.id.button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.text:
                break;
            case R.id.button:
                startAnyOfficeAuth();
                break;
        }
    }

    int authStatus = 0;
    private AnyOfficeVpnServiceProvider vpnProvider;
    private final int BIND_SERVICE_TIMEOUT = 1 * 1000;
    private int appVpnStatus = IAppVpnStatusObserver.VPN_STATUS_CONNECTED;
    private final int BIND_SERVICE_STEP = 100;
    private final int START_VPN_CONNECT_ACTIVITY_REQUESTCODE = 1999;
    private final int QUERY_VPN_STATUS_TIMEOUT = 5 * 1000;
    private final int QUERY_VPN_STATUS_STEP = 200;


    private void startAnyOfficeAuth() {
        SDKInitializer initializer = SDKInitializer.getInstance();
        initializer.setUnifiedAccount(true);
        initializer.init(this);
        authStatus = 0x0;
        setAppVpnStatusObserver(this);
        checkAndStartAppVpn();
    }

    protected void checkAndStartAppVpn() {
        Log.i(TAG, "begin to checkAndStartAppVpn");
        bindVpnService();

        try {
            if (null == vpnProvider || IAppVpnStatusObserver.VPN_STATUS_DISCONNECT == vpnProvider.getVpnStatus()) {
                // vpn服务未绑定，或者vpn未启动，在后台线程中绑定服务并启动vpn
                waitVpnServiceInBackground();
            } else {
                if (null != appVpnStatusObserver) {
                    int vpnStatus = vpnProvider.getVpnStatus();
                    Log.i(TAG, "onCreate, vpnStatus=" + vpnStatus);
                    appVpnStatusObserver.onAppVpnStatusChanged(vpnStatus);
                }
            }
        } catch (RemoteException e) {
            Log.e(TAG, "exception: " + e.toString());
        }
    }

    public void setAppVpnStatusObserver(IAppVpnStatusObserver appVpnStatusObserver) {
        this.appVpnStatusObserver = appVpnStatusObserver;
    }

    private IAppVpnStatusObserver appVpnStatusObserver = null;

    public void bindVpnService() {
        Intent intent = new Intent();
        intent.setAction(IAppVpnStatusObserver.VPN_SERVICE_ACTION);
        intent.setClassName(IAppVpnStatusObserver.ANYOFFICE_PACKAGE_NAME, IAppVpnStatusObserver.ANYOFFICE_VPN_SERVICE_NAME);
        bindService(intent, vpnServiceConn, Context.BIND_AUTO_CREATE);
        Log.i(TAG, "bind vpn service!");
    }

    private void waitVpnServiceInBackground() {
        ExecutorService vpnTask = Executors.newSingleThreadExecutor();
        vpnTask.execute(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "begin to wait vpn service!");
                int timeout = BIND_SERVICE_TIMEOUT; //1s超时
                try {
                    // 等待vpn服务绑定
                    while (timeout > 0) {
                        if (null != vpnProvider)
                            break;
                        Thread.sleep(BIND_SERVICE_STEP);
                        timeout -= BIND_SERVICE_STEP;
                    }
                    if (null == vpnProvider) {
                        // 绑定服务失败，启动vpn连接界面
                        startVpnConnectActivity();
                    } else {
                        int vpnStatus = vpnProvider.getVpnStatus();
                        if (IAppVpnStatusObserver.VPN_STATUS_DISCONNECT == vpnStatus
                                || IAppVpnStatusObserver.VPN_STATUS_CONNECTING == vpnStatus) {
                            // vpn未启动或者正在连接中，则启动并等待vpn连接完成
                            startAndWaitVpn(vpnProvider);
                        } else if (null != appVpnStatusObserver) {
                            // vpn已启动完成，通知监听者
                            Log.i(TAG, "waitVpnServiceInBackground, vpnStatus=" + vpnStatus);
                            appVpnStatusObserver.onAppVpnStatusChanged(vpnStatus);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private ServiceConnection vpnServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "vpnServiceconn onServiceConnected");
            vpnProvider = AnyOfficeVpnServiceProvider.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "vpnServiceconn onServiceDisconnected");
            vpnProvider = null;
        }
    };

    @Override
    public void onAppVpnStatusChanged(int vpnStatus) {
        authStatus |= 0x2;
        appVpnStatus = vpnStatus;

        // app vpn启动失败直接结束
        if (appVpnStatus != IAppVpnStatusObserver.VPN_STATUS_CONNECTED) {
            authenticationComplete();
        }

        // app vpn上线，开始进行sdk认证。
        doSDKAuthentication();
        if ((authStatus & 0x3) == 0x3) {
            authenticationComplete();
        }
    }

    /**
     * SDK认证
     */
    private void doSDKAuthentication() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SDKAuthenticator authenticator = SDKAuthenticatorFactory.getInstance();
                authenticator.setAuthInBackground(true);//后台认证
//                if (cbManualInput.isChecked()) {
//                    EditText etGateway = (EditText) findViewById(R.id.et_auth_gateway_ex);
//                    EditText etUsername = (EditText) findViewById(R.id.et_auth_username_ex);
//                    EditText etPassword = (EditText) findViewById(R.id.et_auth_password_ex);
//                    authenticator.setAccount(etUsername.getText().toString().trim(), etPassword.getText().toString().trim());
//                    authenticator.setGatewayAddress(etGateway.getText().toString().trim());
//                }
                authenticator.setListener(AnyOfficeActivity2.this);
                sdkAuthResult = -1;
                authenticator.authenticate(AnyOfficeActivity2.this);
            }
        }).start();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void startVpnConnectActivity() {
        Log.i(TAG, "begin to start vpn connect activity!");
        try {
            Intent anyofficeIntent = new Intent();
            anyofficeIntent.setClassName(IAppVpnStatusObserver.ANYOFFICE_PACKAGE_NAME, IAppVpnStatusObserver.ANYOFFICE_VPN_CONNECT_ACTIVITY);
            anyofficeIntent.putExtra(IAppVpnStatusObserver.START_APP_VPN_FLAG, "1");
            anyofficeIntent.putExtra(IAppVpnStatusObserver.START_APP_VPN_SRC_PACKAGENAME_FLAG, getPackageName());
            anyofficeIntent.putExtra(IAppVpnStatusObserver.START_APP_VPN_SRC_ACTIVITY_FLAG, this.getClass().getName());
            anyofficeIntent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            startActivityForResult(anyofficeIntent, START_VPN_CONNECT_ACTIVITY_REQUESTCODE);
        } catch (ActivityNotFoundException ex) {
            // AnyOffice未安装，提示用户先安装AnyOffice
            Log.i(TAG, "startVpnConnectActivity, vpnStatus=" + IAppVpnStatusObserver.VPN_STATUS_DISCONNECT);
            appVpnStatusObserver.onAppVpnStatusChanged(IAppVpnStatusObserver.VPN_STATUS_DISCONNECT);
        }
    }

    private void startAndWaitVpn(final AnyOfficeVpnServiceProvider vpnProvider) {
        try {
            Log.i(TAG, "begin to start vpn in background!");
            vpnProvider.startVpn();

            int timeout = QUERY_VPN_STATUS_TIMEOUT;
            int vpnStatus;
            while (timeout > 0) {
                vpnStatus = vpnProvider.getVpnStatus();
                if (vpnStatus == IAppVpnStatusObserver.VPN_STATUS_CONNECTED
                        || vpnStatus == IAppVpnStatusObserver.VPN_STATUS_CONNECT_NO_CONFIG
                        || vpnStatus == IAppVpnStatusObserver.VPN_STATUS_NETWORK_IS_UNAVAILABLE
                        || vpnStatus == IAppVpnStatusObserver.VPN_STATUS_CONNECT_FAILED) {
                    // vpn连接完成（成功/失败），返回界面进行提示。
                    break;
                }
                Thread.sleep(QUERY_VPN_STATUS_STEP);
                timeout -= QUERY_VPN_STATUS_STEP;
            }

            if (null != appVpnStatusObserver) {
                vpnStatus = vpnProvider.getVpnStatus();
                Log.i(TAG, "startAndWaitVpn, vpnStatus=" + vpnStatus);
                if (vpnStatus != IAppVpnStatusObserver.VPN_STATUS_CONNECTED)
                    startVpnConnectActivity();
                else
                    appVpnStatusObserver.onAppVpnStatusChanged(vpnStatus);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int sdkAuthResult = 0;

    private void authenticationComplete() {//验证完成  -1  -3007
        int tmpResult;
        if (sdkAuthResult == 0 && appVpnStatus == IAppVpnStatusObserver.VPN_STATUS_CONNECTED) {
            tmpResult = 0;
        } else if (sdkAuthResult != 0) {
            com.huawei.anyoffice.sdk.log.Log.e(TAG, "sdk authentication error: " + sdkAuthResult);
            tmpResult = sdkAuthResult;
        } else {
            com.huawei.anyoffice.sdk.log.Log.e(TAG, "app vpn error: " + appVpnStatus);
            tmpResult = appVpnStatus - 1;
        }

        final int result = tmpResult;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BaseUtil.showToast("authenticate return:" + result, AnyOfficeActivity2.this);
                text.setText("authenticate return:" + result);
                if (result == 0) {
                    if (SDKConfiguration.getInstance().sdkMode == SDKConfiguration.SDK_MODE_L4VPN) {
                        String tunnelIp = AnyOfficeSDK.getTunnelIPAddress();
                        ToastUtils.showShort(tunnelIp);
                    } else {

                    }
                }
            }
        });
    }

    @Override
    public int onAuthenticationResult(int result) {
        authStatus |= 0x1;
        sdkAuthResult = result;
        if ((authStatus & 0x3) == 0x3) {
            authenticationComplete();
        }
        return 0;
    }

    private final int ESDK_PERMISSION_REQUEST_CODE = 1999;

    private void checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS},
                    ESDK_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == ESDK_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                com.huawei.anyoffice.sdk.log.Log.i(TAG, "permission granted. permissions: " + permissions.length + ", results: " + grantResults.length);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getResources().getString(R.string.permissionNotAllowed));
                builder.setCancelable(false);
                builder.setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AnyOfficeActivity2.this.finish();
                        System.exit(0);
                    }
                });
                builder.show();
            }
        }
    }

    private final String APP_VPN_STATUS_FLAG = "appVpnStatus";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "requestCode=" + requestCode + ", resultCode=" + resultCode + ", intent=" + data);
        if (START_VPN_CONNECT_ACTIVITY_REQUESTCODE == requestCode && RESULT_OK == resultCode) {
            //启动AnyOffice成功，重新尝试绑定vpn service，后续可以用于查询vpn状态
            bindVpnService();
            int vpnStatus = data.getIntExtra(APP_VPN_STATUS_FLAG, 0);
            Log.i(TAG, "vpnStatus=" + vpnStatus);
            if (null != appVpnStatusObserver) {
                appVpnStatusObserver.onAppVpnStatusChanged(vpnStatus);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
