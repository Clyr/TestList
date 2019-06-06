package com.huawei;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.huawei.anyoffice.vpn.aidl.AnyOfficeVpnServiceProvider;
import com.matrix.myapplication.utils.ToastUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by y90004712 on 7/11/2017.
 */
public class AppVpnBaseActivity extends Activity {
    private final String TAG = getClass().getName();

    private final int START_VPN_CONNECT_ACTIVITY_REQUESTCODE = 1999;

    private final String APP_VPN_STATUS_FLAG = "appVpnStatus";

    private final int BIND_SERVICE_TIMEOUT = 1 * 1000;

    private final int BIND_SERVICE_STEP = 100;

    private final int QUERY_VPN_STATUS_TIMEOUT = 5 * 1000;

    private final int QUERY_VPN_STATUS_STEP = 200;

    private AnyOfficeVpnServiceProvider vpnProvider;

    public void setAppVpnStatusObserver(IAppVpnStatusObserver appVpnStatusObserver) {
        this.appVpnStatusObserver = appVpnStatusObserver;
    }

    private IAppVpnStatusObserver appVpnStatusObserver = null;

    protected void checkAndStartAppVpn() {
        Log.i(TAG, "begin to checkAndStartAppVpn");
        bindVpnService();

        try {
            if (null == vpnProvider || IAppVpnStatusObserver.VPN_STATUS_DISCONNECT == vpnProvider.getVpnStatus()){
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

    private void waitVpnServiceInBackground(){
        ExecutorService vpnTask = Executors.newSingleThreadExecutor();
        vpnTask.execute(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "begin to wait vpn service!");
                int timeout = BIND_SERVICE_TIMEOUT; //1s超时
                try {
                    // 等待vpn服务绑定
                    while (timeout > 0){
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
                                || IAppVpnStatusObserver.VPN_STATUS_CONNECTING == vpnStatus){
                            // vpn未启动或者正在连接中，则启动并等待vpn连接完成
                            startAndWaitVpn(vpnProvider);
                        }else if (null != appVpnStatusObserver){
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

    private void startVpnConnectActivity(){
        Log.i(TAG, "begin to start vpn connect activity!");
        try{
            Intent anyofficeIntent = new Intent();
            anyofficeIntent.setClassName(IAppVpnStatusObserver.ANYOFFICE_PACKAGE_NAME, IAppVpnStatusObserver.ANYOFFICE_VPN_CONNECT_ACTIVITY);
            anyofficeIntent.putExtra(IAppVpnStatusObserver.START_APP_VPN_FLAG, "1");
            anyofficeIntent.putExtra(IAppVpnStatusObserver.START_APP_VPN_SRC_PACKAGENAME_FLAG, getPackageName());
            anyofficeIntent.putExtra(IAppVpnStatusObserver.START_APP_VPN_SRC_ACTIVITY_FLAG, this.getClass().getName());
            anyofficeIntent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            startActivityForResult(anyofficeIntent, START_VPN_CONNECT_ACTIVITY_REQUESTCODE);
        } catch (ActivityNotFoundException ex){
            // AnyOffice未安装，提示用户先安装AnyOffice
            Log.i(TAG, "startVpnConnectActivity, vpnStatus=" + IAppVpnStatusObserver.VPN_STATUS_DISCONNECT);
            appVpnStatusObserver.onAppVpnStatusChanged(IAppVpnStatusObserver.VPN_STATUS_DISCONNECT);
        }
    }

    private void startAndWaitVpn(final AnyOfficeVpnServiceProvider vpnProvider){
        try {
            Log.i(TAG, "begin to start vpn in background!");
            vpnProvider.startVpn();

            int timeout = QUERY_VPN_STATUS_TIMEOUT;
            int vpnStatus;
            while (timeout > 0){
                vpnStatus = vpnProvider.getVpnStatus();
                if (vpnStatus == IAppVpnStatusObserver.VPN_STATUS_CONNECTED
                        || vpnStatus == IAppVpnStatusObserver.VPN_STATUS_CONNECT_NO_CONFIG
                        || vpnStatus == IAppVpnStatusObserver.VPN_STATUS_NETWORK_IS_UNAVAILABLE
                        || vpnStatus == IAppVpnStatusObserver.VPN_STATUS_CONNECT_FAILED){
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "requestCode=" + requestCode + ", resultCode=" + resultCode + ", intent=" + data);
        if (START_VPN_CONNECT_ACTIVITY_REQUESTCODE == requestCode && RESULT_OK == resultCode){
            //启动AnyOffice成功，重新尝试绑定vpn service，后续可以用于查询vpn状态
            bindVpnService();
            int vpnStatus = data.getIntExtra(APP_VPN_STATUS_FLAG, 0);
            Log.i(TAG, "vpnStatus=" + vpnStatus);
            if (null != appVpnStatusObserver){
                appVpnStatusObserver.onAppVpnStatusChanged(vpnStatus);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
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

    public void bindVpnService(){
        try {
            Intent intent = new Intent();
            intent.setAction(IAppVpnStatusObserver.VPN_SERVICE_ACTION);
            intent.setClassName(IAppVpnStatusObserver.ANYOFFICE_PACKAGE_NAME, IAppVpnStatusObserver.ANYOFFICE_VPN_SERVICE_NAME);
            bindService(intent, vpnServiceConn, Context.BIND_AUTO_CREATE);
            Log.i(TAG, "bind vpn service!");
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showShort("not bind vpn service!");
        }
    }
}
