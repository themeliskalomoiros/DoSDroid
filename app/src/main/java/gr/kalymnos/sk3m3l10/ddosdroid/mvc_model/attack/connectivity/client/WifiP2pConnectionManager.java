package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
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
    private static final String TAG = "WifiP2PConnectionManage";

    private NetworkConstraintsResolver constraintsResolver;
    private WifiP2pManager.Channel channel;
    private WifiP2pManager wifiP2pManager;
    private BroadcastReceiver wifiDirectReceiver;
    private WifiP2pConnectionThread connectionThread;

    WifiP2pConnectionManager(Context context, Attack attack) {
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
                        NetworkInfo info = intent.getParcelableExtra(EXTRA_NETWORK_INFO);
                        if (info.isConnected()) {
                            //  We are connected with the device, requesting connection info
                            //  to find the group owner's IP
                            wifiP2pManager.requestConnectionInfo(channel, wifiP2pInfo -> {
                                if (wifiP2pInfo.groupFormed && !wifiP2pInfo.isGroupOwner) {
                                    if (connectionThread != null) {
                                        return;
                                    }
                                    initializeConnectionThread(wifiP2pInfo);
                                    connectionThread.start();
                                }
                            });
                        }
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
                                connectTo(device);
                            }
                        }
                    }

                    private boolean isServerDevice(WifiP2pDevice device) {
                        return device.deviceAddress.equals(attack.getHostInfo().get(EXTRA_MAC_ADDRESS))
                                && device.isGroupOwner();
                    }

                    private void connectTo(WifiP2pDevice device) {
                        WifiP2pConfig config = new WifiP2pConfig();
                        config.deviceAddress = device.deviceAddress;
                        wifiP2pManager.connect(channel, config, getConnectActionListener());
                    }

                    @NonNull
                    private WifiP2pManager.ActionListener getConnectActionListener() {
                        return new WifiP2pManager.ActionListener() {
                            @Override
                            public void onSuccess() {
                                Log.d(TAG, "Connection to server device initiated.");
                            }

                            @Override
                            public void onFailure(int reason) {
                                Log.d(TAG, "Failed to initiate connection to server device: " + WifiP2pUtils.getFailureTextFrom(reason));
                                disconnect();
                            }
                        };
                    }
                };
            }

            private void initializeConnectionThread(WifiP2pInfo wifiP2pInfo) {
                connectionThread = new WifiP2pConnectionThread(wifiP2pInfo.groupOwnerAddress, Attacks.getHostLocalPort(attack));
                connectionThread.setConnectionListener(WifiP2pConnectionManager.this);
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

    @Override
    public void onConnectionSuccess() {
        client.onManagerConnection();
    }

    @Override
    public void onConnectionFailure() {
        client.onManagerError();
    }
}
