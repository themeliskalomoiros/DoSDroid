package gr.kalymnos.sk3m3l10.ddosdroid.utils;

import android.net.wifi.p2p.WifiP2pManager;

public class WifiP2pUtil {

    public static String failureTextFrom(int reason) {
        switch (reason) {
            case WifiP2pManager.BUSY:
                return "the operation failed because the framework is busy and unable to service the request";
            case WifiP2pManager.P2P_UNSUPPORTED:
                return " the operation failed because p2p is unsupported on the device.";
            default:
                return "the operation failed due to an internal error. ";
        }
    }
}
