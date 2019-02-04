package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.network_constraints.NetworkConstraintsResolver;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attacks;
import gr.kalymnos.sk3m3l10.ddosdroid.utils.WifiP2pUtils;

import static android.net.wifi.p2p.WifiP2pManager.EXTRA_NETWORK_INFO;
import static android.net.wifi.p2p.WifiP2pManager.EXTRA_WIFI_STATE;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_STATE_ENABLED;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.Extra.EXTRA_MAC_ADDRESS;

class WifiP2pConnectionManager extends ConnectionManager implements NetworkConstraintsResolver.OnConstraintsResolveListener, WifiP2pConnectionThread.OnConnectionListener {
    private static final String TAG = "WifiP2pConnectionManage";

    WifiP2pConnectionManager(Context context, Attack attack) {
        super(context, attack);
    }

    @Override
    void connect() {

    }

    @Override
    void disconnect() {

    }

    @Override
    public void onConnectionSuccess() {

    }

    @Override
    public void onConnectionFailure() {

    }

    @Override
    public void onConstraintsResolved() {

    }

    @Override
    public void onConstraintResolveFailure() {

    }
}
