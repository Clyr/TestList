package com.huawei;

import com.huawei.appvpn.AppVPNAuthenticator;
import com.huawei.l4vpn.L4VPNAuthenticator;
import com.huawei.sandbox.SandboxAuthenticator;

/**
 * Created by y90004712 on 7/10/2017.
 */

public class SDKAuthenticatorFactory {
    public static SDKAuthenticator getInstance() {
        switch (SDKConfiguration.getInstance().sdkMode) {
            case SDKConfiguration.SDK_MODE_L4VPN:
                return L4VPNAuthenticator.getInstance();
            case  SDKConfiguration.SDK_MODE_APPVPN:
                return AppVPNAuthenticator.getInstance();
            case SDKConfiguration.SDK_MODE_SANDBOX:
                return SandboxAuthenticator.getInstance();
            default: {
                return L4VPNAuthenticator.getInstance();
            }
        }
    }
}
