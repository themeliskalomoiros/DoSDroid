package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.annotation.NonNull;
import android.util.Log;

import java.net.InetAddress;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attacks;

import static android.net.wifi.p2p.WifiP2pManager.EXTRA_WIFI_STATE;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_STATE_ENABLED;
import static gr.kalymnos.sk3m3l10.ddosdroid.utils.WifiP2pUtils.getFailureTextFrom;

public class WifiDirectReceiver extends BroadcastReceiver implements SocketConnectionThread.OnServerResponseListener {
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
                Log.d(TAG, "WIFI_P2P_STATE_CHANGED_ACTION");
                int state = intent.getIntExtra(EXTRA_WIFI_STATE, -1);
                handleWifiState(state);
                break;
            case WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION:
                Log.d(TAG, "WIFI_P2P_PEERS_CHANGED_ACTION");
                manager.requestPeers(channel, getPeerListListener());
                break;
            case WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION:
                Log.d(TAG, "WIFI_P2P_CONNECTION_CHANGED_ACTION");
                handleConnectionChange(intent);
                break;
            default:
                throw new IllegalArgumentException(TAG + ": Unknown action");
        }
    }

    private void handleWifiState(int state) {
        if (state == WIFI_P2P_STATE_ENABLED) {
            manager.discoverPeers(channel, getPeerDiscoveryActionListener());
        }
    }

    @NonNull
    private WifiP2pManager.ActionListener getPeerDiscoveryActionListener() {
        return new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Initiating discovering peers process...");
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "Initiating discovering peers process: " + getFailureTextFrom(reason));
            }
        };
    }

    @NonNull
    private WifiP2pManager.PeerListListener getPeerListListener() {
        return wifiP2pDeviceList -> {
            for (WifiP2pDevice device : wifiP2pDeviceList.getDeviceList()) {
                if (isServer(device)) {
                    Log.d(TAG, "Found server peer with name: " + device.deviceName + " and address: " + device.deviceAddress);
                    connectTo(device);
                }
            }
        };
    }

    private boolean isServer(WifiP2pDevice device) {
        String macAddress = device.deviceAddress;
        String serverMacAddress = Attacks.getHostMacAddress(connectionManager.attack);
        return macAddress.equals(serverMacAddress) && device.isGroupOwner();
    }

    private void connectTo(WifiP2pDevice device) {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        manager.connect(channel, config, getDeviceConnectionActionListener());
    }

    @NonNull
    private WifiP2pManager.ActionListener getDeviceConnectionActionListener() {
        return new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Initiated connection with server device...");
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "Connection initiation with server device failed: " + getFailureTextFrom(reason));
            }
        };
    }

    private void handleConnectionChange(Intent intent) {
        NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
        if (networkInfo.isConnected()) {
            Log.d(TAG, "Local device is connected with server device, requesting connection info");
            manager.requestConnectionInfo(channel, getConnectionInfoListener());
        } else {
            Log.d(TAG, "NetworkInfo.isConnected() returned false.");
        }
    }

    @NonNull
    private WifiP2pManager.ConnectionInfoListener getConnectionInfoListener() {
        return wifiP2pInfo -> {
            if (wifiP2pInfo.groupFormed) {
                Log.d(TAG, "Starting a connection thread");
                SocketConnectionThread thread = createConnectionThread(wifiP2pInfo);
                thread.start();
            }
        };
    }

    @NonNull
    private SocketConnectionThread createConnectionThread(WifiP2pInfo wifiP2pInfo) {
        InetAddress groupOwnerAddress = wifiP2pInfo.groupOwnerAddress;
        int hostLocalPort = Attacks.getHostLocalPort(connectionManager.attack);
        SocketConnectionThread thread = new SocketConnectionThread(groupOwnerAddress, hostLocalPort);
        thread.setServerResponseListener(this);
        return thread;
    }

    @Override
    public void onServerResponseReceived() {
        Log.d(TAG, "Received server response");
        connectionManager.client.onManagerConnection();
    }

    @Override
    public void onServerResponseError() {
        Log.d(TAG, "Did not receive response from server");
    }

    public void releaseWifiP2pResources() {
        manager.removeGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Initiated group removal");
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "Failed to initiate group removal: " + getFailureTextFrom(reason));
            }
        });
    }

    @NonNull
    public static IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WIFI_P2P_STATE_CHANGED_ACTION);
        filter.addAction(WIFI_P2P_PEERS_CHANGED_ACTION);
        filter.addAction(WIFI_P2P_CONNECTION_CHANGED_ACTION);
        return filter;
    }
}
