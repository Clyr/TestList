package com.huawei;

import android.app.Activity;

import com.huawei.anyoffice.sdk.login.LoginAgent;
import com.huawei.anyoffice.sdk.login.LoginParam;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * SDK认证封装基类。根据具体使用场景，选择合适的子类进行SDK认证
 */

public class SDKAuthenticator {
    public LoginParam loginParam = new LoginParam();

    public interface IAuthenticationListener {
        int onAuthenticationResult(int result);
    }

    public void setListener(IAuthenticationListener listener) {
        this.listener = listener;
    }

    protected  IAuthenticationListener listener;

    protected SDKAuthenticator(){}

    public SDKAuthenticator setAccount(String userName, String password){
        LoginParam.UserInfo userInfo = loginParam.getUserInfo();
        if (null == userInfo)
            userInfo = loginParam.new UserInfo();
        userInfo.userName = userName;
        userInfo.password = password;
        loginParam.setUserInfo(userInfo);
        return this;
    }

    public SDKAuthenticator setGatewayAddress(String address) {
        String[] pieces = address.split(":");
        if (pieces.length == 1)
            loginParam.setInternetAddress(new InetSocketAddress(pieces[0], 443));
        else
            loginParam.setInternetAddress(new InetSocketAddress(pieces[0], Integer.valueOf(pieces[1])));
        return this;
    }

    public SDKAuthenticator setAuthInBackground(boolean inBackground) {
        loginParam.setLoginBackground(inBackground);
        return this;
    }

    public SDKAuthenticator setCheckBindSwitch(boolean checkBindSwitch) {
        loginParam.setCheckBindWhenLogin(checkBindSwitch);
        return this;
    }

    /**
     *
     * @param context 界面上下文，当缺少账号信息或者网关地址信息时，需要在此上下文基础上弹出界面让用户输入
     * @return 认证返回码，0为认证成功，否则为错误码
     */
    public int authenticate(final Activity context){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                String processName = AppContextUtil.getProcessName(context);
                loginParam.setServiceType(processName);
                loginParam.setAppName(processName);

                // TODO: 测试时关闭网关证书校验。正式环境需要打开，以避免中间人攻击。
                loginParam.setAuthGateway(false);

                int result = LoginAgent.getInstance().loginSync(context, loginParam);
                if (null != listener)
                    listener.onAuthenticationResult(result);
            }
        });

        return 0;
    }
}
