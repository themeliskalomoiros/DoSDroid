package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.network_constraints.NetworkConstraintsResolver;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.utils.WifiP2pUtils;

import static android.net.wifi.p2p.WifiP2pManager.EXTRA_WIFI_STATE;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_STATE_ENABLED;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.Extra.EXTRA_MAC_ADDRESS;

class WifiP2PConnectionManager extends ConnectionManager implements NetworkConstraintsResolver.OnConstraintsResolveListener {
    private static final String TAG = "WifiP2PConnectionManage";

    private NetworkConstraintsResolver constraintsResolver;
    private WifiP2pManager.Channel channel;
    private WifiP2pManager wifiP2pManager;
    private BroadcastReceiver wifiDirectReceiver;

    WifiP2PConnectionManager(Context context, Attack attack) {
        super(context, attack);
        initializeFields(context, attack);
    }

    private void initializeFields(Context context, Attack attack) {
        wifiP2pManager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
        initializeConstraintsResolver(context, attack);
        initializeWifiDirectReceiver();
        registerWifiDirectReceiver(context);
    }

    private void initializeConstraintsResolver(Context context, Attack attack) {
        NetworkConstraintsResolver.Builder builder = new NetworkConstraintsResolver.BuilderImp();
        constraintsResolver = builder.build(context, attack.getNetworkType());
        constraintsResolver.setOnConstraintsResolveListener(this);
    }

    private void initializeWifiDirectReceiver() {
        wifiDirectReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION:
                        //  Check to see if wifi is enabled/disabled
                        int state = intent.getIntExtra(EXTRA_WIFI_STATE, -1);
                        handleWifiState(context, state);
                        break;
                    case WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION:
                        //  Call WifiP2pManager.requestPeers() to get a list of current peers
                        wifiP2pManager.requestPeers(channel, getPeerListener());
                        break;
                    case WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION:
                        //  Respond to new connection or disconnections
                        break;
                    case WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION:
                        //  Respond to this device's wifi state changing
                        break;
                    default:
                        throw new IllegalArgumentException(TAG + ": Unknown action");
                }
            }

            private void handleWifiState(Context context, int state) {
                if (state != WIFI_P2P_STATE_ENABLED) {
                    disconnect();
                }
            }

            private WifiP2pManager.PeerListListener getPeerListener() {
                return new WifiP2pManager.PeerListListener() {
                    @Override
                    public void onPeersAvailable(WifiP2pDeviceList deviceList) {
                        for (WifiP2pDevice device : deviceList.getDeviceList()) {
                            if (isServerDevice(device)) {

                            }
                        }
                    }

                    private boolean isServerDevice(WifiP2pDevice device) {
                        return device.deviceAddress.equals(attack.getHostInfo().get(EXTRA_MAC_ADDRESS))
                                && device.isGroupOwner();
                    }
                };
            }
        };
    }

    private void registerWifiDirectReceiver(Context context) {
        context.registerReceiver(wifiDirectReceiver, getIntentFilter());
    }

    @NonNull
    private IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WIFI_P2P_STATE_CHANGED_ACTION);
        filter.addAction(WIFI_P2P_PEERS_CHANGED_ACTION);
        filter.addAction(WIFI_P2P_CONNECTION_CHANGED_ACTION);
        filter.addAction(WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        return filter;
    }

    @Override
    void connect() {
        constraintsResolver.resolveConstraints();
    }

    @Override
    void disconnect() {
        client.onManagerDisconnection();
        releaseResources();
    }

    @Override
    protected void releaseResources() {
        constraintsResolver.releaseResources();
        context.unregisterReceiver(wifiDirectReceiver);
        super.releaseResources();
    }

    @Override
    public void onConstraintsResolved() {
        channel = wifiP2pManager.initialize(context, Looper.getMainLooper(), null);
        wifiP2pManager.discoverPeers(channel, getDiscoveryActionListener());
    }

    @NonNull
    private WifiP2pManager.ActionListener getDiscoveryActionListener() {
        return new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Peer discovery initiated.");
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "Failed to initiate peer discovery: " + WifiP2pUtils.getFailureTextFrom(reason));
                Log.d(TAG, "Disconnecting...");
                disconnect();
            }
        };
    }

    @Override
    public void onConstraintResolveFailure() {
        client.onManagerError();
        releaseResources();
    }
}
