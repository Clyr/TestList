package com.huawei.anyoffice.vpn.aidl;

interface AnyOfficeVpnServiceProvider
{
    // 重新启动vpn，通常在配置变更时调用
    void restartVpn();

    // 启动vpn，此服务接口主要供第三方应用使用，vpn service不用实现此接口。
    int startVpn();

    // 停止vpn，同时退出vpn service。启动vpn请调用startService以启动vpn service，vpn service启动后会自动启动vpn
    void stopVpn();

    // 获取vpn连接状态
    int getVpnStatus();
}
