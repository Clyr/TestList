package com.huawei;

/**
 * Created by y90004712 on 7/11/2017.
 */

public interface IAppVpnStatusObserver {
    public final int VPN_STATUS_DISCONNECT = 0;

    public final int VPN_STATUS_CONNECTED = 1;

    public final int VPN_STATUS_CONNECTING = 2;

    public final int VPN_STATUS_CONNECT_FAILED = 3;

    public final int VPN_STATUS_CONNECT_NO_CONFIG = 4;

    public final int VPN_STATUS_NETWORK_IS_UNAVAILABLE = 5;

    public final String VPN_SERVICE_ACTION = "com.huawei.anyoffice.sdk.vpn.HANDLE_VPN";

    public final String ANYOFFICE_PACKAGE_NAME = "com.huawei.svn.hiwork";

    public final String ANYOFFICE_VPN_SERVICE_NAME = "com.huawei.anyoffice.vpn.AnyOfficeVpnHandler";

    public final String ANYOFFICE_VPN_CONNECT_ACTIVITY = "com.huawei.anyoffice.vpn.VpnConnectActivity";

    public final String START_APP_VPN_FLAG = "startAppVpn";

    public final String START_APP_VPN_SRC_PACKAGENAME_FLAG = "srcPackageName";

    public final String START_APP_VPN_SRC_ACTIVITY_FLAG = "srcActivityName";

    void onAppVpnStatusChanged(int vpnStatus);
}
