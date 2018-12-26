package gr.kalymnos.sk3m3l10.ddosdroid.utils;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetConnectivity {
    public static boolean isConnectionEstablishedOverWifi(NetworkInfo info) {
        return isConnectedToNetwork(info) && isWifi(info);
    }

    public static boolean isConnectedToNetwork(NetworkInfo info) {
        return info != null && info.isConnectedOrConnecting();
    }

    private static boolean isWifi(NetworkInfo info) {
        return info.getType() == ConnectivityManager.TYPE_WIFI;
    }
}
