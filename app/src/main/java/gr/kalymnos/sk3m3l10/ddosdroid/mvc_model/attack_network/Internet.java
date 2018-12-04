package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack_network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import gr.kalymnos.sk3m3l10.ddosdroid.R;

public class Internet extends AttackNetwork {

    public Internet(@NonNull Context context, @NonNull OnConnectionListener listener) {
        super(context, listener);
    }

    @Override
    public void connect() {
        //  TODO: Consider wifi-scanning when user wants to connect
        //  developer.android.com/guide/topics/connectivity/wifi-scan
    }

    @Override
    public void dissconnect() {
        //  TODO: implementation needed
    }

    @Override
    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return NetworkManager.isConnectionEstablishedOverWifi(cm.getActiveNetworkInfo());
    }

    @Override
    protected void initializeConnectivityReceiver() {
        connectivityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case ConnectivityManager.CONNECTIVITY_ACTION:
                        if (NetworkManager.isConnectedToNetwork(getNetworkInfo(intent))) {
                            connectionListener.onConnected();
                        } else {
                            connectionListener.onDisconnected(context.getString(R.string.internet_disconnected_msg));
                        }
                        break;
                }
            }

            private NetworkInfo getNetworkInfo(Intent intent) {
                return intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            }
        };
    }

    @Override
    protected void initializeConnectivityIntentFilter() {
        connectivityIntentFilter = new IntentFilter();
        connectivityIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
    }

    private static class NetworkManager {
        static boolean isConnectionEstablishedOverWifi(NetworkInfo info) {
            return isConnectedToNetwork(info) && isWifi(info);
        }

        private static boolean isConnectedToNetwork(NetworkInfo info) {
            return info != null && info.isConnectedOrConnecting();
        }

        private static boolean isWifi(NetworkInfo info) {
            return info.getType() == ConnectivityManager.TYPE_WIFI;
        }
    }
}
