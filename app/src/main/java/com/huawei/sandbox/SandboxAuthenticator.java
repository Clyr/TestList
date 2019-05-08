package com.huawei.sandbox;

import android.app.Activity;

import com.huawei.SDKAuthenticator;


/**
 * SDK认证封装子类。
 * 该类使用场景：企业应用集成SDK以使用单点登录、沙箱、防拷贝黏贴等特性，不使用VPN。
 *               这种场景中需要通过SDK进行认证，但不需要在企业应用中创建VPN隧道。
 */

public class SandboxAuthenticator extends SDKAuthenticator {

    private static class SingletonWrapper {
        public static SandboxAuthenticator singleton = new SandboxAuthenticator();
    }

    private SandboxAuthenticator() {
        super();
    }

    public static SandboxAuthenticator getInstance() {
        return SingletonWrapper.singleton;
    }

    /**
     *
     * @param context 界面上下文，当缺少账号信息或者网关地址信息时，需要在此上下文基础上弹出界面让用户输入
     * @return 认证返回码，0为认证成功，否则为错误码
     */
    @Override
    public int authenticate(final Activity context) {
        //使用App VPN，不创建L4VPN
        loginParam.setUseSecureTransfer(false);

        return super.authenticate(context);
    }

}
