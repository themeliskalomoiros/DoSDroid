package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.annotation.NonNull;

import static android.net.wifi.p2p.WifiP2pManager.EXTRA_WIFI_STATE;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_STATE_ENABLED;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION;

public class WifiDirectReceiver extends BroadcastReceiver {
    private static final String TAG = "WifiDirectReceiver";

    private WifiP2pConnectionManager connectionManager;
    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;

    public WifiDirectReceiver(WifiP2pConnectionManager connectionManager, WifiP2pManager manager, WifiP2pManager.Channel channel) {
        this.connectionManager = connectionManager;
        this.manager = manager;
        this.channel = channel;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION:
                int state = intent.getIntExtra(EXTRA_WIFI_STATE, -1);
                handleWifiState(state);
                break;
            case WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION:
                break;
            case WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION:
                break;
            case WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION:
                break;
            default:
                throw new IllegalArgumentException(TAG + ": Unknown action");
        }
    }

    private void handleWifiState(int state) {
        if (state == WIFI_P2P_STATE_ENABLED) {
            connectionManager.client.onManagerConnection();
        }else{
            connectionManager.client.onManagerDisconnection();
        }
    }

    @NonNull
    public static IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WIFI_P2P_STATE_CHANGED_ACTION);
        filter.addAction(WIFI_P2P_PEERS_CHANGED_ACTION);
        filter.addAction(WIFI_P2P_CONNECTION_CHANGED_ACTION);
        filter.addAction(WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        return filter;
    }
}
