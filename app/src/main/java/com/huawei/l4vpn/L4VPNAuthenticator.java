package com.huawei.l4vpn;

import android.app.Activity;

import com.huawei.AppContextUtil;
import com.huawei.SDKAuthenticator;
import com.huawei.SDKInitializer;
import com.huawei.anyoffice.sdk.log.Log;
import com.huawei.anyoffice.sdk.login.LoginAgent;
import com.huawei.anyoffice.sdk.network.NetChangeCallback;
import com.huawei.anyoffice.sdk.network.NetStatusManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * SDK认证封装子类。
 * 该类使用场景：企业应用集成SDK使用L4VPN，同时使用单点登录、沙箱、防拷贝黏贴等特性。
 *               这种场景中，需要通过SDK认证，同时需要在企业应用中创建VPN隧道。
 */

public class L4VPNAuthenticator extends SDKAuthenticator implements NetChangeCallback {

    private int loginResult = -1;

    private static class SingletonWrapper {
        public static L4VPNAuthenticator singleton = new L4VPNAuthenticator();
    }

    private L4VPNAuthenticator() {
        super();
    }

    public static L4VPNAuthenticator getInstance() {
        return SingletonWrapper.singleton;
    }

    /**
     *
     * @param context 界面上下文，当缺少账号信息或者网关地址信息时，需要在此上下文基础上弹出界面让用户输入
     * @return 认证返回码，0为认证成功，否则为错误码
     */
    @Override
    public int authenticate(final Activity context) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                String processName = AppContextUtil.getProcessName(context);
                loginParam.setServiceType(processName);
                loginParam.setAppName(processName);

                // TODO: 测试时关闭网关证书校验。正式环境需要打开，以避免中间人攻击。
                loginParam.setAuthGateway(false);

                try {
                    NetStatusManager.getInstance().setNetChangeCallback(L4VPNAuthenticator.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                loginResult = LoginAgent.getInstance().loginSync(context, loginParam);
                Log.e("L4VPNAuthenticator", "SDK init and login begin. Start time = " + SDKInitializer.getInstance().getBeforeInitAndLoginTime());
                Log.e("L4VPNAuthenticator", "SDK init and login end. return code = " + loginResult + ", end time = " + System.currentTimeMillis());
                if (null != listener) {
                    listener.onAuthenticationResult(loginResult);
                }
            }
        });

        return 0;
    }

    @Override
    public void onNetChanged(int oldStatus, int newStatus, int errCode) {
        if (null == listener)
            return;

        if(newStatus == NetStatusManager.NET_STATUS_ONLINE && loginResult == 0) {
            listener.onAuthenticationResult(0);
        } else if (newStatus == NetStatusManager.NET_STATUS_OFFLINE) {
            listener.onAuthenticationResult(errCode);
        }
    }
}
