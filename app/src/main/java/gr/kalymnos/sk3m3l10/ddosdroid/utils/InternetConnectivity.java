package gr.kalymnos.sk3m3l10.ddosdroid.utils;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetConnectivity {

    public static boolean hasInternetConnection(ConnectivityManager manager) {
        // Determine and monitor the connectivitty status, developer.android.com.
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static boolean isConnectionEstablishedOverWifi(NetworkInfo info) {
        return isConnectedToNetwork(info) && isWifi(info);
    }

    private static boolean isConnectedToNetwork(NetworkInfo info) {
        return info != null && info.isConnectedOrConnecting();
    }

    private static boolean isWifi(NetworkInfo info) {
        return info.getType() == ConnectivityManager.TYPE_WIFI;
    }
}
