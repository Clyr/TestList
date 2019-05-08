package com.huawei;

/**
 * SDK集成Demo配置管理单例对象
 */

public class SDKConfiguration {
    public static final int SDK_MODE_L4VPN = 0;
    public static final int SDK_MODE_APPVPN = 1;
    public static final int SDK_MODE_SANDBOX = 2;

    public int sdkMode = SDK_MODE_L4VPN;

    public boolean authInBackground = false;

    private static class SingletonWrapper {
        public static SDKConfiguration singleton = new SDKConfiguration();
    }

    private SDKConfiguration(){}

    public static SDKConfiguration getInstance(){
        return SingletonWrapper.singleton;
    }
}
